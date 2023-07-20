package com.gof.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.gof.interfaces.HisEntity;
import com.gof.util.HibernateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoUtil {

	private static Session session = HibernateUtil.getSessionFactory().openSession();

	public static <T> List<T> getEntities(String entityName, Map<String, Object> param) {
		try {
			return getEntities(Class.forName("com.gof.entity." + entityName), param);
		} catch (Exception e) {
			log.error("Class Name Error : {}", e);
		}
		return null;
	}


	public static <T> Stream<T> getEntityStream(String entityName, Map<String, Object> param) {
		try {
			return getEntityStream(Class.forName("com.gof.entity." + entityName), param);
		} catch (Exception e) {
			log.error("Class Name Error : {}", e);
		}
		return null;
	}

	public static <T extends HisEntity> Stream<T> getHEntityStream(String entityName, Map<String, Object> param) {
		try {
			return getEntityStream(Class.forName("com.gof.entity.his." + entityName), param);
		} catch (Exception e) {
			log.error("Class Name Error : {}", e);
		}
		return null;
	}

	public static <T> List<T> getEntities(Class klass, Map<String, Object> param) {
		StringBuilder builder = new StringBuilder();
		builder.append("select a from ")
			   .append(klass.getSimpleName())
			   .append(" a where 1=1")
			   ;

		for (Map.Entry<String, Object> entry : param.entrySet()) {
			builder.append(" and ").append(entry.getKey()).append(" = :").append(entry.getKey());
		}
//		log.info("query : {}", builder.toString());
		Query<T> q = session.createQuery(builder.toString(), klass);

		for (Map.Entry<String, Object> entry : param.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}

		List<T> rst = q.getResultList();
		return rst;
	}


	public static <T> List<T> getEntities(Class klass, Map<String, Object> param, Map<String, Map<String, Object>> filter) {
		Filter f ;
		for(Map.Entry<String, Map<String, Object>> entry : filter.entrySet()) {
			f = session.enableFilter(entry.getKey());
			for(Map.Entry<String, Object> zz : entry.getValue().entrySet()) {
				f.setParameter(zz.getKey(), zz.getValue());
//				log.info("filter: {},{},{},{}", klass.getName(), entry.getKey(), zz.getKey(), zz.getValue());
			}
		}

		StringBuilder builder = new StringBuilder();
		builder.append("select a from ")
			   .append(klass.getSimpleName())
			   .append(" a where 1=1")
			   ;

		for (Map.Entry<String, Object> entry : param.entrySet()) {
			builder.append(" and ").append(entry.getKey()).append(" = :").append(entry.getKey());
		}

		Query<T> q = session.createQuery(builder.toString(), klass);

		for (Map.Entry<String, Object> entry : param.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}

//		log.info("Query : {},{}", klass.getSimpleName(), builder.toString());
//		q.stream().forEach(s -> log.info("GetEntities : {},{},{},{}", s.toString()));

		List<T> rst = q.getResultList();
//		for(Map.Entry<String, Map<String, Object>> entry : filter.entrySet()) {
//			session.disableFilter(entry.getKey());
//		}
		return rst;
	}


	public static <T> Stream<T> getEntityStream(Class klass, Map<String, Object> param) {
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		session.beginTransaction();

		StringBuilder builder = new StringBuilder();
		builder.append("select a from ")
			   .append(klass.getSimpleName())
			   .append(" a where 1=1")
			   ;

		for (Map.Entry<String, Object> entry : param.entrySet()) {
			builder.append(" and ").append(entry.getKey()).append(" = :").append(entry.getKey());
		}

		Query<T> q = session.createQuery(builder.toString(), klass);
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}
//		log.info("Query : {},{}", klass.getSimpleName(), builder.toString());

		return q.stream();
	}

	public static <T> Stream<T> getEntityStream(Session s, Class klass, Map<String, Object> param) {
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		session.beginTransaction();

		StringBuilder builder = new StringBuilder();
		builder.append("select a from ")
			   .append(klass.getSimpleName())
			   .append(" a where 1=1")
			   ;

		for (Map.Entry<String, Object> entry : param.entrySet()) {
//			log.info("DaoUtil : {},{},{}", entry.getKey(), entry.getValue());
			if( entry.getKey().equals("applBizDv")) {
				builder.append(" and ")
				.append(entry.getKey())
				.append(" like  '%' || :")
				.append(entry.getKey())
				.append("|| '%'")
				;
			}
			else {
				builder.append(" and ")
					.append(entry.getKey())
					.append(" = :")
					.append(entry.getKey());

			}

//			builder.append(" and ")
//			.append(entry.getKey())
//			.append(" = :")
//			.append(entry.getKey());
		}

		Query<T> q = s.createQuery(builder.toString(), klass);
//		log.info("aaa : {}", builder.toString());
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}
//		log.info("Query : {},{}", klass.getSimpleName(), builder.toString());

		return q.stream();
	}

	public static <T extends HisEntity> Stream<T> getHEntityStream(Session s, String entityName, Map<String, Object> param) {
		try {
			return getEntityStream(s, Class.forName("com.gof.entity.his." + entityName), param);
		} catch (Exception e) {
			log.error("Class Name Error : {}", e);
		}
		return null;
	}


	public static void sesseionClose() {
		session.close();
	}

}
