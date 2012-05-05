package org.metricminer.scm;

import org.metricminer.projectconfig.Config;
import org.metricminer.projectconfig.Configs;

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
