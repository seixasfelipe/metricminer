package org.metricminer.tasks.metric.testedmethods;

import static org.metricminer.tasks.metric.common.ClassAssumptions.isATest;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class TestedMethodVisitor extends VoidVisitorAdapter<Object> {

	private final String productionClass;
	private Stack<String> currentMethod;
	private String referencedType;
	private Map<String, String> methodScopedVariables;
	private Map<String, String> attributeScopedVariables;
	private Map<String, Set<String>> invokedMethods;
	private String assignTarget;

	public TestedMethodVisitor(String testClass) {
		this.productionClass = testClass.replace("Tests", "").replace("Test", "");
		this.invokedMethods = new HashMap<String, Set<String>>();
		this.attributeScopedVariables = new HashMap<String, String>();
		currentMethod = new Stack<String>();
	}
	
	public void visit(FieldDeclaration expr, Object arg) {
		for(VariableDeclarator v : expr.getVariables()) {
			attributeScopedVariables.put(v.getId().getName(), expr.getType().toString());
		}
	}

	public void visit(MethodDeclaration expr, Object arg) {
		if(isATest(expr)) {
			currentMethod.push(expr.getName());
			methodScopedVariables = new HashMap<String, String>();
			
			super.visit(expr, arg);
			currentMethod.pop();
		}
		else {
			super.visit(expr, arg);
		}
	}
	
	public void visit(ObjectCreationExpr expr, Object arg) {
		String type = expr.getType().getName();
		
		if(assignTarget!=null && !assignTarget.isEmpty()) {
			changeType(assignTarget, type, methodScopedVariables);
			changeType(assignTarget, type, attributeScopedVariables);
			assignTarget = null;
		}
		
		super.visit(expr, arg);
	}
	
	public void visit(AssignExpr expr, Object arg) {
		assignTarget = expr.getTarget().toString();
		super.visit(expr, arg);
	}

	private void changeType(String target, String type, Map<String, String> methods) {
		if(methods==null) return;
		if(methods.containsKey(target)) {
			methods.remove(target);
			methods.put(target, type);
		}
		
	}

	public void visit(ReferenceType expr, Object arg) {
		referencedType = expr.toString();
	}

	
	public void visit(PrimitiveType expr, Object arg) {
		referencedType = expr.toString();
	}
	
	public void visit(VariableDeclarator expr, Object arg) {
		String lastReferencedType = referencedType;
		super.visit(expr, arg);
		if(isCollection(lastReferencedType)) lastReferencedType = referencedType;
		
		if(methodScopedVariables!=null) methodScopedVariables.put(expr.getId().toString(), lastReferencedType);
	}
	
	private boolean isCollection(String type) {
		return type.contains("List<") || type.contains("Set<") || type.contains("Map<") || type.contains("Queue<") || type.contains("Stack<"); 
	}

	public void visit(ForStmt n, Object arg) {
		if (n.getInit() != null) {
            for (Expression e : n.getInit()) {
                e.accept(this, arg);
            }
        }
	}

	
	public void visit(MethodCallExpr expr, Object arg) {
		if(!currentMethod.isEmpty()) {
			
			ScopeFinder scopeFinder = new ScopeFinder();

			if(expr.getScope() !=null) { 
				expr.getScope().accept(scopeFinder, null);
				
				String variable = scopeFinder.getScope();
				if(isSameTypeFromProductionClass(variable) || isStaticInvocationInProductionClass(variable)) {
					addInvocation(expr.getName());
				}
			}
		}
		
		super.visit(expr, arg);
	}

	private boolean isStaticInvocationInProductionClass(String variable) {
		return variable != null && variable.equals(productionClass);
	}

	private void addInvocation(String name) {
		
		if(!invokedMethods.containsKey(currentMethod.peek())) {
			invokedMethods.put(currentMethod.peek(), new HashSet<String>());
		}
		
		invokedMethods.get(currentMethod.peek()).add(name);
		
	}

	private boolean isSameTypeFromProductionClass(String variable) {
		if(findInScope(variable, methodScopedVariables) || findInScope(variable, attributeScopedVariables)) return true;
		return false;
	}

	private boolean findInScope(String variable, Map<String, String> scope) {
		return scope.containsKey(variable) && scope.get(variable).equals(productionClass);
	}

	public Map<String, Set<String>> getInvokedMethods() {
		return invokedMethods;
	}
}

class ScopeFinder extends VoidVisitorAdapter<Object> {
	private String scope;

	public void visit(NameExpr expr, Object arg) {
		scope = expr.getName();
	}
	
	public void visit(ObjectCreationExpr expr, Object arg) {
		scope = expr.getType().getName(); // consider it as static invocation
	}
	
	public String getScope() {
		return scope;
	}
}
