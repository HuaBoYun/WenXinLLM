package com.huabo.contract.mapper;

import com.huabo.contract.entity.TblContractTypeof;
import com.huabo.contract.mappersql.TblContractTypeofMapperSqlConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口 合同流程
 * </p>
 *
 * @author huabo
 * @since 2022-03-11
 */
public interface TblContractTypeofMapper extends BaseMapper<TblContractTypeof> {

    @SelectProvider(type=TblContractTypeofMapperSqlConfig.class,method="findAllList")
    List<TblContractTypeof> findAllList(BigDecimal orgid, Integer parentId);

    //@Select("SELECT * FROM TBL_CONTRACT_TYPEOF WHERE ORGID= #{orgid} AND PARENTID IS NULL order by typeid")
    @Select("SELECT * FROM TBL_CONTRACT_TYPEOF WHERE PARENTID IS NULL order by typeid")
    List<TblContractTypeof> selectAllList();//BigDecimal orgid

    @SelectProvider(type=TblContractTypeofMapperSqlConfig.class,method="selectLedgerListPageInfo")
    IPage<TblContractTypeof> selectLedgerListPageInfo(IPage<TblContractTypeof> page, TblContractTypeof condition);

   // @Select("SELECT * FROM TBL_CONTRACT_TYPEOF WHERE ORGID = #{orgid} AND TYPENAME IN (#{typeNameStr})")
    @SelectProvider(type=TblContractTypeofMapperSqlConfig.class,method="selectRepeatTypeName")
    List<TblContractTypeof> selectRepeatTypeName(BigDecimal orgid, String typeNameStr);

    @InsertProvider(type=TblContractTypeofMapperSqlConfig.class,method="saveContractTypeof")
    void saveContractTypeof(TblContractTypeof typeof);

    @Select("SELECT COUNT(*) FROM TBL_CYHW_UNIT WHERE ORGID = #{orgid} AND CONTRACTTYPE = (SELECT TYPENAME FROM TBL_CONTRACT_TYPEOF WHERE TYPEID = #{typeId})")
    Integer selectContractCountByContractType(BigDecimal orgid, String typeId);

    @Select("SELECT COUNT(*) FROM TBL_CONTRACT_TYPEOF WHERE TYPENAME = #{typeName} AND ORGID = #{orgid} AND TYPEID != #{typeId}")
    Integer selectRepeatCount(String typeName, BigDecimal orgid, String typeId);

    @Select("SELECT * FROM TBL_CONTRACT_TYPEOF WHERE TYPEID = #{typeId}")
    TblContractTypeof selectEntityById(String typeId);

    @Update("UPDATE TBL_CONTRACT_TYPEOF SET TYPENAME = #{typename},UPDATESTAFF = #{updatestaff}, UPDATETIME = #{updatetime} WHERE TYPEID = #{typeid}")
    void updateContractTypeof(TblContractTypeof type);

    @Select("SELECT COUNT(*) FROM TBL_CYHW_UNIT WHERE ORGID = #{orgid} AND CONTRACTTYPE IN (SELECT TYPENAME FROM TBL_CONTRACT_TYPEOF WHERE TYPEID = #{typeId} UNION SELECT TYPENAME FROM TBL_CONTRACT_TYPEOF WHERE PARENTID = #{typeId})")
    Integer selectContractCountByType(BigDecimal orgid, String typeId);

    @Delete("DELETE FROM TBL_CONTRACT_TYPEOF WHERE PARENTID = #{typeId}")
    void deleteContractTypeByParentId(String typeId);

    @Delete("DELETE FROM TBL_CONTRACT_TYPEOF WHERE TYPEID = #{typeId}")
    void deleteContractTypeByTypeId(String typeId);
    
    @Select("SELECT * FROM TBL_CONTRACT_TYPEOF WHERE TYPENAME = #{name} ")
    TblContractTypeof findByName(String name, String orgid);

    @Select("SELECT * FROM TBL_CONTRACT_TYPEOF WHERE PARENTID = #{typeid} order by typeid")
    List<TblContractTypeof> selectListByParent(BigDecimal typeid);

    @Select("SELECT TYPENAME FROM TBL_CONTRACT_TYPEOF WHERE PARENTID IS NOT NULL AND TYPENAME IS NOT NULL")
	List<String> selectAllTypeNameByOrgId() throws Exception;

}
