package org.metricminer.tasks;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.model.Task;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class TaskQueueStatus {

	private List<Task> tasksRunning;
	private MetricMinerConfigs configs;

	public TaskQueueStatus(MetricMinerConfigs configs) {
		this.configs = configs;
		tasksRunning = new LinkedList<Task>();
	}

	public synchronized void addRunningTask(Task t) {
		tasksRunning.add(t);
	}

	public boolean isRunningTask() {
		return !tasksRunning.isEmpty();
	}

	public synchronized void finishCurrentTask(Task t) {
		tasksRunning.remove(t);
	}

	public boolean mayStartTask() {
		return tasksRunning.size() < configs.getMaxConcurrentTasks();
	}

	public Collection<Task> getTaskQueue() {
		return Collections.unmodifiableCollection(tasksRunning);
	}

	public MetricMinerConfigs getConfigs() {
		return configs;
	}

	@Override
	public String toString() {
		return "MetricMiner status: " + tasksRunning.size() + " tasks running";
	}
}
