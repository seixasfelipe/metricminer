package tasks.metric.cc;

import tasks.metric.Metric;
import tasks.metric.MetricFactory;

public class CCMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new CCMetric();
    }

}
