package org.metricminer.controller;

import org.metricminer.infra.dao.CommitDao;
import org.metricminer.infra.dao.ProjectDao;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class IndexController {

	private final ProjectDao projectDao;
	private final CommitDao commitDao;
	private final Result result;

	public IndexController(ProjectDao projectDao, CommitDao commitDao, Result result) {
		this.projectDao = projectDao;
		this.commitDao = commitDao;
		this.result = result;
	}
	
	@Get("/")
	public void index() {
		result.include("totalCommitCount", commitDao.totalCommitCount());
		result.include("totalProjectCount", projectDao.totalProjectCount());
		result.include("newProjects", projectDao.getTenNewestProjects());
	}
}
