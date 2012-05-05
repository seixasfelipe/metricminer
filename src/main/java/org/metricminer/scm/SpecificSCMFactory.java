package org.metricminer.scm;

import org.metricminer.projectconfig.Config;

public interface SpecificSCMFactory {
	SCM build(Config config);
}
