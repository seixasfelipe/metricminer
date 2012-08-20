package org.metricminer.controller;

import java.util.List;

import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.infra.dao.ProjectDao;
import org.metricminer.infra.dao.TagDao;
import org.metricminer.infra.interceptor.PublicAccess;
import org.metricminer.model.Project;
import org.metricminer.model.RegisteredMetric;
import org.metricminer.model.Tag;
import org.metricminer.ui.TagTokenizer;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ProjectController {

    private final Result result;
    private final ProjectDao dao;
    private final MetricMinerConfigs configs;
    private final TagTokenizer tokenize;
    private final TagDao tagDao;

    public ProjectController(Result result, ProjectDao dao, TagDao tagDao,
            MetricMinerConfigs metricMinerConfigs, TagTokenizer tokenize) {
        this.result = result;
        this.dao = dao;
        this.tagDao = tagDao;
        this.configs = metricMinerConfigs;
        this.tokenize = tokenize;
    }

    @Get("/projects/new")
    public void form() {
        result.include("metrics", configs.getRegisteredMetrics());
    }

    @Get("/projects/{page}")
    public void list(int page) {
        result.include("projects", dao.listPage(page));
        result.include("totalPages", dao.totalPages());
        result.include("currentPage", page);
    }

    @Get("/project/{id}")
    public void detail(Long id) {
        Project project = dao.findProjectBy(id);
        result.include("tags", tokenize.tags(project.getTags()));
        result.include("avaiableMetrics", project.avaiableMetricsToAddBasedOn(configs.getRegisteredMetrics()));
        result.include("project", project);
    }
    
    @Get("/projects/{id}/commitChartData")
    public void commitChartData(Long id) {
        Project project = dao.findProjectBy(id);
        result.include("lastSixMonthsCommitCountMap", dao.commitCountForLastMonths(project));
    }
    
    @Get("/projects/{id}/fileCountChartData")
    public void fileCountChartData(Long id) {
        Project project = dao.findProjectBy(id);
        result.include("fileCountPerCommit", dao.fileCountPerCommitForLastSixMonths(project));
    }

    @Post("/projects/{projectId}/metrics")
    public void addMetricToCalculate(Long projectId, String metricFactoryClassName) {
        Project project = dao.findProjectBy(projectId);
        project.addMetricToCalculate(metricFactoryClassName);
        result.nothing();
    }

    @Post("/projects/{projectId}/tags/remove")
    public void removeTag(Long projectId, String tagName) {
        Project project = dao.findProjectBy(projectId);
        project.removeTag(tagName);
        dao.update(project);

        result.nothing();
    }

    @Post("/projects/{projectId}/tags/")
    public void addTag(Long projectId, String tagName) {
        Tag tag = tagDao.byName(tagName);
        if (tag == null) {
            tag = new Tag(tagName);
            tagDao.save(tag);
        }
        Project project = dao.findProjectBy(projectId);
        project.addTag(tag);
        dao.update(project);

        result.nothing();
    }

    @Post("/projects")
    public void createProject(Project project, List<RegisteredMetric> metrics) {
        saveProject(project, metrics);
    }
    
    @PublicAccess
    @Post("/projects/06560fb292075c5eeca4ceb586185332")
    public void createProjectRemote(Project project, List<RegisteredMetric> metrics) {
    	saveProject(project, metrics);
    }

	private void saveProject(Project project, List<RegisteredMetric> metrics) {
		Project completeProject = new Project(project, configs);
        if (metrics != null) {
            for (RegisteredMetric metric : metrics) {
                completeProject.addMetricToCalculate(metric.getMetricFactoryClassName());
            }
        }
        dao.save(completeProject);
        result.redirectTo(ProjectController.class).list(1);
	}

}
