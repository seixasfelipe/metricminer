package org.metricminer.tasks.metric.cc;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="Cyclomatic Complexity")
public class CCMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new CCMetric();
    }

}
