package controller;

import model.Project;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ProjectController {

	private final Result result;
	private final ProjectDao dao;

	public ProjectController(Result result, ProjectDao dao) {
		this.result = result;
		this.dao = dao;
	}

	@Get("/project/new")
	public void form() {
	}

	@Get("/projects")
	public void list() {
		result.include("projects", dao.listAll());
	}

	@Post("/projects")
	public void createProject(String name, String scmUrl) {
		Project project = new Project(name, scmUrl);
		dao.save(project);
		result.redirectTo(ProjectController.class).list();
	}

	@Get("/project/{id}/clone")
	public void cloneRepository(Long id) {
		result.include("project", dao.findProjectBy(id));
	}

}
