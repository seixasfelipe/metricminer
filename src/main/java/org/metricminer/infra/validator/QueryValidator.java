package org.metricminer.infra.validator;

import org.metricminer.controller.QueryController;
import org.metricminer.model.Author;
import org.metricminer.model.Query;
import org.metricminer.model.User;

import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Component
public class QueryValidator {

    static final String SOURCECODE_MESSAGE = "It is forbidden to get source code from projects";
    static final String WILDCARD_MESSAGE = "The query should not contain '*' wildcard";
    static String SECRETNAME_MESSAGE = "It is forbidden to get author's names, try using 'AuthorName()' function instead "
            + Author.NAME_COLUMN;
    static String SECRETEMAIL_MESSAGE = "It is forbidden to get author's emails, try using 'AuthorEmail()' function instead "
            + Author.EMAIL_COLUMN;
    static String NOT_ALLOWED_MESSAGE = "You are not allowed to update this query";
    private final Validator validator;

    public QueryValidator(Validator validator) {
        this.validator = validator;
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

    public boolean containsAuthorName(Query query) {
        boolean invalid = false;
        String sql = query.getSqlQuery();
        if (sql.contains("Author") && sql.contains(Author.NAME_COLUMN)) {
            invalid = true;
        }
        return invalid;
    }

    public boolean containsAuthorEmail(Query query) {
        boolean invalid = false;
        String sql = query.getSqlQuery();
        if (sql.contains("Author") && sql.contains(Author.EMAIL_COLUMN)) {
            invalid = true;
        }
        return invalid;
    }

    public void validate(Query query) {
        if (containsWildCard(query)) {
            validator.add(new ValidationMessage(WILDCARD_MESSAGE,
                    "InvalidQuery"));
        }
        if (containsSourceCode(query)) {
            validator.add(new ValidationMessage(SOURCECODE_MESSAGE,
                    "InvalidQuery"));
        }
        if (containsAuthorName(query)) {
            validator.add(new ValidationMessage(SECRETNAME_MESSAGE,
                    "InvalidQuery"));
        }
        if (containsAuthorEmail(query)) {
            validator.add(new ValidationMessage(SECRETEMAIL_MESSAGE,
                    "InvalidQuery"));
        }
        validator.onErrorRedirectTo(QueryController.class).queryForm();
    }

    public void validateEditByAuthor(Query query, User user) {
        if (!query.isAllowedToEdit(user)) {
            validator.add(new ValidationMessage(NOT_ALLOWED_MESSAGE,
                    "InvalidQuery"));
        }
        validator.onErrorRedirectTo(QueryController.class).editQueryForm(query.getId());
    }

}
