package com.huabo.legal.test.mybatis.mapper;

import com.huabo.legal.test.mybatis.entity.TestMyBatisEntity;
import com.huabo.legal.test.vo.param.TestQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TestMyBatisMapper extends Mapper<TestMyBatisEntity> {

	/**
	 * 链表分页  xml 写法
	 * @param queryParam
	 * @return
	 */
	List<TestMyBatisEntity> findList(@Param("queryParam") TestQueryParam queryParam);
}