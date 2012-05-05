package org.metricminer.tasks.metric.lcom;

import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

class MethodsPerAttributeVisitor extends VoidVisitorAdapter<Object> {
	
	private Stack<String> currentMethod;
	private final List<String> attributes;
	private Map<String, Set<String>> methodsPerAttribute;

	
	public MethodsPerAttributeVisitor(List<String> attributes) {
		this.attributes = attributes;
		currentMethod = new Stack<String>();
		methodsPerAttribute = new HashMap<String, Set<String>>();
	}
	
    public void visit(NameExpr n, Object arg) {
    	if(currentMethod.size()>0 && attributes.contains(n.getName())) {
    		if (!methodsPerAttribute.containsKey(n.getName())) methodsPerAttribute.put(n.getName(), new HashSet<String>());
    		methodsPerAttribute.get(n.getName()).add(currentMethod.peek());
    	}
    }

	@Override
	public void visit(MethodDeclaration n, Object arg) {
		currentMethod.push(n.getName());
		super.visit(n, arg);
		currentMethod.pop();
	}
	
	@Override
	public void visit(ConstructorDeclaration n, Object arg) {
		currentMethod.push(n.getName());
		super.visit(n, arg);
		currentMethod.pop();
	}

	public Map<String, Set<String>> getMethodsPerAttribute() {
		return methodsPerAttribute;
	}

	
}
