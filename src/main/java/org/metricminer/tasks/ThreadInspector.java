package org.metricminer.tasks;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class ThreadInspector {
    
    public boolean isRunning(Thread t) {
        return t.isAlive();
    }
}
