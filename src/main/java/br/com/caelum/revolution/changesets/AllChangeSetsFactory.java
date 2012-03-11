package br.com.caelum.revolution.changesets;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.scm.SCM;

public class AllChangeSetsFactory implements SpecificChangeSetFactory {

	public ChangeSetCollection build(SCM scm, Config config) {
		return new AllChangeSets(scm);
	}

}
