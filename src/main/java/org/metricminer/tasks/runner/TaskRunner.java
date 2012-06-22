package org.metricminer.tasks.runner;

import org.apache.log4j.Logger;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
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

	Task taskToRun;
    StatelessSession statelessSession;
    Logger log;
    TaskQueueStatus status;
	MetricMinerConfigs config;
	TaskDao taskDao;

    public TaskRunner(SessionFactory sf, TaskQueueStatus status) {
        this.status = status;
		this.config = status.getConfigs();
        this.statelessSession = sf.openStatelessSession();
        this.taskDao = new TaskDao(statelessSession);
        log = Logger.getLogger(TaskRunner.class);
    }

    @Override
    public void execute() {
    	Runtime rt = Runtime.getRuntime();
		log.debug("rt.totalMemory() - rt.freeMemory(): " + (rt.totalMemory() - rt.freeMemory()));
        try {
            taskToRun = taskDao.getFirstQueuedTask();
            if (!status.mayStartTask() || taskToRun == null || !taskToRun.isDependenciesFinished()) {
            	if (taskToRun != null && taskToRun.hasFailedDependencies()) {
            		log.error(taskToRun + " failed because a dependency for this task also failed.");
            		taskToRun.fail();
            		statelessSession.update(taskToRun);
            	}
                closeSessions();
                return;
            }
            log.info("Starting task: " + taskToRun);
            taskToRun.start();
            status.addRunningTask(taskToRun);
            taskDao.update(taskToRun);
            runTask();
        } catch (Throwable e) {
            handleError(e);
        } finally {
            closeSessions();
        }
    }

    private void runTask() throws InstantiationException, IllegalAccessException {
        RunnableTaskFactory runnableTaskFactory = (RunnableTaskFactory) taskToRun
                .getRunnableTaskFactoryClass().newInstance();
        log.debug("Running task");
        runnableTaskFactory.build(taskToRun, statelessSession, config).run();
        finishTask();
    }

    private void finishTask() {
        taskToRun.finish();
        status.finishCurrentTask(taskToRun);
        taskDao.update(taskToRun);
        log.info("Finished running task: " + taskToRun);
        System.gc();
    }

    private void handleError(Throwable e) {
        taskToRun.fail();
        status.finishCurrentTask(taskToRun);
        taskDao.update(taskToRun);
        log.error("Error while running task " + taskToRun, e);
    }

    private void closeSessions() {
        try {
            statelessSession.close();
        } catch(SessionException e) {
        }
    }

}
