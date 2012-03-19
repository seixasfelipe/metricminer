package tasks;

import model.Task;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

import dao.TaskDao;

public class TasksRunner {
	public static void main(String[] args) throws InterruptedException {

		Logger log = Logger.getLogger(TasksRunner.class);
		while (true) {
			SessionFactory sessionFactory = new Configuration().configure()
					.buildSessionFactory();
			TaskDao taskDao = new TaskDao(sessionFactory.openSession());
			Task task = taskDao.getOldestQueuedTask();
			if (task == null) {
				Thread.sleep(5000);
				log.info("No tasks to run, sleeping for 5000 msec");
				continue;
			}
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
				e.printStackTrace();
			}
		}
	}
}
