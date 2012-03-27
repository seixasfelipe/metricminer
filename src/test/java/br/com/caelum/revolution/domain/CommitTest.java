package br.com.caelum.revolution.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CommitTest {

	@Test
	public void shouldAddAnArtifact() {
		Commit c = new Commit();
		
		c.addArtifact(new Artifact("name", ArtifactKind.CODE));
		
		assertEquals("name", c.getArtifacts().get(0).getName());
		assertEquals(ArtifactKind.CODE, c.getArtifacts().get(0).getKind());
	}
}
