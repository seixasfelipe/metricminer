package org.metricminer.tasks.query;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.metricminer.model.Author;
import org.metricminer.model.Query;

public class QueryProcessorTest {

	private Query query;
	private QueryProcessor processor;

	@Before
	public void setUp() {
		query = new Query();
		processor = new QueryProcessor();
	}

	@Test
	public void shouldReplaceAuthorNameForMD5Function() throws Exception {
		query.setSqlQuery("select AuthorName(  ) from Author");
		Query prcessedQuery = processor.process(query);
		
		assertEquals("select MD5("+Author.NAME_COLUMN+") from Author", prcessedQuery.getSqlQuery());
	}
	
	@Test
	public void shouldReplaceAuthorEmailForMD5Function() throws Exception {
		query.setSqlQuery("select AuthorEmail () from Author");
		Query prcessedQuery = processor.process(query);
		
		assertEquals("select MD5("+Author.EMAIL_COLUMN+") from Author", prcessedQuery.getSqlQuery());
	}
	
}
