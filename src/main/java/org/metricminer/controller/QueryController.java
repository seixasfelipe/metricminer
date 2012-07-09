package org.metricminer.controller;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.metricminer.infra.dao.QueryDao;
import org.metricminer.infra.dao.QueryResultDAO;
import org.metricminer.infra.dao.TaskDao;
import org.metricminer.infra.validator.QueryValidator;
import org.metricminer.model.Query;
import org.metricminer.model.QueryResult;
import org.metricminer.model.Task;
import org.metricminer.model.TaskBuilder;
import org.metricminer.model.TaskConfigurationEntryKey;
import org.metricminer.tasks.query.ExecuteQueryTaskFactory;

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

	public QueryController(TaskDao taskDao, QueryDao queryDao,
			QueryResultDAO queryResultDAO, Result result, QueryValidator queryValidator) {
		this.taskDao = taskDao;
		this.queryDao = queryDao;
		this.queryResultDAO = queryResultDAO;
		this.result = result;
		this.queryValidator = queryValidator;
	}

	@Get("/queries/new")
	public void queryForm() {
	}

	@Post("/queries")
	public void save(Query query) {
		queryValidator.validate(query);
		Task task = new TaskBuilder()
				.withName("Execute query " + query.getName())
				.withRunnableTaskFactory(new ExecuteQueryTaskFactory()).build();
		queryDao.save(query);
		task.addTaskConfigurationEntry(TaskConfigurationEntryKey.QUERY_ID,
				query.getId().toString());
		taskDao.save(task);
		result.redirectTo(TaskController.class).listTasks();
	}

	@Get("/queries")
	public void listQueries() {
		List<Query> queries = queryDao.list();
		Collections.sort(queries);
		result.include("queries", queries);
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
		Task task = new TaskBuilder()
				.withName("Execute query " + query.getName())
				.withRunnableTaskFactory(new ExecuteQueryTaskFactory()).build();
		task.addTaskConfigurationEntry(TaskConfigurationEntryKey.QUERY_ID,
				query.getId().toString());
		taskDao.save(task);
		result.redirectTo(TaskController.class).listTasks();
	}
}
