package tasks.metric.fanout;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.InputStream;

import model.SourceCode;
import tasks.metric.ClassInfoVisitor;
import tasks.metric.Metric;

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
    public Object resultToPersistOf(SourceCode source) {
        return new FanOutResult(source, fanOut());
    }

}
