package org.metricminer.model;

import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.config.project.MapConfig;
import org.metricminer.tasks.metric.CalculateAllMetricsTaskFactory;
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
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ProjectConfigurationEntry> configurationEntries;
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Artifact> artifacts;
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Task> tasks;
	private String projectPath;
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Tag> tags;
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Commit> commits;
	@OneToOne(fetch = FetchType.LAZY)
	private Commit firstCommit;
	@OneToOne(fetch = FetchType.LAZY)
	private Commit lastCommit;
	private Long totalCommits;
	private Long totalCommiters;
	@OneToMany(mappedBy="project", fetch = FetchType.LAZY)
	private List<CalculatedMetric> calculatedMetrics;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar creationDate;

	@Transient
	private MetricMinerConfigs metricMinerConfigs;

	public Project() {
		this.creationDate = Calendar.getInstance();
		this.configurationEntries = new ArrayList<ProjectConfigurationEntry>();
		this.tasks = new ArrayList<Task>();
		this.tags = new ArrayList<Tag>();
		this.calculatedMetrics = new ArrayList<CalculatedMetric>();
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
				+ "/projects/" + this.id, this));
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

		metricTask.addTaskConfigurationEntry(TaskConfigurationEntryKey.METRIC_FACTORY_CLASS,
				metricFactoryClassName);

		metricTask.addDependency(parseLogTask);
		tasks.add(metricTask);
	}

	private void setupInitialTasks() {
		Task cloneTask = new TaskBuilder().forProject(this)
				.withName("Clone SCM")
				.withRunnableTaskFactory(new SCMCloneTaskFactory())
				.withPosition(0).build();

		Task parseLogTask = new TaskBuilder().forProject(this)
				.withName("Parse SCM logs")
				.withRunnableTaskFactory(new ParseSCMLogTaskFactory())
				.withPosition(1).build();

		Task removeDirectoryTask = new TaskBuilder().forProject(this)
				.withName("Remove source code directory")
				.withRunnableTaskFactory(new RemoveSourceDirectoryTaskFactory())
				.withPosition(2).build();

		Task calculateAllMetricsTask = new TaskBuilder().forProject(this)
				.withName("Calculate all metrics")
				.withRunnableTaskFactory(new CalculateAllMetricsTaskFactory())
				.withPosition(3).build();

		parseLogTask.addDependency(cloneTask);
		removeDirectoryTask.addDependency(parseLogTask);
		calculateAllMetricsTask.addDependency(parseLogTask);
		tasks.add(cloneTask);
		tasks.add(parseLogTask);
		tasks.add(removeDirectoryTask);
		tasks.add(calculateAllMetricsTask);

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
			if (!this.alreadyCalculated(registeredMetric)) {
				avaiableMetrics.add(registeredMetric);
			}
		}
		return avaiableMetrics;
	}

	public void addTask(Task task) {
		this.tasks.add(task);
	}

	public int getCommitCount() {
		return commits.size();
	}

	public void addMetricToCalculate(String className) {
		addMetricToCalculate(className, tasks.get(1));
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
	
	public Calendar getCreationDate() {
		return creationDate;
	}
	
	public void setStats(Long totalCommiters, Long totalCommits, Commit first, Commit last) {
		this.totalCommiters = totalCommiters;
		this.totalCommits = totalCommits;
		this.firstCommit = first;
		this.lastCommit = last;

	}

	public void addNewMetrics(List<RegisteredMetric> registeredMetrics) {
		for (RegisteredMetric registeredMetric : registeredMetrics) {
			if (!alreadyCalculated(registeredMetric) && !willCalculateAllMetrics()) {
				addMetricToCalculate(registeredMetric.getMetricFactoryClassName());
			}
		}
	}

	public void addCalculatedMetric(CalculatedMetric calculatedMetric) {
		calculatedMetrics.add(calculatedMetric);
	}

	public boolean alreadyCalculated(RegisteredMetric registeredMetric) {
		for (CalculatedMetric calculatedMetric : calculatedMetrics) {
			if (calculatedMetric.getMetricFactoryClass().equals(registeredMetric.getMetricFactoryClass()))
				return true;
		}
		return false;
	}

    @Override
    public String toString() {
        return "Project [id=" + id + ", name=" + name + "]";
    }

    public boolean willCalculateAllMetrics() {
        for (Task task : tasks) {
            if (!task.hasFinished() && task.getRunnableTaskFactoryClass().equals(CalculateAllMetricsTaskFactory.class)) {
                return true;
            }
        }
        return false;
    }
	
}
