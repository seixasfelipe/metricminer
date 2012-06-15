package org.metricminer.tasks.metric.methods;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.Method;
import org.metricminer.tasks.metric.common.MethodsAndAttributesVisitor;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;


public class MethodsCountMetric implements Metric {

	private MethodsAndAttributesVisitor visitor;

	public String header() {
		return "path;project;class;private methods;public methods;protected methods;default methods;constructors;private attributes;public attributes;protected attributes;default attributes";
	}

	public String content(String path, String project) {
		return 
			path + ";" +
			project + ";" +
			visitor.getName() + ";" +
			visitor.getPrivateMethods().size() + ";"+
			visitor.getPublicMethods().size() + ";"+
			visitor.getProtectedMethods().size() + ";"+
			visitor.getDefaultMethods().size() + ";"+
			visitor.getConstructorMethods().size() + ";"+
			visitor.getPrivateAttributes().size() + ";"+
			visitor.getPublicAttributes().size() + ";"+
			visitor.getProtectedAttributes().size() + ";"+
			visitor.getDefaultAttributes().size() + "\r\n";
	}

	public void calculate(InputStream is) {
		try {
			CompilationUnit cunit = JavaParser.parse(is);
			
			visitor = new MethodsAndAttributesVisitor();
			visitor.visit(cunit, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}


	public String getName() {
		return visitor.getName();
	}

	public List<Method> getMethods() {
		return visitor.getMethods();
	}

	public List<String> getAttributes() {
		return visitor.getAttributes();
	}

	public List<Method> getPublicMethods() {
		return visitor.getPublicMethods();
	}

	public List<Method> getPrivateMethods() {
		return visitor.getPrivateMethods();
	}

	public List<Method> getProtectedMethods() {
		return visitor.getProtectedMethods();
	}

	public List<Method> getDefaultMethods() {
		return visitor.getDefaultMethods();
	}

	public List<Method> getConstructorMethods() {
		return visitor.getConstructorMethods();
	}

	public List<String> getPublicAttributes() {
		return visitor.getPublicAttributes();
	}

	public List<String> getPrivateAttributes() {
		return visitor.getPrivateAttributes();
	}

	public List<String> getProtectedAttributes() {
		return visitor.getProtectedAttributes();
	}

	public List<String> getDefaultAttributes() {
		return visitor.getDefaultAttributes();
	}

    @Override
    public Collection<MetricResult> resultsToPersistOf(SourceCode source) {
        return Arrays.asList((MetricResult) new MethodsCountResult(source, getPrivateAttributes()
                .size(), getPublicMethods().size(), getProtectedMethods().size(),
                getDefaultMethods().size(), getConstructorMethods().size(), getPrivateAttributes()
                        .size(), getPublicAttributes().size(), getProtectedAttributes().size(),
                getDefaultAttributes().size()));
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
		return MethodsCountMetricFactory.class;
	}

}
