package tasks;

import model.Project;

import org.hibernate.Session;

import br.com.caelum.revolution.executor.SimpleCommandExecutor;

public class RemoveSourceDirectoryTaskFactory implements RunnableTaskFactory {

	@Override
	public RunnableTask build(Project project, Session session) {
		return new RemoveSourceDirectoryTask(project,
				new SimpleCommandExecutor());
	}

}
