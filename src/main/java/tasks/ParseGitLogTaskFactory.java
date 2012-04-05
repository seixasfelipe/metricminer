package tasks;

import model.Task;

import org.hibernate.Session;

import tasks.runner.RunnableTask;
import tasks.runner.RunnableTaskFactory;
import br.com.caelum.revolution.persistence.runner.SCMLogParser;
import br.com.caelum.revolution.persistence.runner.SCMLogParserFactory;

public class ParseGitLogTaskFactory implements RunnableTaskFactory {

    @Override
    public RunnableTask build(Task task, Session session) {
        SCMLogParser logParser = new SCMLogParserFactory().basedOn(
                task.getProject().getMapConfig(), session, task.getProject());
        return new ParseGitLogTask(logParser);
    }

}
