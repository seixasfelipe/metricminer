package org.metricminer.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.model.Project;
import org.metricminer.model.SourceCode;

public class SourceCodeDAO {

	private final Session session;
	private final StatelessSession statelessSession;
	public static final int PAGE_SIZE = 5;
    public static final long MAX_SOURCE_SIZE = 10000;

	public SourceCodeDAO(Session session, StatelessSession statelessSession) {
		this.session = session;
		this.statelessSession = statelessSession;
	}

	public List<SourceCode> listSourcesOf(Project project, int page) {
		Query query = statelessSession.createQuery("select source from SourceCode source "
                + "join fetch source.artifact as artifact where artifact.project.id = :project_id "
                + " and source.sourceSize < :sourceSize");
        
        query.setParameter("project_id", project.getId())
	         .setParameter("sourceSize", MAX_SOURCE_SIZE)
	         .setFirstResult(page * PAGE_SIZE)
	         .setMaxResults(PAGE_SIZE);
        
        return (List<SourceCode>) query.list();
	}

}
