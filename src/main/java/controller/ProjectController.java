package controller;

import java.util.List;

import model.Project;
import model.RegisteredMetric;
import model.Tag;
import ui.TagTokenizer;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import config.MetricMinerConfigs;
import dao.ProjectDao;
import dao.TagDao;

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

    @Get("/projects")
    public void list() {
        result.include("projects", dao.listAll());
    }

    @Get("/projects/{id}")
    public void detail(Long id) {
        Project project = dao.findProjectBy(id);
        result.include("tags", tokenize.tags(project.getTags()));
        result.include("avaiableMetrics", project.avaiableMetricsToAddBasedOn(configs.getRegisteredMetrics()));
        result.include("project", project);
        result.include("commitCount", dao.commitCountFor(project));
        result.include("commiterCount", dao.commitersCountFor(project));
        result.include("lastCommit", dao.lastCommitFor(project));
        result.include("firstCommit", dao.firstCommitFor(project));
        result.include("lastSixMonthsCommitCountMap", dao.commitCountForLastSixMonths(project));
    }
    

    @Get("/projects/{id}/delete")
    public void delete(Long id) {
        dao.delete(id);
        result.redirectTo(ProjectController.class).list();
    }
    
    @Post("/projects/{projectId}/metrics")
    public void addMetricToCalculate(Long projectId, RegisteredMetric metric) {
        Project project = dao.findProjectBy(projectId);
        project.addMetricToCalculate(metric.getMetricFactoryClass());
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
        Project completeProject = new Project(project, configs);
        if (metrics != null) {
            for (RegisteredMetric metric : metrics) {
                completeProject.addMetricToCalculate(metric.getMetricFactoryClass());
            }
        }
        dao.save(completeProject);
        result.redirectTo(ProjectController.class).list();
    }

}
