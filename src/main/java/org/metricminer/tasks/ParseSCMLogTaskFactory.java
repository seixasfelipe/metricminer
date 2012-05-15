package org.metricminer.tasks;


import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.model.Task;
import org.metricminer.runner.RunnableTask;
import org.metricminer.runner.RunnableTaskFactory;
import org.metricminer.tasks.parsegit.SCMLogParser;
import org.metricminer.tasks.parsegit.SCMLogParserFactory;


public class ParseSCMLogTaskFactory implements RunnableTaskFactory {

    @Override
    public RunnableTask build(Task task, Session session, StatelessSession statelessSession) {
        SCMLogParser logParser = new SCMLogParserFactory().basedOn(
                task.getProject().getMapConfig(), session, task.getProject());
        return new ParseSCMLogTask(logParser);
    }

}
