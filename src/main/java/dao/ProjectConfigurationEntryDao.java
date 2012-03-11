package dao;

import model.Project;

import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class ProjectConfigurationEntryDao {

	private Session session;

	public ProjectConfigurationEntryDao(Session session) {
		this.session = session;
	}

	public void saveProjectConfigurations(Project p) {

	}
}
