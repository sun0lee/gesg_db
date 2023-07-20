package com.gof.dao;

import java.util.List;
import java.util.stream.Stream;

import org.hibernate.Session;

import com.gof.entity.his.HIrDcntRateBiz;
import com.gof.entity.his.HIrDcntSceDetBiz;
import com.gof.entity.his.HIrDcntSceStoBiz;
import com.gof.entity.his.HIrParamAfnsBiz;
import com.gof.entity.his.HIrParamHwBiz;
import com.gof.entity.his.HIrParamModelBiz;
import com.gof.entity.his.HIrSprdAfnsBiz;
import com.gof.entity.his.HIrSprdLpBiz;
import com.gof.entity.his.HRcCorpPdBiz;
import com.gof.entity.his.TransferJob;
import com.gof.util.HibernateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HisDao extends DaoUtil {

	private static Session session = HibernateUtil.getSessionFactory().openSession();

	public static List<TransferJob> getTransferJob(String bssd) {

		String q = " select a from TransferJob a "
				 + "  where 1=1              "
				 + "    and a.baseYymm = :bssd "
				 ;

		return session.createQuery(q, TransferJob.class)
				.setParameter("bssd"     , bssd)
				.getResultList();
	}

	public static int getSeq(String entityName, String bssd) {
		String query = "select max(a.seq)                    "
					 + "  from "
					 + entityName
					 + " a  where 1=1                                "
					 + "   and a.baseYymm  = :bssd	              "
					 ;
		Object maxSeq = session.createQuery(query)
								.setParameter("bssd", bssd)
								.uniqueResult();

		if(maxSeq == null) {
			log.warn(entityName + " is not found at [{}]. Return 0 ",  bssd);
			return 0;
		}

		return Integer.parseInt(maxSeq.toString());
	}

	public static int getSeqByBaseDate(String entityName, String bssd) {
		String query = "select max(a.seq)                    "
					 + "  from "
					 + entityName
					 + " a  where 1=1                                "
					 + " and substr(a.baseDate,1,6)  = :bssd	              "
					 ;
		Object maxSeq = session.createQuery(query)
								.setParameter("bssd", bssd)
								.uniqueResult();

		if(maxSeq == null) {
			log.warn(entityName + " is not found at [{}]. Return 0",  bssd);
			return 0;
		}

		return Integer.parseInt(maxSeq.toString());
	}

	public static Stream<HIrDcntRateBiz> getHIrDcntRateBiz(Session s , String bssd, int seq, String applBizDv) {
		String query = "select a from HIrDcntRateBiz a "
				 + " where 1=1 "
				 + "   and a.baseYymm     = :bssd      "
				 + "   and a.applBizDv    like '%'|| :applBizDv ||'%' "
				 + "   and a.seq    	  = :seq"
				 ;

		return session.createQuery(query, HIrDcntRateBiz.class)
		      	  .setParameter("bssd", bssd)
		      	  .setParameter("applBizDv", applBizDv)
		      	  .setParameter("seq", seq)
				  .stream();
	}

	public static Stream<HIrDcntSceDetBiz> getHIrDcntSceDetBiz(Session s , String bssd, int seq, String applBizDv) {
		String query = "select a from HIrDcntSceDetBiz a "
				 + " where 1=1 "
				 + "   and a.baseYymm     = :bssd      "
				 + "   and a.applBizDv    like '%'|| :applBizDv ||'%' "
				 + "   and a.seq    	  = :seq"
				 ;

		return session.createQuery(query, HIrDcntSceDetBiz.class)
		      	  .setParameter("bssd", bssd)
		      	  .setParameter("applBizDv", applBizDv)
		      	  .setParameter("seq", seq)
				  .stream();
	}

	public static Stream<HIrDcntSceStoBiz> getHIrDcntSceStoBiz(Session s , String bssd, int seq, String applBizDv) {
		String query = "select a from HIrDcntSceStoBiz a "
				 + " where 1=1 "
				 + "   and a.baseYymm     = :bssd      "
				 + "   and a.applBizDv    like '%'|| :applBizDv ||'%' "
				 + "   and a.seq    	  = :seq"
				 ;

		return session.createQuery(query, HIrDcntSceStoBiz.class)
		      	  .setParameter("bssd", bssd)
		      	  .setParameter("applBizDv", applBizDv)
		      	  .setParameter("seq", seq)
				  .stream();
	}

	public static Stream<HIrParamAfnsBiz> getHIrParamAfnsBiz(Session s , String bssd, int seq, String applBizDv) {
		String query = "select a from HIrParamAfnsBiz a "
				 + " where 1=1 "
				 + "   and a.baseYymm     = :bssd      "
//				 + "   and a.applBizDv    like '%'|| :applBizDv ||'%' "
				 + "   and a.seq    	  = :seq"
				 ;

		return session.createQuery(query, HIrParamAfnsBiz.class)
		      	  .setParameter("bssd", bssd)
//		      	  .setParameter("applBizDv", applBizDv)
		      	  .setParameter("seq", seq)
				  .stream();
	}
	public static Stream<HIrParamHwBiz> getHIrParamHwBiz(Session s , String bssd, int seq, String applBizDv) {
		String query = "select a from HIrParamHwBiz a "
				 + " where 1=1 "
				 + "   and a.baseYymm     = :bssd      "
				 + "   and a.applBizDv    like '%'|| :applBizDv ||'%' "
				 + "   and a.seq    	  = :seq"
				 ;

		return session.createQuery(query, HIrParamHwBiz.class)
		      	  .setParameter("bssd", bssd)
		      	  .setParameter("applBizDv", applBizDv)
		      	  .setParameter("seq", seq)
				  .stream();
	}
	public static Stream<HIrParamModelBiz> getHIrParamModelBiz(Session s , String bssd, int seq, String applBizDv) {
		String query = "select a from HIrParamModelBiz a "
				 + " where 1=1 "
				 + "   and a.baseYymm     = :bssd      "
				 + "   and a.applBizDv    like '%'|| :applBizDv ||'%' "
				 + "   and a.seq    	  = :seq"
				 ;

		return session.createQuery(query, HIrParamModelBiz.class)
		      	  .setParameter("bssd", bssd)
		      	  .setParameter("applBizDv", applBizDv)
		      	  .setParameter("seq", seq)
				  .stream();
	}
	public static Stream<HIrSprdAfnsBiz> getHIrSprdAfnsBiz(Session s , String bssd, int seq, String applBizDv) {
		String query = "select a from HIrSprdAfnsBiz a "
				 + " where 1=1 "
				 + "   and a.baseYymm     = :bssd      "
//				 + "   and a.applBizDv    like '%'|| :applBizDv ||'%' "
				 + "   and a.seq    	  = :seq"
				 ;

		return session.createQuery(query, HIrSprdAfnsBiz.class)
		      	  .setParameter("bssd", bssd)
//		      	  .setParameter("applBizDv", applBizDv)
		      	  .setParameter("seq", seq)
				  .stream();
	}
	public static Stream<HIrSprdLpBiz> getHIrSprdLpBiz(Session s , String bssd, int seq, String applBizDv) {
		String query = "select a from HIrSprdLpBiz a "
				 + " where 1=1 "
				 + "   and a.baseYymm     = :bssd      "
				 + "   and a.applBizDv    like '%'|| :applBizDv ||'%' "
				 + "   and a.seq    	  = :seq"
				 ;

		return session.createQuery(query, HIrSprdLpBiz.class)
		      	  .setParameter("bssd", bssd)
		      	  .setParameter("applBizDv", applBizDv)
		      	  .setParameter("seq", seq)
				  .stream();
	}

	public static Stream<HRcCorpPdBiz> getHRcCorpPdBiz(String bssd, int seq, String applBizDv) {
		String query = "select a from HRcCorpPdBiz a "
				 + " where 1=1 "
				 + "   and a.baseYymm     = :bssd      "
				 + "   and a.applBizDv    like '%'|| :applBizDv ||'%' "
				 + "   and a.seq    	  = :seq"
				 ;

		return session.createQuery(query, HRcCorpPdBiz.class)
			      	  .setParameter("bssd", bssd)
			      	  .setParameter("applBizDv", applBizDv)
			      	  .setParameter("seq", seq)
					  .stream();
	}

	public static List<HRcCorpPdBiz> getHRcCorpPdBizList(String bssd, int seq, String applBizDv) {
		String query = "select a from HRcCorpPdBiz a "
				 + " where 1=1 "
				 + "   and a.baseYymm     = :bssd      "
				 + "   and a.applBizDv    like '%'|| :applBizDv ||'%' "
				 + "   and a.seq    	  = :seq"
				 ;

		return session.createQuery(query, HRcCorpPdBiz.class)
			      	  .setParameter("bssd", bssd)
			      	  .setParameter("applBizDv", applBizDv)
			      	  .setParameter("seq", seq)
					  .getResultList();
	}


}
