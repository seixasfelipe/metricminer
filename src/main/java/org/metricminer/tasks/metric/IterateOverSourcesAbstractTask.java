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

public abstract class IterateOverSourcesAbstractTask implements RunnableTask {

	protected Task task;
	protected Session session;
	protected static Logger log = Logger.getLogger(CalculateMetricTask.class);
	protected SourceCodeDAO sourceCodeDAO;

	public IterateOverSourcesAbstractTask(Task task, Session session, StatelessSession statelessSession) {
		this.task = task;
		this.session = session;
		this.sourceCodeDAO = new SourceCodeDAO(statelessSession);
	}

	@Override
	public void run() {
		Project project = task.getProject();

		log.debug("Starting to iterate over sources");

		Map<Long, String> map = sourceCodeDAO.listSourceCodeIdsAndNamesFor(project);
		List<Long> sourceIds = new ArrayList<Long>(map.keySet());

		int totalIds = sourceIds.size();

		int PAGE_SIZE = 5;
		for (int i = 0; i < totalIds; i += PAGE_SIZE) {
			Long firstId = sourceIds.get(i);
			Long lastId;
			int lastIdIndex = i + (PAGE_SIZE - 1);
			if (lastIdIndex < sourceIds.size())
				lastId = sourceIds.get(lastIdIndex);
			else
				lastId = sourceIds.get(sourceIds.size() - 1);
			log.debug("Getting source codes (page " + i / PAGE_SIZE + ")");
			List<SourceCode> sources = sourceCodeDAO.listSourcesOf(project, firstId, lastId);
			for (SourceCode sc : sources) {
				log.debug("-- Working on " + map.get(sc.getId()) + " id " + sc.getId());
				manipulate(sc, map.get(sc.getId()));
			}
			System.gc();
		}
		log.debug("Finished iterating over sources");

	}

	protected abstract void manipulate(SourceCode sourceCode, String name);

}
