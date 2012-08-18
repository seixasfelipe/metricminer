package org.metricminer.tasks.projectmetric.common;

public class SampleProjectMetricFactory implements ProjectMetricFactory {

    @Override
    public ProjectMetric build() {
        return new SampleProjectMetric();
    }

}
