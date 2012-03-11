package br.com.caelum.revolution.persistence.runner;

import java.text.ParseException;

import org.apache.log4j.Logger;

import br.com.caelum.revolution.changesets.ChangeSet;
import br.com.caelum.revolution.changesets.ChangeSetCollection;
import br.com.caelum.revolution.domain.PersistedCommitConverter;
import br.com.caelum.revolution.persistence.HibernatePersistence;
import br.com.caelum.revolution.scm.CommitData;
import br.com.caelum.revolution.scm.SCM;

public class HibernatePersistenceRunner implements PersistenceRunner {

	private HibernatePersistence persistence;
	private PersistedCommitConverter converter;
	private SCM scm;
	private final ChangeSetCollection collection;
	private static Logger log = Logger.getLogger(HibernatePersistenceRunner.class);

	public HibernatePersistenceRunner(HibernatePersistence persistence,
			PersistedCommitConverter converter, SCM scm, ChangeSetCollection collection) {
		this.persistence = persistence;
		this.converter = converter;
		this.scm = scm;
		this.collection = collection;
	}

	public void start() {
		persistence.initMechanism();
		for (ChangeSet changeSet : collection.get()) {
			CommitData commitData = scm.detail(changeSet.getId());
			try {
				log.info("--------------------------");
				log.info("Persisting change set " + changeSet.getId());
				persistence.beginTransaction();
				converter.toDomain(commitData, persistence.getSession());
				persistence.commit();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		log.info("");
		log.info("--------------------------");
		log.info("Finished persisting commit data.");
	}

}
