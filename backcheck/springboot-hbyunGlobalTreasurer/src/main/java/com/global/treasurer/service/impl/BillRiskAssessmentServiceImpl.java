package com.global.treasurer.service.impl;

import com.global.treasurer.dto.BillRiskAssessmentDTO;
import com.global.treasurer.dto.BillRiskAssessmentQueryDTO;
import com.global.treasurer.entity.TblBillRiskAssessment;
import com.global.treasurer.mapper.BillRiskAssessmentMapper;
import com.global.treasurer.service.IBillRiskAssessmentService;
import com.global.treasurer.vo.BillRiskAssessmentVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 票据风险评估Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Service
public class BillRiskAssessmentServiceImpl extends ServiceImpl<BillRiskAssessmentMapper, TblBillRiskAssessment>
        implements IBillRiskAssessmentService {
    private static final Logger log = LoggerFactory.getLogger(BillRiskAssessmentServiceImpl.class);

    @Resource
    private BillRiskAssessmentMapper billRiskAssessmentMapper;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public PageInfo<BillRiskAssessmentVO> selectBillRiskAssessmentList(BillRiskAssessmentQueryDTO queryDTO) {
        log.info("查询票据风险评估列表, 参数: {}", queryDTO);

        // 分页查询
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<BillRiskAssessmentVO> list = billRiskAssessmentMapper.selectBillRiskAssessmentList(queryDTO);

        return new PageInfo<>(list);
    }

    @Override
    public BillRiskAssessmentVO selectBillRiskAssessmentById(Long riskId) {
        log.info("查询票据风险评估详情, riskId: {}", riskId);
        return billRiskAssessmentMapper.selectBillRiskAssessmentById(riskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillRiskAssessment insertBillRiskAssessment(BillRiskAssessmentDTO dto) {
        log.info("新增票据风险评估, 参数: {}", dto);

        TblBillRiskAssessment entity = new TblBillRiskAssessment();

        // 手动映射字段（DTO和Entity字段名不完全一致）
        entity.setBillNumber(dto.getBillNumber());
        // 优先使用billAmount，如果没有则使用riskAmount
        entity.setBillAmount(dto.getBillAmount() != null ? dto.getBillAmount() : dto.getRiskAmount());
        entity.setRiskLevel(dto.getRiskLevel());
        entity.setRiskType(dto.getRiskType());
        entity.setAcceptorName(dto.getAcceptorName());
        entity.setAssessmentStatus(dto.getAssessmentStatus() != null ? dto.getAssessmentStatus() : "PENDING");
        entity.setMaturityDate(dto.getMaturityDate());
        entity.setRiskScore(dto.getRiskScore());
        entity.setRiskFactors(dto.getRiskFactors());
        entity.setRiskSuggestions(dto.getRecommendations()); // DTO的recommendations对应Entity的riskSuggestions
        entity.setAssessorId(dto.getAssessorId());
        entity.setAssessorName(dto.getAssessorName());
        entity.setCompanyId(dto.getCompanyId());
        entity.setDeptId(dto.getDeptId());

        // 生成ID
        entity.setAssessmentId(snowflakeIdWorker.nextId());
        entity.setAssessmentDate(dto.getAssessmentDate() != null ? dto.getAssessmentDate() : new Date());
        entity.setDeleteFlag(0);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());

        billRiskAssessmentMapper.insert(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBillRiskAssessment updateBillRiskAssessment(BillRiskAssessmentDTO dto) {
        log.info("修改票据风险评估, 参数: {}", dto);

        TblBillRiskAssessment entity = new TblBillRiskAssessment();

        // 手动映射字段（DTO和Entity字段名不完全一致）
        // 注意：DTO中的riskId对应Entity中的assessmentId
        entity.setAssessmentId(dto.getRiskId());
        entity.setBillNumber(dto.getBillNumber());
        // 优先使用billAmount，如果没有则使用riskAmount
        entity.setBillAmount(dto.getBillAmount() != null ? dto.getBillAmount() : dto.getRiskAmount());
        entity.setRiskLevel(dto.getRiskLevel());
        entity.setRiskType(dto.getRiskType());
        entity.setAcceptorName(dto.getAcceptorName());
        entity.setAssessmentStatus(dto.getAssessmentStatus());
        entity.setMaturityDate(dto.getMaturityDate());
        entity.setRiskScore(dto.getRiskScore());
        entity.setRiskFactors(dto.getRiskFactors());
        entity.setRiskSuggestions(dto.getRecommendations()); // DTO的recommendations对应Entity的riskSuggestions
        entity.setAssessorId(dto.getAssessorId());
        entity.setAssessorName(dto.getAssessorName());
        entity.setAssessmentDate(dto.getAssessmentDate());
        entity.setCompanyId(dto.getCompanyId());
        entity.setDeptId(dto.getDeptId());
        entity.setUpdateTime(new Date());

        billRiskAssessmentMapper.updateById(entity);
        return entity;
    }

    @Override
    public List<Map<String, Object>> getRiskAlerts(Map<String, Object> params) {
        log.info("获取风险预警, 参数: {}", params);

        // 查询高风险票据
        List<BillRiskAssessmentVO> highRiskBills = billRiskAssessmentMapper.selectHighRiskBills();

        List<Map<String, Object>> result = new ArrayList<>();
        for (BillRiskAssessmentVO vo : highRiskBills) {
            // 过滤已处理的预警（状态为 RESOLVED 或 DISPOSED 的不显示）
            String assessmentStatus = vo.getAssessmentStatus();
            if ("RESOLVED".equals(assessmentStatus) || "DISPOSED".equals(assessmentStatus)) {
                continue;
            }

            Map<String, Object> alert = new HashMap<>();
            // 前端期望的字段
            alert.put("alertId", vo.getAssessmentId()); // 使用评估ID作为预警ID
            alert.put("alertTime", vo.getAssessmentDate() != null ? vo.getAssessmentDate() : new Date()); // 预警时间
            alert.put("alertLevel", vo.getRiskLevel()); // 预警级别 (HIGH/MEDIUM/LOW)
            alert.put("alertType", getAlertType(vo.getRiskType())); // 预警类型
            alert.put("alertMessage", buildAlertMessage(vo)); // 预警内容
            alert.put("billNumber", vo.getBillNumber()); // 票据号码
            alert.put("status", "PENDING"); // 状态：待处理

            // 保留原有字段，便于其他功能使用
            alert.put("riskId", vo.getRiskId());
            alert.put("billId", vo.getBillId());
            alert.put("riskScore", vo.getRiskScore());
            alert.put("riskDescription", vo.getRiskDescription());
            alert.put("recommendations", vo.getRecommendations());
            result.add(alert);
        }

        return result;
    }

    /**
     * 根据风险类型获取预警类型描述
     */
    private String getAlertType(String riskType) {
        if (riskType == null) {
            return "综合风险";
        }
        switch (riskType) {
            case "CREDIT":
            case "CREDIT_RISK":
                return "信用风险";
            case "MARKET":
            case "MARKET_RISK":
                return "市场风险";
            case "LIQUIDITY":
            case "LIQUIDITY_RISK":
                return "流动性风险";
            case "OPERATION":
            case "OPERATIONAL_RISK":
                return "操作风险";
            default:
                return "综合风险";
        }
    }

    /**
     * 构建预警内容
     */
    private String buildAlertMessage(BillRiskAssessmentVO vo) {
        StringBuilder message = new StringBuilder();
        message.append("票据").append(vo.getBillNumber() != null ? vo.getBillNumber() : "未知");
        message.append("风险评分").append(vo.getRiskScore() != null ? vo.getRiskScore() : 0).append("分");
        if (vo.getRiskDescription() != null && !vo.getRiskDescription().isEmpty()) {
            message.append("，").append(vo.getRiskDescription());
        } else {
            message.append("，请及时关注并处理");
        }
        return message.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disposeBillRisk(Map<String, Object> disposeData) {
        log.info("处置票据风险, 参数: {}", disposeData);

        // 获取前端传递的参数
        Object assessmentIdObj = disposeData.get("assessmentId");
        Long assessmentId = null;
        if (assessmentIdObj instanceof Number) {
            assessmentId = ((Number) assessmentIdObj).longValue();
        } else if (assessmentIdObj instanceof String) {
            assessmentId = Long.parseLong((String) assessmentIdObj);
        }

        String disposeType = (String) disposeData.get("disposeType");
        String disposeDescription = (String) disposeData.get("disposeDescription");
        String expectedEffect = (String) disposeData.get("expectedEffect");

        if (assessmentId == null) {
            log.warn("处置票据风险失败: assessmentId为空");
            return false;
        }

        // 更新风险评估的处置结果
        TblBillRiskAssessment entity = billRiskAssessmentMapper.selectById(assessmentId);
        if (entity != null) {
            // 构建处置结果描述
            String disposeResult = String.format("处置方式: %s, 处置说明: %s, 预期效果: %s",
                disposeType != null ? disposeType : "",
                disposeDescription != null ? disposeDescription : "",
                expectedEffect != null ? expectedEffect : "");
            entity.setRiskSuggestions(disposeResult);
            entity.setAssessmentStatus("DISPOSED");
            entity.setUpdateTime(new Date());
            return billRiskAssessmentMapper.updateById(entity) > 0;
        }

        log.warn("处置票据风险失败: 未找到assessmentId={}的记录", assessmentId);
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resolveRiskAlert(Map<String, Object> resolveData) {
        log.info("标记预警已处理, 参数: {}", resolveData);

        // 获取前端传递的 alertIds 列表
        Object alertIdsObj = resolveData.get("alertIds");
        List<Long> alertIds = new ArrayList<>();

        if (alertIdsObj instanceof List) {
            List<?> list = (List<?>) alertIdsObj;
            for (Object item : list) {
                if (item instanceof Number) {
                    alertIds.add(((Number) item).longValue());
                } else if (item instanceof String) {
                    try {
                        alertIds.add(Long.parseLong((String) item));
                    } catch (NumberFormatException e) {
                        log.warn("无效的alertId: {}", item);
                    }
                }
            }
        }

        if (alertIds.isEmpty()) {
            log.warn("标记预警已处理失败: alertIds为空");
            return false;
        }

        // 更新评估状态为已处理 (RESOLVED)
        int updatedCount = 0;
        for (Long alertId : alertIds) {
            TblBillRiskAssessment entity = billRiskAssessmentMapper.selectById(alertId);
            if (entity != null) {
                entity.setAssessmentStatus("RESOLVED");
                entity.setUpdateTime(new Date());
                if (billRiskAssessmentMapper.updateById(entity) > 0) {
                    updatedCount++;
                }
            }
        }

        log.info("标记预警已处理完成, 共更新{}条记录", updatedCount);
        return updatedCount > 0;
    }

    @Override
    public Map<String, Object> getBillTrend(Map<String, Object> params) {
        log.info("获取票据趋势, 参数: {}", params);

        Map<String, Object> result = new HashMap<>();

        // 统计各风险等级的数量
        Integer highCount = billRiskAssessmentMapper.countByRiskLevel("HIGH");
        Integer mediumCount = billRiskAssessmentMapper.countByRiskLevel("MEDIUM");
        Integer lowCount = billRiskAssessmentMapper.countByRiskLevel("LOW");

        Map<String, Integer> riskLevelStats = new HashMap<>();
        riskLevelStats.put("HIGH", highCount);
        riskLevelStats.put("MEDIUM", mediumCount);
        riskLevelStats.put("LOW", lowCount);

        result.put("riskLevelStats", riskLevelStats);
        result.put("totalAssessments", highCount + mediumCount + lowCount);

        return result;
    }

    @Override
    public Map<String, Object> getBillRiskStatistics(BillRiskAssessmentQueryDTO queryDTO) {
        log.info("获取风险统计数据, 参数: {}", queryDTO);

        Map<String, Object> result = new HashMap<>();

        // 统计各风险等级的数量
        Integer highCount = billRiskAssessmentMapper.countByRiskLevel("HIGH");
        Integer mediumCount = billRiskAssessmentMapper.countByRiskLevel("MEDIUM");
        Integer lowCount = billRiskAssessmentMapper.countByRiskLevel("LOW");

        // 计算总数
        int totalRiskBills = (highCount != null ? highCount : 0)
                           + (mediumCount != null ? mediumCount : 0)
                           + (lowCount != null ? lowCount : 0);

        result.put("riskBills", totalRiskBills);
        result.put("highRiskBills", highCount != null ? highCount : 0);
        result.put("mediumRiskBills", mediumCount != null ? mediumCount : 0);
        result.put("lowRiskBills", lowCount != null ? lowCount : 0);
        // 风险金额暂时返回模拟数据，后续可根据实际业务计算
        result.put("riskAmount", 0);

        return result;
    }

    @Override
    public Map<String, Object> getRiskTrendData(Map<String, Object> params) {
        log.info("获取风险趋势数据, 参数: {}", params);

        Map<String, Object> result = new HashMap<>();

        // 获取天数参数，默认30天
        Integer days = params.get("days") != null ? (Integer) params.get("days") : 30;

        // 生成日期标签
        List<String> labels = new ArrayList<>();
        List<Integer> highRiskData = new ArrayList<>();
        List<Integer> mediumRiskData = new ArrayList<>();
        List<Integer> lowRiskData = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        for (int i = days - 1; i >= 0; i--) {
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            labels.add(month + "/" + day);

            // 暂时返回模拟数据，后续可根据实际业务查询每日数据
            highRiskData.add(0);
            mediumRiskData.add(0);
            lowRiskData.add(0);
        }

        result.put("labels", labels);
        result.put("highRiskData", highRiskData);
        result.put("mediumRiskData", mediumRiskData);
        result.put("lowRiskData", lowRiskData);

        return result;
    }

    @Override
    public Map<String, Object> getRiskDistributionData(Map<String, Object> params) {
        log.info("获取风险分布数据, 参数: {}", params);

        Map<String, Object> result = new HashMap<>();

        // 统计各风险等级的数量
        Integer highCount = billRiskAssessmentMapper.countByRiskLevel("HIGH");
        Integer mediumCount = billRiskAssessmentMapper.countByRiskLevel("MEDIUM");
        Integer lowCount = billRiskAssessmentMapper.countByRiskLevel("LOW");

        result.put("highRisk", highCount != null ? highCount : 0);
        result.put("mediumRisk", mediumCount != null ? mediumCount : 0);
        result.put("lowRisk", lowCount != null ? lowCount : 0);

        return result;
    }

    @Override
    public List<Map<String, Object>> getRiskAssessmentHistory(String identifier) {
        log.info("获取风险评估历史, identifier: {}", identifier);

        List<Map<String, Object>> result = new ArrayList<>();

        // 尝试通过票据号码查询历史记录
        BillRiskAssessmentQueryDTO queryDTO = new BillRiskAssessmentQueryDTO();
        queryDTO.setBillNumber(identifier);
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(100);

        List<BillRiskAssessmentVO> list = billRiskAssessmentMapper.selectBillRiskAssessmentList(queryDTO);

        // 如果通过票据号码没有查到，尝试通过评估ID查询
        if (list == null || list.isEmpty()) {
            try {
                Long assessmentId = Long.parseLong(identifier);
                BillRiskAssessmentVO vo = billRiskAssessmentMapper.selectBillRiskAssessmentById(assessmentId);
                if (vo != null) {
                    // 用这条记录的票据号码再查询所有相关记录
                    if (vo.getBillNumber() != null) {
                        queryDTO.setBillNumber(vo.getBillNumber());
                        list = billRiskAssessmentMapper.selectBillRiskAssessmentList(queryDTO);
                    } else {
                        list = new ArrayList<>();
                        list.add(vo);
                    }
                }
            } catch (NumberFormatException e) {
                // identifier 不是数字，忽略
                log.debug("identifier 不是有效的评估ID: {}", identifier);
            }
        }

        // 转换为历史记录格式
        if (list != null) {
            for (BillRiskAssessmentVO vo : list) {
                Map<String, Object> history = new HashMap<>();
                history.put("assessmentId", vo.getAssessmentId());
                history.put("billNumber", vo.getBillNumber());
                history.put("riskLevel", vo.getRiskLevel());
                history.put("riskScore", vo.getRiskScore());
                history.put("assessmentDate", vo.getAssessmentDate());
                history.put("assessorName", vo.getAssessorName());
                history.put("riskDescription", vo.getRiskDescription());
                result.add(history);
            }
        }

        return result;
    }
}
