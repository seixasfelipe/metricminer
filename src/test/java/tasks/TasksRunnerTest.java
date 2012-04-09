package tasks;

import org.junit.Ignore;
import org.junit.Test;

import tasks.runner.TaskRunner;

public class TasksRunnerTest {

	@Test
	@Ignore
	public void shouldRunAValidTask() throws Exception {
		TaskRunner runner = new TaskRunner();
		runner.execute();
	}
}
