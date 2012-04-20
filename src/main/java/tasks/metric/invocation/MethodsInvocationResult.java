package tasks.metric.invocation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import model.SourceCode;
import tasks.metric.MetricResult;

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

}
