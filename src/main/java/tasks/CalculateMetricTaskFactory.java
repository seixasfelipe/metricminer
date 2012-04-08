package tasks;

import model.Task;
import model.TaskConfigurationEntryKey;

import org.hibernate.Session;

import tasks.metric.Metric;
import tasks.metric.MetricFactory;
import tasks.runner.RunnableTask;
import tasks.runner.RunnableTaskFactory;

public class CalculateMetricTaskFactory implements RunnableTaskFactory {

    @Override
    public RunnableTask build(Task task, Session session) {
        String metricFactoryName = task
                .getTaskConfigurationValueFor(TaskConfigurationEntryKey.METRICFACTORYCLASS);
        try {
            MetricFactory metricFactory = (MetricFactory) Class.forName(metricFactoryName)
                    .newInstance();
            Metric metricToCalculate = metricFactory.build();
            return new CalculateMetricTask(task, metricToCalculate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
