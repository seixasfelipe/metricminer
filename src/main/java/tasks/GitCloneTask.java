package tasks;

import model.Project;

import org.apache.log4j.Logger;

import br.com.caelum.revolution.scm.git.Git;
import config.MetricMinerConfigs;

public class GitCloneTask implements RunnableTask {
	private Project project;
	private static Logger log = Logger.getLogger(GitCloneTask.class);
	private final Git git;

	public GitCloneTask(Git git, Project project) {
		this.git = git;
		this.project = project;
	}

	public void run() {
		String localPath = MetricMinerConfigs.metricMinerHome + "/projects/"
				+ project.getId();
		log.info("Clonning git repo " + project.getScmUrl() + "to " + localPath);

		String output = git.clone(project.getScmUrl(), localPath);

		log.info("Output: " + output);
	}

}
