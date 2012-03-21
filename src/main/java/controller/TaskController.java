package controller;

import model.Project;
import model.Task;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import dao.ProjectDao;
import dao.TaskDao;

@Resource
public class TaskController {

	private final Result result;
	private final ProjectDao projectDao;
	private final TaskDao taskDao;

	public TaskController(Result result, ProjectDao projectDao, TaskDao taskDao) {
		this.result = result;
		this.projectDao = projectDao;
		this.taskDao = taskDao;
	}

	@SuppressWarnings({ "rawtypes" })
	@Get("/tasks/addTask/{className}/{taskName}/{projectId}")
	public void addTaskTo(Long projectId, String className, String taskName) {
		Class taskClass;
		try {
			taskClass = Class.forName(className);
			Project project = projectDao.findProjectBy(projectId);
			taskDao.save(new Task(project, taskName, taskClass, project
					.taskCount()));
			result.redirectTo(ProjectController.class).detail(projectId);

		} catch (ClassNotFoundException e) {
			result.notFound();
		}
	}

}
