package tasks.metric.cc;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.InputStream;

import tasks.metric.ClassInfoVisitor;
import tasks.metric.Metric;

public class CCMetric implements Metric {

    private CCVisitor visitor;
    private ClassInfoVisitor classInfo;

    public String header() {
        return "path;project;class;cc;average cc";
    }

    public String content(String path, String project) {
        return path + ";" + project + ";" + classInfo.getName() + ";" + cc() + ";" + avgCc() + "\n";
    }

    public void calculate(InputStream is) {
        try {
            CompilationUnit cunit = JavaParser.parse(is);

            classInfo = new ClassInfoVisitor();
            classInfo.visit(cunit, null);

            visitor = new CCVisitor();
            visitor.visit(cunit, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double avgCc() {
        return visitor.getAvgCc();
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
