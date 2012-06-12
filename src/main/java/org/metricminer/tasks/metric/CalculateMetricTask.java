package org.metricminer.tasks.metric;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.infra.dao.SourceCodeDAO;
import org.metricminer.model.Project;
import org.metricminer.model.SourceCode;
import org.metricminer.model.Task;
import org.metricminer.tasks.RunnableTask;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class CalculateMetricTask implements RunnableTask {

	private Task task;
	private Metric metric;
	private Session session;
	private static Logger log = Logger.getLogger(CalculateMetricTask.class);
	private SourceCodeDAO sourceCodeDAO;

	public CalculateMetricTask(Task task, Metric metric, Session session, StatelessSession statelessSession) {
		this.task = task;
		this.metric = metric;
		this.session = session;
		this.sourceCodeDAO = new SourceCodeDAO(statelessSession);
	}

	@Override
	public void run() {
		Project project = task.getProject();

		log.debug("Starting to calculate metric " + metric.getClass().getName());
		
		Map<Long, String> map = sourceCodeDAO.listSourceCodeIdsAndNamesFor(project);
		List<Long> sourceIds = new ArrayList<Long>(map.keySet());
		
		int totalIds = sourceIds.size();
		
		int PAGE_SIZE = 5;
		for (int i = 0; i < totalIds; i += PAGE_SIZE) {
			Long firstId = sourceIds.get(i);
			Long lastId;
			int lastIdIndex = i + (PAGE_SIZE - 1);
			if (lastIdIndex < sourceIds.size()) 
				lastId = sourceIds.get(lastIdIndex);
			else
				lastId = sourceIds.get(sourceIds.size() - 1);
			log.debug("Getting source codes (page " + i/PAGE_SIZE + ")");
			List<SourceCode> sources = sourceCodeDAO.listSourcesOf(project, firstId, lastId);
			for (SourceCode sc : sources) {
				log.debug("-- Working on " + map.get(sc.getId()) + " id " + sc.getId());
				calculateAndSaveResultsOf(sc, map.get(sc.getId()));
			}
			System.gc();
		}
		log.debug("Metric " + metric.getClass().getName() + " has finished.");
		
	}

	private void calculateAndSaveResultsOf(SourceCode sourceCode, String name) {

		if (!metric.matches(name))
			return;

		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceCode.getSourceBytesArray());
			metric.calculate(inputStream);
			inputStream.close();
			Collection<MetricResult> results = metric.resultsToPersistOf(sourceCode);
			session.getTransaction().begin();

			for (MetricResult result : results) {
				session.save(result);
				session.flush();
				session.clear();
			}
			session.getTransaction().commit();
		} catch (Throwable t) {
			log.error("Unable to calculate metric: ", t);
		}
	}

}
