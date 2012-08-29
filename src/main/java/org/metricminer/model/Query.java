package org.metricminer.model;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
public class Query implements Comparable<Query> {
    @Type(type = "text")
    private String sqlQuery;
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar submitDate;
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<QueryResult> results;
    @ManyToOne
    private User author;

    public Query() {
        submitDate = Calendar.getInstance();
    }

    public Query(String query) {
        this();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getSubmitDate() {
        return submitDate;
    }

    public User getAuthor() {
        return author;
    }

    @Override
    public int compareTo(Query otherQuery) {
        return -submitDate.compareTo(otherQuery.submitDate);
    }

    public void addResult(QueryResult result) {
        results.add(result);
    }

    public int getResultCount() {
        return results.size();
    }

    public List<QueryResult> getResults() {
        return Collections.unmodifiableList(results);
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public boolean isAllowedToEdit(User otherUser) {
        return author.equals(otherUser);
    }

}
