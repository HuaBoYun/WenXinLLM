package com.global.treasurer.mapper;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtAccountCheck;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 全球司库-账户检查Mapper接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
public interface TblGtAccountCheckMapper extends BaseMapper<TblGtAccountCheck> {

    /**
     * 分页查询账户检查列表
     *
     * @param page 分页对象
     * @param accountNumber 账户号码（数据库字段为ACCOUNT_NUMBER）
     * @param checkType 检查类型
     * @param checkStatus 检查状态
     * @param checkId 检查ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param orgId 机构ID（暂不使用，表中无此字段）
     * @return 分页结果
     */
    @Select("<script>" +
            "SELECT t.*, s.REALNAME AS checkerName FROM TBL_GT_ACCOUNT_CHECK t " +
            "LEFT JOIN TBL_STAFF s ON t.CHECK_USER = s.STAFFID " +
            "WHERE 1=1 " +
            "<if test='checkId != null and checkId != \"\"'> " +
            "AND t.CHECK_ID = #{checkId} " +
            "</if> " +
            "<if test='accountNumber != null and accountNumber != \"\"'> " +
            "AND t.ACCOUNT_NUMBER LIKE CONCAT('%', #{accountNumber}, '%') " +
            "</if> " +
            "<if test='checkType != null and checkType != \"\"'> " +
            "AND t.CHECK_TYPE = #{checkType} " +
            "</if> " +
            "<if test='checkStatus != null and checkStatus != \"\"'> " +
            "AND t.CHECK_STATUS = #{checkStatus} " +
            "</if> " +
            "<if test='startDate != null and startDate != \"\"'> " +
            "AND t.CHECK_DATE &gt;= #{startDate} " +
            "</if> " +
            "<if test='endDate != null and endDate != \"\"'> " +
            "AND t.CHECK_DATE &lt;= #{endDate} " +
            "</if> " +
            "ORDER BY t.CREATE_TIME DESC" +
            "</script>")
    IPage<TblGtAccountCheck> selectPageList(
            Page<TblGtAccountCheck> page,
            @Param("accountNumber") String accountNumber,
            @Param("checkType") String checkType,
            @Param("checkStatus") String checkStatus,
            @Param("checkId") Long checkId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("orgId") BigDecimal orgId
    );
}
