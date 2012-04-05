package tasks;

import model.Task;

import org.hibernate.Session;

import tasks.runner.RunnableTask;
import tasks.runner.RunnableTaskFactory;

public class CalculateMetricTaskFactory implements RunnableTaskFactory {

    @Override
    public RunnableTask build(Task task, Session session) {
        return null;
    }

}
