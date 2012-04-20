package tasks.metric.lines;

import tasks.metric.common.Metric;
import tasks.metric.common.MetricFactory;

public class LinesOfCodeMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new LinesOfCodeMetric();
    }

}
