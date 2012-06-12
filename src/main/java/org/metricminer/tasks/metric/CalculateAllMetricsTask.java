package org.metricminer.tasks.metric;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.model.SourceCode;
import org.metricminer.model.Task;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class CalculateAllMetricsTask extends IterateOverSourcesAbstractTask {

	private final List<Metric> metrics;

	public CalculateAllMetricsTask(Task task, Session session, StatelessSession statelessSession, List<Metric> metrics) {
		super(task, session, statelessSession);
		this.metrics = metrics;
	}

	@Override
	protected void manipulate(SourceCode sourceCode, String name) {
		for (Metric metric : metrics) {
			calculateAndSaveResultsOf(sourceCode, name, metric);
		}
	}

	private void calculateAndSaveResultsOf(SourceCode sourceCode, String name, Metric metric) {
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
