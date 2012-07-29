package org.metricminer.infra.session;

import org.metricminer.model.User;

import br.com.caelum.vraptor.ioc.SessionScoped;

@SessionScoped
public class UserSession {
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
	
}
