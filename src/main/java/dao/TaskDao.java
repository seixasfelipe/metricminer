package dao;

import java.util.List;

import model.Task;
import model.TaskStatus;

import org.hibernate.Session;
import org.hibernate.Transaction;
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
		List tasks = session.createCriteria(Task.class)
				.add(Restrictions.eq("status", TaskStatus.QUEUED))
				.addOrder(Order.asc("submitDate")).setMaxResults(1).list();
		if (tasks.isEmpty())
			return null;
		return (Task) tasks.get(0);

	}

	public void update(Task task) {
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(task);
		tx.commit();
	}
}
