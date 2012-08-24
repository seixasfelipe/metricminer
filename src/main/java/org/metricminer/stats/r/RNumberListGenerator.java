package org.metricminer.stats.r;

import java.util.List;

public class RNumberListGenerator {

	public String generate(List<Double> numbers) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("c(");
		for(Double n : numbers) {
			sb.append(n + ",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")");
		
		return sb.toString();
		
	}
}
