package org.metricminer.tasks.metric;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.infra.dao.SourceCodeDAO;
import org.metricminer.model.Project;
import org.metricminer.model.SourceCode;
import org.metricminer.model.Task;
import org.metricminer.tasks.RunnableTask;

public abstract class SourcesIteratorAbstractTask implements RunnableTask {

	private static final int PAGE_SIZE = 5;
	protected Task task;
	protected Session session;
	protected static Logger log = Logger.getLogger(SourcesIteratorAbstractTask.class);
	protected SourceCodeDAO sourceCodeDAO;

	public SourcesIteratorAbstractTask(Task task, Session session, StatelessSession statelessSession) {
		this.task = task;
		this.session = session;
		this.sourceCodeDAO = new SourceCodeDAO(statelessSession);
	}

	@Override
	public void run() {
		Project project = task.getProject();

		log.debug("Starting to iterate over sources");

		int page = 0;
		Map<Long, String> idsAndNames = sourceCodeDAO.listSourceCodeIdsAndNamesFor(project, page++);
		
		while(idsAndNames.size()>0) {
			System.gc();
			log.debug("More " + idsAndNames.size() + " sources to work on!");

			List<Long> sourceIds = new ArrayList<Long>(idsAndNames.keySet());
			for (int i = 0; i < sourceIds.size(); i += PAGE_SIZE) {
	
				Long firstId = sourceIds.get(i);
				Long lastId = calculateLastId(sourceIds, i);
				
				log.debug("Getting source codes (page " + i / PAGE_SIZE + ")");
				List<SourceCode> sources = sourceCodeDAO.listSourcesOf(project, firstId, lastId);

                for (SourceCode sc : sources) {
                    log.debug("-- Working on " + idsAndNames.get(sc.getId()) + " id " + sc.getId());
                    manipulate(sc, idsAndNames.get(sc.getId()));
				}
				
			}

			idsAndNames = sourceCodeDAO.listSourceCodeIdsAndNamesFor(project, page++);
		}
		
		log.debug("Calling onComplete");
		onComplete();
		log.debug("Finished iterating over sources");

	}
	
	private Long calculateLastId(List<Long> sourceIds, int i) {
		Long lastId;
		int lastIdIndex = i + (PAGE_SIZE - 1);
		if (lastIdIndex < sourceIds.size())
			lastId = sourceIds.get(lastIdIndex);
		else
			lastId = sourceIds.get(sourceIds.size() - 1);
		return lastId;
	}

	protected abstract void manipulate(SourceCode sourceCode, String name);
	
	protected abstract void onComplete();

}
