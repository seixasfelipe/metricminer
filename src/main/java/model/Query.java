package model;

public class Query {
    private String sql;

    public Query(String query) {
        this.sql = query;
    }

    public String getSql() {
        return sql;
    }
}
