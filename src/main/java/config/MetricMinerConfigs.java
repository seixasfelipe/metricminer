package config;

import java.util.ArrayList;
import java.util.List;

import model.RegisteredMetric;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class MetricMinerConfigs {
    private String metricMinerHome;
    private List<RegisteredMetric> registeredMetrics;
    
    public MetricMinerConfigs() {
        this.metricMinerHome = "/home/csokol/ime/tcc/MetricMinerHome";
        this.registeredMetrics = new ArrayList<RegisteredMetric>();
        this.registeredMetrics.add(new RegisteredMetric("Ciclomatic Complexity", "tasks.metric.cc.CCMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("Fan-out", "tasks.metric.fanout.FanOutMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("Invocation",
                "tasks.metric.invocation.MethodsInvocationMetricFactory"));
    }

    public String getMetricMinerHome() {
        return metricMinerHome;
    }

    public List<RegisteredMetric> getRegisteredMetrics() {
        return registeredMetrics;
    }

}
