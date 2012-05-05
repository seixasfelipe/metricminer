package org.metricminer.tasks;


import org.apache.log4j.Logger;
import org.metricminer.model.Project;
import org.metricminer.runner.RunnableTask;
import org.metricminer.scm.git.Git;


public class GitCloneTask implements RunnableTask {
    private Project project;
    private static Logger log = Logger.getLogger(GitCloneTask.class);
    private final Git git;

    public GitCloneTask(Git git, Project project) {
        this.git = git;
        this.project = project;
    }

    public void run() {
        String localPath = project.getLocalPath();
        log.info("Clonning git repo " + project.getScmUrl() + " to " + localPath);

        String output = git.clone(project.getScmUrl(), localPath);

        log.info("Output: " + output);
    }

}
