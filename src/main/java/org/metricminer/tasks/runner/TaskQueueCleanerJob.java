package org.metricminer.tasks.runner;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.metricminer.infra.dao.TaskDao;
import org.metricminer.model.Task;
import org.metricminer.tasks.TaskQueueStatus;

import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;


@PrototypeScoped
@Scheduled(cron = "0/60 * * * * ?")
public class TaskQueueCleanerJob implements br.com.caelum.vraptor.tasks.Task {

    private final Session session;
    private final TaskQueueStatus queueStatus;
    private final TaskDao taskDao;
    private Logger log = Logger.getLogger(TaskQueueCleanerJob.class);

    public TaskQueueCleanerJob(SessionFactory sessionFactory, TaskQueueStatus queueStatus) {
        this.session = sessionFactory.openSession();
        this.queueStatus = queueStatus;
        this.taskDao = new TaskDao(session);
    }
    
    @Override
    public void execute() {
        log.debug("Running TaskQueueCleanerJob...");
        List<Task> tasksRunning = taskDao.tasksRunning();
        for (Task task : tasksRunning) {
            if (!queueStatus.containsTask(task)) {
                log.debug("Found a zombie task, marking as failed: " + task);
                task.setFailed();
                session.beginTransaction();
                session.update(task);
                session.getTransaction().commit();
            }
        }
    }

}
