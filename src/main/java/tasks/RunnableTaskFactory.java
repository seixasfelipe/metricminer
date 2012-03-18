package tasks;

import model.Project;

import org.hibernate.Session;

public interface RunnableTaskFactory {
	public RunnableTask build(Project project, Session session);
}
