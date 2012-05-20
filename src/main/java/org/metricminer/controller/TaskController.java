package org.metricminer.controller;

import java.util.Collections;
import java.util.List;

import org.metricminer.dao.TaskDao;
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
		Collections.reverse(tasks);
	    result.include("tasks", tasks);
	}
}
