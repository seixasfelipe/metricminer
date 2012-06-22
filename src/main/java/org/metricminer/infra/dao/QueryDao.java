package org.metricminer.infra.dao;

import java.util.List;

import org.hibernate.StatelessSession;
import org.metricminer.model.Query;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class QueryDao {
    private StatelessSession session;

    public QueryDao(StatelessSession session) {
        this.session = session;
    }

    public void save(Query query) {
        session.insert(query);
    }

    public Query findBy(Long id) {
        return (Query) session.get(Query.class, id);
    }

    public void update(Query query) {
        session.update(query);
    }

    @SuppressWarnings("unchecked")
    public List<Query> list() {
        return session.createCriteria(Query.class).list();
    }
}
