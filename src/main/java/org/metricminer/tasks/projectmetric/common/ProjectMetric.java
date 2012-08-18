package org.metricminer.tasks.projectmetric.common;

import java.util.List;

import org.hibernate.Session;
import org.metricminer.model.Project;

public interface ProjectMetric {
    public List<Object> calculate(Session session, Project project);

}
