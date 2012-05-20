package org.metricminer.builder;

import org.metricminer.model.Project;
import org.metricminer.model.Task;
import org.metricminer.runner.RunnableTaskFactory;

public class TaskBuilder {

    private String name;
    private RunnableTaskFactory runnableTaskFactory;
    private Integer position;
    private Project project;

    public TaskBuilder() {
        position = 0;
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
        return new Task(project, name, runnableTaskFactory, position);
    }

    public TaskBuilder forProject(Project project) {
        this.project = project;
        return this;
    }

    public TaskBuilder withPosition(Integer position) {
        this.position = position;
        return this;
    }

}
