package org.metricminer.scm.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.metricminer.changesets.ChangeSet;
import org.metricminer.infra.executor.CommandExecutor;
import org.metricminer.scm.SCMException;



public class GitTest {

	private final String output = "some/log/here";
	private final String repository = "/some/path/to/rep";
	private CommandExecutor exec;
	private GitLogParser logParser;
	private GitDiffParser diffParser;
	private GitBlameParser blameParser;
	
	@Before
	public void setUp() {
		exec = mock(CommandExecutor.class);
		logParser = mock(GitLogParser.class);
		diffParser = mock(GitDiffParser.class);
		blameParser = mock(GitBlameParser.class);
	}
	
	//TODO understand and fix it
	@Test @Ignore
	public void shouldReturnChangeSetList() throws Exception {
		List<ChangeSet> csList = aChangeSetList();
		
		when(exec.execute(any(String.class), any(String.class))).thenReturn(output);
		when(logParser.parse(output)).thenReturn(csList);
		
		List<ChangeSet> retrievedList = new Git(repository, logParser, diffParser, blameParser, exec).getChangeSets();
		
		assertSame(retrievedList, csList);
		verify(logParser).parse(output);
		verify(exec, times(2)).execute(any(String.class), any(String.class));
	}
	
	//TODO understand and fix it
	@Test(expected=SCMException.class) 
	public void shouldThrowSCMExceptionIfChangeSetListFails() throws Exception {
		when(exec.execute(any(String.class), any(String.class))).thenThrow(new RuntimeException());
		
		new Git(repository, logParser, diffParser, blameParser, exec).getChangeSets();
	}
	
	//TODO understand and fix it
	@Test @Ignore
	public void shouldGoToASpecificChangeSet() throws Exception {
		String path = new Git(repository, logParser, diffParser,blameParser,exec).goTo("abcd");
		
		assertEquals(repository, path);
		verify(exec, times(3)).execute(any(String.class), any(String.class));		
	}
	
	@Test(expected=SCMException.class)
	public void shouldThrowSCMExceptionIfGetChangeSetFails() throws Exception {
		when(exec.execute(any(String.class), any(String.class))).thenThrow(new RuntimeException());
		
		new Git(repository, logParser, diffParser,blameParser,exec).goTo("123");
	}
	
	@Test @Ignore
	public void shouldParseDiffAndGenerateArtifacts() {
		
	}
	
	@Test @Ignore
	public void shouldReturnBlamedHash() {}

	private List<ChangeSet> aChangeSetList() {
		List<ChangeSet> list = new ArrayList<ChangeSet>();
		list.add(new ChangeSet("1234567890123456789012345678901234567890", Calendar.getInstance()));
		list.add(new ChangeSet("9999999999999999999999999999999999999999", Calendar.getInstance()));
		return list;
	}

    @Test
    public void shouldCleanCDATA() throws Exception {
        when(exec.execute(any(String.class), any(String.class)))
                .thenReturn(
                        "<Commit><commitId>%H</commitId>"
                                + "<author><![CDATA[author]]></author>"
                                + "<email><![CDATA[author@gmal.com]]></email>"
                                + "<date>%ai</date>"
                                + "<message><![CDATA[Don't allow ]]> within ]]> CDATA sections.]]></message>"
                        + "</Commit>");
        new Git(repository, logParser, diffParser, blameParser, exec).detail("123");
    }
    
    @Test
    public void shouldMatchName() throws Exception {
        String out ="^fecdebaa9388cec408eddb0e73267664f0a87b9 (Francisco Sokol 2012-04-26 18:11:55 -0300 1) public class Main {";

        Pattern pattern = Pattern.compile("\\S+\\s+\\(([\\w\\s]+)\\d{4}-\\d{2}-\\d{2}.*\\).*");
        Matcher matcher = pattern.matcher(out);
        matcher.find();
        System.out.println(matcher.group(1));
 
    }
    
}
