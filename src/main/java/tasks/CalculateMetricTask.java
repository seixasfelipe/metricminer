package tasks;

import java.io.ByteArrayInputStream;
import java.util.Collection;

import model.Project;
import model.SourceCode;
import model.Task;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import tasks.metric.common.Metric;
import tasks.metric.common.MetricResult;
import tasks.runner.RunnableTask;

public class CalculateMetricTask implements RunnableTask {

    private Task task;
    private Metric metric;
    private Session session;
    private static Logger log = Logger.getLogger(CalculateMetricTask.class);
    private final StatelessSession statelessSession;

    public CalculateMetricTask(Task task, Metric metric, Session session,
            StatelessSession statelessSession) {
        this.task = task;
        this.metric = metric;
        this.session = session;
        this.statelessSession = statelessSession;
    }

    @Override
    public void run() {
        Project project = task.getProject();
        Query query = statelessSession.createQuery("select source from SourceCode source "
                + "join source.artifact as artifact where artifact.project.id = :project_id");
        query.setParameter("project_id", project.getId());

        ScrollableResults sources = query.scroll();
        while (sources.next()) {
            SourceCode source = (SourceCode) sources.get(0);
            calculateAndSaveResultsOf(source);
        }
    }

    private void calculateAndSaveResultsOf(SourceCode sourceCode) {
        log.info("Calculating " + metric.getClass() + " for: " + sourceCode.getName() + " - "
                + sourceCode.getCommit().getCommitId());
        try {
            metric.calculate(new ByteArrayInputStream(sourceCode.getSourceBytesArray()));
            Collection<MetricResult> results = metric.resultsToPersistOf(sourceCode);
            for (MetricResult result : results) {
                session.save(result);
                session.flush();
                session.clear();
            }
        } catch (Throwable t) {
            log.error("Unable to calculate metric: ", t);
        }
    }

}
