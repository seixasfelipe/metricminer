package tasks;

import java.io.ByteArrayInputStream;
import java.util.List;

import model.Project;
import model.SourceCode;
import model.Task;

import org.hibernate.Session;

import tasks.metric.Metric;
import tasks.runner.RunnableTask;
import br.com.caelum.revolution.domain.Artifact;

public class CalculateMetricTask implements RunnableTask {

    private Task task;
    private Metric metric;
    private Session session;

    public CalculateMetricTask(Task task, Metric metric, Session session) {
        this.task = task;
        this.metric = metric;
        this.session = session;
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
                    session.save(metric.resultToPersistOf(sourceCode));
                }
            }
        }

    }
}
