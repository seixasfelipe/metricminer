package br.com.caelum.revolution.persistence.runner;

import java.text.ParseException;

import model.Project;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import br.com.caelum.revolution.changesets.ChangeSet;
import br.com.caelum.revolution.changesets.ChangeSetCollection;
import br.com.caelum.revolution.domain.PersistedCommitConverter;
import br.com.caelum.revolution.scm.CommitData;
import br.com.caelum.revolution.scm.SCM;

public class SCMLogParser implements PersistenceRunner {

	private Session session;
	private PersistedCommitConverter converter;
	private SCM scm;
	private final ChangeSetCollection collection;
	private static Logger log = Logger.getLogger(SCMLogParser.class);
	private final Project project;

	public SCMLogParser(PersistedCommitConverter converter, SCM scm,
			ChangeSetCollection collection, Session session, Project project) {
		this.converter = converter;
		this.scm = scm;
		this.collection = collection;
		this.session = session;
		this.project = project;
	}

	public void start() {
		// persistence.initMechanism();
		for (ChangeSet changeSet : collection.get()) {
			CommitData commitData = scm.detail(changeSet.getId());
			try {
				log.info("--------------------------");
				log.info("Persisting change set " + changeSet.getId());
				log.info("Author: " + commitData.getAuthor() + " on "
						+ commitData.getDate());
				converter.toDomain(commitData, session, project);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		log.info("");
		log.info("--------------------------");
		log.info("Finished persisting commit data.");
	}

}
