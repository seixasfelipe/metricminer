package org.metricminer.infra.session;

import java.io.Serializable;

import org.metricminer.model.User;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class UserSession implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private User loggedUser;
	
	public void login(User user) {
		this.loggedUser = user;
	}
	
	public void logout() {
		this.loggedUser = null;
	}
	
	public boolean isLoggedIn() {
		return loggedUser == null;
	}
	
	public User user() {
	    return loggedUser;
	}
	
}
