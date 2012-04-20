package tasks.metric.testedmethods;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import model.SourceCode;
import tasks.metric.common.MetricResult;

@Entity
public class TestedMethodFinderResult implements MetricResult {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private SourceCode sourceCode;

    private String testMethod;
    private String productionMethod;

    public TestedMethodFinderResult(SourceCode sourceCode, String testMethod,
            String productionMethod) {
        this.sourceCode = sourceCode;
        this.testMethod = testMethod;
        this.productionMethod = productionMethod;
    }

}
