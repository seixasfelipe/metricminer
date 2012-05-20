package org.metricminer.tasks;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.dao.QueryDao;
import org.metricminer.model.QueryExecutor;
import org.metricminer.model.Task;
import org.metricminer.runner.RunnableTask;
import org.metricminer.runner.RunnableTaskFactory;

public class ExecuteQueryTaskFactory implements RunnableTaskFactory {

    @Override
    public RunnableTask build(Task task, Session session, StatelessSession statelessSession) {
        return new ExecuteQueryTask(task, new QueryExecutor(session), new QueryDao(session));
    }

}
