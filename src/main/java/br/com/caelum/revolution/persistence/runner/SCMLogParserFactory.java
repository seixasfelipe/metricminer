package br.com.caelum.revolution.persistence.runner;

import java.io.FileInputStream;
import java.io.InputStream;

import model.Project;

import org.hibernate.SessionFactory;

import br.com.caelum.revolution.changesets.ChangeSetCollection;
import br.com.caelum.revolution.changesets.ChangeSetCollectionFactory;
import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.PropertiesConfig;
import br.com.caelum.revolution.domain.PersistedCommitConverter;
import br.com.caelum.revolution.scm.SCM;
import br.com.caelum.revolution.scm.SCMFactory;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class SCMLogParserFactory {

	public static void main(String[] args) {
	}

	public SCMLogParser basedOn(Config config,
			SessionFactory sessionFactory, Project project) {
		SCM scm = new SCMFactory().basedOn(config);
		ChangeSetCollection collection = new ChangeSetCollectionFactory(scm)
				.basedOn(config);
		return new SCMLogParser(new PersistedCommitConverter(),
				scm, collection, sessionFactory, project);
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
