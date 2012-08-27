package org.metricminer.tasks.projectmetric.truckfactor;

import org.hibernate.Session;
import org.metricminer.model.Project;
import org.metricminer.tasks.projectmetric.common.ProjectMetric;
import org.metricminer.tasks.projectmetric.common.ProjectMetricFactory;

public class TruckFactorFactory implements ProjectMetricFactory {

    @Override
    public ProjectMetric build(Session session, Project project) {
        return new TruckFactor(new TruckFactorDao(session), project);
    }
    
}
