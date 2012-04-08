package tasks.metric;

import java.io.InputStream;

public interface Metric {
    String header();

    CalculatedMetricResultBuilder result();

    void calculate(InputStream is);

    boolean shouldCalculateMetricOf(String fileName);
}
