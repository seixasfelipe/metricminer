package org.metricminer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.metricminer.tasks.metric.CalculateMetricTaskFactory;
import org.metricminer.tasks.metric.cc.CCMetricFactory;
import org.metricminer.tasks.metric.lcom.LComMetricFactory;

public class TaskTest {

	@Test
	public void shouldFindAConfigurationEntryForAGivenKey() throws Exception {
		Project project = mock(Project.class);
		Task task = new Task(project, "Task", new CalculateMetricTaskFactory(), 0);
		task.addTaskConfigurationEntry(TaskConfigurationEntryKey.METRICFACTORYCLASS, "someclass");
		String entryValue = task
				.getTaskConfigurationValueFor(TaskConfigurationEntryKey.METRICFACTORYCLASS);
		assertNotNull(entryValue);
		assertEquals(entryValue, "someclass");
	}

	@Test
	public void shouldVerifyThatATaskWillCaclulateAMetric() throws Exception {
		Project project = mock(Project.class);
		Task task = new Task(project, "Task", new CalculateMetricTaskFactory(), 0);
		task.addTaskConfigurationEntry(TaskConfigurationEntryKey.METRICFACTORYCLASS,
				new RegisteredMetric("cc", CCMetricFactory.class).getMetricFactoryClassName());
		assertTrue(task.willCalculate(new RegisteredMetric("some metric", CCMetricFactory.class)));
	}

	@Test
	public void shouldVerifyThatATaskWillNotCaclulateAMetric() throws Exception {
		Project project = mock(Project.class);
		Task task = new Task(project, "Task", new CalculateMetricTaskFactory(), 0);
		task.addTaskConfigurationEntry(TaskConfigurationEntryKey.METRICFACTORYCLASS,
				new RegisteredMetric("cc", LComMetricFactory.class).getMetricFactoryClassName());
		assertFalse(task.willCalculate(new RegisteredMetric("some metric", CCMetricFactory.class)));
	}
}
