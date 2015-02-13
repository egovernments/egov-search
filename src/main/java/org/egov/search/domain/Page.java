package org.egov.search.domain;

public class Page {

    public static final Page NULL = new Page();
    private int pageNumber = 1;
    private int size = 999999999;

    private Page() {
    }

    private Page(int pageNumber, int size) {
        this.pageNumber = pageNumber;
        this.size = size;
    }

    public static Page at(int pageNumber) {
        return new Page(pageNumber, 10);
    }

    public Page ofSize(int size) {
        this.size = size;
        return this;
    }

    public int size() {
        return this.size;
    }

    public int offset() {
        return (pageNumber - 1) * size;
    }

    public Page nextPage() {
        return Page.at(pageNumber + 1).ofSize(size);
    }
}
