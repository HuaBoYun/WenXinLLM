package com.huabo.cybermonitor.common.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName PageResult
 * @Description 分页结果
 * @Author ZiYao
 * @Date 2022/4/12 16:25
 * @Version 1.0
 **/
@ApiModel(description = "分页结果")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总记录数")
    private long total;

    @ApiModelProperty(value = "总页数")
    private long pages;

    @ApiModelProperty(value = "当前页码")
    private long pageNum;

    @ApiModelProperty(value = "每页记录数")
    private long pageSize;

    @ApiModelProperty(value = "数据列表")
    private List<T> list;

    public PageResult(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public PageResult(List<T> list) {
        this.list = list;
        this.total = list.size();
    }

    // Alias methods for compatibility
    public void setTlist(List<T> list) {
        this.list = list;
    }

    public List<T> getTlist() {
        return this.list;
    }

    public void setTotalRecord(long total) {
        this.total = total;
    }

    public long getTotalRecord() {
        return this.total;
    }

    public void setPageNumber(long pageNum) {
        this.pageNum = pageNum;
    }

    public long getPageNumber() {
        return this.pageNum;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageSize() {
        return this.pageSize;
    }
}

