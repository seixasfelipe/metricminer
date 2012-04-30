package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import tasks.CalculateMetricTaskFactory;
import tasks.GitCloneTaskFactory;
import tasks.ParseGitLogTaskFactory;
import tasks.RemoveSourceDirectoryTaskFactory;
import br.com.caelum.revolution.config.MapConfig;
import br.com.caelum.revolution.domain.Artifact;
import br.com.caelum.revolution.domain.Commit;
import config.MetricMinerConfigs;

@Entity
public class Project {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String scmUrl;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ConfigurationEntry> configurationEntries;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Artifact> artifacts;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;
    private String scmRootDirectoryName;
    private String projectPath;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Tag> tags;
    @OneToMany(mappedBy = "project")
    private List<Commit> commits;

    @Transient
    private MetricMinerConfigs metricMinerConfigs;

    public Project() {
        this.configurationEntries = new ArrayList<ConfigurationEntry>();
        this.tasks = new ArrayList<Task>();
        this.tags = new ArrayList<Tag>();
    }

    public Project(MetricMinerConfigs metricMinerConfigs) {
        this();
        this.metricMinerConfigs = metricMinerConfigs;
        projectPath = this.metricMinerConfigs.getMetricMinerHome() + "/projects/";
    }

    public Project(String name, String scmUrl, MetricMinerConfigs metricMinerConfigs) {
        this(metricMinerConfigs);
        this.name = name;
        this.scmUrl = scmUrl;

    }

    public Project(Project baseProject, MetricMinerConfigs metricMinerConfigs) {
        this(metricMinerConfigs);
        this.metricMinerConfigs = metricMinerConfigs;
        this.name = baseProject.getName();
        this.scmUrl = baseProject.getScmUrl();
        this.scmRootDirectoryName = baseProject.getScmRootDirectoryName();
        setupInitialTasks();
    }

    public String getName() {
        return name;
    }

    public String getScmUrl() {
        return scmUrl;
    }

    public Long getId() {
        return id;
    }

    public List<ConfigurationEntry> getConfigurationEntries() {
        return configurationEntries;
    }

    public void setupInitialConfigurationsEntries() {
        String metricMinerHome = this.metricMinerConfigs.getMetricMinerHome();

        configurationEntries.add(new ConfigurationEntry("scm",
                "br.com.caelum.revolution.scm.git.GitFactory", this));
        configurationEntries.add(new ConfigurationEntry("scm.repository", metricMinerHome
                + "/projects/" + this.id + "/" + this.scmRootDirectoryName, this));
        configurationEntries.add(new ConfigurationEntry("changesets",
                "br.com.caelum.revolution.changesets.AllChangeSetsFactory", this));
        configurationEntries.add(new ConfigurationEntry("build",
                "br.com.caelum.revolution.builds.nobuild.NoBuildFactory", this));

    }

    public MapConfig getMapConfig() {
        HashMap<String, String> configurations = new HashMap<String, String>();
        for (ConfigurationEntry entry : this.configurationEntries)
            configurations.put(entry.getKey(), entry.getValue());
        return new MapConfig(configurations);
    }

    public List<Task> getTasks() {
        Collections.sort(tasks, new Comparator<Task>() {
            public int compare(Task o1, Task o2) {
                return o1.compareTo(o2);
            };
        });
        return tasks;
    }

    public String getScmRootDirectoryName() {
        return scmRootDirectoryName;
    }

    public void setScmRootDirectoryName(String scmRootDirectoryName) {
        this.scmRootDirectoryName = scmRootDirectoryName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScmUrl(String scmUrl) {
        this.scmUrl = scmUrl;
    }

    public Integer taskCount() {
        return this.tasks.size();
    }

    public String getLocalPath() {
        return projectPath + this.id;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void addMetricToCalculate(String metricFactoryClassName) {
        Task metricTask = new Task(this, "Calculate metric: " + metricFactoryClassName,
                new CalculateMetricTaskFactory(), taskCount());
        metricTask.addTaskConfigurationEntry(TaskConfigurationEntryKey.METRICFACTORYCLASS,
                metricFactoryClassName);
        Task lastTask = tasks.get(tasks.size() - 1);
        metricTask.addDependency(lastTask);
        tasks.add(metricTask);
    }

    private void setupInitialTasks() {
        Task cloneTask = new Task(this, "Clone SCM", new GitCloneTaskFactory(), 0);
        Task parseLogTask = new Task(this, "Parse SCM logs", new ParseGitLogTaskFactory(), 1);
        Task removeDirecotryTask = new Task(this, "Remove source code directory",
                new RemoveSourceDirectoryTaskFactory(), 2);
        parseLogTask.addDependency(cloneTask);
        removeDirecotryTask.addDependency(parseLogTask);
        tasks.add(cloneTask);
        tasks.add(parseLogTask);
        tasks.add(removeDirecotryTask);
    }

    public List<Tag> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public void removeTag(String tagName) {
        Iterator<Tag> it = tags.iterator();
        while (it.hasNext()) {
            Tag tag = it.next();
            if (tag.getName().equals(tagName)) {
                it.remove();
                break;
            }
        }
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public List<RegisteredMetric> avaiableMetricsToAddBasedOn(
            List<RegisteredMetric> registeredMetrics) {
        ArrayList<RegisteredMetric> avaiableMetrics = new ArrayList<RegisteredMetric>();
        for (RegisteredMetric registeredMetric : registeredMetrics) {
            if (!this.containsTaskWith(registeredMetric)) {
                avaiableMetrics.add(registeredMetric);
            }
        }
        return avaiableMetrics;
    }

    private boolean containsTaskWith(RegisteredMetric registeredMetric) {
        for (Task task : this.tasks) {
            if (task.willCalculate(registeredMetric)) {
                return true;
            }
        }
        return false;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public int getCommitCount() {
        return commits.size();
    }
}
