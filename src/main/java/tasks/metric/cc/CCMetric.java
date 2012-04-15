package tasks.metric.cc;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.InputStream;

import model.SourceCode;
import tasks.metric.ClassInfoVisitor;
import tasks.metric.Metric;

public class CCMetric implements Metric {

    private CCVisitor visitor;
    private ClassInfoVisitor classInfo;

    public String header() {
        return "path;project;class;cc;average cc";
    }

    public Object resultToPersistOf(SourceCode sourceCode) {
        return new CCResult(sourceCode, cc(), avgCc());
    }

    public void calculate(InputStream is) {
        try {
            CompilationUnit cunit = JavaParser.parse(is);

            classInfo = new ClassInfoVisitor();
            classInfo.visit(cunit, null);

            visitor = new CCVisitor();
            visitor.visit(cunit, null);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public double avgCc() {
        double avgCc = visitor.getAvgCc();
        if (Double.isNaN(avgCc))
            avgCc = -1.0;
        return avgCc;
    }

    public int cc() {
        return visitor.getCc();
    }

    public int cc(String method) {
        return visitor.getCc(method);
    }

    @Override
    public boolean shouldCalculateMetricOf(String fileName) {
        return fileName.endsWith(".java");
    }
}
