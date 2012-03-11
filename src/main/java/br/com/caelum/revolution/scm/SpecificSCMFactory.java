package br.com.caelum.revolution.scm;

import br.com.caelum.revolution.config.Config;

public interface SpecificSCMFactory {
	SCM build(Config config);
}
