package org.metricminer.scm.git;

import org.metricminer.executor.SimpleCommandExecutor;
import org.metricminer.projectconfig.Config;
import org.metricminer.scm.SCM;
import org.metricminer.scm.SpecificSCMFactory;


public class GitCloneFactory implements SpecificSCMFactory {

	public SCM build(Config config) {
		return new GitClone(
				config.asString("scm.remoteRepository"),
				config.asString("scm.repository"),
				new SimpleCommandExecutor(),
				new Git(config.asString("scm.repository"), new GitLogParser(), new GitDiffParser(), new GitBlameParser(), new SimpleCommandExecutor())
			);
	}

}
