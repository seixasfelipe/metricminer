package org.metricminer.tasks.projectmetric;

import static org.mockito.Mockito.mock;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.junit.Test;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.model.Task;
import org.metricminer.model.TaskBuilder;
import org.metricminer.tasks.RunnableTask;

public class CalculateProjectMetricTaskFactoryTest {

    @Test
    public void shouldBuildATask() {
        CalculateProjectMetricTaskFactory factory = new CalculateProjectMetricTaskFactory();
        Task task = new TaskBuilder().withRunnableTaskFactory(factory).build();
        Session session = mock(Session.class);
        StatelessSession statelessSession = mock(StatelessSession.class);
        MetricMinerConfigs config = mock(MetricMinerConfigs.class);
        RunnableTask runnableTask = factory.build(task, session, statelessSession, config);
        
        runnableTask.getClass().equals(CalculateProjectMetricTask.class);
    }

}
