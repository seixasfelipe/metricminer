package tasks;

import model.Task;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import dao.TaskDao;

public class TasksRunner {
	public static void main(String[] args) {
		SessionFactory sessionFactory = new Configuration().configure()
				.buildSessionFactory();
		TaskDao taskDao = new TaskDao(sessionFactory.openSession());
		Task task = taskDao.getNewestTask();
		try {
			RunnableTaskFactory runnableTaskFactory = (RunnableTaskFactory) task
					.getRunnableTaskFactoryClass().newInstance();
			runnableTaskFactory.build(task.getProject(),
					sessionFactory.openSession()).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
