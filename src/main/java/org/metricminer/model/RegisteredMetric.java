package org.metricminer.model;

public class RegisteredMetric {
    private String name;
    private String metricFactoryClass;

    public RegisteredMetric() {
    }

    public RegisteredMetric(String name, String metricFactoryClass) {
        this.name = name;
        this.metricFactoryClass = metricFactoryClass;
    }

    public String getName() {
        return name;
    }

    public String getMetricFactoryClassName() {
        return metricFactoryClass;
    }

    public void setMetricFactoryClass(String metricFactoryClass) {
        this.metricFactoryClass = metricFactoryClass;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return metricFactoryClass;
    }

}
