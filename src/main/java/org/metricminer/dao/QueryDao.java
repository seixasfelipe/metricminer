package org.metricminer.dao;

import org.hibernate.Session;
import org.metricminer.model.Query;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class QueryDao {
    private Session session;

    public QueryDao(Session session) {
        this.session = session;
    }

    public void save(Query query) {
        session.save(query);
    }
}
