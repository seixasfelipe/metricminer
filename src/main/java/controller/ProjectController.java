package controller;

import java.util.ArrayList;
import java.util.List;

import model.Project;
import model.RegisteredMetric;
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
        List<RegisteredMetric> metrics = new ArrayList<RegisteredMetric>();
        metrics.add(new RegisteredMetric("Ciclomatic Complexity",
 "tasks.metric.cc.CCMetricFactory"));
        metrics.add(new RegisteredMetric("Fan-out", "tasks.metric.fanout.FanOutMetricFactory"));
        result.include("metrics", metrics);
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
    public void createProject(Project project, List<RegisteredMetric> metrics) {
        System.out.println(metrics);
		Project completeProject = new Project(project);
        if (metrics != null) {
            for (RegisteredMetric metric : metrics) {
                completeProject.addMetricToCalculate(metric.getMetricFactoryClass());
            }
        }
        dao.save(completeProject);
		completeProject.setupInitialConfigurationsEntries();
		dao.save(completeProject);
		result.redirectTo(ProjectController.class).list();
	}

}
