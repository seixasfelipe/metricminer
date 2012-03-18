package tasks;

import model.Project;

import org.hibernate.Session;

import br.com.caelum.revolution.executor.SimpleCommandExecutor;

public class GitCloneTaskFactory implements RunnableTaskFactory {

	@Override
	public RunnableTask build(Project project, Session session) {
		return new GitCloneTask(new SimpleCommandExecutor(), project);
	}
}
