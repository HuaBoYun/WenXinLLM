package com.huabo.audit.util;

import java.util.List;

/**
 * 分页工具类
 * @author lihongpeng
 * @param <T>
 */
public class PageInfo<T> {
	
	private Integer totalRecord;//总记录数
	private Integer currentPage;//当前页数
	private Integer totalPage;//总页数
	private Integer currentRecord;//起始查询下表
	private T condition;//查询条件
	private List<T> tlist;//符合条件的集合
	private Integer pageSize = 15;//每页现实的数量
	private String sqlStr;//查询条件拼接
	
	
	public String getSqlStr() {
		return sqlStr;
	}
	public void setSqlStr(String sqlStr) {
		this.sqlStr = sqlStr;
	}
	public Integer getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getCurrentRecord() {
		this.currentRecord= (this.currentPage-1)*this.pageSize;
		return currentRecord;
	}
	public T getCondition() {
		return condition;
	}
	public void setCondition(T condition) {
		this.condition = condition;
	}
	public List<T> getTlist() {
		return tlist;
	}
	public void setTlist(List<T> tlist) {
		this.tlist = tlist;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTotalPage() {
		this.totalPage = this.totalRecord/this.pageSize;
		if(this.totalRecord%this.pageSize!=0){
			this.totalPage++;
		}
		return totalPage;
	}
}
