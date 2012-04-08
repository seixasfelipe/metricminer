package tasks;

import model.Task;
import tasks.metric.Metric;
import tasks.runner.RunnableTask;

public class CalculateMetricTask implements RunnableTask {

    private Task task;
    private Metric metric;

    public CalculateMetricTask(Task task, Metric metric) {
        this.task = task;
        this.metric = metric;
    }

    @Override
    public void run() {

    }

}
