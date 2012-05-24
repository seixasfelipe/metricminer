package org.metricminer.tasks.metric.invocation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.MetricResult;


@Entity
public class MethodsInvocationResult implements MetricResult {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private SourceCode sourceCode;
    private double methodsInvocation;
    private String methodName;

    public MethodsInvocationResult(SourceCode sourceCode, double methodsInvocation,
            String methodName) {
        this.sourceCode = sourceCode;
        this.methodsInvocation = methodsInvocation;
        this.methodName = methodName;
    }

	public Long getId() {
		return id;
	}

	public SourceCode getSourceCode() {
		return sourceCode;
	}

	public double getMethodsInvocation() {
		return methodsInvocation;
	}

	public String getMethodName() {
		return methodName;
	}

    
}
