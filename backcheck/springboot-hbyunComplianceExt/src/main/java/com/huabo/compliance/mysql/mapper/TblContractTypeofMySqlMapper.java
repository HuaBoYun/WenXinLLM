package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblContractTypeofMySql;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TblContractTypeofMySqlMapper extends BaseMapper<TblContractTypeofMySql> {


    @SelectProvider(method = "selectPageInfoList", type = TblContractTypeofMapperSqlMySqlConfig.class)
    List<TblContractTypeofMySql> selectPageInfoList(PageInfo<TblContractTypeofMySql> pageInfo);

    @SelectProvider(method = "selectPageInfoCount", type = TblContractTypeofMapperSqlMySqlConfig.class)
    Integer selectPageInfoCount(PageInfo<TblContractTypeofMySql> pageInfo);

    @Select("SELECT * FROM TBL_CONTRACT_TYPEOF WHERE typeId = #{typeId}")
    List<TblContractTypeofMySql> selectAllList(String typeId);

    @Update("UPDATE TBL_CONTRACT_TYPEOF SET SETTINGID = #{settingid} WHERE typeId = #{typeId}")
    void updateContractTypeof(TblContractTypeofMySql typeof);
}
