package org.metricminer.tasks;

import org.metricminer.executor.CommandExecutor;
import org.metricminer.model.Project;
import org.metricminer.runner.RunnableTask;


public class RemoveSourceDirectoryTask implements RunnableTask {

	private final Project project;
	private final CommandExecutor exec;

	public RemoveSourceDirectoryTask(Project project, CommandExecutor exec) {
		this.project = project;
		this.exec = exec;
	}

	@Override
	public void run() {
		String localPath = project.getLocalPath();
		exec.execute("rm -rf " + localPath, localPath);
	}
}
