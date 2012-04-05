package tasks;

import model.Project;

import org.hibernate.Session;

import tasks.runner.RunnableTask;
import tasks.runner.RunnableTaskFactory;

import br.com.caelum.revolution.scm.git.Git;
import br.com.caelum.revolution.scm.git.GitFactory;

public class GitCloneTaskFactory implements RunnableTaskFactory {

	@Override
	public RunnableTask build(Project project, Session session) {
		Git git = (Git) new GitFactory().build(project.getMapConfig());
		return new GitCloneTask(git, project);
	}
}
