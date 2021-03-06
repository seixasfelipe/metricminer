package org.metricminer.tasks.query;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.infra.dao.QueryDao;
import org.metricminer.model.Query;
import org.metricminer.model.QueryResult;
import org.metricminer.model.Task;
import org.metricminer.model.TaskConfigurationEntryKey;
import org.metricminer.tasks.RunnableTask;

public class ExecuteQueryTask implements RunnableTask {

    private final Long queryId;
    private final QueryExecutor queryExecutor;
    private final QueryDao queryDao;
	private final MetricMinerConfigs config;

    public ExecuteQueryTask(Task task, QueryExecutor queryExecutor, QueryDao queryDao, MetricMinerConfigs config) {
        this.queryDao = queryDao;
		this.config = config;
        this.queryId = Long.parseLong(task.getTaskConfigurationValueFor(TaskConfigurationEntryKey.QUERY_ID)); 
        this.queryExecutor = queryExecutor;
    }

    @Override
    public void run() {
        Query query = queryDao.findBy(queryId);
        String csvFileName = config.getQueriesResultsDir() + "/result-" + query.getId() + "-" + query.getResultCount() + ".csv";
        FileOutputStream outputStream = createFile(csvFileName);
        queryExecutor.execute(query, outputStream);
        query.addResult(new QueryResult(csvFileName));
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
