package com.global.treasurer.mapper;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtAccountClosing;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 全球司库-销户申请Mapper接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
public interface TblGtAccountClosingMapper extends BaseMapper<TblGtAccountClosing> {

    /**
     * 分页查询销户申请列表
     *
     * @param page 分页对象
     * @param accountNumber 账户号码
     * @param applicationStatus 申请状态
     * @param orgId 机构ID
     * @return 分页结果
     */
    @Select("<script>" +
            "SELECT APPLICATION_ID, APPLICATION_NO, ACCOUNT_ID, ACCOUNT_NAME, " +
            "ACCOUNT_NUMBER AS accountNo, " +
            "CLOSING_REASON, BALANCE_HANDLING, TRANSFER_ACCOUNT_NUMBER, " +
            "APPLICATION_STATUS, APPLICATION_DATE, APPROVER_ID, APPROVAL_DATE, " +
            "APPROVAL_OPINION, COMPLETE_DATE, ATTACHMENTS, REMARK, " +
            "ORG_ID, CREATE_USER, CREATE_TIME, UPDATE_USER, UPDATE_TIME " +
            "FROM TBL_GT_ACCOUNT_CLOSING " +
            "WHERE 1=1 " +
            "<if test='applicationNo != null and applicationNo != \"\"'> " +
            "AND APPLICATION_NO LIKE CONCAT('%', #{applicationNo}, '%') " +
            "</if> " +
            "<if test='accountNumber != null and accountNumber != \"\"'> " +
            "AND ACCOUNT_NUMBER LIKE CONCAT('%', #{accountNumber}, '%') " +
            "</if> " +
            "<if test='applicationStatus != null and applicationStatus != \"\"'> " +
            "AND APPLICATION_STATUS = #{applicationStatus} " +
            "</if> " +
            "<if test='orgId != null'> " +
            "AND ORG_ID = #{orgId} " +
            "</if> " +
            "ORDER BY CREATE_TIME DESC" +
            "</script>")
    IPage<TblGtAccountClosing> selectPageList(
            Page<TblGtAccountClosing> page,
            @Param("applicationNo") String applicationNo,
            @Param("accountNumber") String accountNumber,
            @Param("applicationStatus") String applicationStatus,
            @Param("orgId") BigDecimal orgId
    );
}
