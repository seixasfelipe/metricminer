package tasks.metric.fanout;

import tasks.metric.Metric;
import tasks.metric.MetricFactory;

public class FanOutMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new FanOutMetric();
    }

}
