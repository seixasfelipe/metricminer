package org.metricminer.tasks.metric.common;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.AnnotationExpr;

import java.util.List;

public class ClassAssumptions {

	public static boolean isAnnotation(String annotation, AnnotationExpr annotationExpr) {
		return annotationExpr.getName().getName().toUpperCase().equals(annotation.toString());
	}

	public static boolean isATest(MethodDeclaration n) {
    	return n.getName().startsWith("test") || annotationExist("TEST", n.getAnnotations());
	}

	public static boolean annotationExist(String annotation, List<AnnotationExpr> annotations) {
		if(annotations == null) return false;
		for (AnnotationExpr annotationExpr : annotations) {
			if(isAnnotation(annotation, annotationExpr)) return true;
		}
		return false;
	}

}
