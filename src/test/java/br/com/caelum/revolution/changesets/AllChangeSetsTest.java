package br.com.caelum.revolution.changesets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import br.com.caelum.revolution.changesets.AllChangeSets;
import br.com.caelum.revolution.changesets.ChangeSet;
import br.com.caelum.revolution.scm.SCM;

public class AllChangeSetsTest {

	@Test
	public void shouldIterateIntoAllChangeSets() {
		SCM scm = mock(SCM.class);

		List<ChangeSet> changeSets = someChangeSets();
		when(scm.getChangeSets()).thenReturn(changeSets);

		AllChangeSets collection = new AllChangeSets(scm);

		List<ChangeSet> list = collection.get();

		assertEquals(2, list.size());

		ChangeSet cs1 = list.get(0);
		ChangeSet cs2 = list.get(1);
		assertEquals("abcd", cs1.getId());
		assertEquals("efgh", cs2.getId());
		verify(scm).getChangeSets();
	}

	private List<ChangeSet> someChangeSets() {
		List<ChangeSet> changeSets = new ArrayList<ChangeSet>();
		changeSets.add(new ChangeSet("abcd", Calendar.getInstance()));
		changeSets.add(new ChangeSet("efgh", Calendar.getInstance()));
		return changeSets;
	}
}
