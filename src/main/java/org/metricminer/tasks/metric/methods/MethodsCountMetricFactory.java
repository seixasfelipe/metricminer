package org.metricminer.tasks.metric.methods;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="Quantity of Methods")
public class MethodsCountMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new MethodsCountMetric();
    }

}
