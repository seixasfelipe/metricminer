package org.metricminer.tasks.metric.lcom;

import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

public class LComMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new LComMetric();
    }

}
