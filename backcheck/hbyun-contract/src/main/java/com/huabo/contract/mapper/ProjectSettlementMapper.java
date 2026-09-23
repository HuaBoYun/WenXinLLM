package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.contract.vo.ProjectSettlementQueryParam;
import com.huabo.contract.entity.ProjectSettlement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 项目结算Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Mapper
public interface ProjectSettlementMapper extends BaseMapper<ProjectSettlement> {

    /**
     * 分页查询项目结算列表
     */
    IPage<ProjectSettlement> selectSettlementPage(Page<ProjectSettlement> page, @Param("param") ProjectSettlementQueryParam param);

    /**
     * 根据项目ID查询结算统计信息
     */
    @Select("SELECT " +
            "COUNT(*) as totalSettlements, " +
            "COALESCE(SUM(settlement_amount), 0) as totalAmount, " +
            "COALESCE(SUM(CASE WHEN settlement_status >= 3 THEN settlement_amount ELSE 0 END), 0) as completedAmount, " +
            "COALESCE(SUM(CASE WHEN settlement_status < 3 THEN settlement_amount ELSE 0 END), 0) as pendingAmount " +
            "FROM project_settlement WHERE project_id = #{projectId}")
    ProjectSettlementStatistics getSettlementStatistics(@Param("projectId") Long projectId);

    /**
     * 根据结算编号查询是否存在
     */
    @Select("SELECT COUNT(*) FROM project_settlement WHERE settlement_no = #{settlementNo}")
    int countBySettlementNo(@Param("settlementNo") String settlementNo);

    /**
     * 根据项目ID和结算类型查询最新结算记录
     */
    @Select("SELECT * FROM project_settlement " +
            "WHERE project_id = #{projectId} AND settlement_type = #{settlementType} " +
            "ORDER BY settlement_date DESC LIMIT 1")
    ProjectSettlement getLatestSettlementByType(@Param("projectId") Long projectId, @Param("settlementType") Short settlementType);

    /**
     * 查询待审核的结算记录
     */
    @Select("SELECT ps.*, p.project_name, s1.staff_name as settlor_name, s2.staff_name as reviewer_name " +
            "FROM project_settlement ps " +
            "LEFT JOIN tbl_contract_project p ON ps.project_id = p.id " +
            "LEFT JOIN tbl_staff s1 ON ps.settlor_id = s1.staff_id " +
            "LEFT JOIN tbl_staff s2 ON ps.reviewer_id = s2.staff_id " +
            "WHERE ps.settlement_status = 2 " +
            "ORDER BY ps.settlement_date ASC")
    List<ProjectSettlement> getPendingReviewSettlements();

    /**
     * 结算统计信息内部类
     */
    class ProjectSettlementStatistics {
        private Integer totalSettlements;
        private BigDecimal totalAmount;
        private BigDecimal completedAmount;
        private BigDecimal pendingAmount;

        // Getters and Setters
        public Integer getTotalSettlements() { return totalSettlements; }
        public void setTotalSettlements(Integer totalSettlements) { this.totalSettlements = totalSettlements; }
        
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        
        public BigDecimal getCompletedAmount() { return completedAmount; }
        public void setCompletedAmount(BigDecimal completedAmount) { this.completedAmount = completedAmount; }
        
        public BigDecimal getPendingAmount() { return pendingAmount; }
        public void setPendingAmount(BigDecimal pendingAmount) { this.pendingAmount = pendingAmount; }
    }
}
