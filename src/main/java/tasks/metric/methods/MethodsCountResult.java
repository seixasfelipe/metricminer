package tasks.metric.methods;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import model.SourceCode;
import tasks.metric.common.MetricResult;

@Entity
public class MethodsCountResult implements MetricResult {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private SourceCode sourceCode;
    
    private int privateMethods;
    private int publicMethods;
    private int crotectedMethods;
    private int defaultMethods;
    private int constructorMethods;
    private int privateAttributes;
    private int publicAttributes;
    private int protectedAttributes;
    private int defaultAttributes;

    public MethodsCountResult(SourceCode sourceCode, int privateMethods, int publicMethods,
            int crotectedMethods, int defaultMethods, int constructorMethods,
            int privateAttributes, int publicAttributes, int protectedAttributes,
            int defaultAttributes) {
        super();
        this.sourceCode = sourceCode;
        this.privateMethods = privateMethods;
        this.publicMethods = publicMethods;
        this.crotectedMethods = crotectedMethods;
        this.defaultMethods = defaultMethods;
        this.constructorMethods = constructorMethods;
        this.privateAttributes = privateAttributes;
        this.publicAttributes = publicAttributes;
        this.protectedAttributes = protectedAttributes;
        this.defaultAttributes = defaultAttributes;
    }


}
