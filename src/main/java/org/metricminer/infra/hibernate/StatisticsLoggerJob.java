package org.metricminer.infra.hibernate;

import java.lang.reflect.Method;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.hibernate.stat.Statistics;

import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.tasks.Task;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;

@PrototypeScoped
@Scheduled(cron = "0 0/2 * * * ?")
public class StatisticsLoggerJob implements Task {

	private final Statistics stats;
	private static Logger log = Logger.getLogger(StatisticsLoggerJob.class);

	public StatisticsLoggerJob(Statistics stats) {
		this.stats = stats;
	}

	@Override
	public void execute() {
		log.info("------------- " + Calendar.getInstance().getTime());
		for (Method m : Statistics.class.getMethods()) {
			try {
				if (m.getName().startsWith("get"))
					log.info(m.getName() + " = " + m.invoke(stats));
			} catch (Exception e) {
				// do nothing!
			}
		}

	}

}
