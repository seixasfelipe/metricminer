package tasks.metric.fanout;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import model.SourceCode;
import tasks.metric.MetricResult;

@Entity
public class FanOutResult implements MetricResult {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private SourceCode sourceCode;

    private int fanOut;

    public FanOutResult(SourceCode sourceCode, int fanOut) {
        this.sourceCode = sourceCode;
        this.fanOut = fanOut;
    }

}
