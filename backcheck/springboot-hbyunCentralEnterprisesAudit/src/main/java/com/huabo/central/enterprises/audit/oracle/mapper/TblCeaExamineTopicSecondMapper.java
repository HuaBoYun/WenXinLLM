package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopicSecond;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicSecondQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaExamineTopicSecondMapper extends Mapper<TblCeaExamineTopicSecond> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaExamineTopicSecond> getList(@Param("param") TblCeaExamineTopicSecondQueryParam param);
}