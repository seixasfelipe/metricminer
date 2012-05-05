package org.metricminer.tasks.metric.invocation;

import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

public class MethodsInvocationMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new MethodsInvocationMetric();
    }

}
