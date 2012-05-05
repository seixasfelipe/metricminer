package org.metricminer.tasks.metric.common;

import japa.parser.ast.body.Parameter;

import java.util.List;

public class FullMethodName {

	public static String fullMethodName(String name, List<Parameter> parameters) {
		return name + "/" + (parameters == null ? "0" : (parameters.size() + typesIn(parameters)));
	}

	private static String typesIn(List<Parameter> parameters) {
		StringBuilder types = new StringBuilder();
		types.append("[");
		for(Parameter p : parameters) {
			types.append(p.getType() + ",");
		}
		
		return types.substring(0, types.length() - 1) + "]";
	}
}
