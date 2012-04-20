package tasks.metric.invocation;

import tasks.metric.common.Metric;
import tasks.metric.common.MetricFactory;

public class MethodsInvocationMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new MethodsInvocationMetric();
    }

}
