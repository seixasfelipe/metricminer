package br.com.caelum.revolution.persistence.runner;

import java.io.FileInputStream;
import java.io.InputStream;

import br.com.caelum.revolution.changesets.ChangeSetCollection;
import br.com.caelum.revolution.changesets.ChangeSetCollectionFactory;
import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.PropertiesConfig;
import br.com.caelum.revolution.domain.PersistedCommitConverter;
import br.com.caelum.revolution.persistence.HibernatePersistence;
import br.com.caelum.revolution.scm.SCM;
import br.com.caelum.revolution.scm.SCMFactory;

public class HibernatePersistenceRunnerFactory {

	public static void main(String[] args) {
		HibernatePersistenceRunner persistenceRunner = new HibernatePersistenceRunnerFactory()
		.basedOn(config(args));
		persistenceRunner.start();
	}
	
	public HibernatePersistenceRunner basedOn(Config config) {
		SCM scm = new SCMFactory().basedOn(config);
		ChangeSetCollection collection = new ChangeSetCollectionFactory(scm)
				.basedOn(config);
		return new HibernatePersistenceRunner(new HibernatePersistence(config),
				new PersistedCommitConverter(), scm, collection);
	}


	private static Config config(String[] args) {
		try {
			InputStream configStream = new FileInputStream(args[0]);
			return new PropertiesConfig(configStream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
