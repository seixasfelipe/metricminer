package tasks.metric.fanout;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import model.SourceCode;
import tasks.metric.common.ClassInfoVisitor;
import tasks.metric.common.Metric;
import tasks.metric.common.MetricResult;

public class FanOutMetric implements Metric {

	private FanOutVisitor visitor;
    private ClassInfoVisitor classInfo;

	public String header() {
		return "path;project;class;efferent class coupling";
	}

	public String content(String path, String project) {
		return path + ";" + project + ";" + classInfo.getName() + ";" + fanOut() + "\n";
	}

	public void calculate(InputStream is) {
		try {
			CompilationUnit cunit = JavaParser.parse(is);
			
			classInfo = new ClassInfoVisitor();
			classInfo.visit(cunit, null);
			
			visitor = new FanOutVisitor(classInfo.getName());
			visitor.visit(cunit, null);
			
			
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
	}

	public int fanOut() {
		return visitor.typesQty();
	}

    @Override
    public boolean shouldCalculateMetricOf(String fileName) {
        return fileName.endsWith(".java");
    }

    @Override
    public Collection<MetricResult> resultsToPersistOf(SourceCode source) {
        return Arrays.asList((MetricResult) new FanOutResult(source, fanOut()));
    }

    @Override
    public String fileNameSQLRegex() {
        return "%.java";
    }
    
}
