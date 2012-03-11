package dao;

import java.util.List;

import model.Project;

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
	}

	public List<Project> listAll() {
		return session.createCriteria(Project.class).list();
	}

	public Project findProjectBy(Long id) {
		return (Project) session.load(Project.class, id);
	}
}
