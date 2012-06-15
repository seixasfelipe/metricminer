package org.metricminer.tasks.metric.testedmethods;

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


public class TestedMethodFinderMetric implements Metric{

	private ClassInfoVisitor classInfo;
	private TestedMethodVisitor visitor;

	public String header() {
		return "path;project;class;test method; production method";
	}

	public String content(String path, String project) {
		for(Entry<String, Set<String>> testMethod : getMethods().entrySet()) {
			for(String productionMethod : testMethod.getValue()) {
				System.out.println(path + ";" + project + ";" + classInfo.getName() + ";" + testMethod.getKey()  +";" + productionMethod);
			}
		}
		return null;
	}

	public void calculate(InputStream is) {
		try {
			CompilationUnit cunit = JavaParser.parse(is);
			
			classInfo = new ClassInfoVisitor();
			classInfo.visit(cunit, null);

			visitor = new TestedMethodVisitor(classInfo.getName());
			visitor.visit(cunit, null);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}		
	}

	public Map<String, Set<String>> getMethods() {
		return visitor.getInvokedMethods();
	}

    @Override
    public Collection<MetricResult> resultsToPersistOf(SourceCode source) {
        ArrayList<MetricResult> results = new ArrayList<MetricResult>();
        for(Entry<String, Set<String>> testMethod : getMethods().entrySet()) {
            for(String productionMethod : testMethod.getValue()) {
                results.add(new TestedMethodFinderResult(source, testMethod.getKey(),
                        productionMethod));
            }
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
		return TestedMethodsFinderMetricFactory.class;
	}

}
