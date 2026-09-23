package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.DebtRecoveryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 债权回收登记记录Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Mapper
public interface DebtRecoveryRecordMapper extends BaseMapper<DebtRecoveryRecord> {

    /**
     * 根据债权ID查询回收记录列表
     *
     * @param debtId 债权ID
     * @return 回收记录列表
     */
    @Select("SELECT * FROM debt_recovery_record WHERE debt_id = #{debtId} AND deleted = 0 ORDER BY recovery_date DESC")
    List<DebtRecoveryRecord> selectByDebtId(@Param("debtId") Long debtId);

    /**
     * 根据回收人员ID查询回收记录列表
     *
     * @param recoveryPersonId 回收人员ID
     * @return 回收记录列表
     */
    @Select("SELECT * FROM debt_recovery_record WHERE recovery_person_id = #{recoveryPersonId} AND deleted = 0 ORDER BY recovery_date DESC")
    List<DebtRecoveryRecord> selectByRecoveryPersonId(@Param("recoveryPersonId") Long recoveryPersonId);

    /**
     * 根据回收方式查询回收记录列表
     *
     * @param recoveryMethod 回收方式
     * @return 回收记录列表
     */
    @Select("SELECT * FROM debt_recovery_record WHERE recovery_method = #{recoveryMethod} AND deleted = 0 ORDER BY recovery_date DESC")
    List<DebtRecoveryRecord> selectByRecoveryMethod(@Param("recoveryMethod") Integer recoveryMethod);

    /**
     * 根据债权ID计算总回收金额
     *
     * @param debtId 债权ID
     * @return 总回收金额
     */
    @Select("SELECT COALESCE(SUM(recovery_amount), 0) FROM debt_recovery_record WHERE debt_id = #{debtId} AND deleted = 0")
    BigDecimal getTotalRecoveryAmountByDebtId(@Param("debtId") Long debtId);

    /**
     * 获取债权回收统计信息
     *
     * @param debtId 债权ID
     * @return 统计信息
     */
    @Select("SELECT " +
            "COUNT(*) as totalRecoveries, " +
            "COALESCE(SUM(recovery_amount), 0) as totalRecoveredAmount, " +
            "CASE WHEN COUNT(*) > 0 THEN COALESCE(SUM(recovery_amount), 0) / COUNT(*) ELSE 0 END as averageRecoveryAmount, " +
            "COALESCE(MAX(recovery_amount), 0) as maxSingleRecovery " +
            "FROM debt_recovery_record WHERE debt_id = #{debtId} AND deleted = 0")
    Map<String, Object> getRecoverySummary(@Param("debtId") Long debtId);
}
