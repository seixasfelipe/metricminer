package org.metricminer.integration.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.metricminer.config.project.MapConfig;
import org.metricminer.scm.SCM;
import org.metricminer.scm.git.GitFactory;


public class GitIntegrationTest {

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
	
	@Test
	public void shouldBlameAFile() throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		String repoPath = new File( "." ).getCanonicalPath() + "/src/test/resources/test-repository";
		map.put("scm.repository", repoPath);
		SCM git = new GitFactory().build(new MapConfig(map));
		
		String commmitId = "f8aa2621a180a87d6d5cf672547ab64520674836";
		Map<Integer, String> blamedLines = git.blame(commmitId, "SCMFactoryTest.java");
		assertFalse(blamedLines.isEmpty());
		for (Integer line : blamedLines.keySet()) {
			assertEquals("Francisco Sokol", blamedLines.get(line));
		}
	}
	
	@Test
	public void shouldBlameAFileWithTwoAuthors() throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		String repoPath = new File( "." ).getCanonicalPath() + "/src/test/resources/test-repository";
		map.put("scm.repository", repoPath);
		SCM git = new GitFactory().build(new MapConfig(map));
		
		String commmitId = "11a7aa648abec5014bd71377bf02fb3f5003f9cc";
		Map<Integer, String> blamedLines = git.blame(commmitId, "SCMFactoryTest.java");
		assertFalse(blamedLines.isEmpty());
		for (int line = 1; line < 29; line++) {
			assertEquals("Francisco Sokol", blamedLines.get(line));
		}
		for (int line = 30; line < 32; line++) {
			assertEquals("Francisco Zigmund Sokol", blamedLines.get(line));
		}
		assertEquals("Francisco Zigmund Sokol", blamedLines.get(32));
	}
}
