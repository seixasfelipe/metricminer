package org.metricminer.tasks.runner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.ServletContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.metricminer.config.ClassScan;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.infra.dao.TaskDao;
import org.metricminer.model.Task;
import org.metricminer.tasks.TaskQueueStatus;

public class TasksRunnerTest {

	private Session mockedSession;
	private TaskRunner taskRunner;
	private TaskDao mockedDao;
	private Session mockedTaskSession;
	private StatelessSession mockedStatelessSession;

	@Before
	public void setUp() {
		SessionFactory sf = mock(SessionFactory.class);
		ServletContext context = mock(ServletContext.class);
		when(context.getRealPath("/WEB-INF/metricminer.properties"))
				.thenReturn("src/test/resources/metricminer.properties");
		
		mockedSession = mock(Session.class);
		mockedTaskSession = mock(Session.class);
		mockedStatelessSession = mock(StatelessSession.class);
		mockedDao = mock(TaskDao.class);
		when(mockedSession.close()).thenReturn(null);
		when(mockedTaskSession.close()).thenReturn(null);
		
		taskRunner = new TaskRunner(sf, new TaskQueueStatus(new MetricMinerConfigs(
				new ClassScan(), context)));
		taskRunner.taskDao = mockedDao;
		taskRunner.statelessSession = mockedStatelessSession;
	}

	@Test
	public void shouldRunATaskWithoutDependencies() throws Exception {
		Task mockedTask = mock(Task.class);
		when(mockedDao.getFirstQueuedTask()).thenReturn(mockedTask);
		when(mockedTask.isDependenciesFinished()).thenReturn(true);
		Transaction mockedTransaction = mock(Transaction.class);
		when(mockedSession.beginTransaction()).thenReturn(mockedTransaction);

		taskRunner.execute();
		verify(mockedTask).start();
	}

	@Test
	public void shouldNotRunATaskWithDependencies() throws Exception {
		Task mockedTask = mock(Task.class);
		when(mockedDao.getFirstQueuedTask()).thenReturn(mockedTask);
		when(mockedTask.isDependenciesFinished()).thenReturn(false);
		Transaction mockedTransaction = mock(Transaction.class);
		when(mockedSession.beginTransaction()).thenReturn(mockedTransaction);

		taskRunner.execute();
		verify(mockedTask, never()).start();
	}

}
