package com.gof.test;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.groupingBy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.hql.internal.classic.GroupByParser;
import org.hibernate.query.Query;

import com.gof.dao.HisDao;
import com.gof.dao.IrCurveSpotWeekDao;
import com.gof.dao.IrCurveYtmDao;
import com.gof.dao.IrParamSwDao;
import com.gof.entity.IrCurveSpotWeek;
import com.gof.entity.IrCurveYtm;
import com.gof.entity.IrParamSw;
import com.gof.util.HibernateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class dbtest {
	private static Session session = HibernateUtil.getSessionFactory().openSession();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, Object> param;
//		List<IrParamSw> sw = IrParamSwDao.getIrParamSwList(", biz, curve, sceNo);

		String iRateHisStBaseDate ="20100101";
//		String s ="select a from HIrDcntRateBiz a where 1=1 and applBizDv like  '%' || :applBizDv|| '%' and baseYymm = :baseYymm and seq = :seq ";
		List<IrCurveSpotWeek> azzz     =  IrCurveSpotWeekDao.getIrCurveSpotWeekHisDate("202206", "1010000", 5).stream()
															.limit(416)
															.collect(toList());
		
		String aaa = IrCurveSpotWeekDao.getIrCurveSpotWeekHisDate("202206", "1010000", 5).stream()
				.limit(416)
				.sorted()
				.limit(1)
				.map(s->s.getBaseDate())
				.findFirst().orElse(iRateHisStBaseDate)
				;
		
		
		String bbb = azzz.stream()
				.sorted()
				.limit(1)
				.map(s->s.getBaseDate())
				.findFirst().get()
				;
		azzz.forEach(s-> log.info("aaa :{}, {}, {}", aaa, bbb, s.toString()));
		
		
		for( IrCurveSpotWeek aa : azzz) {
			if( iRateHisStBaseDate.compareTo(aa.getBaseDate()) > 1) {
				iRateHisStBaseDate = aa.getBaseDate();
			}
//			log.info("aaa : {},{}", iRateHisStBaseDate, aa.toString());
		}
//
//		Query<T> q = session.createQuery(s);
//
//		param = new HashMap<String, Object>();
//		param.put("baseYymm", "202209");
//		param.put("seq", 1);
//		param.put("applBizDv", "IFRS");
//
//
//		for (Map.Entry<String, Object> entry : param.entrySet()) {
//			q.setParameter(entry.getKey(), entry.getValue());
//		}
////		log.info("Query : {},{}", klass.getSimpleName(), builder.toString());
//
//		q.stream().forEach( s-> log.info("a : {}", s.toString()));

		String bssd ="202209";
		String bizDv ="IFRS";

//		HisDao.getHIrDcntRateBiz(session, bssd, 3, bizDv).forEach(s-> log.info("aaa : {}", s.toString()));
//		HisDao.getHIrDcntSceDetBiz(session, bssd, 3, bizDv).forEach(s-> log.info("aaa : {}", s.toString()));
//		HisDao.getHIrDcntSceStoBiz(session, bssd, 1, bizDv).forEach(s-> log.info("aaa : {}", s.toString()));
//		HisDao.getHIrParamAfnsBiz(session, bssd, 3, bizDv).forEach(s-> log.info("aaa : {}", s.toString()));
//		HisDao.getHIrParamHwBiz(session, bssd, 3, bizDv).forEach(s-> log.info("aaa : {}", s.toString()));
//		HisDao.getHIrSprdAfnsBiz(session, bssd, 3, bizDv).forEach(s-> log.info("aaa : {}", s.toString()));
//		HisDao.getHIrSprdLpBiz(session, bssd, 3, bizDv).forEach(s-> log.info("aaa : {}", s.toString()));
//		HisDao.getHRcCorpPdBiz(session, bssd, 3, bizDv).forEach(s-> log.info("aaa : {}", s.toString()));

	}

}
