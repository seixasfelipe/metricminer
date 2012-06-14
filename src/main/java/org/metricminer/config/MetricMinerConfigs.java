package org.metricminer.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.metricminer.MetricMinerExeption;
import org.metricminer.model.RegisteredMetric;
import org.metricminer.tasks.MetricComponent;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class MetricMinerConfigs {
    private String repositoriesDir;
    private List<RegisteredMetric> registeredMetrics;
    private int maxConcurrentTasks;
	private final ClassScan scan;
	
	private Logger logger = Logger.getLogger(MetricMinerConfigs.class);
	private final ServletContext context;
	private final String configPath = "/WEB-INF/metricminer.properties";

    public MetricMinerConfigs(ClassScan scan, ServletContext context) {
        this.scan = scan;
		this.context = context;
        
		this.maxConcurrentTasks = 1;
        this.registeredMetrics = new ArrayList<RegisteredMetric>();
        readConfigurationFile();
        registerMetrics();
    }

	private void readConfigurationFile() {
		Properties properties = new Properties();
        try {
			loadConfsFrom(properties);
		} catch (FileNotFoundException e) {
			throw new MetricMinerExeption("Configuration file not found.", e);
		} catch (IOException e) {
			throw new MetricMinerExeption("Could not read configuration file.", e);
		}
	}

	private void loadConfsFrom(Properties properties) throws IOException,
			FileNotFoundException {
		String configFilePath = context.getRealPath(configPath);
		properties.load(new FileInputStream(configFilePath));
		this.repositoriesDir = properties.getProperty("repositories.dir", "/tmp/metricminer");
		File file = new File(repositoriesDir);
		if (!file.canWrite())
			throw new MetricMinerExeption(repositoriesDir + " is not writable.");
	}

    private void registerMetrics() {
    	
    	Set<String> metrics = scan.findAll(MetricComponent.class);
    	logger.info("Metrics found: " + metrics.size());
    	
    	for(String clazz : metrics) {
    		try {
				Class<?> clazzDef = Class.forName(clazz);
				MetricComponent annotation = clazzDef.getAnnotation(MetricComponent.class);
				
				logger.info("Registering metric: " + clazz);
				this.registeredMetrics.add(new RegisteredMetric(annotation.name(), clazzDef));
			} catch (ClassNotFoundException e) {
				logger.error("Metric not found: " + clazz);
			}
    	}
    }

    public String getRepositoriesDir() {
        return repositoriesDir;
    }

    public List<RegisteredMetric> getRegisteredMetrics() {
    	return Collections.unmodifiableList(registeredMetrics);
    }

    public int getMaxConcurrentTasks() {
        return maxConcurrentTasks;
    }

}
