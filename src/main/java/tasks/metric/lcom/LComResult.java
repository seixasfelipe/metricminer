package tasks.metric.lcom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import model.SourceCode;
import tasks.metric.common.MetricResult;

@Entity
public class LComResult implements MetricResult {

    @Id
    @GeneratedValue
    private Long Id;

    @OneToOne
    private SourceCode source;
    
    private double lcom;

    public LComResult(SourceCode source, double lcom) {
        this.source = source;
        this.lcom = lcom;
    }

}
