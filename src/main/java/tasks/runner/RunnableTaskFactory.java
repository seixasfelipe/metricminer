package tasks.runner;

import model.Task;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

public interface RunnableTaskFactory {
    public RunnableTask build(Task task, Session session, StatelessSession statelessSession);
}
