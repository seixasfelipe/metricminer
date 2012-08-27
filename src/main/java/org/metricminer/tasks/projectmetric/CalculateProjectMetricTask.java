package org.metricminer.tasks.projectmetric;

import java.util.List;

import org.hibernate.Session;
import org.metricminer.model.Project;
import org.metricminer.tasks.RunnableTask;
import org.metricminer.tasks.metric.common.MetricResult;
import org.metricminer.tasks.projectmetric.common.ProjectMetric;

public class CalculateProjectMetricTask implements RunnableTask {

    private final ProjectMetric metric;
    private final Project project;
    private final Session session;

    public CalculateProjectMetricTask(ProjectMetric metric, Project project, Session session) {
        this.metric = metric;
        this.project = project;
        this.session = session;
    }

    @Override
    public void run() {
        List<MetricResult> results = metric.calculate();
        for (MetricResult object : results) {
            session.save(object);
        }
    }

}
