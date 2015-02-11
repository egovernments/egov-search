package org.egov.search.domain;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PageTest {

    @Test
    public void shouldSetDefaults() {
        Page nullPage = Page.NULL;

        assertThat(nullPage.offset(), is(0));
        assertThat(nullPage.size(), is(999999999));
    }

    @Test
    public void shouldSetRightSizeAndOffset() {
        Page page = Page.at(1).ofSize(3);
        assertThat(page.offset(), is(0));
        assertThat(page.size(), is(3));

        page = Page.at(2).ofSize(3);
        assertThat(page.offset(), is(3));
        assertThat(page.size(), is(3));
    }

    @Test
    public void shouldGiveNextPage() {
        Page page = Page.at(1).ofSize(3);

        Page nextPage = page.nextPage();
        assertThat(nextPage.offset(), is(3));
        assertThat(nextPage.size(), is(3));
    }

}