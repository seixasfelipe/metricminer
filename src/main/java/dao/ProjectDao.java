package dao;

import java.util.List;

import model.ConfigurationEntry;
import model.Project;
import model.Task;

import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class ProjectDao {
	private final Session session;

	public ProjectDao(Session session) {
		this.session = session;
	}

	public void save(Project project) {
		session.save(project);
		for (ConfigurationEntry entry : project.getConfigurationEntries())
			session.save(entry);
		for (Task task : project.getTasks()) {
			session.save(task);
		}
	}

	public List<Project> listAll() {
		return session.createCriteria(Project.class).list();
	}

	public Project findProjectBy(Long id) {
		return (Project) session.load(Project.class, id);
	}

	public Session getSession() {
		return this.session;
	}
}
