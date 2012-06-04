package org.metricminer.tasks.parser;

import org.hibernate.Session;
import org.metricminer.changesets.ChangeSetCollection;
import org.metricminer.changesets.ChangeSetCollectionFactory;
import org.metricminer.config.project.Config;
import org.metricminer.model.PersistedCommitConverter;
import org.metricminer.model.Project;
import org.metricminer.scm.SCM;
import org.metricminer.scm.SCMFactory;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class SCMLogParserFactory {

	public SCMLogParser basedOn(Config config, Session session, Project project) {
		SCM scm = new SCMFactory().basedOn(config);
		ChangeSetCollection collection = new ChangeSetCollectionFactory(scm)
				.basedOn(config);
		return new SCMLogParser(new PersistedCommitConverter(), scm,
				collection, session, project);
	}
}
