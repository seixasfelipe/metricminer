package tasks.metric.cc;

import model.SourceCode;
import tasks.metric.CalculatedMetricResultBuilder;

public class CalculatedCCMetricBuilder implements CalculatedMetricResultBuilder {

    private int CC;
    private double averageCC;
    private SourceCode source;

    public CalculatedCCMetric build() {
        return new CalculatedCCMetric(source, CC, averageCC);
    }

    public CalculatedCCMetricBuilder withCC(int CC) {
        this.CC = CC;
        return this;
    }

    public CalculatedCCMetricBuilder withSource(SourceCode source) {
        this.source = source;
        return this;
    }

    public CalculatedCCMetricBuilder withAverageCC(double averageCC) {
        this.averageCC = averageCC;
        return this;
    }
}
