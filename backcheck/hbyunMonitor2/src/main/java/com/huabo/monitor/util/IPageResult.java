package com.huabo.monitor.util;

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
public class IPageResult<T> {
	 	
		private Integer total;//总记录数
		private Integer current;//当前页数
		private Integer pages;//总页数
		private Integer size = 15;//每页现实的数量
		private List<T> records;//符合条件的集合
		
		
	public IPageResult<T> buildIpage(PageInfo<T> pageInfo) {
		pageInfo = Optional.ofNullable(pageInfo).orElse(new PageInfo<T>());
		this.total = (int) pageInfo.getTotal();
		this.current = pageInfo.getPageNum();
		this.pages = pageInfo.getPages();
		this.size = pageInfo.getPageSize();
		this.records = pageInfo.getList();
		return this;
	}
	
	public static <T> IPageResult<T> buildNoData() {
		return new IPageResult<>(0, 0, 0, 0, new ArrayList<>(0));
	}
}
