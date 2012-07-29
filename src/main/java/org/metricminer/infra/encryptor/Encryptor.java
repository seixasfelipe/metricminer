package org.metricminer.infra.encryptor;

import org.apache.commons.codec.digest.DigestUtils;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class Encryptor {
	public String encrypt(String pass) {
		return DigestUtils.sha256Hex(pass);
	}
}
