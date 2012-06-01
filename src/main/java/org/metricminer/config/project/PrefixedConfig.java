package org.metricminer.config.project;

public class PrefixedConfig implements Config{

	private final Config cfg;
	private final String prefix;

	public PrefixedConfig(Config cfg, String prefix) {
		this.cfg = cfg;
		this.prefix = prefix + ".";
	}

	public String asString(String key) {
		return cfg.asString(prefix + key);
	}

	public boolean contains(String key) {
		return cfg.contains(prefix + key);
	}

	public int asInt(String key) {
		return cfg.asInt(prefix + key);
	}
}
