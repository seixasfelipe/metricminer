package org.metricminer.changesets;

import org.metricminer.projectconfig.Config;
import org.metricminer.scm.SCM;


public class AllChangeSetsFactory implements SpecificChangeSetFactory {

	public ChangeSetCollection build(SCM scm, Config config) {
		return new AllChangeSets(scm);
	}

}
