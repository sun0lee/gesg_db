package com.gof.dao;

import java.util.List;

import org.hibernate.Session;

import com.gof.entity.IrDiscRateUsr;
import com.gof.entity.RcCorpPd;
import com.gof.entity.RcCorpPdBiz;
import com.gof.entity.RcCorpTm;
import com.gof.entity.RcCorpTmUsr;
import com.gof.entity.RcInflationBiz;
import com.gof.entity.RcInflationUsr;
import com.gof.entity.RcSegLgdBiz;
import com.gof.entity.RcSegLgdUsr;
import com.gof.entity.RcSegPrepRateBiz;
import com.gof.entity.RcSegPrepRateUsr;
import com.gof.util.HibernateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RcDao extends DaoUtil {

	private static Session session = HibernateUtil.getSessionFactory().openSession();

	
	public static String getLgdMaxBaseYymm(String bssd) {

		String query = "select max(a.applStYymm)                    "
					 + "  from RcSegLgdUsr a                         "
					 + "  where a.applStYymm >= :baseYymm             "
				     + "  and a.applEdYymm <= :baseYymm "
					 ;

		Object maxDate = session.createQuery(query)
								.setParameter("baseYymm", bssd)
								.uniqueResult();

		if(maxDate == null) {
			return bssd;
		}

		return maxDate.toString();
	}
	
	
	public static List<RcSegLgdUsr> getRcSegLgdUsr(String bssd) {
		String baseYymm = getLgdMaxBaseYymm(bssd);
		
		String query = " select a                                  "
				     + "   from RcSegLgdUsr a                      "
		 		 	 + "  where a.applStYymm = :baseYymm          "
		 		 	 ;

		return session.createQuery(query, RcSegLgdUsr.class)
					  .setParameter("baseYymm", baseYymm)
					  .getResultList()
					  ;
	}

	
	public static String getPrepayMaxBaseYymm(String bssd) {

		String query = "select max(a.applStYymm)                    "
					 + "  from RcSegPrepRateUsr a                         "
					 + "  where a.applStYymm >= :baseYymm             "
				     + "  and a.applEdYymm <= :baseYymm "
					 ;

		Object maxDate = session.createQuery(query)
								.setParameter("baseYymm", bssd)
								.uniqueResult();

		if(maxDate == null) {
			return bssd;
		}

		return maxDate.toString();
	}
	public static List<RcSegPrepRateUsr> getRcSegPrepRateUsr(String bssd) {
		
		String baseYymm = getPrepayMaxBaseYymm(bssd);
		
		String query = " select a                                  "
				     + "   from RcSegPrepRateUsr a                         "
				     + "  where a.applStYymm = :baseYymm             "
		 		 	 ;

		return session.createQuery(query, RcSegPrepRateUsr.class)
					  .setParameter("baseYymm", baseYymm)
					  .getResultList()
					  ;
	}

	public static List<RcInflationUsr> getRcInflationUsr(String bssd) {
		String query = " select a                           "
				     + "   from RcInflationUsr a            "
		 		 	 + "  where a.baseYymm = :baseYymm      "
		 		 	 + "  order by a.settingYymm desc"
		 		 	 ;

		return session.createQuery(query, RcInflationUsr.class)
					  .setParameter("baseYymm", bssd)
					  .getResultList()
					  ;
	}

	public static List<IrDiscRateUsr> getIrDiscRateUsr(String bssd) {
		String query = " select a                           "
				     + "   from IrDiscRateUsr a            "
		 		 	 + "  where a.baseYymm = :baseYymm      "
		 		 	 ;

		return session.createQuery(query, IrDiscRateUsr.class)
					  .setParameter("baseYymm", bssd)
					  .getResultList()
					  ;
	}

}
