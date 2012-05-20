package org.metricminer.controller;

import org.metricminer.dao.TaskDao;

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
	    result.include("tasks", taskDao.listTasks());
	}
}
