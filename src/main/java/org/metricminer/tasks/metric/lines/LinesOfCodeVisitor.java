package org.metricminer.tasks.metric.lines;

import static org.metricminer.tasks.metric.common.FullMethodName.fullMethodName;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

public class LinesOfCodeVisitor extends VoidVisitorAdapter<Object>{

	private Map<String, Integer> methodLines = new HashMap<String, Integer>();
	
	public void visit(MethodDeclaration n, Object arg) {
		methodLines.put(fullMethodName(n.getName(), n.getParameters()), n.getEndLine() - n.getBeginLine());
	}
	
	public Map<String, Integer> methodLines() {
		return methodLines;
	}

}
