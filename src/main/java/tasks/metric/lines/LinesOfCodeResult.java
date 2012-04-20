package tasks.metric.lines;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import model.SourceCode;
import tasks.metric.common.MetricResult;

@Entity(name = "LinesOfCodeResult")
public class LinesOfCodeResult implements MetricResult {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private SourceCode sourceCode;

    private String methodName;
    private int linesOfCode;

    public LinesOfCodeResult(SourceCode sourceCode, String method, int lines) {
        this.sourceCode = sourceCode;
        this.methodName = method;
        this.linesOfCode = lines;
    }

}
