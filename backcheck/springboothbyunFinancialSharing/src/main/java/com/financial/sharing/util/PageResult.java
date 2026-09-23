package com.financial.sharing.util;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 分页工具类
 * @author wuqian
 * @param <T>
 */
@Data
@NoArgsConstructor
public class PageResult<T> {

	private Integer totalRecord;//总记录数
	private Integer currentPage;//当前页数
	private Integer totalPage;//总页数
	private Integer pageSize = 15;//每页现实的数量
	private List<T> tlist;//符合条件的集合

	// 全参数构造函数
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

	// 手动添加setter方法，确保兼容性
	public void setTlist(List<T> tlist) {
		this.tlist = tlist;
	}

	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	// 手动添加getter方法，确保兼容性
	public List<T> getTlist() {
		return this.tlist;
	}

	public Integer getTotalRecord() {
		return this.totalRecord;
	}

	public Integer getCurrentPage() {
		return this.currentPage;
	}

	public Integer getTotalPage() {
		return this.totalPage;
	}

	public Integer getPageSize() {
		return this.pageSize;
	}

	// 兼容方法 - 为了与新代码兼容
	public void setPageNo(Integer pageNo) {
		this.currentPage = pageNo;
	}

	public Integer getPageNo() {
		return this.currentPage;
	}

	// 兼容方法
	public void setPageNumber(Integer pageNumber) {
		this.currentPage = pageNumber;
	}

	public Integer getPageNumber() {
		return this.currentPage;
	}

	// 兼容方法 - 为了与新代码兼容
	public List<T> getRecords() {
		return this.tlist;
	}

	public void setRecords(List<T> records) {
		this.tlist = records;
	}

	public long getTotal() {
		return this.totalRecord != null ? this.totalRecord.longValue() : 0L;
	}

	public void setTotal(long total) {
		this.totalRecord = (int) total;
	}

	public long getCurrent() {
		return this.currentPage != null ? this.currentPage.longValue() : 1L;
	}

	public void setCurrent(long current) {
		this.currentPage = (int) current;
	}

	public long getSize() {
		return this.pageSize != null ? this.pageSize.longValue() : 15L;
	}

	public void setSize(long size) {
		this.pageSize = (int) size;
	}

	public long getPages() {
		return this.totalPage != null ? this.totalPage.longValue() : 0L;
	}

	public void setPages(long pages) {
		this.totalPage = (int) pages;
	}

	public boolean isHasNext() {
		return this.currentPage != null && this.totalPage != null && this.currentPage < this.totalPage;
	}

	public boolean isHasPrevious() {
		return this.currentPage != null && this.currentPage > 1;
	}
}
