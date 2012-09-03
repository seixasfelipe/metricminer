package org.metricminer.infra.dao;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.model.Author;
import org.metricminer.model.Commit;
import org.metricminer.model.CommitMessage;
import org.metricminer.model.Diff;
import org.metricminer.model.Project;
import org.metricminer.model.Task;
import org.metricminer.model.TaskBuilder;

public class ProjectDaoTest extends DaoTest {

	private static ProjectDao projectDao;
    private MetricMinerConfigs mockedConfigs;

	@Before
	public void setUp() {
	    projectDao = new ProjectDao(session);
		session.getTransaction().begin();
		mockedConfigs = mock(MetricMinerConfigs.class);
		when(mockedConfigs.getRepositoriesDir()).thenReturn("");
	}

	@After
	public void tearDown() {
		session.getTransaction().rollback();
	}

	@Test
	public void shouldGetCommitCount() {
		Project projectWithThreeCommits = aProjectWithCommits(3,
				Calendar.getInstance());
		Project projectWithTwoCommits = aProjectWithCommits(5,
				Calendar.getInstance());
		Project projectWithTenCommits = aProjectWithCommits(10,
				Calendar.getInstance());
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
		addCommitsOfAuthor(2, project, author1, Calendar.getInstance());
		addCommitsOfAuthor(2, project, author2, Calendar.getInstance());

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
		addCommitsOfAuthor(10, project2, author3, Calendar.getInstance());
		addCommitsOfAuthor(5, project2, author4, Calendar.getInstance());
		addCommitsOfAuthor(21, project2, author5, Calendar.getInstance());
		addCommitsOfAuthor(33, project2, author6, Calendar.getInstance());

		assertEquals((Long) 2l, projectDao.commitersCountFor(project));
		assertEquals((Long) 4l, projectDao.commitersCountFor(project2));
	}

	@Test
	public void shouldGetCommitCountForLastMonths() {
		Project project = new Project();
		Author author = new Author();
		session.save(author);
		session.save(project);
		Calendar calendar;
		for (int i = 0; i < 12; i++) {
			calendar = Calendar.getInstance();
			calendar.set(2012, i, 10);
			addCommitsOfAuthor(i, project, author, calendar);
		}
		// some old commits
		calendar = Calendar.getInstance();
		calendar.set(2011, 10, 11);
		addCommitsOfAuthor(5, project, author, calendar);
		calendar = Calendar.getInstance();
		calendar.set(2011, 9, 11);
		addCommitsOfAuthor(5, project, author, calendar);
		session.flush();

		Map<Calendar, Long> lastCommits = projectDao
				.commitCountForLastMonths(project);
		assertEquals(12, lastCommits.size());
		for (Entry<Calendar, Long> entry : lastCommits.entrySet()) {
			int month = entry.getKey().get(Calendar.MONTH);
			assertEquals(month, (long) entry.getValue());
		}

	}
	
	@Test
	public void shouldFindTenProjectCount() throws Exception {
		for (int i = 0; i < 10; i++)
			session.save(new Project());
		Long totalProjectCount = projectDao.totalProjects();
		assertEquals(new Long(10L), totalProjectCount);
	}
	
	@Test
	public void shouldGetLastTenProjects() throws Exception {
		session.save(new Project("this should not appear", "", mockedConfigs));
		for (int i = 0; i < 10; i++) {
			Thread.sleep(5);
			session.save(new Project("new project " + i, "", mockedConfigs));
		}
		List<Project> projects = projectDao.tenNewestProjects();
		int i = 9;
		for (Project project : projects) {
			assertEquals("new project " + i, project.getName());
			i--;
		}
	}
	
	@Test
    public void shouldGetTotalPages() throws Exception {
	    for (int i = 0; i < 100; i++) {
            session.save(new Project("new project " + i, "", mockedConfigs));
        }
	    assertEquals((int)Math.ceil(100.0/ProjectDao.PAGE_SIZE), projectDao.totalPages());
    }
	
	@Test
    public void shouldDeleteProject() throws Exception {
	    Project project = new Project("some project", "", mockedConfigs);
	    Task task = new TaskBuilder().build();
        project.addTask(task);
	    session.save(project);
	    session.save(task);
	    session.flush();
	    projectDao.delete(project.getId());
	    session.flush();
        
    }

	private Project aProjectWithCommits(int totalCommits, Calendar commitDate) {
		Project project = new Project();
		session.save(project);
		Author author = new Author();
		session.save(author);
		addCommitsOfAuthor(totalCommits, project, author, commitDate);
		return project;
	}
	

	private void addCommitsOfAuthor(int totalCommits, Project project,
			Author author, Calendar commitDate) {
		for (int i = 0; i < totalCommits; i++) {
			session.save(new Commit("" + i, author, commitDate, new CommitMessage(""), new Diff(""), "",
					project));
		}
	}
	
	

}
