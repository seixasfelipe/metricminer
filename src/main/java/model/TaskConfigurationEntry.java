package model;

import javax.persistence.CascadeType;
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

}
