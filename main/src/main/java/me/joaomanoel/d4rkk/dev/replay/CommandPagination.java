/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.ArrayList;
import java.util.List;

public class CommandPagination<T> {
    private List<T> content;
    private int elementsPerPage;

    public CommandPagination(List<T> content, int elementsPerPage) {
        this.content = content;
        this.elementsPerPage = elementsPerPage;
    }

    public void printPage(int page, IPaginationExecutor<T> executor) {
        for (T element : this.getElementsFor(page)) {
            executor.print(element);
        }
    }

    public List<T> getElementsFor(int page) {
        if (page <= 0 || page > this.getPages()) {
            return new ArrayList();
        }
        int startIndex = (page - 1) * this.elementsPerPage;
        int endIndex = page * this.elementsPerPage;
        return this.content.subList(startIndex, endIndex <= this.content.size() ? endIndex : this.content.size());
    }

    public int getPages() {
        return (int)Math.ceil((double)this.content.size() / (double)this.elementsPerPage);
    }

    public void addElement(T element) {
        if (!this.content.contains(element)) {
            this.content.add(element);
        }
    }

    public int getElementsPerPage() {
        return this.elementsPerPage;
    }
}

