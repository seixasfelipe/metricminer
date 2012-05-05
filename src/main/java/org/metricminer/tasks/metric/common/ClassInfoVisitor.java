package org.metricminer.tasks.metric.common;

import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class ClassInfoVisitor extends VoidVisitorAdapter<Object>{

	private String name;
	private boolean firstTime = true;
	private boolean isInterface;
	private boolean isAnnotation;
	private boolean isEnum;
	
	public void visit(EnumDeclaration n, Object arg) {
		found(n.getName(), false, false, true);
		
	}
	public void visit(AnnotationDeclaration n, Object arg) {
		found(n.getName(), false, true, false);
		
		super.visit(n, arg);
	}
	
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		found(n.getName(), n.isInterface(), false, false);
		super.visit(n, arg);
	}
	
	private void found(String name, boolean isInterface, boolean isAnnotation, boolean isEnum) {
		if(firstTime) {
			this.isInterface = isInterface;
			this.isAnnotation = isAnnotation;
			this.isEnum = isEnum;
			this.name = name;
			firstTime = false;
		}
	}

	public String getName() {
		return name;
	}

	public boolean isInterface() {
		return isInterface;
	}
	
	public boolean isAnnotation() {
		return isAnnotation;
	}
	
	public boolean isEnum() {
		return isEnum;
	}
}
