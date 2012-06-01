package org.metricminer.tasks.metric.fanout;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="Fan Out")
public class FanOutMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new FanOutMetric();
    }

}
