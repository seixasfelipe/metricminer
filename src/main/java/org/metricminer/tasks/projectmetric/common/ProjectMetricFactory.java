package org.metricminer.tasks.projectmetric.common;

import org.hibernate.Session;
import org.metricminer.model.Project;

public interface ProjectMetricFactory {
    
    public ProjectMetric build(Session session, Project project);
    
}
