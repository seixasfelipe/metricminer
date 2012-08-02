package org.metricminer.infra.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.metricminer.model.Project;
import org.metricminer.model.Task;
import org.metricminer.model.TaskBuilder;

public class TaskDaoTest {

	private static Session session;
	private static TaskDao taskDao;

	@BeforeClass
	public static void setUpClass() {
		SessionFactory sessionFactory = new Configuration().configure(
				"/hibernate.test.cfg.xml").buildSessionFactory();
		session = sessionFactory.openSession();
		taskDao = new TaskDao(session);
	}

	@Before
	public void setUp() {
		session.getTransaction().begin();
	}

	@After
	public void tearDown() {
		session.getTransaction().rollback();
	}

	@Test
	public void shouldGetLastTenFinishedTasks() throws Exception {
		String oldestTaskName = "should not appear";
		Project project = new Project();
		Task task = new TaskBuilder().withName(oldestTaskName)
				.forProject(project).build();
		task.finish();
		session.save(project);
		session.save(task);
		Thread.sleep(1000);
		for (int i = 0; i < 10; i++) {
			project = new Project();
			task = new TaskBuilder().withName("task " + i)
					.forProject(project).build();
			task.finish();
			session.save(project);
			session.save(task);
		}
		session.flush();

		List<Task> lastTasks = taskDao.lastTasks(10);

		assertEquals(10, lastTasks.size());
		for (Task t : lastTasks) {
			assertNotSame(oldestTaskName, t.getName());
		}

	}

}
