package org.metricminer.config;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import org.metricminer.model.Task;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class MetricMinerStatus {

    private Queue<Task> tasksRunning;
    private MetricMinerConfigs configs;
    
    public MetricMinerStatus(MetricMinerConfigs configs) {
        this.configs = configs;
        tasksRunning = new LinkedBlockingDeque<Task>();
    }
    
    public void addRunningTask(Task t) {
        tasksRunning.add(t);
    }
    
    public boolean isRunningTask() {
        return !tasksRunning.isEmpty();
    }
    
    public Task finishCurrentTask() {
        return tasksRunning.poll();
    }
    
    public boolean mayStartTask() {
        return tasksRunning.size() < configs.getMaxConcurrentTasks();
    }
    
    @Override
    public String toString() {
        return "MetricMiner status: " + tasksRunning.size() + " tasks running";
    }
}
