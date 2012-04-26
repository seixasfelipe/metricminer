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
        this.metricMinerHome = "/Users/mauricioaniche/dev/lixo-mm/";
        this.registeredMetrics = new ArrayList<RegisteredMetric>();
        this.registeredMetrics.add(new RegisteredMetric("Ciclomatic Complexity", "tasks.metric.cc.CCMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("Fan-out", "tasks.metric.fanout.FanOutMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("Invocation",
                "tasks.metric.invocation.MethodsInvocationMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("LCom",
                "tasks.metric.lcom.LComMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("Lines of Code",
                "tasks.metric.lines.LinesOfCodeMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("Methods Count",
                "tasks.metric.methods.MethodsCountMetricFactory"));
        this.registeredMetrics.add(new RegisteredMetric("Tested Methods Finder",
                "tasks.metric.testedmethods.TestedMethodsFinderMetricFactory"));

    }

    public String getMetricMinerHome() {
        return metricMinerHome;
    }

    public List<RegisteredMetric> getRegisteredMetrics() {
        return registeredMetrics;
    }

}
