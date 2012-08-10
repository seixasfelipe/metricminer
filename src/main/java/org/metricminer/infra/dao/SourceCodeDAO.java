package org.metricminer.infra.dao;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.StatelessSession;
import org.metricminer.model.Project;
import org.metricminer.model.SourceCode;

public class SourceCodeDAO {

	private final StatelessSession statelessSession;
	public static final int PAGE_SIZE = 5;
    public static final long MAX_SOURCE_SIZE = 10000;

	public SourceCodeDAO(StatelessSession statelessSession) {
		this.statelessSession = statelessSession;
	}

	@SuppressWarnings("unchecked")
	public List<SourceCode> listSourcesOf(Project project, int page) {
		Query query = statelessSession.createQuery("select source from SourceCode source "
                + " where and source.sourceSize < :sourceSize");
        
        query.setParameter("project_id", project.getId())
	         .setParameter("sourceSize", MAX_SOURCE_SIZE)
	         .setFirstResult(page * PAGE_SIZE)
	         .setMaxResults(PAGE_SIZE);
        
        return (List<SourceCode>) query.list();
	}

	@SuppressWarnings("unchecked")
	public Map<Long, String> listSourceCodeIdsAndNamesFor(Project project, int page) {
		Query query = statelessSession.createQuery("select source.id, artifact.name from SourceCode source "
                + "join source.artifact as artifact where artifact.project.id = :project_id "
                + "and source.sourceSize < :sourceSize order by source.id asc");
		query.setParameter("project_id", project.getId())
			.setParameter("sourceSize", MAX_SOURCE_SIZE)
			.setFirstResult(500*page)
			.setMaxResults(500);
		List<Object[]> idsAndNames = query.list();
		Map<Long, String> map = new TreeMap<Long, String>();
		for (Object[] objects : idsAndNames) {
			map.put((Long) objects[0], (String) objects[1]);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<SourceCode> listSourcesOf(Project project, Long firstId, Long lastId) {
		Query query = statelessSession.createQuery("select source from SourceCode source " +
				"where source.id >= :first_id and source.id <= :last_id and source.sourceSize < :sourceSize");
        
        query.setParameter("first_id", firstId)
        	 .setParameter("last_id", lastId)
        	 .setParameter("sourceSize", MAX_SOURCE_SIZE);
        
        return (List<SourceCode>) query.list();
	}

    public SourceCode findByIdAndSourceSize(Long id) {
        Query query = statelessSession.createQuery("select source from SourceCode source " +
        		"where id=:id and source.sourceSize < :sourceSize");
        query.setParameter("id", id)
             .setParameter("sourceSize", MAX_SOURCE_SIZE);
        return (SourceCode) query.uniqueResult();
    }
	

}
