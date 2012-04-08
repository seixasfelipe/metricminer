package tasks.metric.cc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import model.SourceCode;
import tasks.metric.CalculatedMetricResult;

@Entity
public class CalculatedCCMetric implements CalculatedMetricResult {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private SourceCode source;
    private int CC;
    private double averageCC;

    public CalculatedCCMetric(SourceCode source, int CC, double averageCC) {
        this.source = source;
        this.CC = CC;
        this.averageCC = 0.0;
    }

}
