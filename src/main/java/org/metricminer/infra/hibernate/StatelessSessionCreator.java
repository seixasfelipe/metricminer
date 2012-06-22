package org.metricminer.infra.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
public class StatelessSessionCreator implements ComponentFactory<StatelessSession>{

	private final SessionFactory sf;
	
	public StatelessSessionCreator(SessionFactory sf) {
		this.sf = sf;
	}
	
	@Override
	public StatelessSession getInstance() {
		return sf.openStatelessSession();
	}

}
