package tasks.runner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import model.SourceCode;
import model.Task;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import config.MetricMinerConfigs;
import config.MetricMinerStatus;
import dao.TaskDao;

public class TasksRunnerTest {

    private Session mockedSession;
    private TaskRunner taskRunner;
    private TaskDao mockedDao;

    @Before
    public void setUp() {
        SessionFactory sf = mock(SessionFactory.class);
        taskRunner = new TaskRunner(sf, new MetricMinerStatus(new MetricMinerConfigs()));
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

    @Ignore
    @Test
    public void testStatelessSession() throws Exception {
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        StatelessSession statelessSession = sf.openStatelessSession();
        Query query = statelessSession.createQuery("select source from SourceCode source "
                + "join source.artifact as artifact where artifact.project.id = :project_id");
        query.setParameter("project_id", 1L);
        ScrollableResults results = query.scroll();
        while (results.next()) {
            SourceCode source = (SourceCode) results.get(0);
            System.out.println(source.getName());
        }
    }
    
}
