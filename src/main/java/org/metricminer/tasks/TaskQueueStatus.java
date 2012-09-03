package org.metricminer.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.model.Task;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class TaskQueueStatus {

	private MetricMinerConfigs configs;
    private Map<Task, Thread> threadByTask;
    private final ThreadInspector inspector;

	public TaskQueueStatus(MetricMinerConfigs configs, ThreadInspector inspector) {
		this.configs = configs;
        this.inspector = inspector;
		this.threadByTask = new HashMap<Task, Thread>();
	}

	public synchronized void addRunningTask(Task t, Thread runningThread) {
		threadByTask.put(t, runningThread);
	}

	public boolean isRunningTask() {
		return !threadByTask.isEmpty();
	}

	public synchronized void finishCurrentTask(Task t) {
	    threadByTask.remove(t);
	}

	public synchronized boolean mayStartTask() {
		return threadByTask.size() < configs.getMaxConcurrentTasks();
	}
	
	public boolean containsTask(Task t) {
	    return threadByTask.containsKey(t);
	}

    
    public List<Task> cleanTasksNotRunning() {
        ArrayList<Task> tasksToRemove = findTasksTasksNotRunning();
        for (Task task : tasksToRemove) {
            threadByTask.remove(task);
        }
        return tasksToRemove;
    }

    private ArrayList<Task> findTasksTasksNotRunning() {
        Set<Entry<Task, Thread>> entrySet = threadByTask.entrySet();
        ArrayList<Task> tasksToRemove = new ArrayList<Task>();
        for (Entry<Task, Thread> entry : entrySet) {
            if (!inspector.isRunning(entry.getValue())) {
                tasksToRemove.add(entry.getKey());
            }
        }
        return tasksToRemove;
    }

	public MetricMinerConfigs getConfigs() {
		return configs;
	}

	@Override
	public String toString() {
		return "MetricMiner status: " + threadByTask.size() + " tasks running";
	}
	
	public List<Task> getTaskQueue() {
	    Set<Task> tasks = threadByTask.keySet();
        return new ArrayList<Task>(tasks);
	}
	
}
