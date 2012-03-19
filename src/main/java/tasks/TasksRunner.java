package tasks;

import model.Task;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import dao.TaskDao;

public class TasksRunner {
	public static void main(String[] args) throws InterruptedException {
		SessionFactory sessionFactory = new Configuration().configure()
				.buildSessionFactory();
		TaskDao taskDao = new TaskDao(sessionFactory.openSession());
		Logger log = Logger.getLogger(TasksRunner.class);
		while (true) {
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
				runnableTaskFactory.build(task.getProject(),
						sessionFactory.openSession()).run();
				task.finish();
				taskDao.update(task);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
