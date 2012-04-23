package tasks.runner;

import model.Task;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;
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

    public TaskRunner(SessionFactory sf) {
        this.daoSession = sf.openSession();
        this.taskSession = sf.openSession();
        this.statelessSession = sf.openStatelessSession();
        this.taskDao = new TaskDao(daoSession);
        log = Logger.getLogger(TaskRunner.class);
    }

    @Override
    public void execute() {
        taskToRun = taskDao.getFirstQueuedTask();
        if (taskToRun != null) {
            if (!taskToRun.isDependenciesFinished()) {
                log.info(taskToRun + ": Waiting for unresolved dependecies");
                closeSessions();
                return;
            }
            log.info("Starting task: " + taskToRun);
            taskToRun.start();
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
        Transaction tx = daoSession.beginTransaction();
        taskDao.update(taskToRun);
        tx.commit();
        log.info("Finished running task: " + taskToRun);
    }

    private void handleError(Exception e) {
        taskToRun.fail();
        taskDao.update(taskToRun);
        log.error("Error while running task " + taskToRun, e);
    }

    private void closeSessions() {
        if (daoSession.isOpen()) {
            daoSession.disconnect();
            daoSession.close();
        }
    }

}
