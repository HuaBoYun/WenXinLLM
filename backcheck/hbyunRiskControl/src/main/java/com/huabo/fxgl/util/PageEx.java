package com.huabo.fxgl.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;


/**
 * @author kangjx
 * @createTime 2022/7/12
 */
public class PageEx<T> extends Page {

    public PageEx(long page,long limit){
        this.page = page;
        this.limit = limit;
    }

    private long page;
    private long limit;

    private long total;

    private List<T> data;

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getLimit() {
        return this.limit;
    }

    public void setLimit(long limit){
        this.limit = limit;
    }

    @Override
    public long getTotal() {
        return super.getTotal();
    }


    public List<T> getData() {
        return super.getRecords();
    }

}
