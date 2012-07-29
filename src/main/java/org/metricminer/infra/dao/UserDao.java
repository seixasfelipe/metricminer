package org.metricminer.infra.dao;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.metricminer.model.User;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class UserDao {
	private final Session session;

	public UserDao(Session session) {
		this.session = session;
	}
	
	public void save(User user) {
		session.save(user);
	}

	public User findByEmail(String email) {
		return (User) session.createCriteria(User.class).add(Restrictions.like("email", email)).uniqueResult();
	}
}
