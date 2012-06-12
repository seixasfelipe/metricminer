package org.metricminer.tasks.runner;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.infra.dao.TaskDao;
import org.metricminer.model.Task;
import org.metricminer.tasks.RunnableTaskFactory;
import org.metricminer.tasks.TaskStatus;

import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;

@PrototypeScoped
@Scheduled(cron = "0/10 * * * * ?")
public class TaskRunner implements br.com.caelum.vraptor.tasks.Task {

    TaskDao taskDao;
    Task taskToRun;
    Session daoSession;
    Session taskSession;
    StatelessSession statelessSession;
    Logger log;
    private final TaskStatus status;
	private final MetricMinerConfigs config;

    public TaskRunner(SessionFactory sf, TaskStatus status) {
        this.status = status;
		this.config = status.getConfigs();
        this.daoSession = sf.openSession();
        this.taskSession = sf.openSession();
        this.statelessSession = sf.openStatelessSession();
        this.taskDao = new TaskDao(daoSession);
        log = Logger.getLogger(TaskRunner.class);
    }

    @Override
    public void execute() {
        try {
            taskToRun = taskDao.getFirstQueuedTask();
            if (!status.mayStartTask() || taskToRun == null || !taskToRun.isDependenciesFinished()) {
                closeSessions();
                return;
            }
            log.info("Starting task: " + taskToRun);
            taskToRun.start();
            log.debug("Started task");
            status.addRunningTask(taskToRun);
            log.debug("Added task to status");
            Transaction tx = daoSession.beginTransaction();
            taskDao.update(taskToRun);
            log.debug("Updated task");
            tx.commit();
            log.debug("Commited update");
            runTask(taskSession);
            log.info("Finished running task");
        } catch (Throwable e) {
            handleError(e);
        } finally {
            closeSessions();
        }
    }

    private void runTask(Session taskSession) throws InstantiationException, IllegalAccessException {
        RunnableTaskFactory runnableTaskFactory = (RunnableTaskFactory) taskToRun
                .getRunnableTaskFactoryClass().newInstance();
        log.debug("Beginning transaction");
        taskSession.beginTransaction();
        log.debug("Running task");
        runnableTaskFactory.build(taskToRun, taskSession, statelessSession, config).run();
        Transaction transaction = taskSession.getTransaction();
        log.debug("Closing transaction");
        if (!transaction.isActive()) {
            transaction.begin();
        }
        finishTask();
    }

    private void finishTask() {
        taskToRun.finish();
        status.finishCurrentTask(taskToRun);
        Transaction tx = daoSession.beginTransaction();
        taskDao.update(taskToRun);
        log.info("Finished running task: " + taskToRun);
        tx.commit();
    }

    private void handleError(Throwable e) {
        taskToRun.fail();
        status.finishCurrentTask(taskToRun);
        Transaction tx = daoSession.beginTransaction();
        taskDao.update(taskToRun);
        tx.commit();
        log.error("Error while running task " + taskToRun, e);
    }

    private void closeSessions() {
        if (daoSession.isOpen())
            daoSession.close();
        if (taskSession.isOpen())
            taskSession.close();
        try {
            statelessSession.close();
        } catch(SessionException e) {
            log.info("Already closed session");
        }
    }

}
