package org.metricminer.tasks.metric;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.model.CalculatedMetric;
import org.metricminer.model.SourceCode;
import org.metricminer.model.Task;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class CalculateAllMetricsTask extends SourcesIteratorAbstractTask {

	private final List<Metric> metrics;
	private final StatelessSession statelessSession;

	public CalculateAllMetricsTask(Task task, Session session, StatelessSession statelessSession,
			List<Metric> metrics) {
		super(task, session, statelessSession);
		this.statelessSession = statelessSession;
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
			ByteArrayInputStream inputStream = new ByteArrayInputStream(
					sourceCode.getSourceBytesArray());
			metric.calculate(inputStream);
			inputStream.close();
			Collection<MetricResult> results = metric.resultsToPersistOf(sourceCode);

			for (MetricResult result : results) {
				statelessSession.insert(result);
			}
		} catch (Throwable t) {
			log.error("Unable to calculate metric: ", t);
		}
	}

	@Override
	protected void onComplete() {
		statelessSession.beginTransaction();
		for (Metric metric : metrics) {
			CalculatedMetric calculatedMetric = new CalculatedMetric(task.getProject(),
					metric.getFactoryClass());
			statelessSession.insert(calculatedMetric);
		}
		statelessSession.getTransaction().commit();
	}

}
