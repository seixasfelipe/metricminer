package org.metricminer.runner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.metricminer.config.ClassScan;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.config.MetricMinerStatus;
import org.metricminer.dao.TaskDao;
import org.metricminer.model.Task;
import org.metricminer.runner.TaskRunner;



public class TasksRunnerTest {

    private Session mockedSession;
    private TaskRunner taskRunner;
    private TaskDao mockedDao;

    @Before
    public void setUp() {
        SessionFactory sf = mock(SessionFactory.class);
        taskRunner = new TaskRunner(sf, new MetricMinerStatus(new MetricMinerConfigs(new ClassScan())));
        mockedSession = mock(Session.class);
        mockedDao = mock(TaskDao.class);
        taskRunner.daoSession = mockedSession;
        taskRunner.taskDao = mockedDao;
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
