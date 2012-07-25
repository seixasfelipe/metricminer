package org.metricminer.infra.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class CommitDao {

	private final Session session;

	public CommitDao(Session session) {
		this.session = session;
	}
	
	public Long totalCommitCount() {
		Query query = session.createQuery("select count(id) from Commit");
		return (Long) query.uniqueResult();
	}
	
}
