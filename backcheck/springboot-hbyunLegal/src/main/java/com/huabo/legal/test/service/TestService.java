package com.huabo.legal.test.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.test.vo.param.TestQueryParam;

public interface TestService {

	/**
	 * mybatis 单表查询分页
	 * @param queryParam
	 * @return
	 */
	JsonBean getMybatisSinglePageList(TestQueryParam queryParam);

	/**
	 * mybatis 链表分页查询
	 * @param queryParam
	 * @return
	 */
	JsonBean getMybatisDoublePageList(TestQueryParam queryParam);

	/**
	 * mybatis-plus 单表分页查询
	 * @param queryParam
	 * @return
	 */
	JsonBean getMybatisPlusSinglePageList(TestQueryParam queryParam);

	/**
	 * mybatis-plus 链表分页查询
	 * @param testQueryParam
	 * @return
	 */
	JsonBean getMybatisPlusDoublePageList(TestQueryParam testQueryParam);
}
