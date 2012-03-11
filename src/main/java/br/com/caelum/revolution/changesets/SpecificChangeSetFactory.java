package br.com.caelum.revolution.changesets;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.scm.SCM;

public interface SpecificChangeSetFactory {
	ChangeSetCollection build(SCM scm, Config config);
}
