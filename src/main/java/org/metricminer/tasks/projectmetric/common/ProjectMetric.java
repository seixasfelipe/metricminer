package org.metricminer.tasks.projectmetric.common;

import java.util.List;

import org.metricminer.tasks.metric.common.MetricResult;

public interface ProjectMetric {
    public List<MetricResult> calculate();

}
