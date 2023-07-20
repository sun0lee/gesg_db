package com.gof.process;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.gof.enums.EJob;
import com.gof.dao.IrCurveSpotDao;
import com.gof.dao.IrCurveYtmDao;
import com.gof.dao.IrDcntRateDao;
import com.gof.dao.IrSprdDao;
import com.gof.entity.IrCurveSpot;
import com.gof.entity.IrCurveYtm;
import com.gof.entity.IrDcntRateBuIm;
import com.gof.entity.IrDcntSceIm;
import com.gof.entity.IrParamSw;
import com.gof.entity.IrSprdAfnsCalc;
import com.gof.model.SmithWilsonKics;
import com.gof.model.entity.SmithWilsonRslt;
import com.gof.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Esg770_ShkScen extends Process {

	public static final Esg770_ShkScen INSTANCE = new Esg770_ShkScen();
	public static final String jobId = INSTANCE.getClass().getSimpleName().toUpperCase().substring(0, ENTITY_LENGTH);


	public static List<IrDcntSceIm> createAfnsShockScenario(String bssd
			                                              , String irModelId
			                                              , String applBizDv
			                                              , Map<String, Map<Integer, IrParamSw>> paramSwMap
			                                              , Integer projectionYear)
{

		List<IrDcntSceIm> irScenarioList  = new ArrayList<IrDcntSceIm>();

		for(Map.Entry<String, Map<Integer, IrParamSw>> curveSwMap : paramSwMap.entrySet()) {

			int detScen = 1 ; //swSce.getKey()
			Map<Integer, IrParamSw> swMap = curveSwMap.getValue();
			IrParamSw swSce = swMap.get(detScen);
			LocalDate baseDate = DateUtil.convertFrom(bssd).with(TemporalAdjusters.lastDayOfMonth());

			//�옄�궛 sw 蹂닿컙 紐⑹쟻 ltfr
			List<IrCurveYtm> ytmList = IrCurveYtmDao.getIrCurveYtm(bssd, curveSwMap.getKey());
			List<IrCurveSpot> spotList = IrCurveSpotDao.getIrCurveSpot(bssd, curveSwMap.getKey());

			if (swSce != null) {

			// �깮�꽦�빐�빞 �븯�뒗 寃곌낵�뒗 det, sto �떆�굹由ъ삤 �몮 �떎 �엳�쓬. ( irModel�뿉 �뵲�씪 �떖�씪吏� )
//			int scenCnt = 5 ;
//			int scenCnt = Math.min(IrSprdDao.getIrSprdAfnsCalcScenCnt(bssd, irModelId, curveSwMap.getKey()),10);
//			int scenCnt = IrSprdDao.getIrSprdAfnsCalcScenCnt(bssd, irModelId, curveSwMap.getKey());
			long scenCnt = IrSprdDao.getIrSprdAfnsCalcAll(bssd, irModelId, curveSwMap.getKey()).stream().count();

			for (int sceNo = 1; sceNo <= scenCnt; sceNo++) {
				Map<String,Double> irSprdShkMap = IrSprdDao.getIrSprdAfnsCalcList(bssd, irModelId, curveSwMap.getKey(), sceNo).stream()
								.collect(Collectors.toMap(IrSprdAfnsCalc::getMatCd, IrSprdAfnsCalc::getShkSprdCont));

				List<IrCurveSpot> irDcntRateBuImList = IrDcntRateDao.getIrDcntRateBuImToAdjSpotList(bssd, applBizDv, irModelId, curveSwMap.getKey(), sceNo);
				if(irDcntRateBuImList.size()==0) {
					log.warn("No IR Dcnt Rate Data [BIZ: {}, IR_CURVE_ID: {}, IR_CURVE_SCE_NO: {}] in [{}] for [{}]", applBizDv, curveSwMap.getKey(),sceNo , toPhysicalName(IrDcntRateBuIm.class.getSimpleName()), bssd);
					continue;
				}

				// sw 蹂닿컙/蹂댁쇅 check �뿰�냽蹂듬━�씠�쑉�쓣 媛��졇�솕�쑝誘�濡� CMPD_MTD_CONT
	//			SmithWilsonKics sw = new SmithWilsonKics(baseDate, irDcntRateBuImList, CMPD_MTD_CONT, true, swSce.getLtfr(), swSce.getLtfrCp(), projectionYear, 1, 100, DCB_MON_DIF);
	//			List<SmithWilsonRslt> swRslt = sw.getSmithWilsonResultList();

	//			�옄�궛, 遺�梨� 蹂닿컙
				SmithWilsonKics sw ;
				List<SmithWilsonRslt> swRslt;

				if(applBizDv.endsWith("_A")) {
					String matCd = ytmList.get(ytmList.size()-1).getMatCd();
					int matMonths = Integer.parseInt(matCd.substring(1));

					double ytm = ytmList.get(ytmList.size()-1).getYtm();
					int ltfrTA = matMonths/12;
					double spot = spotList.get(ytmList.size()-1).getSpotRate();
					
					

					String tmpMatCd = curveSwMap.getKey().equals("1010000")? "M0240": "M0360";
//					String tmpMatCd = curveSwMap.getKey().equals("1010000")? "M0240": "M0240";
					double spread = irSprdShkMap.get(tmpMatCd);
					double ltfrA = irDiscToCont(spot) + spread;
					
					

//					log.info("Job770 ASSET :  {},{},{},{},{},{},{},{}", sceNo, curveSwMap.getKey(), ltfrA, ltfrTA, ytm, spot, spread);

//					sw = new SmithWilsonKics(baseDate, irDcntRateBuImList, CMPD_MTD_CONT, true, ltfrA, ltfrTA, projectionYear, 1, 100, DCB_MON_DIF);
					sw = new SmithWilsonKics(baseDate, irDcntRateBuImList, CMPD_MTD_CONT, true, ltfrA, swSce.getLtfrCp(), projectionYear, 1, 100, DCB_MON_DIF);
					swRslt = sw.getSmithWilsonResultList();
				}
				else {
					sw = new SmithWilsonKics(baseDate, irDcntRateBuImList, CMPD_MTD_CONT, true, swSce.getLtfr(), swSce.getLtfrCp(), projectionYear, 1, 100, DCB_MON_DIF);
					swRslt = sw.getSmithWilsonResultList();
				}

			// 寃곌낵 �벐湲�
				for(SmithWilsonRslt rslt : swRslt) {

					IrDcntSceIm ir  = new IrDcntSceIm();

					ir.setBaseYymm(bssd);
				    ir.setApplBizDv(applBizDv);
					ir.setIrModelId(irModelId);
					ir.setIrCurveId(curveSwMap.getKey());
					ir.setMatCd(rslt.getMatCd());
					ir.setIrCurveSceNo(sceNo);
					ir.setSpotRate(rslt.getSpotDisc());
					ir.setFwdRate(0.0);
					ir.setLastModifiedBy(jobId);
					ir.setLastUpdateDate(LocalDateTime.now());

					irScenarioList.add(ir);

					if( rslt.getSpotDisc() > 100) {
						log.error("zzzzzz : {}", rslt.getMatCd(), rslt.getSpotCont());

					}
				}
//			log.info("scenNo: {}, swAlpha: {}", sceNo, sw.getAlphaApplied() );
		  }
		}
	   }
		log.info("{}({}) creates {} results. They are inserted into [{}] Table", jobId, EJob.valueOf(jobId).getJobName(), irScenarioList.size(), toPhysicalName(IrDcntSceIm.class.getSimpleName()));

		return irScenarioList;
		}

}
