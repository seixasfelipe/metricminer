package org.metricminer.changesets;

import org.metricminer.config.project.Config;
import org.metricminer.scm.SCM;


public interface SpecificChangeSetFactory {
	ChangeSetCollection build(SCM scm, Config config);
}
