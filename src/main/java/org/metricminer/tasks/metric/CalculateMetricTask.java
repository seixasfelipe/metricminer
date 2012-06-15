package org.metricminer.tasks.metric;

import java.io.ByteArrayInputStream;
import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.model.CalculatedMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.model.Task;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class CalculateMetricTask extends SourcesIteratorAbstractTask {

	private final Metric metric;

	public CalculateMetricTask(Task task, Metric metric, Session session, StatelessSession statelessSession) {
		super(task, session, statelessSession);
		this.metric = metric;
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

	@Override
	protected void onComplete() {
		session.beginTransaction();
		CalculatedMetric calculatedMetric = new CalculatedMetric(task.getProject(),
				metric.getFactoryClass());
		session.save(calculatedMetric);
		session.getTransaction().commit();
	}

}
