package org.metricminer.config.project;

import java.util.Map;

public class MapConfig implements Config {

	private Map<String, String> cfgs;
	
	public MapConfig(Map<String, String> cfgs) {
		this.cfgs = cfgs;
	}
	
	public String asString(String key) {
		if(!cfgs.containsKey(key)) throw new ConfigNotFoundException("config not found: " + key);
		return cfgs.get(key);
	}

	public boolean contains(String key) {
		return cfgs.containsKey(key);
	}

	public int asInt(String key) {
		return Integer.parseInt(asString(key));
	}

}
