package org.metricminer.tasks.metric.invocation;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="Quantity of Methods Invocation")
public class MethodsInvocationMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new MethodsInvocationMetric();
    }

}
