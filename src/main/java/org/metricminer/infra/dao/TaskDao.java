package org.metricminer.infra.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.metricminer.model.Task;
import org.metricminer.model.TaskStatus;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class TaskDao {

	private StatelessSession session;

    public TaskDao(StatelessSession statelessSession) {
		this.session = statelessSession;
    }

    public void save(Task task) {
    	session.insert(task);
    }

    @SuppressWarnings("rawtypes")
    public Task getFirstQueuedTask() {
        List tasks = createCriteria().add(
                Restrictions.eq("status", TaskStatus.QUEUED)).addOrder(Order.asc("submitDate")).addOrder(Order.asc("position"))
                .setMaxResults(1).list();
        if (tasks.isEmpty())
            return null;
        return (Task) tasks.get(0);

    }

	private Criteria createCriteria() {
		return session.createCriteria(Task.class);
	}

    public void update(Task task) {
    	session.update(task);
    }

    @SuppressWarnings("unchecked")
    public List<Task> listTasks() {
        return createCriteria().list();
    }
}
