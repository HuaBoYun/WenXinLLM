package com.huabo.legal.test.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.legal.test.mybatisplus.entity.TestMyBatisPlusEntity;
import com.huabo.legal.test.vo.param.TestQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestMyBatisPlusMapper extends BaseMapper<TestMyBatisPlusEntity> {

	/**
	 * 链表分页  xml 写法
	 * @param queryParam
	 * @return
	 */
	List<TestMyBatisPlusEntity> findList(@Param("queryParam") TestQueryParam queryParam);
}