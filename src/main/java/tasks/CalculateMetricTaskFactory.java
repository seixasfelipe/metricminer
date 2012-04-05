package tasks;

import model.Project;

import org.hibernate.Session;

import tasks.runner.RunnableTask;
import tasks.runner.RunnableTaskFactory;

public class CalculateMetricTaskFactory implements RunnableTaskFactory {

	@Override
	public RunnableTask build(Project project, Session session) {
		return null;
	}

}
