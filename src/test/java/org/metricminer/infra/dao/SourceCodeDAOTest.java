package org.metricminer.infra.dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.model.Artifact;
import org.metricminer.model.ArtifactKind;
import org.metricminer.model.Commit;
import org.metricminer.model.Project;
import org.metricminer.model.SourceCode;

public class SourceCodeDAOTest {

	private static Session session;
	private static StatelessSession statelessSession;
	private static SourceCodeDAO sourceCodeDAO;

	@BeforeClass
	public static void setUpClass() {
		SessionFactory sessionFactory = new Configuration().configure(
				"/hibernate.test.cfg.xml").buildSessionFactory();
		session = sessionFactory.openSession();
		statelessSession = sessionFactory.openStatelessSession();
		sourceCodeDAO = new SourceCodeDAO(statelessSession);
	}

	@Before
	public void setUp() {
		session.getTransaction().begin();
		statelessSession.getTransaction().begin();
	}

	@After
	public void tearDown() {
		session.getTransaction().rollback();
		statelessSession.getTransaction().rollback();
	}

	@Test
	public void shouldGetAllSourceCodeIdsFromProject() throws Exception {
		MetricMinerConfigs config = mock(MetricMinerConfigs.class);
		when(config.getRepositoriesDir()).thenReturn("/tmp");
		Project project = new Project("project", "", config);
		Project otherProject = new Project("other project", "", config);
		
		saveProjectData(project, otherProject);
		
		Map<Long, String> idsMap = sourceCodeDAO.listSourceCodeIdsAndNamesFor(project);
		List<Entry<Long, String>> idsProject = new ArrayList<Entry<Long, String>>(idsMap.entrySet());
		idsMap = sourceCodeDAO.listSourceCodeIdsAndNamesFor(otherProject);
		List<Entry<Long, String>> idsOtherProject = new ArrayList<Entry<Long, String>>(idsMap.entrySet());
		
		assertEquals(35, idsProject.size());
		assertEquals(12, idsOtherProject.size());
		for (Entry<Long, String> entry : idsOtherProject) {
			assertNotNull(entry.getValue());
		}
	}
	
	@Test
	public void shouldGetSourceCodesFromIds() throws Exception {
		MetricMinerConfigs config = mock(MetricMinerConfigs.class);
		when(config.getRepositoriesDir()).thenReturn("/tmp");
		Project project = new Project("project", "", config);
		Project otherProject = new Project("other project", "", config);
		saveProjectData(project, otherProject);
		
		Map<Long, String> idsAndNamesMap = sourceCodeDAO.listSourceCodeIdsAndNamesFor(project);
		ArrayList<Entry<Long, String>> entries = new ArrayList<Entry<Long, String>>(idsAndNamesMap.entrySet());
		List<SourceCode> sources = sourceCodeDAO.listSourcesOf(project, entries.get(0).getKey(), entries.get(entries.size()-1).getKey());
		
		assertEquals(entries.size(), sources.size());
	}

	private void saveProjectData(Project project, Project otherProject) {
		session.save(project);
		session.save(otherProject);
		Artifact A = new Artifact("A.java", ArtifactKind.CODE, project);
		session.save(A);
		Artifact B = new Artifact("B.java", ArtifactKind.CODE, project);
		session.save(B);
		Artifact C = new Artifact("C.java", ArtifactKind.CODE, otherProject);
		session.save(C);
		saveSourceCodesFor(A, 10);
		saveSourceCodesFor(B, 25);
		saveSourceCodesFor(C, 12);
	}

	private void saveSourceCodesFor(Artifact A, int n) {
		for (int i = 0; i < n; i++) {
			Commit commit = new Commit();
			session.save(commit);
			session.save(new SourceCode(A, commit , "some code " + i));
		}
	}
}
