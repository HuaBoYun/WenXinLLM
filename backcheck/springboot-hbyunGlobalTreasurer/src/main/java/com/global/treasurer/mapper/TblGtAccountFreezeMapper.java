package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtAccountFreeze;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 全球司库-账户冻结Mapper接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
public interface TblGtAccountFreezeMapper extends BaseMapper<TblGtAccountFreeze> {

    /**
     * 分页查询冻结记录列表
     */
    @Select("<script>" +
            "SELECT * FROM TBL_GT_ACCOUNT_FREEZE " +
            "WHERE 1=1 " +
            "<if test='recordId != null'> " +
            "AND RECORD_ID = #{recordId} " +
            "</if> " +
            "<if test='accountNumber != null and accountNumber != \"\"'> " +
            "AND ACCOUNT_NUMBER LIKE CONCAT('%', #{accountNumber}, '%') " +
            "</if> " +
            "<if test='freezeType != null and freezeType != \"\"'> " +
            "AND FREEZE_TYPE = #{freezeType} " +
            "</if> " +
            "<if test='freezeStatus != null and freezeStatus != \"\"'> " +
            "AND FREEZE_STATUS = #{freezeStatus} " +
            "</if> " +
            "ORDER BY CREATE_TIME DESC" +
            "</script>")
    IPage<TblGtAccountFreeze> selectPageList(
            Page<TblGtAccountFreeze> page,
            @Param("recordId") Long recordId,
            @Param("accountNumber") String accountNumber,
            @Param("freezeType") String freezeType,
            @Param("freezeStatus") String freezeStatus
    );
}

