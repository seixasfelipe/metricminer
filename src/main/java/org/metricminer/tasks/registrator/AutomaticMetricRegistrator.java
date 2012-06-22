package org.metricminer.tasks.registrator;

import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
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
	private StatelessSession projectDaoSession;
	
	public AutomaticMetricRegistrator(SessionFactory sf, MetricMinerConfigs metricMinerConfigs) {
		projectDaoSession = sf.openStatelessSession();
		this.projectDao = new ProjectDao(projectDaoSession);
		this.metricMinerConfigs = metricMinerConfigs;
	}

	@PostConstruct
	public void execute() {
		List<Project> projects = projectDao.listAll();
		for (Project project : projects) {
			project.addNewMetrics(metricMinerConfigs.getRegisteredMetrics());
			projectDao.update(project);
		}
		projectDaoSession.close();
	}
	
}
