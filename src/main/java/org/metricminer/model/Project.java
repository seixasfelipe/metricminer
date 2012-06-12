package org.metricminer.model;

import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.config.project.MapConfig;
import org.metricminer.tasks.metric.CalculateMetricTaskFactory;
import org.metricminer.tasks.scm.ParseSCMLogTaskFactory;
import org.metricminer.tasks.scm.RemoveSourceDirectoryTaskFactory;
import org.metricminer.tasks.scm.SCMCloneTaskFactory;

@Entity
public class Project {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String scmUrl;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectConfigurationEntry> configurationEntries;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Artifact> artifacts;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;
    private String scmRootDirectoryName;
    private String projectPath;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Tag> tags;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Commit> commits;
    @OneToOne
    private Commit firstCommit;
    @OneToOne
    private Commit lastCommit;
    private Long totalCommits;
    private Long totalCommiters;
    
    @Transient
    private MetricMinerConfigs metricMinerConfigs;

    public Project() {
        this.configurationEntries = new ArrayList<ProjectConfigurationEntry>();
        this.tasks = new ArrayList<Task>();
        this.tags = new ArrayList<Tag>();
    }

    private Project(MetricMinerConfigs metricMinerConfigs) {
        this();
        this.metricMinerConfigs = metricMinerConfigs;
        projectPath = this.metricMinerConfigs.getRepositoriesDir() + "/projects/";
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

    public List<ProjectConfigurationEntry> getConfigurationEntries() {
        return configurationEntries;
    }

    public void setupInitialConfigurationsEntries() {
        String metricMinerHome = this.metricMinerConfigs.getRepositoriesDir();

        configurationEntries.add(new ProjectConfigurationEntry("scm",
                "org.metricminer.scm.git.GitFactory", this));
        configurationEntries.add(new ProjectConfigurationEntry("scm.repository", metricMinerHome
                + "/projects/" + this.id + "/" + this.scmRootDirectoryName, this));
        configurationEntries.add(new ProjectConfigurationEntry("changesets",
                "org.metricminer.changesets.AllChangeSetsFactory", this));

    }

    public MapConfig getMapConfig() {
        HashMap<String, String> configurations = new HashMap<String, String>();
        for (ProjectConfigurationEntry entry : this.configurationEntries)
            configurations.put(entry.getKey(), entry.getValue());
        return new MapConfig(configurations);
    }

    @SuppressWarnings("unchecked")
	public List<Task> getTasks() {
        Collections.sort(tasks);
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

    public void addMetricToCalculate(String metricFactoryClassName, Task parseLogTask) {
        Task metricTask = new TaskBuilder().withName("Calculate metric: " + metricFactoryClassName)
                .forProject(this).withRunnableTaskFactory(new CalculateMetricTaskFactory())
                .withPosition(taskCount()).build();

        metricTask.addTaskConfigurationEntry(TaskConfigurationEntryKey.METRICFACTORYCLASS,
                metricFactoryClassName);

        metricTask.addDependency(parseLogTask);
        tasks.add(metricTask);
    }

    private void setupInitialTasks() {
        Task cloneTask = new TaskBuilder().forProject(this).withName("Clone SCM")
                .withRunnableTaskFactory(new SCMCloneTaskFactory()).withPosition(0).build();
        
        Task parseLogTask = new TaskBuilder().forProject(this).withName("Parse SCM logs")
                .withRunnableTaskFactory(new ParseSCMLogTaskFactory()).withPosition(1).build();
        
        Task removeDirectoryTask  = new TaskBuilder().forProject(this).withName("Remove source code directory")
                .withRunnableTaskFactory(new RemoveSourceDirectoryTaskFactory()).withPosition(2).build();
        
        parseLogTask.addDependency(cloneTask);
        removeDirectoryTask.addDependency(parseLogTask);
        tasks.add(cloneTask);
        tasks.add(parseLogTask);
        tasks.add(removeDirectoryTask);
        
        for (RegisteredMetric registeredMetric : metricMinerConfigs.getRegisteredMetrics()) {
        	addMetricToCalculate(registeredMetric.getMetricFactoryClassName(), parseLogTask);
		}
        
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

	public void addMetricToCalculate(String metricFactoryClass) {
		addMetricToCalculate(metricFactoryClass, tasks.get(1));
	}
	
	public Commit getFirstCommit() {
		return firstCommit;
	}
	
	public Commit getLastCommit() {
		return lastCommit;
	}
	
	public Long getTotalCommiters() {
		return totalCommiters;
	}
	
	public Long getTotalCommits() {
		return totalCommits;
	}
	
	public void setStats(Long totalCommiters, Long totalCommits, Commit first, Commit last) {
		this.totalCommiters = totalCommiters;
		this.totalCommits = totalCommits;
		this.firstCommit = first;
		this.lastCommit = last;
		
	}

	public void addNewMetrics(List<RegisteredMetric> registeredMetrics) {
		for (RegisteredMetric registeredMetric : registeredMetrics) {
			if (!containsTaskWith(registeredMetric)) {
				addMetricToCalculate(registeredMetric.getMetricFactoryClassName());
			}
		}
	}
}
