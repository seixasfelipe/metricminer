package tasks.metric;

import model.SourceCode;

public interface CalculatedMetricResultBuilder {

    public CalculatedMetricResultBuilder withSource(SourceCode source);

    public CalculatedMetricResult build();

}
