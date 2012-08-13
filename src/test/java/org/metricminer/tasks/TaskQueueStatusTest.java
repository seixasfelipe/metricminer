package org.metricminer.tasks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.model.TaskBuilder;

public class TaskQueueStatusTest {

    private MetricMinerConfigs mockedConfigs;
    private TaskQueueStatus taskQueueStatus;
    private ThreadInspector mockedThreadInspector;

    @Before
    public void setUp() {
        mockedConfigs = mock(MetricMinerConfigs.class);
        mockedThreadInspector = mock(ThreadInspector.class);
        taskQueueStatus = new TaskQueueStatus(mockedConfigs, mockedThreadInspector);
    }

    @Test
    public void shouldAllowToStartTask() {
        when(mockedConfigs.getMaxConcurrentTasks()).thenReturn(1);
        assertTrue(taskQueueStatus.mayStartTask());
    }
    
    @Test
    public void shouldNotAllowToStartTask() {
        when(mockedConfigs.getMaxConcurrentTasks()).thenReturn(2);
        Thread runningThread1 = mock(Thread.class);
        when(mockedThreadInspector.isRunning(runningThread1)).thenReturn(true);
        Thread runningThread2 = mock(Thread.class);
        when(mockedThreadInspector.isRunning(runningThread2)).thenReturn(true);
        
        taskQueueStatus.addRunningTask(new TaskBuilder().withId(1l).build(), runningThread1);
        taskQueueStatus.addRunningTask(new TaskBuilder().withId(2l).build(), runningThread2);
        
        taskQueueStatus.cleanTasksNotRunning();
        
        assertFalse(taskQueueStatus.mayStartTask());
    }
    
    @Test
    public void shouldAllowToStartTaskIfThreadtIsNotRunning() {
        when(mockedConfigs.getMaxConcurrentTasks()).thenReturn(2);
        Thread threadNotRunning = mock(Thread.class);
        when(mockedThreadInspector.isRunning(threadNotRunning)).thenReturn(true);
        Thread runningThread2 = mock(Thread.class);
        when(mockedThreadInspector.isRunning(threadNotRunning)).thenReturn(false);
        
        taskQueueStatus.addRunningTask(new TaskBuilder().withId(1l).build(), threadNotRunning);
        taskQueueStatus.addRunningTask(new TaskBuilder().withId(2l).build(), runningThread2);
        
        taskQueueStatus.cleanTasksNotRunning();
        
        assertTrue(taskQueueStatus.mayStartTask());
    }

    

}
