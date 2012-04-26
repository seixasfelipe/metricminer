package tasks;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;

import model.Project;
import model.SourceCode;
import model.Task;

import org.apache.log4j.Logger;
import org.hibernate.Query;
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
    private int pageSize;
    private Project project;

    public CalculateMetricTask(Task task, Metric metric, Session session,
            StatelessSession statelessSession) {
        this.task = task;
        this.metric = metric;
        this.session = session;
        this.statelessSession = statelessSession;
    }

    @Override
    public void run() {
        pageSize = 100;
        int page = 0;
        project = task.getProject();
        List<SourceCode> sources = listSources(page);
        while (!sources.isEmpty()) {
            for (SourceCode sourceCode : sources) {
                calculateAndSaveResultsOf(sourceCode);
            }
            page++;
        }
    }

    private List<SourceCode> listSources(int page) {
        Query query = session.createQuery("select source from SourceCode source "
                + "join source.artifact as artifact where artifact.project.id = :project_id");
        query.setParameter("project_id", project.getId());
        query.setFirstResult(page * pageSize);
        query.setMaxResults(page * pageSize + pageSize);
        List<SourceCode> sources = query.list();
        return sources;
    }

    private void calculateAndSaveResultsOf(SourceCode sourceCode) {
        log.info("Calculating " + metric.getClass() + " for: " + sourceCode.getName());
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
