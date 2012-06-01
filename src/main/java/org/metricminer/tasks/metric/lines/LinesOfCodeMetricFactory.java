package org.metricminer.tasks.metric.lines;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="Lines of Code")
public class LinesOfCodeMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new LinesOfCodeMetric();
    }

}
