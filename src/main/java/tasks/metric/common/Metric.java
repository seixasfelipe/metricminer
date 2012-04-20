package tasks.metric.common;

import java.io.InputStream;
import java.util.Collection;

import model.SourceCode;

public interface Metric {
    String header();

    Collection<MetricResult> resultsToPersistOf(SourceCode source);

    void calculate(InputStream is);

    boolean shouldCalculateMetricOf(String fileName);
}
