package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class TaskTest {

    @Test
    public void shouldFindAConfigurationEntryForAGivenKey() throws Exception {
        Project project = mock(Project.class);
        Task task = new Task(project, "Task", this.getClass(), 0);
        task.addTaskConfigurationEntry(TaskConfigurationEntryKey.METRICFACTORYCLASS, "someclass");
        String entryValue = task
                .getTaskConfigurationValueFor(TaskConfigurationEntryKey.METRICFACTORYCLASS);
        assertNotNull(entryValue);
        assertEquals(entryValue, "someclass");
    }
}
