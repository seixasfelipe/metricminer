package org.metricminer.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TaskConfigurationEntry {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Task task;
    @Enumerated(EnumType.STRING)
    @Column(name = "task_key")
    private TaskConfigurationEntryKey key;
    private String value;

    public TaskConfigurationEntry() {
    }

    public TaskConfigurationEntry(TaskConfigurationEntryKey key, String value, Task task) {
        this.key = key;
        this.value = value;
        this.task = task;
    }

    public TaskConfigurationEntryKey getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean isEqualToMetric(RegisteredMetric registeredMetric) {
        return this.key.equals(TaskConfigurationEntryKey.METRIC_FACTORY_CLASS)
                && registeredMetric.getMetricFactoryClassName().equals(this.value);
    }

    public Task getTask() {
		return task;
	}
    
    public Long getId() {
		return id;
	}
}
