package com.huabo.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblContractTypeof;
import com.huabo.system.mappersql.TblContractTypeofMapperSqlConfig;

public interface TblContractTypeofMapper extends BaseMapper<TblContractTypeof> {


    @SelectProvider(method = "selectPageInfoList", type = TblContractTypeofMapperSqlConfig.class)
    IPage<TblContractTypeof> selectPageInfoList(IPage<TblContractTypeof> page, TblContractTypeof condition);

    @Select("SELECT * FROM TBL_CONTRACT_TYPEOF WHERE typeId = #{typeId}")
    List<TblContractTypeof> selectAllList(String typeId);

    @Update("UPDATE TBL_CONTRACT_TYPEOF SET SETTINGID = #{settingid} WHERE typeId = #{typeId}")
    void updateContractTypeof(TblContractTypeof typeof);
    
    @Select("SELECT * FROM TBL_CONTRACT_TYPEOF WHERE PARENTID IS NULL order by typeid")
 	List<TblContractTypeof> seletAllFatherType() throws Exception;
}
