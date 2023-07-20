package com.gof.dao;

import java.util.List;

import org.hibernate.Session;

import com.gof.entity.RcCorpPd;
import com.gof.entity.RcCorpPdBiz;
import com.gof.entity.RcCorpTm;
import com.gof.entity.RcCorpTmUsr;
import com.gof.entity.RcInflationBiz;
import com.gof.entity.RcSegLgdBiz;
import com.gof.entity.RcSegPrepRateBiz;
import com.gof.util.HibernateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RcCorpPdDao extends DaoUtil {

	private static Session session = HibernateUtil.getSessionFactory().openSession();

	public static List<RcCorpTm> getRcCorpTm(String bssd, String crdEvalAgncyCd) {

		String baseYymm = getMaxBaseYymm(bssd, crdEvalAgncyCd);

		String query = " select a                                  "
				     + "   from RcCorpTm a                         "
		 		 	 + "  where a.baseYymm = :baseYymm             "
		 		 	 + "    and a.crdEvalAgncyCd = :crdEvalAgncyCd "
		 		 	 ;

		return session.createQuery(query, RcCorpTm.class)
					  .setParameter("baseYymm", baseYymm)
					  .setParameter("crdEvalAgncyCd", crdEvalAgncyCd)
					  .getResultList()
					  ;
	}

	public static List<RcCorpTm> getRcCorpTm(String bssd) {
		String query = " select a                                  "
				     + "   from RcCorpTm a                         "
		 		 	 + "  where a.baseYymm = :baseYymm             "
		 		 	 ;

		return session.createQuery(query, RcCorpTm.class)
					  .setParameter("baseYymm", bssd)
					  .getResultList()
					  ;
	}

	public static List<RcCorpPd> getRcCorpPd(String bssd) {
		String query = " select a                                  "
				     + "   from RcCorpPd a                         "
		 		 	 + "  where a.baseYymm = :baseYymm             "
		 		 	 ;

		return session.createQuery(query, RcCorpPd.class)
					  .setParameter("baseYymm", bssd)
					  .getResultList()
					  ;
	}

	public static List<RcCorpPdBiz> getRcCorpPdBiz(String bssd) {
		String query = " select a                                  "
				     + "   from RcCorpPdBiz a                         "
		 		 	 + "  where a.baseYymm = :baseYymm             "
		 		 	 ;

		return session.createQuery(query, RcCorpPdBiz.class)
					  .setParameter("baseYymm", bssd)
					  .getResultList()
					  ;
	}

	public static String getMaxBaseYymm(String bssd, String crdEvalAgncyCd) {

		String query = "select max(a.baseYymm)                    "
					 + "  from RcCorpTm a                         "
					 + " where 1=1                                "
					 + "   and a.baseYymm <= :bssd	              "
					 + "   and a.crdEvalAgncyCd = :crdEvalAgncyCd "
					 ;

		Object maxDate = session.createQuery(query)
								.setParameter("bssd", bssd)
								.setParameter("crdEvalAgncyCd", crdEvalAgncyCd)
								.uniqueResult();

		if(maxDate == null) {
			log.warn("Corporate Transition Matrix is not found [AGENCY: {}] at [{}]", crdEvalAgncyCd, bssd);
			return bssd;
		}

		return maxDate.toString();
	}


	public static String getMaxBaseYymm(String bssd) {

		String query = "select max(a.baseYymm)                    "
					 + "  from RcCorpTm a                         "
					 + " where 1=1                                "
					 + "   and a.baseYymm <= :bssd	              "
					 ;

		Object maxDate = session.createQuery(query)
								.setParameter("bssd", bssd)
								.uniqueResult();

		if(maxDate == null) {
			log.warn("Corporate Transition Matrix is not found at [{}]", bssd);
			return bssd;
		}

		return maxDate.toString();
	}


	public static List<String> getAgencyCd(String bssd) {

		String baseYymm = getMaxBaseYymm(bssd);

		String query = "select distinct(a.crdEvalAgncyCd)         "
					 + "  from RcCorpTm a                         "
					 + " where 1=1                                "
					 + "   and a.baseYymm = :baseYymm	          "
					 ;

		@SuppressWarnings("unchecked")
		List<String> agencyList = session.createQuery(query)
					   				     .setParameter("baseYymm", baseYymm)
									     .getResultList();

		if(agencyList == null) log.warn("Corporate Transition Matrix is not found at [{}]", bssd);

		return agencyList;
	}


	public static List<RcCorpTmUsr> getRcCorpTmUsr(String bssd, String crdEvalAgncyCd) {

		String baseYymm = getMaxBaseYymmUsr(bssd, crdEvalAgncyCd);

		String query = " select a                                  "
				     + "   from RcCorpTmUsr a                      "
		 		 	 + "  where a.baseYymm = :baseYymm             "
		 		 	 + "    and a.crdEvalAgncyCd = :crdEvalAgncyCd "
		 		 	 ;

		return session.createQuery(query, RcCorpTmUsr.class)
					  .setParameter("baseYymm", baseYymm)
					  .setParameter("crdEvalAgncyCd", crdEvalAgncyCd)
					  .getResultList()
					  ;
	}


	public static String getMaxBaseYymmUsr(String bssd, String crdEvalAgncyCd) {

		String query = "select max(a.baseYymm)                    "
					 + "  from RcCorpTmUsr a                      "
					 + " where 1=1                                "
					 + "   and a.baseYymm <= :bssd	              "
					 + "   and a.crdEvalAgncyCd = :crdEvalAgncyCd "
					 ;

		Object maxDate = session.createQuery(query)
								.setParameter("bssd", bssd)
								.setParameter("crdEvalAgncyCd", crdEvalAgncyCd)
								.uniqueResult();

		if(maxDate == null) {
			log.warn("Corporate Transition Matrix User Defined is not found [AGENCY: {}] at [{}]", crdEvalAgncyCd, bssd);
			return bssd;
		}

		return maxDate.toString();
	}


	public static String getMaxBaseYymmUsr(String bssd) {

		String query = "select max(a.baseYymm)                    "
					 + "  from RcCorpTmUsr a                      "
					 + " where 1=1                                "
					 + "   and a.baseYymm <= :bssd	              "
					 ;

		Object maxDate = session.createQuery(query)
								.setParameter("bssd", bssd)
								.uniqueResult();

		if(maxDate == null) {
			log.warn("Corporate Transition Matrix User Defined is not found at [{}]", bssd);
			return bssd;
		}

		return maxDate.toString();
	}


	public static List<String> getAgencyCdUsr(String bssd) {

		String baseYymm = getMaxBaseYymmUsr(bssd);

		String query = "select distinct(a.crdEvalAgncyCd)         "
					 + "  from RcCorpTmUsr a                      "
					 + " where 1=1                                "
					 + "   and a.baseYymm = :baseYymm	          "
					 ;

		@SuppressWarnings("unchecked")
		List<String> agencyList = session.createQuery(query)
					   				     .setParameter("baseYymm", baseYymm)
									     .getResultList();

		if(agencyList == null) log.warn("Corporate Transition Matrix User Defined is not found at [{}]", bssd);

		return agencyList;
	}


	public static List<RcInflationBiz> getRcInflationBiz(String bssd) {
		String query = " select a                                  "
				     + "   from RcInflationBiz a                         "
		 		 	 + "  where a.baseYymm = :baseYymm             "
		 		 	 ;

		return session.createQuery(query, RcInflationBiz.class)
					  .setParameter("baseYymm", bssd)
					  .getResultList()
					  ;
	}

	public static List<RcSegLgdBiz> getRcSegLgdBiz(String bssd) {
		String query = " select a                                  "
				     + "   from RcSegLgdBiz a                         "
		 		 	 + "  where a.baseYymm = :baseYymm             "
		 		 	 ;

		return session.createQuery(query, RcSegLgdBiz.class)
					  .setParameter("baseYymm", bssd)
					  .getResultList()
					  ;
	}

	public static List<RcSegPrepRateBiz> getRcSegPrepRateBiz(String bssd) {
		String query = " select a                                  "
				     + "   from RcSegPrepRateBiz a                         "
		 		 	 + "  where a.baseYymm = :baseYymm             "
		 		 	 ;

		return session.createQuery(query, RcSegPrepRateBiz.class)
					  .setParameter("baseYymm", bssd)
					  .getResultList()
					  ;
	}
}
