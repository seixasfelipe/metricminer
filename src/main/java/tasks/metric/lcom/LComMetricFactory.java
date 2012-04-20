package tasks.metric.lcom;

import tasks.metric.common.Metric;
import tasks.metric.common.MetricFactory;

public class LComMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new LComMetric();
    }

}
