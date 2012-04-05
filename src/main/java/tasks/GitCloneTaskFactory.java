package tasks;

import model.Task;

import org.hibernate.Session;

import tasks.runner.RunnableTask;
import tasks.runner.RunnableTaskFactory;
import br.com.caelum.revolution.scm.git.Git;
import br.com.caelum.revolution.scm.git.GitFactory;

public class GitCloneTaskFactory implements RunnableTaskFactory {

    @Override
    public RunnableTask build(Task task, Session session) {
        Git git = (Git) new GitFactory().build(task.getProject().getMapConfig());
        return new GitCloneTask(git, task.getProject());
    }
}
