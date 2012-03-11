package br.com.caelum.revolution.config;

public class ConfigNotFoundException extends RuntimeException {

	public ConfigNotFoundException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
