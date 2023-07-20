package com.gof.process;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.gof.dao.IrCurveSpotDao;
import com.gof.dao.IrSprdDao;
import com.gof.entity.IrCurveSpot;
import com.gof.entity.IrDcntRateBu;
import com.gof.entity.IrDcntRateBuIm;
import com.gof.entity.IrParamSw;
import com.gof.entity.IrSprdAfnsCalc;
import com.gof.entity.IrSprdLpBiz;
import com.gof.enums.EJob;
import com.gof.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Esg760_IrDcntRateBu extends Process {

	public static final Esg760_IrDcntRateBu INSTANCE = new Esg760_IrDcntRateBu();
	public static final String jobId = INSTANCE.getClass().getSimpleName().toUpperCase().substring(0, ENTITY_LENGTH);

	public static List<IrDcntRateBuIm> setIrDcntRateBu(String bssd, String irModelId, String applBizDv, Map<String, Map<Integer, IrParamSw>> paramSwMap) {

		List<IrDcntRateBuIm> rst = new ArrayList<IrDcntRateBuIm>();

		for(Map.Entry<String, Map<Integer, IrParamSw>> curveSwMap : paramSwMap.entrySet()) {
			String irCurveId = curveSwMap.getKey();
			
//			List<IrCurveSpot> spotList = IrCurveSpotDao.getIrCurveSpot(bssd, curveSwMap.getKey());
			List<IrCurveSpot> spotList = IrCurveSpotDao.getIrCurveSpot(bssd, irCurveId);

			TreeMap<String, Double> spotMap = spotList.stream().collect(Collectors.toMap(IrCurveSpot::getMatCd, IrCurveSpot::getSpotRate, (k, v) -> k, TreeMap::new));

			if(spotList.isEmpty()) {
				log.warn("No IR Curve Spot Data [BIZ: {}, IR_CURVE_ID: {}] in [{}] for [{}]", applBizDv, irCurveId, toPhysicalName(IrCurveSpot.class.getSimpleName()), bssd);
				continue;
			}
			
			
			
			int detScen = 1 ;
			Map<Integer, IrParamSw> swMap = curveSwMap.getValue();
			IrParamSw swSce = swMap.get(detScen);

			if (swSce != null) {
						// 23.06.13 議곌굔�닔�젙. �궡遺�紐⑦삎�쓽 紐⑹쟻�쑝濡�, 異붽��쟻�쑝濡� paramSw�쓣 異붽�/�닔�젙/愿�由� �븯吏� �븡怨� 泥섎━�븷 �닔 �엳�룄濡� 湲곗〈 �꽕�젙�쓣 �궗�슜�븿.
						// swSce �뿉�꽌 KICS �쓽 1踰� �떆�굹由ъ삤 �꽕�젙 �뜲�씠�꽣瑜� �궗�슜�븿.
						// �떆�굹由ъ삤 媛��닔�뒗 det / sto瑜� 援щ텇�빐�꽌 媛곴컖 �젙�쓽�븿.

				// �쑀�룞�꽦 �봽由щ�몄뾼�� KICS�쓽 寃곗젙濡� �떆�굹由ъ삤 �깮�꽦�떆 �궗�슜�븯�뒗 �닔以��쓣 �궗�슜�븿.
//					Map<String, Double> irSprdLpMap = IrSprdDao.getIrSprdLpBizList(bssd, applBizDv, curveSwMap.getKey(), detScen).stream()
					Map<String, Double> irSprdLpMap = IrSprdDao.getIrSprdLpBizList(bssd, "KICS", curveSwMap.getKey(), detScen).stream()
							                                   .collect(Collectors.toMap(IrSprdLpBiz::getMatCd, IrSprdLpBiz::getLiqPrem));

					// �깮�꽦�빐�빞 �븯�뒗 寃곌낵�뒗 det, sto �떆�굹由ъ삤 �몮 �떎 �엳�쓬. ( irModel�뿉 �뵲�씪 �떖�씪吏� )
//					int scenCnt = IrSprdDao.getIrSprdAfnsCalcScenCnt(bssd, irModelId, curveSwMap.getKey());
					long scenCnt = IrSprdDao.getIrSprdAfnsCalcAll(bssd, irModelId, curveSwMap.getKey()).stream().count();
					log.info("IrCruveId, Sce No : {}, {}", curveSwMap.getKey(), scenCnt);


					for (int sceNo = 1; sceNo <= scenCnt; sceNo++) {
				
						
					Map<String, Double> irSprdShkMap = IrSprdDao.getIrSprdAfnsCalcList(bssd, irModelId, curveSwMap.getKey(), sceNo).stream()
																.collect(Collectors.toMap(IrSprdAfnsCalc::getMatCd, IrSprdAfnsCalc::getShkSprdCont));

	//				log.info("sceNo :{}, irSprdShkMap:{}" ,sceNo, irSprdShkMap );

					List<IrCurveSpot> spotSceList = spotList.stream().map(s -> s.deepCopy(s)).collect(Collectors.toList());

					String fwdMatCd = StringUtil.objectToPrimitive(swSce.getFwdMatCd(), "M0000");
					if(!fwdMatCd.equals("M0000")) {
						Map<String, Double> fwdSpotMap = irSpotDiscToFwdMap(bssd, spotMap, fwdMatCd);
						spotSceList.stream().forEach(s -> s.setSpotRate(fwdSpotMap.get(s.getMatCd())));
					}

					String pvtMatCd = StringUtil.objectToPrimitive(swSce.getPvtRateMatCd() , "M0000");
					double pvtRate  = StringUtil.objectToPrimitive(spotMap.getOrDefault(pvtMatCd, 0.0), 0.0    );
	//				double pvtMult  = StringUtil.objectToPrimitive(swSce.getValue().getMultPvtRate()  , 0.0    );
					double intMult  = StringUtil.objectToPrimitive(swSce.getMultIntRate()  , 1.0    );
					double addSprd  = StringUtil.objectToPrimitive(swSce.getAddSprd()      , 0.0    );
					int    llp      = StringUtil.objectToPrimitive(swSce.getLlp()          , 20     );

	//				log.info("{}, {}, {}, {}, {}, {}, {}, {}, {}", applBizDv, curveSwMap.getKey(), detScen, pvtMatCd, pvtRate, pvtMult, intMult, addSprd, llp);
					for(IrCurveSpot spot : spotSceList) {
						if(Integer.valueOf(spot.getMatCd().substring(1)) <= llp * MONTH_IN_YEAR) {

							IrDcntRateBuIm dcntRateBuIm = new IrDcntRateBuIm();

							double baseSpot = intMult * (StringUtil.objectToPrimitive(spot.getSpotRate()) -  pvtRate) + addSprd + pvtRate;  //pvtRate doesn't have an effect on parallel shift(only addSprd)
							double baseSpotCont = irDiscToCont(baseSpot);

							double shkCont      = irSprdShkMap.getOrDefault(spot.getMatCd(), 0.0);
//							double lpDisc       = irSprdLpMap.getOrDefault(spot.getMatCd(), 0.0);
							double lpDisc       = applBizDv.equals("KICS_A")? 0.0 : irSprdLpMap.getOrDefault(spot.getMatCd(), 0.0);

//							double spotCont     = baseSpotCont + shkCont;
//							double spotDisc     = irContToDisc(spotCont);
//							double adjSpotDisc  = spotDisc + lpDisc;
//							double adjSpotCont  = irDiscToCont(adjSpotDisc);

							// 議곗젙 諛섏쁺 �쟾 �썑 鍮꾧탳瑜� �쐞�빐 湲곗〈 肄붾뱶�뿉�꽌 �닔�젙�븿.
							double spotCont     = baseSpotCont ; // 議곗젙 諛섏쁺 �쟾
							double spotDisc     = irContToDisc(spotCont);	// 議곗젙 諛섏쁺 �쟾
							double adjSpotDisc  = spotDisc + lpDisc; // �쑀�룞�꽦 �봽由щ�몄뾼 諛섏쁺
							double adjSpotCont  = irDiscToCont(adjSpotDisc) + shkCont; 	// �쑀�룞�꽦 �봽由щ�몄뾼 + shock 諛섏쁺

							dcntRateBuIm.setBaseYymm(bssd);
							dcntRateBuIm.setApplBizDv(applBizDv);
							dcntRateBuIm.setIrModelId(irModelId);
							dcntRateBuIm.setIrCurveId(curveSwMap.getKey());
							dcntRateBuIm.setIrCurveSceNo(sceNo);
							dcntRateBuIm.setMatCd(spot.getMatCd());
							dcntRateBuIm.setSpotRateDisc(spotDisc);
							dcntRateBuIm.setSpotRateCont(spotCont);
							dcntRateBuIm.setLiqPrem(lpDisc);
							dcntRateBuIm.setAdjSpotRateDisc(adjSpotDisc);
							dcntRateBuIm.setAdjSpotRateCont(adjSpotCont);
							dcntRateBuIm.setAddSprd(shkCont); // addSprd
							dcntRateBuIm.setLastModifiedBy(jobId);
							dcntRateBuIm.setLastUpdateDate(LocalDateTime.now());

							rst.add(dcntRateBuIm);
						  }
						}
				}
		  }
		}
		log.info("{}({}) creates [{}] results of [{}]. They are inserted into [{}] Table", jobId, EJob.valueOf(jobId).getJobName(), rst.size(), applBizDv, toPhysicalName(IrDcntRateBu.class.getSimpleName()));

		return rst;
	}

}

