package org.metricminer.controller;

import java.util.List;

import org.metricminer.infra.dao.TaskDao;
import org.metricminer.model.Task;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class TaskController {

	private final Result result;
	private final TaskDao taskDao;

	public TaskController(Result result, TaskDao taskDao) {
		this.result = result;
		this.taskDao = taskDao;
	}

	@Get("/tasks")
	public void listTasks() {
		List<Task> tasks = taskDao.listTasks();
	    result.include("tasks", tasks);
	}
}
