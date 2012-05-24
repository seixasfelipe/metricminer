package org.metricminer.tasks.metric.fanout;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.MetricResult;


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

	public Long getId() {
		return id;
	}

	public SourceCode getSourceCode() {
		return sourceCode;
	}

	public int getFanOut() {
		return fanOut;
	}
    
    

}
