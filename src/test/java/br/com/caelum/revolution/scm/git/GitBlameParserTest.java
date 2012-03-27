package br.com.caelum.revolution.scm.git;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.revolution.scm.git.GitBlameParser;

public class GitBlameParserTest {

	@Test
	public void shouldGetHashFromARegularBlame() {
		String line = "3cd4ab40cba80761165ca0ecf01ec427b6d6e150 (Mauricio Aniche 2011-04-15 16:21:39 -0300 20)                 } catch (Exception e) {";
		
		String hash = new GitBlameParser().getHash(line);
		
		assertEquals("3cd4ab40cba80761165ca0ecf01ec427b6d6e150", hash);
	}
}
