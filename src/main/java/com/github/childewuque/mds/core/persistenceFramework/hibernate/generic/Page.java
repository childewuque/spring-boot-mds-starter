package com.github.childewuque.mds.core.persistenceFramework.hibernate.generic;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: tanhuiren
 * @date: Created in 2020/3/11 下午3:44
 * @modified By:
 */


public class Page<T> implements Serializable {
    private static final long serialVersionUID = -1348961313845519580L;
    private List<T> result;
    private PageModel pageModel;

    public Page() {
    }

    public Page(List lstResult, PageModel pageModel) {
        this.result = lstResult;
        this.pageModel = pageModel;
    }

    public <T> List getResult() {
        return this.result;
    }

    public void setResult(List<T> lstResult) {
        this.result = lstResult;
    }

    public PageModel getPageBean() {
        return this.pageModel;
    }

    public void setPageBean(PageModel pageModel) {
        this.pageModel = pageModel;
    }
}

