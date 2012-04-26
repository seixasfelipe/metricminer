package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ProjectTest {

	@Test
	public void shouldAddTags() {
		Project project = new Project();
		assertEquals(0, project.getTags().size());
		
		project.addTag(new Tag("mvc"));
		assertEquals(new Tag("mvc"), project.getTags().get(0));
	}
	
	@Test
	public void shouldRemoveTags() {
		Project project = new Project();
		project.addTag(new Tag("mvc"));
		project.removeTag("mvc");
		
		assertEquals(0, project.getTags().size());
	}
}
