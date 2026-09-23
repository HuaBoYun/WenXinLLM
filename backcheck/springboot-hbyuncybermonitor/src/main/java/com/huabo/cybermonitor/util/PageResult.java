package com.huabo.cybermonitor.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.ActualController;
import com.huabo.cybermonitor.entity.EquityStructure;
import com.huabo.cybermonitor.entity.RiskAssessment;

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
	private Integer currentPage;//当前页数
	private Integer pageNumber;//当前页数（兼容字段）
	private Integer totalPage;//总页数
	private Integer pageSize = 15;//每页现实的数量
	private List<T> tlist;//符合条件的集合

	public PageResult<T> build(PageInfo<T> pageInfo) {
		pageInfo = Optional.ofNullable(pageInfo).orElse(new PageInfo<T>());
		this.totalRecord = (int) pageInfo.getTotalRecord();
		this.currentPage = pageInfo.getCurrentPage();
		this.pageNumber = pageInfo.getCurrentPage(); // 兼容字段
		this.totalPage = pageInfo.getTotalPage();
		this.pageSize = pageInfo.getPageSize();
		this.tlist = pageInfo.getTlist();
		return this;
	}

	public static <T> PageResult<T> buildNoData() {
		PageResult<T> result = new PageResult<T>();
		result.setTotalRecord(0);
		result.setCurrentPage(0);
		result.setPageNumber(0);
		result.setTotalPage(0);
		result.setPageSize(0);
		result.setTlist(new ArrayList<T>(0));
		return result;
	}

	public PageResult(Integer total, List<T> list) {
		this.totalRecord = total;
		this.tlist = list;
	}

	/**
	 * 从MyBatis-Plus Page对象构建PageResult
	 */
	public static <T> PageResult<T> of(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
		PageResult<T> result = new PageResult<>();
		result.setTotalRecord((int) page.getTotal());
		result.setCurrentPage((int) page.getCurrent());
		result.setPageNumber((int) page.getCurrent());
		result.setTotalPage((int) page.getPages());
		result.setPageSize((int) page.getSize());
		result.setTlist(page.getRecords());
		return result;
	}

	/**
	 * 从列表和总数构建PageResult
	 */
	public static <T> PageResult<T> of(List<T> list, long total, int pageNumber, int pageSize) {
		PageResult<T> result = new PageResult<>();
		result.setTotalRecord((int) total);
		result.setCurrentPage(pageNumber);
		result.setPageNumber(pageNumber);
		result.setTotalPage(pageSize > 0 ? (int) Math.ceil((double) total / pageSize) : 0);
		result.setPageSize(pageSize);
		result.setTlist(list);
		return result;
	}

	/**
	 * 从列表和总数构建PageResult (Long版本)
	 */
	public static <T> PageResult<T> of(List<T> list, Long total) {
		PageResult<T> result = new PageResult<>();
		result.setTotalRecord(total != null ? total.intValue() : 0);
		result.setTlist(list);
		return result;
	}
}
