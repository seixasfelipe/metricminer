package org.metricminer.tasks.metric.lcom;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="LCOM")
public class LComMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new LComMetric();
    }

}
