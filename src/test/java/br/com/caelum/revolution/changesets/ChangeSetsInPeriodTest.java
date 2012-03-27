package br.com.caelum.revolution.changesets;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.revolution.changesets.ChangeSet;
import br.com.caelum.revolution.changesets.ChangeSetsInPeriod;
import br.com.caelum.revolution.scm.SCM;

public class ChangeSetsInPeriodTest {
	private Calendar someNewDate;
	private Calendar someOldDate;
	private Calendar startPeriod;
	private Calendar endPeriod;
	private Calendar someNewDate2;
	
	@Before
	public void setUp(){
		someNewDate = Calendar.getInstance();
		someNewDate.set(2010, 05, 20);

		someNewDate2 = Calendar.getInstance();
		someNewDate2.set(2010, 05, 21);
		
		someOldDate = Calendar.getInstance();
		someOldDate.set(2009, 05, 20);
		
		startPeriod = Calendar.getInstance();
		endPeriod = Calendar.getInstance();
	}

	@Test
	public void shouldReturnOnlyChangeSetsInPeriod() {
		SCM scm = mock(SCM.class);
		
		List<ChangeSet> changeSets = someChangeSets();
		when(scm.getChangeSets()).thenReturn(changeSets);
		
		startPeriod.set(2010, 05, 01);
		endPeriod.set(2010, 06, 01);
		
		ChangeSetsInPeriod collection = new ChangeSetsInPeriod(scm, startPeriod, endPeriod);
		
		List<ChangeSet> list = collection.get();
		assertEquals(1, list.size());
		
		ChangeSet cs1 = list.get(0);
		assertEquals("abcd", cs1.getId());
		
		verify(scm).getChangeSets();
	}
	

	@Test
	public void shouldReturnAllChangeSetsInPeriod() {
		SCM scm = mock(SCM.class);
		
		List<ChangeSet> changeSets = someChangeSetsInPeriod();
		when(scm.getChangeSets()).thenReturn(changeSets);
		
		startPeriod.set(2010, 05, 01);
		endPeriod.set(2010, 06, 01);
		
		ChangeSetsInPeriod collection = new ChangeSetsInPeriod(scm, startPeriod, endPeriod);
		List<ChangeSet> list = collection.get();

		assertEquals(2, list.size());
		
		ChangeSet cs1 = list.get(0);
		assertEquals("abcd", cs1.getId());
		
		ChangeSet cs2 = list.get(1);
		assertEquals("efgh", cs2.getId());
		
		verify(scm).getChangeSets();
	}
	
	private List<ChangeSet> someChangeSets() {
		List<ChangeSet> changeSets = new ArrayList<ChangeSet>();
		changeSets.add(new ChangeSet("abcd", someNewDate));
		changeSets.add(new ChangeSet("efgh", someOldDate));
		return changeSets;
	}
	

	private List<ChangeSet> someChangeSetsInPeriod() {
		List<ChangeSet> changeSets = new ArrayList<ChangeSet>();
		changeSets.add(new ChangeSet("abcd", someNewDate));
		changeSets.add(new ChangeSet("efgh", someNewDate2));
		return changeSets;
	}

}
