package com.gof.dao;

import java.util.List;
import java.util.stream.Stream;

import org.hibernate.Session;

import com.gof.entity.IrDcntSceDetBiz;
import com.gof.entity.IrDcntSceStoBiz;
import com.gof.entity.IrDcntSceStoGnr;
import com.gof.entity.IrDcntSceStoHis;
import com.gof.util.FinUtils;
import com.gof.util.HibernateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IrCurveSceDao extends DaoUtil {

	private static Session session = HibernateUtil.getSessionFactory().openSession();

	public static List<IrDcntSceStoHis> getIrDcntSceStoHis(String bssd, Integer jobSeq, String applBizDv) {

		String query = "select a from IrDcntSceStoHis a "
					 + "where 1=1 "
					 + "and a.baseYymm  = :bssd	"
					 + "and a.applBizDv = :applBizDv	"
					 + "and a.jobSeq    = :jobSeq	"
					 ;

		return   session.createQuery(query, IrDcntSceStoHis.class)
									.setParameter("bssd", FinUtils.toEndOfMonth(bssd))
									.setParameter("applBizDv", applBizDv)
									.setParameter("jobSeq", jobSeq)
								 .getResultList();

	}

	public static int getMaxJobSeq(String bssd, String applBizDv) {

		String query = "select max(a.jobSeq) "
					 + "from IrDcntSceStoHis a "
					 + "where 1=1 "
					 + "and a.baseYymm = :bssd	"
					 + "and a.applBizDv = :applBizDv	"
					 ;

		Object maxSeq = session.createQuery(query)
								.setParameter("bssd", FinUtils.toEndOfMonth(bssd))
								.setParameter("applBizDv", applBizDv)
								.uniqueResult();

		if(maxSeq == null) {
			return 1;
		}

		return Integer.valueOf(maxSeq.toString());
	}

	public static List<IrDcntSceStoBiz> getIrDcntSceStoBiz(String bssd) {

		String query = "select a from IrDcntSceStoBiz a "
					 + "where 1=1 "
					 + "and a.baseYymm  = :bssd	"
					 ;

		return   session.createQuery(query, IrDcntSceStoBiz.class)
									.setParameter("bssd", bssd)
								 .getResultList();

	}

	public static Stream<IrDcntSceStoBiz> getIrDcntSceStoBizStream(Session s, String bssd) {

		String query = "select a from IrDcntSceStoBiz a "
					 + "where 1=1 "
					 + "and a.baseYymm  = :bssd	"
					 ;

		return   s.createQuery(query, IrDcntSceStoBiz.class)
									.setParameter("bssd", bssd)
									.stream();

	}

	public static Stream<IrDcntSceStoBiz> getIrDcntSceStoBizStream() {

		String query = "select a from IrDcntSceStoBiz a "
					 + "where 1=1 "
					 ;

		return   session.createQuery(query, IrDcntSceStoBiz.class)
									.stream();

	}

	public static List<IrDcntSceDetBiz> getIrDcntSceDetBiz(String bssd) {

		String query = "select a from IrDcntSceDetBiz a "
					 + "where 1=1 "
					 + "and a.baseYymm  = :bssd	"
					 ;

		return   session.createQuery(query, IrDcntSceDetBiz.class)
									.setParameter("bssd", bssd)
								 .getResultList();

	}

	public static Stream<IrDcntSceDetBiz> getIrDcntSceDetBizStream(String bssd) {

		String query = "select a from IrDcntSceDetBiz a "
					 + "where 1=1 "
					 + "and a.baseYymm  = :bssd	"
					 ;

		return   session.createQuery(query, IrDcntSceDetBiz.class)
									.setParameter("bssd", bssd)
								 .stream();

	}

	public static List<IrDcntSceStoGnr> getIrDcntSceStoGnr(String bssd) {

		String query = "select a from IrDcntSceStoGnr a "
					 + "where 1=1 "
					 + "and a.baseYymm  = :bssd	"
					 ;

		return   session.createQuery(query, IrDcntSceStoGnr.class)
									.setParameter("bssd", bssd)
								 .getResultList();

	}

	public static Stream<IrDcntSceStoGnr> getIrDcntSceStoGnrStream(String bssd) {

		String query = "select a from IrDcntSceStoGnr a "
					 + "where 1=1 "
					 + "and a.baseYymm  = :bssd	"
					 ;

		return   session.createQuery(query, IrDcntSceStoGnr.class)
									.setParameter("bssd", bssd)
									.stream();

	}

}
