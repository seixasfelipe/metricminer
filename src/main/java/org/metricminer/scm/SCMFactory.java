package org.metricminer.scm;

import org.metricminer.config.project.Config;
import org.metricminer.config.project.Configs;

public class SCMFactory {

	public SCM basedOn(Config config) {
		SpecificSCMFactory scmFactory = getToolFactory(config.asString(Configs.SCM));
		return scmFactory.build(config);		
	}

	@SuppressWarnings({ "rawtypes" })
	private SpecificSCMFactory getToolFactory(String scmName) {
		try {
			Class theClass = Class.forName(scmName);
			return (SpecificSCMFactory)theClass.newInstance();
		} catch (Exception e) {
			throw new SCMNotFoundException();
		}
	}

}
