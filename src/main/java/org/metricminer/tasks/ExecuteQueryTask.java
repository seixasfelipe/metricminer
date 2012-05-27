package org.metricminer.tasks;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.metricminer.dao.QueryDao;
import org.metricminer.model.Query;
import org.metricminer.model.QueryExecutor;
import org.metricminer.model.Task;
import org.metricminer.model.TaskConfigurationEntryKey;
import org.metricminer.runner.RunnableTask;

public class ExecuteQueryTask implements RunnableTask {

    private final Long queryId;
    private final QueryExecutor queryExecutor;
    private final QueryDao queryDao;

    public ExecuteQueryTask(Task task, QueryExecutor queryExecutor, QueryDao queryDao) {
        this.queryDao = queryDao;
        this.queryId = Long.parseLong(task.getTaskConfigurationValueFor(TaskConfigurationEntryKey.QUERY_ID)); 
        this.queryExecutor = queryExecutor;
    }

    @Override
    public void run() {
        Query query = queryDao.findBy(queryId);
        String tmpFileName = "/tmp/result-" + query.getId() + ".csv";
        FileOutputStream outputStream;
        outputStream = createFile(tmpFileName);
        queryExecutor.execute(query, outputStream);
        query.executed(tmpFileName);
        queryDao.update(query);
    }

    private FileOutputStream createFile(String tmpFileName) {
        try {
            return new FileOutputStream(tmpFileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
