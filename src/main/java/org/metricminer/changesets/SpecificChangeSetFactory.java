package org.metricminer.changesets;

import org.metricminer.projectconfig.Config;
import org.metricminer.scm.SCM;


public interface SpecificChangeSetFactory {
	ChangeSetCollection build(SCM scm, Config config);
}
