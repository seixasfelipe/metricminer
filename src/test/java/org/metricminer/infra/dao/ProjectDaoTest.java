package org.metricminer.infra.dao;

import static junit.framework.Assert.assertEquals;

import java.util.Calendar;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.metricminer.model.Author;
import org.metricminer.model.Commit;
import org.metricminer.model.Project;

public class ProjectDaoTest {

	private static Session session;
	private static ProjectDao projectDao;

	@BeforeClass
	public static void setUpClass() {
		SessionFactory sessionFactory = new Configuration().configure(
				"/hibernate.test.cfg.xml").buildSessionFactory();
		session = sessionFactory.openSession();
		projectDao = new ProjectDao(session);
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
	
	@Test
	public void shouldGetCommitersCount() throws Exception {
		Project project = new Project();
		Author author1 = new Author();
		Author author2 = new Author();
		session.save(project);
		session.save(author1);
		session.save(author2);
		addCommitsOfAuthor(2, project, author1);
		addCommitsOfAuthor(2, project, author2);
		
		Project project2 = new Project();
		Author author3 = new Author();
		Author author4 = new Author();
		Author author5 = new Author();
		Author author6 = new Author();
		session.save(project2);
		session.save(author3);
		session.save(author4);
		session.save(author5);
		session.save(author6);
		addCommitsOfAuthor(10, project2, author3);
		addCommitsOfAuthor(5, project2, author4);
		addCommitsOfAuthor(21, project2, author5);
		addCommitsOfAuthor(33, project2, author6);
		
		assertEquals((Long)2l, projectDao.commitersCountFor(project));
		assertEquals((Long)4l, projectDao.commitersCountFor(project2));
	}

	private Project aProjectWithCommits(int totalCommits) {
		Project project = new Project();
		session.save(project);
		Author author = new Author();
		session.save(author);
		addCommitsOfAuthor(totalCommits, project, author);
		return project;
	}

	private void addCommitsOfAuthor(int totalCommits, Project project, Author author) {
		for (int i = 0; i < totalCommits; i++) {
			session.save(new Commit("" + i, author, Calendar.getInstance(), "", "", "", project));
		}
	}
	
}
