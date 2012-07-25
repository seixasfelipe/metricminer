package org.metricminer.infra.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class ArtifactDao {
	
	private final Session session;

	public ArtifactDao(Session session) {
		this.session = session;
	}
	
	public Long totalArtifacts() {
		Query query = session.createQuery("select count(id) from Artifact");
		return (Long) query.uniqueResult();
	}

}
