package org.metricminer.tasks.metric;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
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
    
    private int page;
    
    public static final int PAGE_SIZE = 5;
    public static final long MAX_SOURCE_SIZE = 10000;

    public CalculateMetricTask(Task task, Metric metric, Session session,
            StatelessSession statelessSession) {
        this.task = task;
        this.metric = metric;
        this.session = session;
        this.statelessSession = statelessSession;
    }

    @Override
    public void run() {
        
        log.debug("Starting calculating metric " + metric.getClass().getName());
        
        List<SourceCode> sourceCodes;
        do {
        	log.debug("Getting source codes (page " + page + ")");
        	sourceCodes = scrollableSources(page++);
        	
        	for(SourceCode sc : sourceCodes) {
        		log.debug("-- Working on " + sc.getName());
        		calculateAndSaveResultsOf(sc);
        	}
        	
        	System.gc();
        	
        } while(sourceCodes.size() > 0);
        
        log.debug("Metric " + metric.getClass().getName() + " has finished.");
    }

    @SuppressWarnings("unchecked")
	private List<SourceCode> scrollableSources(int page) {
        session.clear();
        Query query = statelessSession.createQuery("select source from SourceCode source "
                + "join fetch source.artifact as artifact where artifact.project.id = :project_id "
                + " and source.sourceSize < :sourceSize");
        
        query.setParameter("project_id", task.getProject().getId())
	         .setParameter("sourceSize", MAX_SOURCE_SIZE)
	         .setFirstResult(page * PAGE_SIZE)
	         .setMaxResults(PAGE_SIZE);
        
        return (List<SourceCode>) query.list();
    }

    private void calculateAndSaveResultsOf(SourceCode sourceCode) {
    	
    	if(!metric.matches(sourceCode.getName())) return;
    	
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceCode.getSourceBytesArray());
            metric.calculate(inputStream);
            inputStream.close();
            Collection<MetricResult> results = metric.resultsToPersistOf(sourceCode);
            session.getTransaction().begin();
            
            for (MetricResult result : results) {
                session.save(result);
            	session.flush();
            	session.clear();
            }
            session.getTransaction().commit();
        } catch (Throwable t) {
            log.error("Unable to calculate metric: ", t);
        }
    }

}
