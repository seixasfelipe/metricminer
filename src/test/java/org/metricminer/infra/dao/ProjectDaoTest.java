package org.metricminer.infra.dao;

import static junit.framework.Assert.assertEquals;

import java.util.Calendar;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;
import org.metricminer.model.Author;
import org.metricminer.model.Commit;
import org.metricminer.model.Project;

public class ProjectDaoTest {

	private Session session;
	private ProjectDao projectDao;

	@Before
	public void setUp() {
		SessionFactory sessionFactory = new Configuration().configure(
				"/hibernate.test.cfg.xml").buildSessionFactory();
		session = sessionFactory.openSession();
		projectDao = new ProjectDao(session);
	}

	@Test
	public void shouldGetCommitCount() {
		Project projectWithThreeCommits = aProjectWithCommits(3);
		Project projectWithTwoCommits = aProjectWithCommits(5);
		Project projectWithTenCommits = aProjectWithCommits(10);
		session.flush();
		assertEquals((Long) 3l,
				projectDao.commitCountFor(projectWithThreeCommits));
		assertEquals((Long) 5l,
				projectDao.commitCountFor(projectWithTwoCommits));
		assertEquals((Long) 10l,
				projectDao.commitCountFor(projectWithTenCommits));
	}

	private Project aProjectWithCommits(int totalCommits) {
		Project project = new Project();
		session.save(project);
		Author author = new Author();
		session.save(author);
		for (int i = 0; i < totalCommits; i++) {
			session.save(new Commit("" + i, author, Calendar.getInstance(), "", "", "", project));
		}
		return project;
	}
}
