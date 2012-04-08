package tasks.metric.cc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import model.SourceCode;

@Entity
public class CCMetricResultToPersist {

    @Id
    @GeneratedValue
    private Long id;
    private int cc;
    @OneToOne
    private SourceCode sourceCode;
    private double avgCc;

    public CCMetricResultToPersist(SourceCode sourceCode, int cc, double avgCc) {
        this.sourceCode = sourceCode;
        this.cc = cc;
        this.avgCc = avgCc;
    }

}
