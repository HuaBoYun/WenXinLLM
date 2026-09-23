package com.huabo.financialdata.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.financialdata.entity.entity.GbiTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * <p>
 * GBI上传的表信息 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-04-21
 */
@Mapper
public interface GbiTableMapper extends BaseMapper<GbiTable> {

    @Select("SELECT * FROM ${tableName}")
    IPage<Map> selectTableData(@Param("tableName") String tableName, Page<JSONObject> page);

    void createTable(@Param("createTableSql") String createTableSql);

    void updateTable(@Param("updateTableSql") String updateTableSql);

    void batchInsert(String insertSql);

}
