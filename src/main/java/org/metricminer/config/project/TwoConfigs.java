package org.metricminer.config.project;


public class TwoConfigs implements Config {

	private final Config cfg;
	private final Config extendedCfgs;

	public TwoConfigs(Config cfg, Config extendedCfgs) {
		this.cfg = cfg;
		this.extendedCfgs = extendedCfgs;
	}
	
	public String asString(String key) {
		if(cfg.contains(key)) return cfg.asString(key);
		return extendedCfgs.asString(key);
	}

	public boolean contains(String key) {
		return cfg.contains(key) || extendedCfgs.contains(key);
	}

	public int asInt(String key) {
		return Integer.parseInt(asString(key));
	}

}
