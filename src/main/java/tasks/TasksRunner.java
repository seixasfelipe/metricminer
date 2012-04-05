package tasks;

import model.Task;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;
import dao.TaskDao;

@PrototypeScoped
@Scheduled(cron = "0/30 * * * * ?")
public class TasksRunner implements br.com.caelum.vraptor.tasks.Task {

	private static Logger log = Logger.getLogger(TasksRunner.class);

	@Override
	public void execute() {
		SessionFactory sessionFactory = new Configuration().configure()
				.buildSessionFactory();
		Session daoSession = sessionFactory.openSession();
		TaskDao taskDao = new TaskDao(daoSession);
		Task task = taskDao.getFirstQueuedTask();
		if (task != null) {
			if (!task.isDependenciesFinished()) {
				log.info("Waiting for task to finish...");
				return;
			}
			log.info("Starting task: " + task.getName());
			task.start();
			taskDao.update(task);
			Session taskSession = sessionFactory.openSession();
			try {
				RunnableTaskFactory runnableTaskFactory = (RunnableTaskFactory) task
						.getRunnableTaskFactoryClass().newInstance();
				taskSession.beginTransaction();
				runnableTaskFactory.build(task.getProject(), taskSession).run();
				task.finish();
				taskDao.update(task);
				taskSession.getTransaction().commit();

			} catch (Exception e) {
				task.fail();
				taskDao.update(task);
				log.error("Error when running a task", e);
			} finally {
				daoSession.close();
				taskSession.close();
			}
		}
	}
}
