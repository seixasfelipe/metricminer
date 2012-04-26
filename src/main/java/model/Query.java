package model;

public class Query {
    private String sql;

    public Query() {
    }
    
    public Query(String query) {
        this.sql = query;
    }

    public String getSql() {
        return sql;
    }
    
    public void setSql(String sql) {
        this.sql = sql;
    }
}
