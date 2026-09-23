package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.BillRiskAssessmentQueryDTO;
import com.global.treasurer.entity.TblBillRiskAssessment;
import com.global.treasurer.vo.BillRiskAssessmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 票据风险评估Mapper接口
 */
@Mapper
public interface BillRiskAssessmentMapper extends BaseMapper<TblBillRiskAssessment> {

    /**
     * 查询票据风险评估列表
     */
    List<BillRiskAssessmentVO> selectBillRiskAssessmentList(@Param("query") BillRiskAssessmentQueryDTO queryDTO);

    /**
     * 根据ID查询票据风险评估详情
     */
    BillRiskAssessmentVO selectBillRiskAssessmentById(Long assessmentId);

    /**
     * 根据票据号码查询最新风险评估
     */
    BillRiskAssessmentVO selectLatestByBillNumber(String billNumber);

    /**
     * 批量删除票据风险评估(逻辑删除)
     */
    int deleteBillRiskAssessmentByIds(@Param("assessmentIds") Long[] assessmentIds);

    /**
     * 统计风险等级数量
     */
    Integer countByRiskLevel(String riskLevel);

    /**
     * 查询高风险票据预警
     */
    List<BillRiskAssessmentVO> selectHighRiskBills();
}
