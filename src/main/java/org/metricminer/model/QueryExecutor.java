package org.metricminer.model;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class QueryExecutor {
    private Session session;
    
    public QueryExecutor(Session session) {
        this.session = session;
    }
    
    public void execute(Query query, OutputStream csvOutputStream) {
        System.out.println(query.getSqlQuery());
        SQLQuery sqlQuery = session.createSQLQuery(query.getSqlQuery());
        sqlQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List<Map<String, Object>> results = sqlQuery.list();
        writeCSVTo(csvOutputStream, results);
    }

    private void writeCSVTo(OutputStream csvOutputStream, List<Map<String,Object>> results) {
        PrintStream csvPrint = new PrintStream(csvOutputStream);
        Map<String, Object> first = results.get(0);
        printHeader(csvPrint, first);
        for (Map<String,Object> row : results) {
            for (Entry<String, Object> entry : row.entrySet()) {
                csvPrint.print(entry.getValue() + ";");
            }
            csvPrint.print("\n");
        }
    }

    private void printHeader(PrintStream csvPrint, Map<String, Object> first) {
        for (Entry<String, Object> entry : first.entrySet()) {
            csvPrint.print(entry.getKey() + ";");
        }
        csvPrint.print("\n");
    }

}
