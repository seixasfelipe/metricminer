package org.metricminer.tasks.metric.fanout;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Set;

public class FanOutVisitor extends VoidVisitorAdapter<Object> {

	private Set<String> referencedClasses;
	private Set<String> importedClasses;
	private final String clazzName;

	public FanOutVisitor(String clazzName) {
		this.clazzName = clazzName;
		referencedClasses = new HashSet<String>();
		importedClasses = new HashSet<String>();
	}

	public void visit(ImportDeclaration n, Object arg) {
		importedClasses.add(n.getName().toString());
		
		if(isStaticImport(n)) {
			String importLine = n.getName().toString();
			String importedClass;
			
			if (importLine.contains(".")) {
				String[] splittedImport = importLine.split("\\.");
				importedClass = splittedImport[splittedImport.length - 2];
			} else {
				importedClass = importLine;
			}
	
			addReference(importedClass);
		}
	}

	private void addReference(String importedClass) {
		if(!importedClass.equals(clazzName)) referencedClasses.add(importedClass);
	}
	
	public void visit(MethodCallExpr n, Object arg) {
		String invocatedClass = getInvocatedClassInExpr(n);
		if(itWasImported(invocatedClass)) {
			addReference(invocatedClass);
		}

		super.visit(n, arg);
	}

	private String getInvocatedClassInExpr(MethodCallExpr n) {
		String expr = n.toString();
		String invocatedClass;
		if(expr.contains(".")) invocatedClass = expr.split("\\.")[0];
		else invocatedClass = expr;
		return invocatedClass;
	}

	private boolean itWasImported(String invocatedClass) {
		for(String importedClass : importedClasses) {
			if(importedClass.endsWith(invocatedClass)) return true;
		}
		return false;
	}

	private boolean isStaticImport(ImportDeclaration n) {
		return n.toString().contains("import static");
	}

	public void visit(ClassOrInterfaceType n, Object arg) {
		addReference(n.getName());
	}

	public int typesQty() {
		return referencedClasses.size();
	}

}
