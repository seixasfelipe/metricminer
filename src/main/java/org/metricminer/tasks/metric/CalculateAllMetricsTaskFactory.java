package org.metricminer.tasks.metric;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.MetricMinerExeption;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.model.RegisteredMetric;
import org.metricminer.model.Task;
import org.metricminer.tasks.RunnableTask;
import org.metricminer.tasks.RunnableTaskFactory;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

public class CalculateAllMetricsTaskFactory implements RunnableTaskFactory {

	@Override
    public RunnableTask build(Task task, Session session, StatelessSession statelessSession, MetricMinerConfigs config) {
    	List<RegisteredMetric> registeredMetrics = config.getRegisteredMetrics();
    	List<Metric> metrics = new ArrayList<Metric>();
    	for (RegisteredMetric registeredMetric : registeredMetrics) {
    		MetricFactory metricFactory;
    		String className = registeredMetric.getMetricFactoryClassName();
			try {
				metricFactory = (MetricFactory) Class.forName(className).newInstance();
			} catch (Exception e) {
				throw new MetricMinerExeption("Could not instatiate metric factory for: "+ className, e);
			}
    		metrics.add(metricFactory.build());
		}
    	System.out.println("calculate all tasks");
		return new CalculateAllMetricsTask(task, session, statelessSession, metrics);
    }
}
