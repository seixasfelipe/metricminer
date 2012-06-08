package org.metricminer.tasks.metric;

import java.io.ByteArrayInputStream;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.model.SourceCode;
import org.metricminer.model.Task;
import org.metricminer.tasks.RunnableTask;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricResult;

public class CalculateMetricTask implements RunnableTask {

    private Task task;
    private Metric metric;
    private Session session;
    private static Logger log = Logger.getLogger(CalculateMetricTask.class);
    private final StatelessSession statelessSession;
    private int pageSize;
    private Long projectId;

    public CalculateMetricTask(Task task, Metric metric, Session session,
            StatelessSession statelessSession) {
        this.task = task;
        this.metric = metric;
        this.session = session;
        this.statelessSession = statelessSession;
    }

    @Override
    public void run() {
        pageSize = 20;
        int page = 0;
        projectId = task.getProject().getId();
        long maxSourceSize = 10000;
        boolean notFinishedPage;
        
        log.debug("Starting calculating metric");
        
        ScrollableResults sources = scrollableSources(page, metric.fileNameSQLRegex(),
                maxSourceSize);
        log.debug("Fecthed results");
        boolean notFinishedAllSources = sources.first();

        while (notFinishedAllSources) {
            notFinishedPage = true;
            while (notFinishedPage) {
                SourceCode source = (SourceCode) sources.get(0);
                calculateAndSaveResultsOf(source);
                notFinishedPage = sources.next();
            }
            page++;
            log.info("Calculated " + metric.getClass() + " for " + page * pageSize + " sources.");
            sources = scrollableSources(page, metric.fileNameSQLRegex(), maxSourceSize);
            notFinishedAllSources = sources.first();
            System.gc();
        }
    }

    private ScrollableResults scrollableSources(int page, String fileNameRegex, long maxSourceSize) {
        session.clear();
        Query query = statelessSession.createQuery("select source from SourceCode source "
                + "join source.artifact as artifact where artifact.project.id = :project_id "
                + "and artifact.name like '" + fileNameRegex + "'" + " and source.sourceSize < "
                + maxSourceSize);
        query.setParameter("project_id", projectId);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        ScrollableResults sources = query.scroll(ScrollMode.FORWARD_ONLY);
        return sources;
    }

    private void calculateAndSaveResultsOf(SourceCode sourceCode) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceCode.getSourceBytesArray());
            metric.calculate(inputStream);
            inputStream.close();
            Collection<MetricResult> results = metric.resultsToPersistOf(sourceCode);
            session.getTransaction().begin();
            
            int flushSession = 0;
            for (MetricResult result : results) {
                session.save(result);
                
                if(++flushSession % 20 == 0) {
                	session.flush();
                	session.clear();
                }
            }
            session.getTransaction().commit();
        } catch (Throwable t) {
            log.error("Unable to calculate metric: ", t);
        }
    }

}
