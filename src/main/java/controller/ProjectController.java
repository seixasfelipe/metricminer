package controller;

import model.Project;

import org.hibernate.SessionFactory;

import br.com.caelum.revolution.executor.CommandExecutor;
import br.com.caelum.revolution.executor.SimpleCommandExecutor;
import br.com.caelum.revolution.persistence.runner.SCMLogParser;
import br.com.caelum.revolution.persistence.runner.SCMLogParserFactory;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import config.MetricMinerConfigs;
import dao.ProjectDao;

@Resource
public class ProjectController {

	private final Result result;
	private final ProjectDao dao;
	private final SessionFactory sessionFactory;

	public ProjectController(Result result, ProjectDao dao,
			SessionFactory sessionFactory) {
		this.result = result;
		this.dao = dao;
		this.sessionFactory = sessionFactory;
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
	public void createProject(String name, String scmUrl) {
		Project project = new Project(name, scmUrl);
		dao.save(project);
		project.setupInitialConfigurationsEntries();
		dao.save(project);
		result.redirectTo(ProjectController.class).list();
	}

	@Get("/project/{id}/parse")
	public void parseScmLogs(Long id) {
		Project project = dao.findProjectBy(id);
		SCMLogParser logParser = new SCMLogParserFactory().basedOn(
				project.getMapConfig(), sessionFactory, project);
		new Thread(new ScmLogParserThread(logParser)).start();
		result.redirectTo(ProjectController.class).list();
	}

	@Get("/project/{id}/clone")
	public void cloneRepository(Long id) {
		Project project = dao.findProjectBy(id);
		SimpleCommandExecutor executor = new SimpleCommandExecutor();

		executor.execute("mkdir -p " + MetricMinerConfigs.metricMinerHome
				+ "/projects/" + project.getId(), "/");
		new Thread(new GitCloneThread(executor, project)).start();
		result.redirectTo(ProjectController.class).list();
	}

	private class GitCloneThread implements Runnable {

		private CommandExecutor executor;
		private Project project;

		public GitCloneThread(CommandExecutor executor, Project project) {
			this.executor = executor;
			this.project = project;
		}

		@Override
		public void run() {
			System.out.println("Clonning project...");
			String output = executor.execute(
					"git clone " + project.getScmUrl(),
					MetricMinerConfigs.metricMinerHome + "/projects/"
							+ project.getId());
			System.out.println(output);
		}
	}

	private class ScmLogParserThread implements Runnable {

		private final SCMLogParser logParser;

		public ScmLogParserThread(SCMLogParser persistenceRunner) {
			this.logParser = persistenceRunner;
		}

		@Override
		public void run() {
			logParser.start();
		}
	}
}
