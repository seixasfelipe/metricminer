package org.metricminer.integrationtests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;

import org.junit.Test;
import org.metricminer.projectconfig.MapConfig;
import org.metricminer.scm.SCM;
import org.metricminer.scm.git.GitFactory;


public class GitTest {

	@Test
	public void shouldBlameLine() throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		String repoPath = new File( "." ).getCanonicalPath() + "/src/test/resources/test-repository";
		map.put("scm.repository", repoPath);
		SCM git = new GitFactory().build(new MapConfig(map));
		
		String commmitId = "f8aa2621a180a87d6d5cf672547ab64520674836";
		String author = git.blame(commmitId, "SCMFactoryTest.java", 1);
		assertEquals("Francisco Sokol", author);
	}
}
