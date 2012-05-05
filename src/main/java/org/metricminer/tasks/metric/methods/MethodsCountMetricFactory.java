package org.metricminer.tasks.metric.methods;

import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

public class MethodsCountMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new MethodsCountMetric();
    }

}
