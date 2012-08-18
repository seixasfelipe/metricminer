package org.metricminer.tasks.projectmetric.common;

import java.util.List;

import org.hibernate.Session;
import org.metricminer.model.Project;

public class SampleProjectMetric implements ProjectMetric {

    @Override
    public List<Object> calculate(Session session, Project project) {
        return null;
    }

}
