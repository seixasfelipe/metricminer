package org.metricminer.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.metricminer.tasks.RunnableTaskFactory;

@SuppressWarnings("rawtypes")
@Entity
public class Task implements Comparable {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(cascade=CascadeType.PERSIST)
    private Project project;
    private String name;
    private Class runnableTaskFactoryClass;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar submitDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar endDate;
    
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Task> depends;
    
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskConfigurationEntry> configurationEntries;
    
    private int position;

    public Task() {
        this.depends = new ArrayList<Task>();
        this.configurationEntries = new ArrayList<TaskConfigurationEntry>();
    }

    public Task(Project project, String name,
            RunnableTaskFactory runnableTaskFactory, Integer position) {
        this();
        this.project = project;
        this.name = name;
        this.runnableTaskFactoryClass = runnableTaskFactory.getClass();
        this.submitDate = new GregorianCalendar();
        this.status = TaskStatus.QUEUED;
        this.position = position;
    }

    public Task(Project project, String name,
            RunnableTaskFactory runnableTaskFactory, Integer position, Long id) {
        this(project, name, runnableTaskFactory, position);
        this.id = id;
    }

    public void setStarted() {
        this.status = TaskStatus.STARTED;
        this.startDate = new GregorianCalendar();
    }

    public void setFinished() {
        this.status = TaskStatus.FINISHED;
        this.endDate = new GregorianCalendar();
    }

    public void setFailed() {
        this.status = TaskStatus.FAILED;
    }

    public void addDependency(Task task) {
        if (depends == null)
            depends = new ArrayList<Task>();
        depends.add(task);
    }

    public boolean isDependenciesFinished() {
        for (Task depencyTask : depends) {
            if (!depencyTask.hasFinished())
                return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task) {
            Task otherTask = (Task) obj;
            return id == otherTask.getId();
        } else {
            return false;
        }
    }

    public Long getId() {
        return id;
    }

    public boolean hasFinished() {
        return this.status == TaskStatus.FINISHED;
    }

    public boolean hasStarted() {
        return this.status == TaskStatus.STARTED;
    }

    public void addTaskConfigurationEntry(TaskConfigurationEntryKey key,
            String value) {
        configurationEntries.add(new TaskConfigurationEntry(key, value, this));
    }

    public boolean willCalculate(RegisteredMetric registeredMetric) {
        for (TaskConfigurationEntry entry : this.configurationEntries) {
            if (entry.isEqualToMetric(registeredMetric))
                return true;
        }
        return false;
    }

    public String getTaskConfigurationValueFor(TaskConfigurationEntryKey key) {
        for (TaskConfigurationEntry entry : this.configurationEntries) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return project == null ? name : project.getName() + " - " + name;
    }

    public List<TaskConfigurationEntry> getConfigurationEntries() {
        return Collections.unmodifiableList(configurationEntries);
    }

    public Map<TaskConfigurationEntryKey, TaskConfigurationEntry> getConfigurationEntriesMap() {
        Map<TaskConfigurationEntryKey, TaskConfigurationEntry> map = new HashMap<TaskConfigurationEntryKey, TaskConfigurationEntry>();
        for (TaskConfigurationEntry entry : configurationEntries) {
            map.put(entry.getKey(), entry);
        }
        return map;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public Calendar getStartDate() {
        return startDate;
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
        Integer positionInteger = position;
        return positionInteger.compareTo(otherTask.getPosition());
    }

    public Integer getPosition() {
        return position;
    }

    public boolean hasFailedDependencies() {
        for (Task dependecy : depends) {
            if (dependecy.hasFailed())
                return true;
        }
        return false;
    }

    private boolean hasFailed() {
        return this.status == TaskStatus.FAILED;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
