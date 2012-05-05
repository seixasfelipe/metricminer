package org.metricminer.config;

import java.util.ArrayList;
import java.util.List;

import org.metricminer.model.RegisteredMetric;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class MetricMinerConfigs {
    private String metricMinerHome;
    private List<RegisteredMetric> registeredMetrics;
    private int maxConcurrentTasks;

    public MetricMinerConfigs() {
        this.maxConcurrentTasks = 1;
        this.metricMinerHome = "/tmp/metricminer";
        this.registeredMetrics = new ArrayList<RegisteredMetric>();
        registerMetrics();
    }

    private void registerMetrics() {
        this.registeredMetrics.add(new RegisteredMetric("Ciclomatic Complexity",
                "org.metricminer.tasks.metric.cc.CCMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("Fan-out",
                "org.metricminer.tasks.metric.fanout.FanOutMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("Invocation",
                "org.metricminer.tasks.metric.invocation.MethodsInvocationMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("LCom",
                "org.metricminer.tasks.metric.lcom.LComMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("Lines of Code",
                "org.metricminer.tasks.metric.lines.LinesOfCodeMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("Methods Count",
                "org.metricminer.tasks.metric.methods.MethodsCountMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("Tested Methods Finder",
                "org.metricminer.tasks.metric.testedmethods.TestedMethodsFinderMetricFactory"));
    }

    public String getMetricMinerHome() {
        return metricMinerHome;
    }

    public List<RegisteredMetric> getRegisteredMetrics() {
        return registeredMetrics;
    }

    public int getMaxConcurrentTasks() {
        return maxConcurrentTasks;
    }

}
