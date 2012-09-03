package org.metricminer.tasks;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

// necessary to mock threads behavior
@Component
@ApplicationScoped
public class ThreadInspector {
    
    public boolean isRunning(Thread t) {
        return t.isAlive();
    }
}
