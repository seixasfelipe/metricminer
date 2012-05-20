package org.metricminer.controller;

import org.metricminer.builder.TaskBuilder;
import org.metricminer.dao.QueryDao;
import org.metricminer.dao.TaskDao;
import org.metricminer.model.Query;
import org.metricminer.model.Task;
import org.metricminer.model.TaskConfigurationEntryKey;
import org.metricminer.tasks.ExecuteQueryTaskFactory;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class QueryExecutorController {

    private final TaskDao taskDao;
    private final Result result;
    private final QueryDao queryDao;

    public QueryExecutorController(TaskDao taskDao, QueryDao queryDao, Result result) {
        this.taskDao = taskDao;
        this.queryDao = queryDao;
        this.result = result;
    }

    @Get("/query")
    public void queryForm() {
    }

    @Post("/query")
    public void execute(Query query) {
        Task task = new TaskBuilder().withName("Execute query")
                .withRunnableTaskFactory(new ExecuteQueryTaskFactory()).build();
        queryDao.save(query);
        task.addTaskConfigurationEntry(TaskConfigurationEntryKey.QUERY_ID, query.getId().toString());
        taskDao.save(task);
        result.redirectTo(TaskController.class).listTasks();
    }

}
