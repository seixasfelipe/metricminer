package org.metricminer.infra.executor;

public interface CommandExecutor {
	String execute(String command, String basePath);
	void setEnvironmentVar(String name, String value);
}