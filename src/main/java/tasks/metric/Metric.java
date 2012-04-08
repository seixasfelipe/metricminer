package tasks.metric;

import java.io.InputStream;

import model.SourceCode;

public interface Metric {
    String header();

    Object resultToPersistOf(SourceCode source);

    void calculate(InputStream is);

    boolean shouldCalculateMetricOf(String fileName);
}
