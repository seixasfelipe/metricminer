package br.com.caelum.revolution.scm.git;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import br.com.caelum.revolution.changesets.ChangeSet;
import br.com.caelum.revolution.scm.git.GitLogParser;


public class GitLogParserTest {

	private String mediumLog =  "commit 4cb75005f86ce1ba7bb5164ca1e0918693a22e42\n"+
								"Author: Mauricio Aniche <some@email.com>\n" +
								"Date:   2010-05-24 23:50:53 -0300\n"+
								"\n"+
								"    Teste\n"+
								"\n"+
								"commit 7e4f749e8d321a0c90a3fa403301211d6b0942b0\n"+
								"Author: Mauricio Aniche <some@email.com>\n"+
								"Date:   2010-05-24 23:52:01 -0300\n"+
								"\n"+
								"    Teste2";
	
	@Test
	public void shouldGetAllSHAs() throws Exception {
		List<ChangeSet> shas = new GitLogParser().parse(mediumLog);
	
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		assertEquals(2, shas.size());
		assertEquals("4cb75005f86ce1ba7bb5164ca1e0918693a22e42", shas.get(0).getId());
		assertEquals(sf.format(shas.get(0).getTime().getTime()), "2010-05-24 23:50:53");
		assertEquals("7e4f749e8d321a0c90a3fa403301211d6b0942b0", shas.get(1).getId());
		assertEquals(sf.format(shas.get(1).getTime().getTime()), "2010-05-24 23:52:01");
	}
	
	@Test
	public void shouldIgnoreTrashContent() throws Exception {
		List<ChangeSet> shas = new GitLogParser().parse(mediumLog + "\r\n????");
		
		assertEquals(2, shas.size());
		assertEquals("4cb75005f86ce1ba7bb5164ca1e0918693a22e42", shas.get(0).getId());
		assertEquals("7e4f749e8d321a0c90a3fa403301211d6b0942b0", shas.get(1).getId());		
	}
}
