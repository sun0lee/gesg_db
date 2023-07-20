package com.gof.dao;

import java.util.List;

import org.hibernate.Session;

import com.gof.entity.FxRateHis;
import com.gof.entity.IrCurve;
import com.gof.enums.EBoolean;
import com.gof.util.HibernateUtil;

public class FxRateDao extends DaoUtil {

	private static Session session = HibernateUtil.getSessionFactory().openSession();

	public static List<FxRateHis> getFxRateHis(String bssd) {

		String q = " select a from FxRateHis a "
				 + "  where 1=1              "
				 + "  and substr(a.baseDate, 1,6)  = :bssd	  "
				 ;

		return session.createQuery(q, FxRateHis.class)
				   .setParameter("bssd", bssd)
				   .getResultList();
	}

}
