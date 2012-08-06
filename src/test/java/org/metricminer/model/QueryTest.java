package org.metricminer.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class QueryTest {

    @Test
    public void shouldNotAllowOtherUserToUpdateQuery() {
        Query query = new Query();
        User author = mock(User.class);
        User otherUser = mock(User.class);
        when(author.getId()).thenReturn(1L);
        when(otherUser.getId()).thenReturn(2L);
        query.setAuthor(author);
        
        assertFalse(query.isAllowedToEdit(otherUser));
        assertTrue(query.isAllowedToEdit(author));
    }

}
