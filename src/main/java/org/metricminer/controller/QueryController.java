package org.metricminer.controller;

import java.util.Collections;
import java.util.List;

import org.metricminer.infra.dao.QueryDao;
import org.metricminer.infra.dao.TaskDao;
import org.metricminer.model.Query;
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

    public QueryController(TaskDao taskDao, QueryDao queryDao, Result result) {
        this.taskDao = taskDao;
        this.queryDao = queryDao;
        this.result = result;
    }

    @Get("/queries/new")
    public void queryForm() {
    }

    @Post("/queries")
    public void save(Query query) {
        Task task = new TaskBuilder().withName("Execute query")
                .withRunnableTaskFactory(new ExecuteQueryTaskFactory()).build();
        queryDao.save(query);
        task.addTaskConfigurationEntry(TaskConfigurationEntryKey.QUERY_ID, query.getId().toString());
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
    public Download downloadCSV(Long queryId) {
        Query query = queryDao.findBy(queryId);
        return new FileDownload(query.getCSV(), "text/csv", "result.csv");
    }
}
