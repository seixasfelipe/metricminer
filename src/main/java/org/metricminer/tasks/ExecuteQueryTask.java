package org.metricminer.tasks;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.hibernate.Session;
import org.metricminer.model.Query;
import org.metricminer.model.QueryExecutor;
import org.metricminer.model.Task;
import org.metricminer.runner.RunnableTask;

public class ExecuteQueryTask implements RunnableTask {

    private final Query query;
    private final QueryExecutor queryExecutor;
    private final Session session;

    public ExecuteQueryTask(Task task, QueryExecutor queryExecutor, Session session) {
        this.session = session;
        this.query = task.getQuery();
        this.queryExecutor = queryExecutor;
    }

    @Override
    public void run() {
        String tmpFileName = "/tmp/result-" + query.getId() + ".csv";
        FileOutputStream outputStream;
        outputStream = createFile(tmpFileName);
        queryExecutor.execute(query, outputStream);
        query.executed(tmpFileName);
        session.update(query);
    }

    private FileOutputStream createFile(String tmpFileName) {
        try {
            return new FileOutputStream(tmpFileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
