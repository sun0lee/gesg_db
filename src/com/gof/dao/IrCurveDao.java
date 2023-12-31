package com.gof.dao;

import java.util.List;

import org.hibernate.Session;

import com.gof.entity.FxRateHis;
import com.gof.entity.IrCurve;
import com.gof.entity.IrCurveFwd;
import com.gof.enums.EBoolean;
import com.gof.util.HibernateUtil;

public class IrCurveDao extends DaoUtil {

	private static Session session = HibernateUtil.getSessionFactory().openSession();

	public static List<IrCurve> getIrCurveList() {

		String q = " select a from IrCurve a "
				 + "  where 1=1              "
				 + "    and a.useYn = :useYn "
				 ;

		return session.createQuery(q, IrCurve.class)
				.setParameter("useYn"     , EBoolean.Y)
				.getResultList();
	}


	public static List<IrCurve> getIrCurveList(String applMethDv) {

		String q = " select a from IrCurve a           "
				 + "  where 1=1                        "
				 + "  	and a.applMethDv = :applMethDv "
				 + "    and a.useYn      = :useYn      "
				 ;

		return session.createQuery(q, IrCurve.class)
				.setParameter("applMethDv", applMethDv)
				.setParameter("useYn"     , EBoolean.Y)
				.getResultList();
	}


	public static List<IrCurveFwd> getIrCurveFwdList(String bssd) {

		String q = " select a from IrCurveFwd a "
				 + "  where 1=1              "
				 + "  and substr(a.baseDate, 1,6)  = :bssd	  "
				 ;


		return session.createQuery(q, IrCurveFwd.class)
				.setParameter("bssd", bssd)
				.getResultList();
	}

}
