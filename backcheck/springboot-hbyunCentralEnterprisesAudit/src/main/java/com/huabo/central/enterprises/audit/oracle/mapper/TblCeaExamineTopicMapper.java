package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopic;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaExamineTopicMapper extends Mapper<TblCeaExamineTopic> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaExamineTopic> getList(@Param("param") TblCeaExamineTopicQueryParam param);
}