package org.metricminer.tasks.scm;


import org.apache.log4j.Logger;
import org.metricminer.model.Project;
import org.metricminer.scm.SCM;
import org.metricminer.scm.SCMFactory;
import org.metricminer.tasks.RunnableTask;


public class SCMCloneTask implements RunnableTask {
    private Project project;
    private static Logger log = Logger.getLogger(SCMCloneTask.class);
    private final SCM scm;

    public SCMCloneTask(Project project) {
        this.scm = new SCMFactory().basedOn(project.getMapConfig());
        this.project = project;
    }

    public void run() {
        String localPath = project.getLocalPath();
        log.info("Clonning git repo " + project.getScmUrl() + " to " + localPath);

        String output = scm.clone(project.getScmUrl(), localPath);

        log.info("Output: " + output);
    }

}
