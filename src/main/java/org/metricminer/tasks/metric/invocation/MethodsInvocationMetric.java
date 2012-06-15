package org.metricminer.tasks.metric.invocation;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.ClassInfoVisitor;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;


public class MethodsInvocationMetric implements Metric{

	private MethodsInvocationVisitor visitor;
	private ClassInfoVisitor info;
	
	public String header() {
		return "path;project;class;method;invocations";
	}

	public String content(String path, String project) {
		for(Entry<String, Set<String>> e : visitor.getMethods().entrySet()) {
			System.out.println(path + ";" + project + ";" + info.getName() + ";" + e.getKey() + ";" + e.getValue().size() + "\r\n");
		}
		return null;
	}

	public void calculate(InputStream is) {
		try {
			CompilationUnit cunit = JavaParser.parse(is);
			
			info = new ClassInfoVisitor();
			info.visit(cunit, null);
			
			visitor = new MethodsInvocationVisitor();
			visitor.visit(cunit, null);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Map<String, Set<String>> getMethods() {
		return visitor.getMethods();
	}

    @Override
    public Collection<MetricResult> resultsToPersistOf(SourceCode source) {
        ArrayList<MetricResult> results = new ArrayList<MetricResult>();
        for (Entry<String, Set<String>> e : visitor.getMethods().entrySet()) {
            results.add(new MethodsInvocationResult(source, e.getValue().size(), e.getKey()));
        }
        return results;
    }

    @Override
    public boolean shouldCalculateMetricOf(String fileName) {
        return fileName.endsWith(".java");
    }
    
    @Override
    public boolean matches(String name) {
        return name.endsWith(".java");
    }

	@Override
	public Class<?> getFactoryClass() {
		return MethodsInvocationMetricFactory.class;
	}

}
