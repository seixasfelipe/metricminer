package tasks.runner;

import model.Task;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;
import config.MetricMinerStatus;
import dao.TaskDao;
@PrototypeScoped
@Scheduled(cron = "0/10 * * * * ?")
public class TaskRunner implements br.com.caelum.vraptor.tasks.Task {

    TaskDao taskDao;
    Task taskToRun;
    Session daoSession;
    Session taskSession;
    StatelessSession statelessSession;
    Logger log;
    private final MetricMinerStatus status;

    public TaskRunner(SessionFactory sf, MetricMinerStatus status) {
        this.status = status;
        this.daoSession = sf.openSession();
        this.taskSession = sf.openSession();
        this.statelessSession = sf.openStatelessSession();
        this.taskDao = new TaskDao(daoSession);
        log = Logger.getLogger(TaskRunner.class);
    }

    @Override
    public void execute() {
        taskToRun = taskDao.getFirstQueuedTask();
        if (!status.mayStartTask() || taskToRun == null || !taskToRun.isDependenciesFinished()) {
            closeSessions();
            return;
        }
        log.info("Starting task: " + taskToRun);
        taskToRun.start();
        status.addRunningTask(taskToRun);
        Transaction tx = daoSession.beginTransaction();
        taskDao.update(taskToRun);
        tx.commit();
        try {
            runTask(taskSession);
        } catch (Exception e) {
            handleError(e);
        } finally {
            closeSessions();
        }
        closeSessions();
    }

    private void runTask(Session taskSession) throws InstantiationException, IllegalAccessException {
        RunnableTaskFactory runnableTaskFactory = (RunnableTaskFactory) taskToRun
                .getRunnableTaskFactoryClass().newInstance();
        taskSession.beginTransaction();
        runnableTaskFactory.build(taskToRun, taskSession, statelessSession).run();
        Transaction transaction = taskSession.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        finishTask();
    }

    private void finishTask() {
        taskToRun.finish();
        status.finishCurrentTask();
        Transaction tx = daoSession.beginTransaction();
        taskDao.update(taskToRun);
        log.info("Finished running task: " + taskToRun);
        tx.commit();
    }

    private void handleError(Exception e) {
        taskToRun.fail();
        status.finishCurrentTask();
        Transaction tx = daoSession.beginTransaction();
        taskDao.update(taskToRun);
        tx.commit();
        log.error("Error while running task " + taskToRun, e);
    }

    private void closeSessions() {
        if (daoSession.isOpen()) {
            daoSession.disconnect();
            daoSession.close();
        }
    }

}
