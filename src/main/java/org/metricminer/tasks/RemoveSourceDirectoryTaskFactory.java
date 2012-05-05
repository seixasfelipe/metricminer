package org.metricminer.tasks;


import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.executor.SimpleCommandExecutor;
import org.metricminer.model.Task;
import org.metricminer.runner.RunnableTask;
import org.metricminer.runner.RunnableTaskFactory;


public class RemoveSourceDirectoryTaskFactory implements RunnableTaskFactory {

    @Override
    public RunnableTask build(Task task, Session session, StatelessSession statelessSession) {
        return new RemoveSourceDirectoryTask(task.getProject(), new SimpleCommandExecutor());
    }
}
