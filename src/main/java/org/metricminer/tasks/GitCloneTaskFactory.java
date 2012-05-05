package org.metricminer.tasks;


import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.model.Task;
import org.metricminer.runner.RunnableTask;
import org.metricminer.runner.RunnableTaskFactory;
import org.metricminer.scm.git.Git;
import org.metricminer.scm.git.GitFactory;


public class GitCloneTaskFactory implements RunnableTaskFactory {

    @Override
    public RunnableTask build(Task task, Session session, StatelessSession statelessSession) {
        Git git = (Git) new GitFactory().build(task.getProject().getMapConfig());
        return new GitCloneTask(git, task.getProject());
    }
}
