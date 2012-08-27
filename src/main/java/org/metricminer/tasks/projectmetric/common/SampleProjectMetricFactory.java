package org.metricminer.tasks.projectmetric.common;

import org.hibernate.Session;
import org.metricminer.model.Project;

public class SampleProjectMetricFactory implements ProjectMetricFactory {

    @Override
    public ProjectMetric build(Session session, Project project) {
        return null;
    }

}
