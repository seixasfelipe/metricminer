package org.metricminer.controller;

import org.metricminer.tasks.TaskStatus;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class StatusController {

	private TaskStatus status;
	private final Result result;

	public StatusController(TaskStatus status, Result result) {
		this.status = status;
		this.result = result;
	}

	@Get("/status")
	public void showStatus() {
		result.include("status", status);
	}
	
}
