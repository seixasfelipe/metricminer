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
@Scheduled(cron = "0 0/1 * * * ?")
public class TasksRunner implements br.com.caelum.vraptor.tasks.Task {

	private static Logger log = Logger.getLogger(TasksRunner.class);

	@Override
	public void execute() {

		SessionFactory sessionFactory = new Configuration().configure()
				.buildSessionFactory();
		TaskDao taskDao = new TaskDao(sessionFactory.openSession());
		Task task = taskDao.getFirstQueuedTask();

		if (task != null) {
			log.info("Starting task: " + task.getName());
			task.start();
			taskDao.update(task);
			try {
				RunnableTaskFactory runnableTaskFactory = (RunnableTaskFactory) task
						.getRunnableTaskFactoryClass().newInstance();
				Session session = sessionFactory.openSession();
				session.beginTransaction();
				runnableTaskFactory.build(task.getProject(), session).run();
				task.finish();
				taskDao.update(task);
				session.getTransaction().commit();

			} catch (Exception e) {
				log.error("error when running a task", e);
			}
		}
	}
}
