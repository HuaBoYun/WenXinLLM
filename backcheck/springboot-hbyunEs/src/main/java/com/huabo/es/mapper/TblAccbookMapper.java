package com.huabo.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.es.domain.TblAccbook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * TblAccbook表数据库访问层
 *
 * @author : caozhibo
 * @date : 2023-3-22
 */
@Mapper
public interface TblAccbookMapper extends BaseMapper<TblAccbook> {

    /**
     * acctid 去重
     *
     * @return
     */
    List<String> queryDistinctByAcctid();

    /**
     * 检查MySQL表是否存在
     *
     * @param schemaName
     * @param tableName
     * @return
     */
    Integer checkMysqlTableExists(@Param("schemaName") String schemaName, @Param("tableName") String tableName);

    /**
     * 检查oracle表是否存在
     *
     * @param schemaName
     * @param tableName
     * @return
     */
    Integer checkOracleTableExists(@Param("schemaName") String schemaName, @Param("tableName") String tableName);

    List<Map<String, Object>> queryAccAssDatas(@Param("acctid") String acctid, @Param("accass") String accass);
}