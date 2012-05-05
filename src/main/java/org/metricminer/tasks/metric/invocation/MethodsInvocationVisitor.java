package org.metricminer.tasks.metric.invocation;

import static org.metricminer.tasks.metric.common.FullMethodName.fullMethodName;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class MethodsInvocationVisitor extends VoidVisitorAdapter<Object> {

	private Map<String, Set<String>> methods;
	private Stack<String> currentMethods;
	
	public MethodsInvocationVisitor() {
		methods = new HashMap<String, Set<String>>();
		currentMethods = new Stack<String>();
		
	}
	
	public void visit(MethodDeclaration expr, Object arg) {
		currentMethods.push(fullMethodName(expr.getName(), expr.getParameters()));
		super.visit(expr, arg);
		currentMethods.pop();
	}
	
	public void visit(MethodCallExpr n, Object arg) {
		invoked(n.getName());
		super.visit(n, arg);
		
	}
	
	private void invoked(String name) {
		if(!methods.containsKey(currentMethods.peek())) {
			methods.put(currentMethods.peek(), new HashSet<String>());
		}
		methods.get(currentMethods.peek()).add(name);
	}

	public Map<String, Set<String>> getMethods() {
		return methods;
	}

}
