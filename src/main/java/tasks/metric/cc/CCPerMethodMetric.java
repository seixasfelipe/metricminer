package tasks.metric.cc;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.InputStream;
import java.util.Map.Entry;

import tasks.metric.CalculatedMetricResultBuilder;
import tasks.metric.ClassInfoVisitor;
import tasks.metric.Metric;

public class CCPerMethodMetric implements Metric {

    private CCVisitor visitor;
    private ClassInfoVisitor classInfo;

    public String header() {
        return "path;project;class;method;cc";
    }

    public String content(String path, String project) {
        for (Entry<String, Integer> method : visitor.getCcPerMethod().entrySet()) {
            System.out.println(path + ";" + project + ";" + classInfo.getName() + ";"
                    + method.getKey() + ";" + method.getValue());
        }

        return null;
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

    @Override
    public boolean shouldCalculateMetricOf(String fileName) {
        return fileName.endsWith(".java");
    }

    @Override
    public CalculatedMetricResultBuilder result() {
        // TODO Auto-generated method stub
        return null;
    }

}
