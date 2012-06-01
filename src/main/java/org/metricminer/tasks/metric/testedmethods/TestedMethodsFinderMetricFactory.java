package org.metricminer.tasks.metric.testedmethods;

import org.metricminer.tasks.MetricComponent;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

@MetricComponent(name="Tested Methods")
public class TestedMethodsFinderMetricFactory implements MetricFactory {

    @Override
    public Metric build() {
        return new TestedMethodFinderMetric();
    }

}
