package org.metricminer.tasks.metric.cc;

import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

public class CCMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new CCMetric();
    }

}
