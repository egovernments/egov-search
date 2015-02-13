package org.egov.search.domain;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RangeTest {

    @Test
    public void shouldConstructRangeWithFrom() {
        Range range = Range.forField("someField").from("10");

        assertThat(range.field(), is("someField"));
        assertThat(range.from(), is("10"));
    }

}