package com.huabo.financialdata.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hbfk.util.PageInfo;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PageBean {

    //指定的 传递参数的
    private int pageSize = 15; //每页显示多少条

    private int currentPage;//当前页

    private List recordList;//本页的数据列表

    private int recordCount; //总记录数

    // 跳到上一页
    public int previousPage(int cPage) {
        System.out.println(cPage);
        if (cPage == 1) {
            cPage = 0;
        }
        return currentPage = (cPage - 1) > 0 ? (cPage - 1) : 1;
    }

    // 跳到下一页  
    public int nextPage(int cPage) {
        if (cPage != getPages()) {
            currentPage = cPage + 1;
        } else {
            currentPage = cPage;
        }
        if (currentPage * pageSize > this.recordCount) {
            return lastPage();
        } else {
            return currentPage;
        }

    }

    // 跳到最后一页  
    public int lastPage() {
        if (this.recordCount % pageSize == 0) {
            return currentPage = this.recordCount / pageSize;
        } else {
            return currentPage = this.recordCount / pageSize + 1;
        }
    }

    // 得到总页数
    public int getPages() {
        if (this.recordCount % pageSize == 0)
            return this.recordCount / this.pageSize;
        else
            return this.recordCount / this.pageSize + 1;
    }

    public List<Integer> getOptionList() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; getPages() > i; i++) {
            list.add(i);
        }
        return list;
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public List getRecordList() {
        return recordList;
    }

    public void setRecordList(List recordList) {
        this.recordList = recordList;
    }

    /**
     * @param pageInfo
     * @return
     */
    public void switchPageInfo(PageInfo pageInfo) {
        //每页显示多少条
        pageSize = pageInfo.getPageSize();
        //当前页
        currentPage = pageInfo.getCurrentPage();
        //本页的数据列表
        recordList = pageInfo.getTlist();
        //总记录数
        recordCount = ((int) pageInfo.getTotalRecord());
        //return pageBean;
    }

    public void switchPageInfo1(String tree, Class<T> clazz) {
        PageInfo<T> pageInfo = JSON.parseObject(tree, PageInfo.class);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(tree);
        String result = jsonObject.getString("list");
        List<T> list = JSON.parseObject(result, new TypeReference<List<T>>() {
        });
        pageInfo.setTlist(list);
        //每页显示多少条
        pageSize = pageInfo.getPageSize();
        //当前页
        currentPage = pageInfo.getCurrentPage();
        //本页的数据列表
        recordList = pageInfo.getTlist();
        //总记录数
        recordCount = ((int) pageInfo.getTotalRecord());
        //return pageBean;
    }

}
