package org.metricminer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Query {
    private String sqlQuery;
    private String csvFilename;
    @Id
    @GeneratedValue
    private Long id;

    public Query() {
    }
    
    public Query(String query) {
        this.sqlQuery = query;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }
    
    public void setSqlQuery(String sql) {
        this.sqlQuery = sql;
    }

    public Long getId() {
        return id;
    }
    
    public void executed(String outputFileName) {
        csvFilename = outputFileName;
    }
    
}
