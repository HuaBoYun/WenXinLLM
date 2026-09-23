package com.global.treasurer.util;

import com.github.pagehelper.PageInfo;
// import lombok.AllArgsConstructor; // 已移除,使用手动编写的getter/setter
// import lombok.Data; // 已移除,使用手动编写的getter/setter
// import lombok.NoArgsConstructor; // 已移除,使用手动编写的getter/setter

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 分页工具类
 * @author wuqian
 * @param <T>
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除,使用手动编写的getter/setter
// @NoArgsConstructor // 已移除,使用手动编写的getter/setter
public class PageResult<T> {
	private Integer totalRecord;//总记录数
	private Integer currentPage;//当前页数
	private Integer totalPage;//总页数
	private Integer pageSize = 15;//每页现实的数量
	private List<T> tlist;//符合条件的集合

	// 手动添加的构造函数
	public PageResult() {
	}

	public PageResult(Integer totalRecord, Integer currentPage, Integer totalPage, Integer pageSize, List<T> tlist) {
		this.totalRecord = totalRecord;
		this.currentPage = currentPage;
		this.totalPage = totalPage;
		this.pageSize = pageSize;
		this.tlist = tlist;
	}

	public PageResult<T> build(PageInfo<T> pageInfo) {
		pageInfo = Optional.ofNullable(pageInfo).orElse(new PageInfo<T>());
		this.totalRecord = (int) pageInfo.getTotal();
		this.currentPage = pageInfo.getPageNum();
		this.totalPage = pageInfo.getPages();
		this.pageSize = pageInfo.getPageSize();
		this.tlist = pageInfo.getList();
		return this;
	}

	public static <T> PageResult<T> buildNoData() {
		return new PageResult<T>(0, 0, 0, 0, new ArrayList<T>(0));
	}

	// 完整的getter和setter方法
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

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getTlist() {
		return tlist;
	}

	public void setTlist(List<T> tlist) {
		this.tlist = tlist;
	}

	// 别名方法，用于兼容不同的命名习惯
	public void setRecords(List<T> records) {
		this.tlist = records;
	}

	public void setTotal(long total) {
		this.totalRecord = (int) total;
	}

	public void setSize(long size) {
		this.pageSize = (int) size;
	}

	public void setCurrent(long current) {
		this.currentPage = (int) current;
	}

	public void setPages(long pages) {
		this.totalPage = (int) pages;
	}

	public List<T> getRecords() {
		return this.tlist;
	}

	public long getTotal() {
		return this.totalRecord;
	}

	public long getSize() {
		return this.pageSize;
	}

	public long getCurrent() {
		return this.currentPage;
	}

	public long getPages() {
		return this.totalPage;
	}
}
