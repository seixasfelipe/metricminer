package dao;

import model.Task;
import model.TaskStatus;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class TaskDao {

	private Session session;

	public TaskDao(Session session) {
		this.session = session;
	}

	public void save(Task task) {
		session.save(task);
	}

	public Task getOldestQueuedTask() {
		return (Task) session.createCriteria(Task.class)
				.add(Restrictions.eq("status", TaskStatus.QUEUED))
				.addOrder(Order.asc("submitDate")).setMaxResults(1).list()
				.get(0);

	}

	public void update(Task task) {
		session.saveOrUpdate(task);
		session.flush();
	}
}
