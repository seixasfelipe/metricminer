package tasks.runner;

import model.Task;

import org.hibernate.Session;

public interface RunnableTaskFactory {
    public RunnableTask build(Task task, Session session);
}
