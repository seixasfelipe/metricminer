package org.metricminer.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.metricminer.runner.RunnableTaskFactory;

@SuppressWarnings("rawtypes")
@Entity
public class Task implements Comparable {

    @SuppressWarnings("unused")
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Project project;
    private String name;
    private Class runnableTaskFactoryClass;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar submitDate;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private Integer position;
    @ManyToMany
    private List<Task> depends;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TaskConfigurationEntry> configurationEntries;

    public Task() {
        this.depends = new ArrayList<Task>();
        this.configurationEntries = new ArrayList<TaskConfigurationEntry>();
    }

    public Task(Project project, String name, RunnableTaskFactory runnableTaskFactory,
            Integer position) {
        this();
        this.project = project;
        this.name = name;
        this.runnableTaskFactoryClass = runnableTaskFactory.getClass();
        this.submitDate = new GregorianCalendar();
        this.status = TaskStatus.QUEUED;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public Class getRunnableTaskFactoryClass() {
        return runnableTaskFactoryClass;
    }

    public Project getProject() {
        return project;
    }

    public void start() {
        this.status = TaskStatus.STARTED;
    }

    public void finish() {
        this.status = TaskStatus.FINISHED;
    }

    public void fail() {
        this.status = TaskStatus.FAILED;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Calendar getSubmitDate() {
        return submitDate;
    }

    @Override
    public int compareTo(Object o) {
    	if (!(o instanceof Task)) {
    		return 1;
    	}
        Task otherTask = (Task) o;
        if (this.submitDate.compareTo(otherTask.getSubmitDate()) != 0)
        	return this.submitDate.compareTo(otherTask.getSubmitDate());
        return position.compareTo(otherTask.getPosition());
    }
    
    public Integer getPosition() {
        return position;
    }

    public String getTaskConfigurationValueFor(TaskConfigurationEntryKey key) {
        for (TaskConfigurationEntry entry : this.configurationEntries) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void addDependency(Task task) {
        if (depends == null)
            depends = new ArrayList<Task>();
        depends.add(task);
    }

    public boolean isDependenciesFinished() {
        for (Task depencyTask : depends) {
            if (!depencyTask.isFinished())
                return false;
        }
        return true;
    }

    private boolean isFinished() {
        return this.status == TaskStatus.FINISHED;
    }

    public void addTaskConfigurationEntry(TaskConfigurationEntryKey key, String value) {
        configurationEntries.add(new TaskConfigurationEntry(key, value, this));
    }

    public List<TaskConfigurationEntry> getConfigurationEntries() {
        return Collections.unmodifiableList(configurationEntries);
    }

    @Override
    public String toString() {
        return project == null ? name : project.getName() + " - " + name;
    }

    public boolean willCalculate(RegisteredMetric registeredMetric) {
        for (TaskConfigurationEntry entry : this.configurationEntries) {
            if (entry.isEqualToMetric(registeredMetric))
                return true;
        }
        return false;
    }

    public String getConfiguration(TaskConfigurationEntryKey queryId) {
        for (TaskConfigurationEntry entry : configurationEntries) {
            if (entry.getKey().equals(queryId))
                return entry.getValue();
        }
        return null;
    }

}
