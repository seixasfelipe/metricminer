package controller;

import model.Project;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import dao.ProjectDao;

@Resource
public class ProjectController {

	private final Result result;
	private final ProjectDao dao;

	public ProjectController(Result result, ProjectDao dao) {
		this.result = result;
		this.dao = dao;
	}

	@Get("/projects/new")
	public void form() {
	}

	@Get("/projects")
	public void list() {
		result.include("projects", dao.listAll());
	}

	@Get("/projects/{id}")
	public void detail(Long id) {
		result.include("project", dao.findProjectBy(id));
	}

	@Post("/projects")
	public void createProject(Project project) {
		project.setupInitialConfigurationsEntries();
		dao.save(project);
		result.redirectTo(ProjectController.class).list();
	}

}
