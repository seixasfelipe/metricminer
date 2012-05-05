package org.metricminer.tasks.metric.common;

import static org.metricminer.tasks.metric.common.FullMethodName.fullMethodName;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.VariableDeclaratorId;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MethodsAndAttributesVisitor extends ClassInfoVisitor {

	public List<Method> publicMethods;
	public List<Method> privateMethods;
	public List<Method> protectedMethods;
	public List<Method> defaultMethods;
	public List<Method> constructorMethods;
	
	public List<String> publicAttributes;
	public List<String> privateAttributes;
	public List<String> protectedAttributes;
	public List<String> defaultAttributes;
	
	private boolean isVisitingAnAttribute;
	private int currentAttributeModifier;

	private Stack<String> methodsInARow;
	
	public MethodsAndAttributesVisitor() {
		constructorMethods = new ArrayList<Method>();
		publicMethods = new ArrayList<Method>();
		privateMethods = new ArrayList<Method>();
		protectedMethods = new ArrayList<Method>();
		defaultMethods = new ArrayList<Method>();
		
		publicAttributes = new ArrayList<String>();
		privateAttributes = new ArrayList<String>();
		protectedAttributes = new ArrayList<String>();
		defaultAttributes = new ArrayList<String>();

		methodsInARow = new Stack<String>();
		
		
	}

	@Override
	public void visit(MethodDeclaration n, Object arg) {
		
			methodsInARow.push(n.getName());
			if(!methodIsInAInnerClass()) addMethod(n);
			super.visit(n, arg);
			methodsInARow.pop();
			
	}

	private void addMethod(MethodDeclaration n) {
		int modifier = n.getModifiers();
		String name = fullMethodName(n.getName(), n.getParameters());
		int lines = n.getEndLine() - n.getBeginLine();
		
		if(ModifierSet.isPrivate(modifier)) privateMethods.add(new Method(name, lines));
		else if(ModifierSet.isProtected(modifier)) protectedMethods.add(new Method(name, lines));
		else if(ModifierSet.isPublic(modifier)) publicMethods.add(new Method(name, lines));
		else defaultMethods.add(new Method(name, lines));
	}
	
	private boolean methodIsInAInnerClass() {
		return methodsInARow.size() > 1;
	}
	
	@Override
	public void visit(ConstructorDeclaration n, Object arg) {
		addConstructor(n.getName(), n.getEndLine() - n.getBeginLine());
		super.visit(n, arg);
	}

	private void addConstructor(String name, int lines) {
		constructorMethods.add(new Method(name, lines));		
	}

	@Override
	public void visit(FieldDeclaration n, Object arg) {
		isVisitingAnAttribute = true;
		currentAttributeModifier = n.getModifiers();
		super.visit(n, arg);
		isVisitingAnAttribute = false;
	}

	public void visit(VariableDeclaratorId n, Object arg) {
		if(isVisitingAnAttribute) {
			if(ModifierSet.isPrivate(currentAttributeModifier)) privateAttributes.add(n.getName());
			else if(ModifierSet.isPublic(currentAttributeModifier)) publicAttributes.add(n.getName());
			else if(ModifierSet.isProtected(currentAttributeModifier)) protectedAttributes.add(n.getName());
			else defaultAttributes.add(n.getName());
		}
    }
	
	public List<Method> getMethods() {
		List<Method> methods = new ArrayList<Method>();
		methods.addAll(protectedMethods);
		methods.addAll(publicMethods);
		methods.addAll(privateMethods);
		methods.addAll(defaultMethods);
		methods.addAll(constructorMethods);

		return methods;
	}
	

	public List<String> getAttributes() {
		List<String> attributes = new ArrayList<String>();
		attributes.addAll(protectedAttributes);
		attributes.addAll(publicAttributes);
		attributes.addAll(privateAttributes);
		attributes.addAll(defaultAttributes);

		return attributes;
	}

	public List<Method> getPublicMethods() {
		return publicMethods;
	}

	public List<Method> getPrivateMethods() {
		return privateMethods;
	}

	public List<Method> getProtectedMethods() {
		return protectedMethods;
	}

	public List<Method> getDefaultMethods() {
		return defaultMethods;
	}

	public List<Method> getConstructorMethods() {
		return constructorMethods;
	}

	public List<String> getPublicAttributes() {
		return publicAttributes;
	}

	public List<String> getPrivateAttributes() {
		return privateAttributes;
	}

	public List<String> getProtectedAttributes() {
		return protectedAttributes;
	}

	public List<String> getDefaultAttributes() {
		return defaultAttributes;
	}

	

}
