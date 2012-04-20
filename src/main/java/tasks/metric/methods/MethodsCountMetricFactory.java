package tasks.metric.methods;

import tasks.metric.common.Metric;
import tasks.metric.common.MetricFactory;

public class MethodsCountMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new MethodsCountMetric();
    }

}
