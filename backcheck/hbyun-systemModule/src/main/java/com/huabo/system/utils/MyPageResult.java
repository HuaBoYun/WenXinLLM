package com.huabo.system.utils;

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
public class MyPageResult<T> {

	private Integer totalRecord;//总记录数
	private Integer currentPage;//当前页数
	private Integer totalPage;//总页数
	private Integer pageSize = 15;//每页现实的数量
	private List<T> tlist;//符合条件的集合

	public MyPageResult<T> build(PageInfo<T> pageInfo) {
		pageInfo = Optional.ofNullable(pageInfo).orElse(new PageInfo<T>());
		this.totalRecord = (int) pageInfo.getTotal();
		this.currentPage = pageInfo.getPageNum();
		this.totalPage = pageInfo.getPages();
		this.pageSize = pageInfo.getPageSize();
		this.tlist = pageInfo.getList();
		return this;
	}

	public static <T> MyPageResult<T> buildNoData() {
		return new MyPageResult<>(0, 0, 0, 0, new ArrayList<>(0));
	}
}
