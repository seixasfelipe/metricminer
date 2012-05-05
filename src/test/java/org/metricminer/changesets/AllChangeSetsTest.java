package org.metricminer.changesets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.metricminer.changesets.AllChangeSets;
import org.metricminer.changesets.ChangeSet;
import org.metricminer.scm.SCM;


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
