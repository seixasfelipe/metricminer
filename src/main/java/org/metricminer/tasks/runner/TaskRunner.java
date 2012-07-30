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
import org.metricminer.tasks.TaskQueueStatus;

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
    private TaskQueueStatus status;
	private MetricMinerConfigs config;

    public TaskRunner(TaskQueueStatus status, SessionFactory sessionFactory) {
    	this.status = status;
    	this.config = status.getConfigs();
    	this.daoSession = sessionFactory.openSession();
    	this.taskSession = sessionFactory.openSession();
    	this.statelessSession = sessionFactory.openStatelessSession();
    	this.taskDao = new TaskDao(daoSession);
    	log = Logger.getLogger(TaskRunner.class);
    }

    @Override
    public void execute() {
    	try {
            taskToRun = taskDao.getFirstQueuedTask();
            if (!status.mayStartTask() || taskToRun == null || !taskToRun.isDependenciesFinished()) {
            	if (taskToRun != null && taskToRun.hasFailedDependencies()) {
            		log.error(taskToRun + " failed because a dependency for this task also failed.");
            		daoSession.beginTransaction();
            		taskToRun.fail();
            		daoSession.update(taskToRun);
            		daoSession.getTransaction().commit();
            	}
                closeSessions();
                return;
            }
            log.info("Starting task: " + taskToRun);
            taskToRun.start();
            status.addRunningTask(taskToRun);
            daoSession.beginTransaction();
            taskDao.update(taskToRun);
            daoSession.getTransaction().commit();
            runTask(taskSession);
        } catch (Throwable e) {
            handleError(e);
        } finally {
            closeSessions();
        }
    }

    private void runTask(Session taskSession) throws InstantiationException, IllegalAccessException {
        RunnableTaskFactory runnableTaskFactory = (RunnableTaskFactory) taskToRun
                .getRunnableTaskFactoryClass().newInstance();
        taskSession.beginTransaction();
        log.debug("Running task");
        runnableTaskFactory.build(taskToRun, taskSession, statelessSession, config).run();
        Transaction transaction = taskSession.getTransaction();
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
        System.gc();
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
        }
    }

}
