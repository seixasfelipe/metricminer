package org.metricminer.tasks.metric;

import java.io.ByteArrayInputStream;
import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.model.SourceCode;
import org.metricminer.model.Task;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class CalculateMetricTask extends IterateOverSourcesAbstractTask {

	public CalculateMetricTask(Task task, Metric metric, Session session, StatelessSession statelessSession) {
		super(task, metric, session, statelessSession);
	}

	@Override
	protected void manipulate(SourceCode sourceCode, String name) {
		calculateAndSaveResultsOf(sourceCode, name);
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
