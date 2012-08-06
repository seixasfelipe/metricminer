package org.metricminer.controller;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.metricminer.infra.dao.QueryDao;
import org.metricminer.infra.dao.QueryResultDAO;
import org.metricminer.infra.dao.TaskDao;
import org.metricminer.infra.session.UserSession;
import org.metricminer.infra.validator.QueryValidator;
import org.metricminer.model.Query;
import org.metricminer.model.QueryResult;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.FileDownload;

@Resource
public class QueryController {

    private final TaskDao taskDao;
    private final Result result;
    private final QueryDao queryDao;
    private final QueryResultDAO queryResultDAO;
    private final QueryValidator queryValidator;
    private final UserSession userSession;

    public QueryController(TaskDao taskDao, QueryDao queryDao,
            QueryResultDAO queryResultDAO, Result result,
            QueryValidator queryValidator, UserSession userSession) {
        this.taskDao = taskDao;
        this.queryDao = queryDao;
        this.queryResultDAO = queryResultDAO;
        this.result = result;
        this.queryValidator = queryValidator;
        this.userSession = userSession;
    }

    @Get("/queries/new")
    public void queryForm() {
    }

    @Post("/queries")
    public void save(Query query) {
        queryValidator.validate(query);
        query.setAuthor(userSession.user());
        queryDao.save(query);
        taskDao.saveTaskToExecuteQuery(query);
        result.redirectTo(TaskController.class).listTasks();
    }

    @Post("/query/{queryId}")
    public void updateQuery(Query updatedQuery, Long queryId) {
        Query persistedUpdatedQuery = queryDao.findBy(queryId);
        persistedUpdatedQuery.setSqlQuery(updatedQuery.getSqlQuery());
        queryValidator.validate(persistedUpdatedQuery);
        queryValidator.validateEditByAuthor(persistedUpdatedQuery, userSession.user());
        
        queryDao.update(persistedUpdatedQuery);
        
        taskDao.saveTaskToExecuteQuery(persistedUpdatedQuery);
        result.redirectTo(QueryController.class).detailQuery(queryId);
    }

    @Get("/queries")
    public void listQueries() {
        List<Query> queries = queryDao.list();
        Collections.sort(queries);
        result.include("queries", queries);
    }

    @Get("/query/edit/{queryId}")
    public void editQueryForm(Long queryId) {
        Query query = queryDao.findBy(queryId);
        result.include("query", query);
    }

    @Get("/query/{queryId}")
    public void detailQuery(Long queryId) {
        Query query = queryDao.findBy(queryId);
        result.include("query", query);
    }

    @Get("/query/download/{resultId}")
    public Download downloadCSV(Long resultId) {
        QueryResult result = queryResultDAO.findBy(resultId);
        return new FileDownload(new File(result.getCsvFilename()), "text/csv",
                "result.csv");
    }

    @Post("/query/run")
    public void runQuery(Long queryId) {
        Query query = queryDao.findBy(queryId);
        taskDao.saveTaskToExecuteQuery(query);
        result.redirectTo(TaskController.class).listTasks();
    }
}
