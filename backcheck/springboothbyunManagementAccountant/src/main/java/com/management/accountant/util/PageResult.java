package com.management.accountant.util;

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
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {

	private Integer totalRecord;//总记录数
	private Integer pageNo;//当前页数
	private Integer totalPage;//总页数
	private Integer pageSize = 15;//每页现实的数量
	private List<T> tlist;//符合条件的集合

	// 兼容旧字段名
	private Integer total;//总记录数
	private Integer pageNum;//当前页数
	private List<T> list;//符合条件的集合

	public PageResult<T> build(PageInfo<T> pageInfo) {
		pageInfo = Optional.ofNullable(pageInfo).orElse(new PageInfo<T>());
		this.total = (int) pageInfo.getTotal();
		this.totalRecord = this.total;
		this.pageNum = pageInfo.getPageNum();
		this.pageNo = this.pageNum;
		this.totalPage = pageInfo.getPages();
		this.pageSize = pageInfo.getPageSize();
		this.list = pageInfo.getList();
		this.tlist = this.list;
		return this;
	}

	public static <T> PageResult<T> buildNoData() {
		PageResult<T> result = new PageResult<>();
		result.totalRecord = 0;
		result.pageNo = 0;
		result.totalPage = 0;
		result.pageSize = 0;
		result.tlist = new ArrayList<>(0);
		result.total = 0;
		result.pageNum = 0;
		result.list = new ArrayList<>(0);
		return result;
	}

	// setter方法：双向同步新旧字段名
	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
		this.total = totalRecord;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
		this.pageNum = pageNo;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setTlist(List<T> tlist) {
		this.tlist = tlist;
		this.list = tlist;
	}

	public void setTotal(Integer total) {
		this.total = total;
		this.totalRecord = total;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
		this.pageNo = pageNum;
	}

	public void setList(List<T> list) {
		this.list = list;
		this.tlist = list;
	}

	// Getter方法
	public Integer getTotal() {
		return total;
	}

	public List<T> getList() {
		return list;
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public List<T> getTlist() {
		return tlist;
	}

	public Integer getPageNum() {
		return pageNum;
	}
}
