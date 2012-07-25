package org.metricminer.infra.dao;

import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class AuthorDao {

	private Session session;

	public AuthorDao(Session session) {
		this.session = session;
	}
	
	public Long totalAuthors() {
		return (Long) session.createQuery("select count(id) from Author").uniqueResult();
	}
	
}
