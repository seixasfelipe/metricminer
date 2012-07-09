package org.metricminer.infra.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.metricminer.model.Query;

import br.com.caelum.vraptor.util.test.MockValidator;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.validator.ValidationException;

public class QueryValidatorTest {

	private QueryValidator queryValidator;
	private Query query;
	private MockValidator mockValidator;

	@Before
	public void setUp() {
		mockValidator = new MockValidator();
		queryValidator = new QueryValidator(mockValidator);
		query = new Query();
	}

	@Test
	public void shouldNotValidateQueryContainingSourceCode() {
		query.setSqlQuery("select source as code from SourceCode;");
		try {
			queryValidator.validate(query);
			fail("should throw exception");
		} catch (ValidationException e) {
			List<Message> errors = e.getErrors();
			for (Message message : errors) {
				assertEquals(QueryValidator.SOURCECODE_MESSAGE, message.getMessage());
			}
		}
	}

	@Test
	public void shouldNotValidateQueryContainingWildCard() {
		query.setSqlQuery("select * from Project;");
		try {
			queryValidator.validate(query);
			fail("should throw exception");
		} catch (ValidationException e) {
			List<Message> errors = e.getErrors();
			for (Message message : errors) {
				assertEquals(QueryValidator.WILDCARD_MESSAGE, message.getMessage());
			}
		}
	}
	
	@Test
	public void shouldValidateCommonQuery() {
		query.setSqlQuery("select name from Project;");
	}
	

}
