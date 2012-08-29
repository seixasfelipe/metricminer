package org.metricminer.tasks.runner;

import java.util.List;

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
    private TaskQueueStatus queueStatus;
    private MetricMinerConfigs config;

    public TaskRunner(TaskQueueStatus status, SessionFactory sessionFactory) {
        this.queueStatus = status;
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
            List<Task> tasksToClean = queueStatus.cleanTasksNotRunning();
            failZombieTasks(tasksToClean);
            taskToRun = taskDao.getFirstQueuedTask();
            if (shouldNotStartTask()) {
                if (taskHasFailedDependency()) {
                    log.error(taskToRun
                            + " failed because a dependency for this task also failed.");
                    daoSession.beginTransaction();
                    taskToRun.fail();
                    daoSession.update(taskToRun);
                    daoSession.getTransaction().commit();
                }
                closeSessions();
                return;
            }
            log.info("Starting task: " + taskToRun);
            taskToRun.setStarted();
            queueStatus.addRunningTask(taskToRun, Thread.currentThread());
            daoSession.beginTransaction();
            taskDao.update(taskToRun);
            daoSession.getTransaction().commit();
            runTask();
        } catch (Throwable e) {
            handleError(e);
        } finally {
            closeSessions();
        }
    }

    private void failZombieTasks(List<Task> tasksToClean) {
        for (Task task : tasksToClean) {
            log.error("The thread running: " + task + " died, setting status to FAILED");
            daoSession.beginTransaction();
            task.fail();
            daoSession.update(task);
            daoSession.getTransaction().commit();
        }

    }

    private boolean taskHasFailedDependency() {
        return taskToRun != null && taskToRun.hasFailedDependencies();
    }

    private boolean shouldNotStartTask() {
        return !queueStatus.mayStartTask() || taskToRun == null
                || !taskToRun.isDependenciesFinished();
    }

    private void runTask() throws InstantiationException,
            IllegalAccessException {
        RunnableTaskFactory runnableTaskFactory = (RunnableTaskFactory) taskToRun
                .getRunnableTaskFactoryClass().newInstance();
        taskSession.beginTransaction();
        log.debug("Running task");
        runnableTaskFactory.build(taskToRun, taskSession, statelessSession,
                config).run();
        finishTask();
    }

    private void finishTask() {
        taskToRun.setFinished();
        Transaction transaction = taskSession.getTransaction();
        if (transaction.isActive()) {
            transaction.commit();
        }
        queueStatus.finishCurrentTask(taskToRun);
        Transaction tx = daoSession.beginTransaction();
        taskDao.update(taskToRun);
        log.info("Finished running task: " + taskToRun);
        tx.commit();
        System.gc();
    }

    private void handleError(Throwable e) {
        taskToRun.fail();
        queueStatus.finishCurrentTask(taskToRun);
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
        } catch (SessionException e) {
        }
    }

}
