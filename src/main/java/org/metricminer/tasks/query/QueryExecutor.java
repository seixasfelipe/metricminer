package org.metricminer.tasks.query;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.metricminer.infra.csv.CSVWriter;
import org.metricminer.model.Query;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class QueryExecutor {
    private Session session;
	private final QueryProcessor queryProcessor;
	private final CSVWriter writer;
    
    public QueryExecutor(Session session, QueryProcessor queryProcessor, CSVWriter writer) {
        this.session = session;
		this.queryProcessor = queryProcessor;
		this.writer = writer;
    }
    
    @SuppressWarnings("unchecked")
    public void execute(Query query, OutputStream csvOutputStream) {
        session.setDefaultReadOnly(true);
        Query processedQuery = queryProcessor.process(query);
		SQLQuery sqlQuery = session.createSQLQuery(processedQuery.getSqlQuery());
        sqlQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List<Map<String, Object>> results = sqlQuery.list();

        if (results.isEmpty()) {
        	writer.emptyResult(csvOutputStream);
        }
        else {
        	writer.write(csvOutputStream, results);
        }
        
        session.setDefaultReadOnly(false);
    }

}
