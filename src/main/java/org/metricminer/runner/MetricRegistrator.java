package org.metricminer.runner;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.dao.ProjectDao;
import org.metricminer.model.Project;

import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;

@PrototypeScoped
@Scheduled(cron = "0 * * * * ?")
public class MetricRegistrator implements br.com.caelum.vraptor.tasks.Task {

	private ProjectDao projectDao;
	private final MetricMinerConfigs metricMinerConfigs;
	private Session projectDaoSession;
	
	public MetricRegistrator(SessionFactory sf, MetricMinerConfigs metricMinerConfigs) {
		projectDaoSession = sf.openSession();
		this.projectDao = new ProjectDao(projectDaoSession);
		this.metricMinerConfigs = metricMinerConfigs;
	}

	@Override
	public void execute() {
		List<Project> projects = projectDao.listAll();
		projectDaoSession.beginTransaction();
		for (Project project : projects) {
			project.addNewMetrics(metricMinerConfigs.getRegisteredMetrics());
			projectDao.update(project);
		}
		projectDaoSession.getTransaction().commit();
		projectDaoSession.close();
	}
	
}
