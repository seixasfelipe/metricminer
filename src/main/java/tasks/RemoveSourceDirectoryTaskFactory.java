package tasks;

import model.Task;

import org.hibernate.Session;

import tasks.runner.RunnableTask;
import tasks.runner.RunnableTaskFactory;
import br.com.caelum.revolution.executor.SimpleCommandExecutor;

public class RemoveSourceDirectoryTaskFactory implements RunnableTaskFactory {

    @Override
    public RunnableTask build(Task task, Session session) {
        return new RemoveSourceDirectoryTask(task.getProject(), new SimpleCommandExecutor());
    }
}
