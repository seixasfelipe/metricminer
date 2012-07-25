package org.metricminer.controller;

import org.metricminer.infra.dao.ArtifactDao;
import org.metricminer.infra.dao.AuthorDao;
import org.metricminer.infra.dao.CommitDao;
import org.metricminer.infra.dao.ProjectDao;
import org.metricminer.infra.dao.TaskDao;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class IndexController {

	private final ProjectDao projectDao;
	private final CommitDao commitDao;
	private final Result result;
	private final ArtifactDao artifactDao;
	private final AuthorDao authorDao;
	private final TaskDao taskDao;

	public IndexController(Result result, ProjectDao projectDao, CommitDao commitDao, ArtifactDao artifactDao, AuthorDao authorDao, TaskDao taskDao) {
		this.projectDao = projectDao;
		this.commitDao = commitDao;
		this.artifactDao = artifactDao;
		this.result = result;
		this.authorDao = authorDao;
		this.taskDao = taskDao;
	}
	
	@Get("/")
	public void index() {
		result.include("totalCommits", commitDao.totalCommits());
		result.include("totalProjects", projectDao.totalProjects());
		result.include("totalArtifacts", artifactDao.totalArtifacts());
		result.include("totalAuthors", authorDao.totalAuthors());
		result.include("newProjects", projectDao.tenNewestProjects());
		result.include("lastTasks", taskDao.lastTasks(10));
	}
}
