package org.metricminer.model;

import org.metricminer.tasks.RunnableTaskFactory;
import org.metricminer.tasks.scm.SCMCloneTaskFactory;

public class TaskBuilder {

    private String name;
    private RunnableTaskFactory runnableTaskFactory;
    private Integer position;
    private Project project;
    private Long id;

    public TaskBuilder() {
        position = 0;
        name = "Default task name";
        this.runnableTaskFactory = new SCMCloneTaskFactory();
    }

    public TaskBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TaskBuilder withRunnableTaskFactory(RunnableTaskFactory runnableTaskFactory) {
        this.runnableTaskFactory = runnableTaskFactory;
        return this;
    }

    public Task build() {
        return new Task(project, name, runnableTaskFactory, position, id);
    }

    public TaskBuilder forProject(Project project) {
        this.project = project;
        return this;
    }

    public TaskBuilder withPosition(Integer position) {
        this.position = position;
        return this;
    }
    
    public TaskBuilder withId(Long id) {
        this.id = id;
        return this;
    }

}
