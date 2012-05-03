package tasks.metric.lines;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import model.SourceCode;
import tasks.metric.common.MethodsAndAttributesVisitor;
import tasks.metric.common.Metric;
import tasks.metric.common.MetricResult;

public class LinesOfCodeMetric implements Metric {

	private LinesOfCodeVisitor visitor;
	private String clazzName;

	public String header() {
		return "path;project;class;method;lines";
	}

	public String content(String path, String project) {
		StringBuilder msg = new StringBuilder();
		for(Entry<String, Integer> entry : visitor.methodLines().entrySet()) {
			msg.append(path + ";" + project + ";" + clazzName + ";" + entry.getKey() + ";" + entry.getValue() + "\n");
		}
		
		return msg.toString();
	}

	public void calculate(InputStream is) {
		try {
			CompilationUnit cunit = JavaParser.parse(is);
			
			MethodsAndAttributesVisitor nameVisitor = new MethodsAndAttributesVisitor();
			nameVisitor.visit(cunit, null);
			
			clazzName = nameVisitor.getName();
			
			visitor = new LinesOfCodeVisitor();
			visitor.visit(cunit, null);
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Map<String, Integer> getMethodLines() {
		return visitor.methodLines();
	}

    @Override
    public Collection<MetricResult> resultsToPersistOf(SourceCode source) {
        StringBuilder msg;
        ArrayList<MetricResult> results = new ArrayList<MetricResult>();
        for (Entry<String, Integer> entry : visitor.methodLines().entrySet()) {
            results.add(new LinesOfCodeResult(source, entry.getKey(), entry.getValue()));
        }
        return results;
    }

    @Override
    public boolean shouldCalculateMetricOf(String fileName) {
        return fileName.endsWith(".java");
    }
    
    @Override
    public String fileNameSQLRegex() {
        return "%.java";
    }

}
