package com.gof.process;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.gof.dao.IrCurveYtmDao;
import com.gof.dao.IrDcntRateDao;
import com.gof.entity.IrCurveSpot;
import com.gof.entity.IrCurveYtm;
import com.gof.entity.IrDcntRate;
import com.gof.entity.IrDcntRateBu;
import com.gof.entity.IrParamSw;
import com.gof.enums.EJob;
import com.gof.model.SmithWilsonKics;
import com.gof.model.SmithWilsonKicsBts;
import com.gof.model.entity.SmithWilsonRslt;
import com.gof.util.DateUtil;
import com.gof.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Esg271_IrDcntRate extends Process {	
	
	public static final Esg271_IrDcntRate INSTANCE = new Esg271_IrDcntRate();
	public static final String jobId = INSTANCE.getClass().getSimpleName().toUpperCase().substring(0, ENTITY_LENGTH);	
	
	public static List<IrDcntRate> createIrDcntRate(String bssd,  Map<String, Map<Integer, IrParamSw>> paramSwMap, Integer projectionYear) {	
		List<IrDcntRate> rst = new ArrayList<IrDcntRate>();
		
		Map<Double, Map<String, IrDcntRate>> aaaMap = new HashMap<Double, Map<String,IrDcntRate>>();
		
		for(Map.Entry<String, Map<Integer, IrParamSw>> curveSwMap : paramSwMap.entrySet()) {			
			List<IrCurveYtm> ytmList = IrCurveYtmDao.getIrCurveYtm(bssd, curveSwMap.getKey());
			for(Map.Entry<Integer, IrParamSw> swSce : curveSwMap.getValue().entrySet()) {
				
				String applBizDv = swSce.getValue().getApplBizDv();
				
				
				log.info("BIZ: [{}], IR_CURVE_ID: [{}], IR_CURVE_SCE_NO: [{}]", applBizDv, curveSwMap.getKey(), swSce.getKey());

				List<IrCurveSpot> irCurveSpotLiabList = IrDcntRateDao.getIrDcntRateBuToAdjSpotList(bssd, applBizDv, curveSwMap.getKey(), swSce.getKey());
				List<IrCurveYtm> ytmAddList = ytmList.stream().map(s->s.addSpread(swSce.getValue().getYtmSpread())).collect(Collectors.toList());
				
				if(irCurveSpotLiabList.size()==0) {
					log.warn("No IR Dcnt Rate Data [BIZ: {}, IR_CURVE_ID: {}, IR_CURVE_SCE_NO: {}] in [{}] for [{}]", applBizDv, curveSwMap.getKey(), swSce.getKey(), toPhysicalName(IrDcntRateBu.class.getSimpleName()), bssd);
					continue;
				}				
				LocalDate baseDate = DateUtil.convertFrom(bssd).with(TemporalAdjusters.lastDayOfMonth());
//				log.info("{}, {}, {}", swSce.getValue().getLtfr(), swSce.getValue().getLtfrCp(), projectionYear);
				
				SmithWilsonKics swKics = new SmithWilsonKics(baseDate, irCurveSpotLiabList, CMPD_MTD_DISC, true, swSce.getValue().getLtfr(), swSce.getValue().getLtfrCp(), projectionYear, 1, 100, DCB_MON_DIF);				
				List<IrDcntRate> adjRateLiabList = swKics.getSmithWilsonResultList().stream().map(s -> s.convert()).collect(Collectors.toList());
				Map<String, IrDcntRate> adjRateLiabMap = adjRateLiabList.stream().collect(Collectors.toMap(IrDcntRate::getMatCd, Function.identity(), (k, v) -> k, TreeMap::new));
				
				TreeSet<Double> tenorList = adjRateLiabList.stream().map(s -> Double.valueOf(1.0 * Integer.valueOf(s.getMatCd().substring(1)) / MONTH_IN_YEAR)).collect(Collectors.toCollection(TreeSet::new));
				double[] prjTenor = tenorList.stream().mapToDouble(Double::doubleValue).toArray();
				
				
//				---------------ytm spreadm �옄�궛 sw
				SmithWilsonKicsBts swBts = SmithWilsonKicsBts.of()
															 .baseDate(baseDate)					
															 .ytmCurveHisList(ytmAddList)
															 .alphaApplied(swSce.getValue().getSwAlphaYtm())													 
															 .freq(swSce.getValue().getFreq())
															 .build();
				
				
				
				List<IrDcntRate> adjRateAssetList = swBts.getSmithWilsonResultList(prjTenor).stream().map(s -> s.convertForAsset()).collect(Collectors.toList());
				Map<String, IrDcntRate> assetRateMap = adjRateAssetList.stream().collect(Collectors.toMap(IrDcntRate::getMatCd, Function.identity()));
				
				
				
//				log.info("adjRateList : {}, {}, {}", adjRateList.size());
				
				for(IrDcntRate rslt : adjRateLiabList) {						
					
					String matCd   = rslt.getMatCd();						
					
					IrDcntRate assetTemp = assetRateMap.getOrDefault(matCd, new IrDcntRate());	
					rslt.setSpotRate(assetTemp.getSpotRate());						
					rslt.setFwdRate(assetTemp.getFwdRate());
				}					
				
				
//				AFNS 異⑷꺽 �떆�굹由ъ삤 �쟻�슜�떆�뒗 �옄�궛 湲덈━怨≪꽑�� 遺�梨� 湲덈━怨≪꽑�쓽 李⑥씠濡� �궛異쒕뵥
				if(swSce.getValue().getShkSprdSceNo()==1) {
					Map<String, IrDcntRate> baseRateSce1Map = adjRateLiabList.stream().collect(Collectors.toMap(IrDcntRate::getMatCd, Function.identity()));
					aaaMap.put(swSce.getValue().getYtmSpread(), baseRateSce1Map);
				}
				else if(swSce.getValue().getShkSprdSceNo()!=null && swSce.getValue().getShkSprdSceNo() !=1) {
					if(aaaMap.containsKey(swSce.getValue().getYtmSpread())) {
						
						TreeMap<String, Double> spotRateMap = new TreeMap<String, Double>();
						TreeMap<String, Double> fwdRateMap  = new TreeMap<String, Double>();
						Map<String, IrDcntRate> tempBaseRateSce1Map = aaaMap.get(swSce.getValue().getYtmSpread());
						for(IrDcntRate rslt : adjRateLiabList) {						
							String matCd   = rslt.getMatCd();						
							double adjRate = adjRateLiabMap.get(matCd).getAdjSpotRate();
							double adjDiff = tempBaseRateSce1Map.get(matCd).getSpotRate() - tempBaseRateSce1Map.get(matCd).getAdjSpotRate();						
							
							rslt.setSpotRate(adjRate + adjDiff);						
							spotRateMap.put(matCd, adjRate + adjDiff);
						}					
						fwdRateMap = irSpotDiscToFwdM1Map(spotRateMap);					
						
						for(IrDcntRate rslt : adjRateLiabList) {
							rslt.setFwdRate(fwdRateMap.get(rslt.getMatCd()).doubleValue());
						}
					}
					
				}
				
				
				
//				fwdRateMap = irSpotDiscToFwdM1Map(spotRateMap);					
//				for(IrDcntRate rslt : adjRateList) {
//					rslt.setFwdRate(fwdRateMap.get(rslt.getMatCd()).doubleValue());
//				}		
				

				for(IrDcntRate rslt : adjRateLiabList) {
					rslt.setBaseYymm(bssd);
					rslt.setApplBizDv(applBizDv);
//					rslt.setApplBizDv(swSce.getValue().getApplBizDv());
					rslt.setIrCurveId(curveSwMap.getKey());
					rslt.setIrCurveSceNo(swSce.getKey());
					rslt.setLastModifiedBy(jobId);
					rslt.setLastUpdateDate(LocalDateTime.now());
//					log.info("adjRateList : {}, {}, {}", rslt.getMatCd(), rslt.getSpotRate(), rslt.getAdjSpotRate());
				}
				
				for(IrDcntRate dcnt : adjRateLiabList) {
					if(dcnt.getSpotRate().isNaN() 
							|| dcnt.getSpotRate().isInfinite() 
							|| dcnt.getAdjSpotRate().isNaN() 
							|| dcnt.getAdjSpotRate().isInfinite()
							|| dcnt.getFwdRate().isNaN() 
							|| dcnt.getFwdRate().isInfinite()
							|| dcnt.getAdjFwdRate().isNaN() 
							|| dcnt.getAdjFwdRate().isInfinite()
							|| dcnt.getAdjSpotRate()> 1.0 
							|| dcnt.getAdjFwdRate() > 1.0 ) {
//						log.info("{}, {}, {}", curveSwMap.getKey(), swSce.getKey(), dcnt);
						log.error("Smith-Wilson Interpolation is failed. Check Shock Spread Data in [{}] Table for [BIZ: {}, IR_CURVE_ID: {}, IR_CURVE_SCE_NO: {}] in [{}], {}", Process.toPhysicalName(IrCurveYtm.class.getSimpleName()), applBizDv, curveSwMap.getKey(), swSce.getKey(), bssd, dcnt.toString());
						try {
							throw new Exception();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						return new ArrayList<IrDcntRate>();
					}
					else {
						rst.add(dcnt);
					}
				}
				
//				rst.addAll(adjRateLiabList);
			}	
		}		
		log.info("{}({}) creates [{}] results of [{}] {}. They are inserted into [{}] Table", jobId, EJob.valueOf(jobId).getJobName(), rst.size(), paramSwMap.keySet(), toPhysicalName(IrDcntRate.class.getSimpleName()));
		
		return rst;		
	}	

}

