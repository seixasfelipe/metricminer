package tasks;

import org.junit.Ignore;
import org.junit.Test;

import tasks.runner.TasksRunner;

public class TasksRunnerTest {

	@Test
	@Ignore
	public void shouldRunAValidTask() throws Exception {
		TasksRunner runner = new TasksRunner();
		runner.execute();
	}
}
