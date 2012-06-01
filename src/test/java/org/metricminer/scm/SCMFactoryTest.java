package org.metricminer.scm;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.metricminer.config.project.Config;
import org.metricminer.config.project.Configs;
import org.metricminer.scm.git.Git;

public class SCMFactoryTest {
	@Test
	public void shouldBuildGit() {
		Config config = mock(Config.class);
		when(config.asString(Configs.SCM)).thenReturn("org.metricminer.scm.git.GitFactory");
		
		assertTrue(new SCMFactory().basedOn(config).getClass() == Git.class);
	}
	
	@Test(expected=SCMNotFoundException.class)
	public void shouldWarnIfInvalidSCM() {
		Config config = mock(Config.class);
		when(config.asString(Configs.SCM)).thenReturn("invalid-scm");
		
		new SCMFactory().basedOn(config);		
	}

}
