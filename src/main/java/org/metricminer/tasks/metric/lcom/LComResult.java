package org.metricminer.tasks.metric.lcom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.MetricResult;


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

	public Long getId() {
		return Id;
	}

	public SourceCode getSource() {
		return source;
	}

	public double getLcom() {
		return lcom;
	}
    
    

}
