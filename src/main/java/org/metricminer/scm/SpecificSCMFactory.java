package org.metricminer.scm;

import org.metricminer.config.project.Config;

public interface SpecificSCMFactory {
	SCM build(Config config);
}
