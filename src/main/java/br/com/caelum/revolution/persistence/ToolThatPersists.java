package br.com.caelum.revolution.persistence;

import org.hibernate.Session;

public interface ToolThatPersists {

	Class<?>[] classesToPersist();
	void setSession(Session session);
}
