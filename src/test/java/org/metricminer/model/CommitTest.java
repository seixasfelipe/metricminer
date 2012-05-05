package org.metricminer.model;

import static org.junit.Assert.assertEquals;


import org.junit.Test;
import org.metricminer.model.Artifact;
import org.metricminer.model.ArtifactKind;
import org.metricminer.model.Commit;

public class CommitTest {

	@Test
	public void shouldAddAnArtifact() {
		Commit c = new Commit();
		
		c.addArtifact(new Artifact("name", ArtifactKind.CODE));
		
		assertEquals("name", c.getArtifacts().get(0).getName());
		assertEquals(ArtifactKind.CODE, c.getArtifacts().get(0).getKind());
	}
}
