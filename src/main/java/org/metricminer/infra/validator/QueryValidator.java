package org.metricminer.infra.validator;

import org.metricminer.controller.QueryController;
import org.metricminer.model.Query;

import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Component
public class QueryValidator {

	static final String SOURCECODE_MESSAGE = "The query should not get source code from projects";
	static final String WILDCARD_MESSAGE = "The query should not contain '*' wildcard";
	private final Validator validator;

	public QueryValidator(Validator validator) {
		this.validator = validator;
	}

	public boolean isValid(Query query) {
		boolean valid = true;
		if (containsWildCard(query)) {
			valid = false;
		}
		if (containsSourceCode(query)) {
			valid = false;
		}
		return valid;
	}

	private boolean containsSourceCode(Query query) {
		boolean invalid = false;
		String sql = query.getSqlQuery();
		if (sql.contains("source") && sql.contains("SourceCode")) {
			invalid = true;
		}
		return invalid;
	}

	private boolean containsWildCard(Query query) {
		boolean invalid = false;
		String sql = query.getSqlQuery();
		if (sql.contains("*")) {
			invalid = true;
		}
		return invalid;
	}

	public void validate(Query query) {
		if (containsWildCard(query)) {
			validator.add(new ValidationMessage(WILDCARD_MESSAGE, "InvalidQuery"));
		}
		if (containsSourceCode(query)) {
			validator.add(new ValidationMessage(SOURCECODE_MESSAGE, "InvalidQuery"));
		}
		validator.onErrorRedirectTo(QueryController.class).queryForm();
	}
}
