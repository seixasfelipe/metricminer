package tasks;

import java.io.ByteArrayInputStream;
import java.util.List;

import model.Project;
import model.SourceCode;
import model.Task;
import tasks.metric.Metric;
import tasks.runner.RunnableTask;
import br.com.caelum.revolution.domain.Artifact;

public class CalculateMetricTask implements RunnableTask {

    private Task task;
    private Metric metric;

    public CalculateMetricTask(Task task, Metric metric) {
        this.task = task;
        this.metric = metric;
    }

    @Override
    public void run() {
        Project project = task.getProject();
        List<Artifact> artifacts = project.getArtifacts();
        for (Artifact artifact : artifacts) {
            List<SourceCode> sources = artifact.getSources();
            for (SourceCode sourceCode : sources) {
                if (metric.shouldCalculateMetricOf(sourceCode.getName())) {
                    metric.calculate(new ByteArrayInputStream(sourceCode.getSourceBytesArray()));
                    System.out.println(metric.content(sourceCode.getName(), project.getName()));
                }
            }
        }

    }
}
