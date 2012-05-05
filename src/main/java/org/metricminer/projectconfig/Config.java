package org.metricminer.projectconfig;

public interface Config {
	String asString(String key);
	boolean contains(String key);
	int asInt(String key);
}
