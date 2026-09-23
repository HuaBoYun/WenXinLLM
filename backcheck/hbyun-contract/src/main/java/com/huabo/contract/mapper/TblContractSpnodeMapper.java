package com.huabo.contract.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractSpnode;
import com.huabo.contract.mappersql.TblContractSpnodeMapperSqlConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-22
 */
public interface TblContractSpnodeMapper extends BaseMapper<TblContractSpnode> {

	@SelectProvider(type = TblContractSpnodeMapperSqlConfig.class,method = "findListByXdf")
	IPage<TblContractSpnode> findListByXdf(IPage<TblContractSpnode> page, String budgetid) throws Exception;

	@UpdateProvider(type = TblContractSpnodeMapperSqlConfig.class,method = "updateBySpnode")
	void updateBySpnode(TblContractSpnode spNode);

	@InsertProvider(type = TblContractSpnodeMapperSqlConfig.class,method = "saveBySpnode")
	void saveBySpnode(TblContractSpnode spNode);

}
