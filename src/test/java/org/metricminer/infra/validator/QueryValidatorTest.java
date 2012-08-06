package org.metricminer.infra.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.metricminer.model.Author;
import org.metricminer.model.Query;
import org.metricminer.model.User;

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
		shouldFailValidation(QueryValidator.SOURCECODE_MESSAGE);
	}

	@Test
	public void shouldNotValidateQueryContainingWildCard() {
		query.setSqlQuery("select * from Project;");
		shouldFailValidation(QueryValidator.WILDCARD_MESSAGE);
	}

	@Test
	public void shouldNotValidateQueryContainingAuthorName() {
		query.setSqlQuery("select " + Author.NAME_COLUMN + " from Author;");
		shouldFailValidation(QueryValidator.SECRETNAME_MESSAGE);
	}

	@Test
	public void shouldNotValidateQueryContainingAuthorEmail() {
		query.setSqlQuery("select " + Author.EMAIL_COLUMN + " from Author;");
		shouldFailValidation(QueryValidator.SECRETEMAIL_MESSAGE);
	}
	
	@Test
	public void shouldNotAllowOtherUserToUpdateQuery() {
	    query.setSqlQuery("select id from Project;");
	    User author = mock(User.class);
	    User otherUser = mock(User.class);
	    when(author.getId()).thenReturn(1L);
	    when(otherUser.getId()).thenReturn(2L);
	    query.setAuthor(author);
	    
	    queryValidator.validateEditByAuthor(query, author);

	    try {
            queryValidator.validateEditByAuthor(query, otherUser);
            fail("should throw exception");
        } catch (ValidationException e) {
            List<Message> errors = e.getErrors();
            assertEquals(QueryValidator.NOT_ALLOWED_MESSAGE, errors.get(0).getMessage());
        }
	    
	}

	@Test
	public void shouldValidateCommonQuery() {
		query.setSqlQuery("select name from Project;");
	}

	private void shouldFailValidation(String errorMessage) {
		try {
			queryValidator.validate(query);
			fail("should throw exception");
		} catch (ValidationException e) {
			List<Message> errors = e.getErrors();
			assertEquals(errorMessage, errors.get(0).getMessage());
		}
	}

}
