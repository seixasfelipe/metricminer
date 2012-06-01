package org.metricminer.changesets;

import org.metricminer.config.project.Config;
import org.metricminer.config.project.Configs;
import org.metricminer.scm.SCM;


public class ChangeSetCollectionFactory {

	private final SCM scm;

	public ChangeSetCollectionFactory(SCM scm) {
		this.scm = scm;
	}

	public ChangeSetCollection basedOn(Config config) {
		SpecificChangeSetFactory factory = getConfigFactory(config.asString(Configs.CHANGESETS));
		return factory.build(scm, config);
	}

	@SuppressWarnings("rawtypes")
	private SpecificChangeSetFactory getConfigFactory(String name) {
		try {
			Class theClass = Class.forName(name);
			return (SpecificChangeSetFactory)theClass.newInstance();
		} catch (Exception e) {
			throw new ChangeSetNotFoundException(e);
		}
	}

}
