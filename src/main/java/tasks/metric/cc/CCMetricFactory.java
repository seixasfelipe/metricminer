package tasks.metric.cc;

import tasks.metric.common.Metric;
import tasks.metric.common.MetricFactory;

public class CCMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new CCMetric();
    }

}
