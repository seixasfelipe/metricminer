package org.metricminer.tasks.query;

import org.metricminer.model.Author;
import org.metricminer.model.Query;

public class QueryProcessor {

	private static final String NAME_FUNCTION = "AuthorName";
	private static final String EMAIL_FUNCTION = "AuthorEmail";

	public Query process(Query originalQuery) {
		Query query = new Query();
		query.setName(originalQuery.getName());
		String sql = originalQuery.getSqlQuery();
		sql = sql.replaceAll(NAME_FUNCTION + "\\s*\\(\\s*\\)", "MD5(" + Author.NAME_COLUMN + ")");
		sql = sql.replaceAll(EMAIL_FUNCTION + "\\s*\\(\\s*\\)", "MD5(" + Author.EMAIL_COLUMN + ")");
		query.setSqlQuery(sql);
		return query;
	}

}
