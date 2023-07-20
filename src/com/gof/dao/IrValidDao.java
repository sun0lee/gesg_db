package com.gof.dao;

import java.util.List;
import java.util.stream.Stream;

import org.hibernate.Session;

import com.gof.entity.IrQvalSce;
import com.gof.entity.IrValidParamHw;
import com.gof.entity.IrValidRnd;
import com.gof.entity.IrValidSceSto;
import com.gof.util.HibernateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IrValidDao extends DaoUtil {

	private static Session session = HibernateUtil.getSessionFactory().openSession();

	public static List<IrQvalSce> getIrQvalSceList(String bssd) {

		String query = " select a from IrQvalSce a       	  "
		 		 	 + "  where a.baseYymm = :bssd       	  "
		 		 	 ;

		return session.createQuery(query, IrQvalSce.class)
					  .setParameter("bssd", bssd)
					  .getResultList()
					  ;
	}

	public static Stream<IrQvalSce> getIrQvalSceStream(String bssd) {

		String query = " select a from IrQvalSce a       	  "
		 		 	 + "  where a.baseYymm = :bssd       	  "
		 		 	 ;

		return session.createQuery(query, IrQvalSce.class)
					  .setParameter("bssd", bssd)
					  .stream()
					  ;
	}

	public static List<IrValidParamHw> getIrValidParamHwList(String bssd) {

		String query = " select a from IrValidParamHw a       	  "
		 		 	 + "  where a.baseYymm = :bssd       	  "
		 		 	 ;

		return session.createQuery(query, IrValidParamHw.class)
					  .setParameter("bssd", bssd)
					  .getResultList()
					  ;
	}
	public static Stream<IrValidParamHw> getIrValidParamHwStream(String bssd) {

		String query = " select a from IrValidParamHw a       	  "
		 		 	 + "  where a.baseYymm = :bssd       	  "
		 		 	 ;

		return session.createQuery(query, IrValidParamHw.class)
					  .setParameter("bssd", bssd)
					  .stream()
					  ;
	}

	public static Stream<IrValidRnd> getIrValidRndStream(String bssd) {

		String query = " select a from IrValidRnd a       	  "
		 		 	 + "  where a.baseYymm = :bssd       	  "
		 		 	 ;

		return session.createQuery(query, IrValidRnd.class)
					  .setParameter("bssd", bssd)
					  .stream()
					  ;
	}
	public static Stream<IrValidSceSto> getIrValidSceStoStream(String bssd) {

		String query = " select a from IrValidSceSto a       	  "
		 		 	 + "  where a.baseYymm = :bssd       	  "
		 		 	 ;

		return session.createQuery(query, IrValidSceSto.class)
					  .setParameter("bssd", bssd)
					  .stream()
					  ;
	}
}
