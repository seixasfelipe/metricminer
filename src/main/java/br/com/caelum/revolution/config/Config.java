package br.com.caelum.revolution.config;

public interface Config {
	String asString(String key);
	boolean contains(String key);
	int asInt(String key);
}
