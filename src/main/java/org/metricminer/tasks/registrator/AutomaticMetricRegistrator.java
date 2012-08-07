package org.metricminer.tasks.registrator;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.infra.dao.ProjectDao;
import org.metricminer.model.Project;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@ApplicationScoped
@Component
public class AutomaticMetricRegistrator {

	private ProjectDao projectDao;
	private final MetricMinerConfigs metricMinerConfigs;
	private Session projectDaoSession;
	private Logger logger = Logger.getLogger(AutomaticMetricRegistrator.class);
	
	public AutomaticMetricRegistrator(SessionFactory sf, MetricMinerConfigs metricMinerConfigs) {
		projectDaoSession = sf.openSession();
		this.projectDao = new ProjectDao(projectDaoSession);
		this.metricMinerConfigs = metricMinerConfigs;
	}

	@PostConstruct
	public void execute() {
		List<Project> projects = projectDao.listAll();
		projectDaoSession.beginTransaction();
		for (Project project : projects) {
		    logger.info("Adding new metric to project " + project);
			project.addNewMetrics(metricMinerConfigs.getRegisteredMetrics());
			projectDao.update(project);
		}
		projectDaoSession.getTransaction().commit();
		projectDaoSession.close();
	}
	
}
