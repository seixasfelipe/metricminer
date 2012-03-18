package tasks;

import model.Project;
import br.com.caelum.revolution.executor.CommandExecutor;
import config.MetricMinerConfigs;

public class GitCloneTask implements RunnableTask {
	private CommandExecutor executor;
	private Project project;

	public GitCloneTask(CommandExecutor executor, Project project) {
		this.executor = executor;
		this.project = project;
	}

	public void run() {
		System.out.println("Clonning project...");
		executor.execute(
				"git clone " + project.getScmUrl(),
				MetricMinerConfigs.metricMinerHome + "/projects/"
						+ project.getId());
		project.taskEnded();
	}

}
