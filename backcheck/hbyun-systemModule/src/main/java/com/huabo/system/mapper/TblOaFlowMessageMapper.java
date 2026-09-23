package com.huabo.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblOaFlowMessage;

public interface TblOaFlowMessageMapper extends BaseMapper<TblOaFlowMessage> {

	@Select("SELECT FLOWTYPE FROM TBL_OAFLOW_MESSAGE WHERE ID = #{id}")
	String selectFlowTypeById(String id) throws Exception;

	@SelectProvider(method = "selectPageInfoList",type = TblOaFlowMessageMapperSqlConifg.class)
	IPage<TblOaFlowMessage> selectPageInfoList(IPage<TblOaFlowMessage> page, TblOaFlowMessage condition);

}
