package br.com.caelum.revolution.scm;

import org.junit.Test;

import br.com.caelum.revolution.config.Config;
import br.com.caelum.revolution.config.Configs;
import br.com.caelum.revolution.scm.SCMFactory;
import br.com.caelum.revolution.scm.SCMNotFoundException;
import br.com.caelum.revolution.scm.git.Git;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SCMFactoryTest {
	@Test
	public void shouldBuildGit() {
		Config config = mock(Config.class);
		when(config.asString(Configs.SCM)).thenReturn("br.com.caelum.revolution.scm.git.GitFactory");
		
		assertTrue(new SCMFactory().basedOn(config).getClass() == Git.class);
	}
	
	@Test(expected=SCMNotFoundException.class)
	public void shouldWarnIfInvalidSCM() {
		Config config = mock(Config.class);
		when(config.asString(Configs.SCM)).thenReturn("invalid-scm");
		
		new SCMFactory().basedOn(config);		
	}

}
