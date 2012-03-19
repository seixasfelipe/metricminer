package tasks;

import model.Project;

import org.apache.log4j.Logger;

import br.com.caelum.revolution.executor.CommandExecutor;
import config.MetricMinerConfigs;

public class GitCloneTask implements RunnableTask {
	private CommandExecutor executor;
	private Project project;
	private static Logger log = Logger.getLogger(GitCloneTask.class);

	public GitCloneTask(CommandExecutor executor, Project project) {
		this.executor = executor;
		this.project = project;
	}

	public void run() {
		String command = "git clone " + project.getScmUrl();
		String basePath = MetricMinerConfigs.metricMinerHome + "/projects/"
				+ project.getId();
		log.info("Executing command: " + command);
		log.info("With baspath: " + basePath);
		executor.execute("mkdir -p " + basePath, "/");
		String output = executor.execute(command, basePath);
		log.info("Git clone output: ");
		log.info(output);
	}

}
