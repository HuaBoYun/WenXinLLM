package com.huabo.audit.util;

import com.github.pagehelper.Page;
import com.hbfk.util.PageInfo;

public class PageInfoUtil<T> {

    public PageInfo<T> parsePageInfo(Page<T> page){
        PageInfo<T> pageInfo = new PageInfo<>();

        pageInfo.setCurrentPage(page.getPageNum());
        pageInfo.setPageSize(page.getPageSize());
        pageInfo.setTlist(page.getResult());
        pageInfo.setTotalRecord((int) page.getTotal());

        return pageInfo;
    }

}
