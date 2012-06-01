package org.metricminer.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.metricminer.model.RegisteredMetric;
import org.metricminer.tasks.metric.MetricComponent;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class MetricMinerConfigs {
    private String metricMinerHome;
    private List<RegisteredMetric> registeredMetrics;
    private int maxConcurrentTasks;
	private final ClassScan scan;
	
	private Logger logger = Logger.getLogger(MetricMinerConfigs.class);

    public MetricMinerConfigs(ClassScan scan) {
        this.scan = scan;
        
		this.maxConcurrentTasks = 1;
        this.metricMinerHome = "/tmp/metricminer";
        this.registeredMetrics = new ArrayList<RegisteredMetric>();
        registerMetrics();
    }

    private void registerMetrics() {
    	
    	Set<String> metrics = scan.findAll(MetricComponent.class);
    	logger.info("Metrics found: " + metrics.size());
    	
    	for(String clazz : metrics) {
    		try {
				Class<?> clazzDef = Class.forName(clazz);
				MetricComponent annotation = clazzDef.getAnnotation(MetricComponent.class);
				
				logger.info("Registering metric: " + clazz);
				this.registeredMetrics.add(new RegisteredMetric(annotation.name(), clazz));
			} catch (ClassNotFoundException e) {
				logger.error("Metric not found: " + clazz);
			}
    	}
    }

    public String getMetricMinerHome() {
        return metricMinerHome;
    }

    public List<RegisteredMetric> getRegisteredMetrics() {
    	return Collections.unmodifiableList(registeredMetrics);
    }

    public int getMaxConcurrentTasks() {
        return maxConcurrentTasks;
    }

}
