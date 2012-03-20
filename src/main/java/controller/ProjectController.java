package controller;

import model.Project;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import dao.ProjectDao;
import dao.TaskDao;

@Resource
public class ProjectController {

	private final Result result;
	private final ProjectDao dao;
	private final TaskDao taskDao;

	public ProjectController(Result result, ProjectDao dao, TaskDao taskDao) {
		this.result = result;
		this.dao = dao;
		this.taskDao = taskDao;
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
		Project completeProject = new Project(project);
		dao.save(completeProject);
		result.redirectTo(ProjectController.class).list();
	}

}
