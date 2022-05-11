package com.github.childewuque.mds.core.persistenceFramework.hibernate.generic;

import java.io.Serializable;

/**
 * @description:
 * @author: tanhuiren
 * @date: Created in 2020/3/11 下午3:45
 * @modified By:
 */

public class PageModel implements Serializable {
    private static int DEF_PAGE_VIEW_SIZE = 20;
    private int page;
    private int pageSize;
    private int count;

    public PageModel() {
    }

    public PageModel(int page, int pageSize) {
        this.page = page <= 0 ? 1 : page;
        this.pageSize = pageSize <= 0 ? DEF_PAGE_VIEW_SIZE : pageSize;
    }

    public int getPage() {
        return this.page <= 0 ? 1 : this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return this.pageSize <= 0 ? DEF_PAGE_VIEW_SIZE : this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getCount() {
        return (long)this.count;
    }

    public void setCount(int count) {
        this.count = count < 0 ? 0 : count;
        if (this.page > this.getPages()) {
            this.page = this.getPages();
        }

    }

    public int getPages() {
        return (this.count + this.getPageSize() - 1) / this.getPageSize();
    }

    public int getStartNo() {
        return (this.getPage() - 1) * this.getPageSize() + 1;
    }

    public int getEndNo() {
        return Math.min(this.getPage() * this.getPageSize(), this.count);
    }

    public int getPrePageNo() {
        return Math.max(this.getPage() - 1, 1);
    }

    public int getNextPageNo() {
        return Math.min(this.getPage() + 1, this.getPages());
    }
}