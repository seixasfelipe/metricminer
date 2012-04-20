package tasks.metric.invocation;

import tasks.metric.Metric;
import tasks.metric.MetricFactory;

public class MethodsInvocationMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new MethodsInvocationMetric();
    }

}
