package tasks.metric.testedmethods;

import tasks.metric.common.Metric;
import tasks.metric.common.MetricFactory;

public class TestedMethodsFinderMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new TestedMethodFinderMetric();
    }

}
