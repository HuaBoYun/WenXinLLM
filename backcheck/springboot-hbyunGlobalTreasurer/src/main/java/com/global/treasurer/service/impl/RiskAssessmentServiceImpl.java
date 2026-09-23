package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RiskAssessmentDTO;
import com.global.treasurer.dto.RiskAssessmentQueryDTO;
import com.global.treasurer.entity.TblRiskAssessment;
import com.global.treasurer.mapper.RiskAssessmentMapper;
import com.global.treasurer.service.IRiskAssessmentService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 风险评估Service实现类
 * 对应已存在的 TBL_RISK_ASSESSMENT 表
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
@Service
public class RiskAssessmentServiceImpl extends ServiceImpl<RiskAssessmentMapper, TblRiskAssessment>
        implements IRiskAssessmentService {
    @Override
    public PageInfo<TblRiskAssessment> selectRiskAssessmentList(RiskAssessmentQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getLimit());
        LambdaQueryWrapper<TblRiskAssessment> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getRiskId()), TblRiskAssessment::getRiskId, queryDTO.getRiskId())
                .eq(StringUtils.hasText(queryDTO.getEnterpriseId()), TblRiskAssessment::getEnterpriseId, queryDTO.getEnterpriseId())
                .eq(StringUtils.hasText(queryDTO.getRiskCategory()), TblRiskAssessment::getRiskCategory, queryDTO.getRiskCategory())
                .like(StringUtils.hasText(queryDTO.getRiskItem()), TblRiskAssessment::getRiskItem, queryDTO.getRiskItem())
                .eq(StringUtils.hasText(queryDTO.getRiskLevel()), TblRiskAssessment::getRiskLevel, queryDTO.getRiskLevel())
                .eq(StringUtils.hasText(queryDTO.getStatus()), TblRiskAssessment::getStatus, queryDTO.getStatus())
                .ge(queryDTO.getAssessmentDateStart() != null, TblRiskAssessment::getAssessmentDate, queryDTO.getAssessmentDateStart())
                .le(queryDTO.getAssessmentDateEnd() != null, TblRiskAssessment::getAssessmentDate, queryDTO.getAssessmentDateEnd())
                .orderByDesc(TblRiskAssessment::getCreateTime);
        List<TblRiskAssessment> list = this.list(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblRiskAssessment selectRiskAssessmentById(String riskId) {
        return this.getById(riskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRiskAssessment insertRiskAssessment(RiskAssessmentDTO dto) {
        TblRiskAssessment entity = new TblRiskAssessment();
        BeanUtils.copyProperties(dto, entity);
        entity.setCreateTime(new Date());
        entity.setStatus("1");
        this.save(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRiskAssessment updateRiskAssessment(RiskAssessmentDTO dto) {
        TblRiskAssessment entity = this.getById(dto.getRiskId());
        if (entity == null) {
            throw new RuntimeException("风险评估记录不存在");
        }
        BeanUtils.copyProperties(dto, entity, getNullPropertyNames(dto));
        entity.setUpdateTime(new Date());
        this.updateById(entity);
        return entity;
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRiskAssessment(String riskId) {
        return this.removeById(riskId);
    }

    @Override
    public Map<String, Object> getDistributionAnalysis(String enterpriseId) {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<TblRiskAssessment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(enterpriseId), TblRiskAssessment::getEnterpriseId, enterpriseId);
        List<TblRiskAssessment> allAssessments = this.list(wrapper);

        result.put("total", allAssessments.size());
        result.put("totalAssessments", allAssessments.size());

        // 高风险项目数（HIGH/CRITICAL/高）
        long highRiskCount = allAssessments.stream()
                .filter(a -> {
                    String level = a.getRiskLevel();
                    return "HIGH".equalsIgnoreCase(level) || "CRITICAL".equalsIgnoreCase(level) || "高".equals(level);
                })
                .count();
        result.put("highRiskCount", highRiskCount);

        // 待评估数（status = "1"）
        long pendingCount = allAssessments.stream()
                .filter(a -> "1".equals(a.getStatus()))
                .count();
        result.put("pendingAssessments", pendingCount);

        // 平均风险评分（兼容中英文风险等级）
        double avgScore = 0;
        if (!allAssessments.isEmpty()) {
            Map<String, Integer> scoreMap = new HashMap<>();
            scoreMap.put("LOW", 1);
            scoreMap.put("MEDIUM", 2);
            scoreMap.put("HIGH", 3);
            scoreMap.put("CRITICAL", 4);
            scoreMap.put("低", 1);
            scoreMap.put("中", 2);
            scoreMap.put("高", 3);
            double totalScore = allAssessments.stream()
                    .mapToInt(a -> {
                        String level = a.getRiskLevel();
                        if (level == null) return 0;
                        return scoreMap.getOrDefault(level.toUpperCase(), scoreMap.getOrDefault(level, 0));
                    })
                    .sum();
            avgScore = Math.round(totalScore / allAssessments.size() * 10.0) / 10.0;
        }
        result.put("averageRiskScore", avgScore);

        // 按风险类别统计（过滤null和空值，避免FastJSON序列化null key导致无效JSON）
        Map<String, Long> categoryCount = new HashMap<>();
        allAssessments.forEach(a -> {
            String cat = a.getRiskCategory();
            if (cat != null && !cat.isEmpty()) {
                categoryCount.merge(cat, 1L, Long::sum);
            }
        });
        result.put("categoryDistribution", categoryCount);

        // 按风险等级统计
        Map<String, Long> levelCount = new HashMap<>();
        allAssessments.forEach(a -> {
            String level = a.getRiskLevel();
            if (level != null && !level.isEmpty()) {
                levelCount.merge(level, 1L, Long::sum);
            }
        });
        result.put("levelDistribution", levelCount);

        // 按影响程度统计
        Map<String, Long> impactCount = new HashMap<>();
        allAssessments.forEach(a -> {
            String impact = a.getImpactDegree();
            if (impact != null && !impact.isEmpty()) {
                impactCount.merge(impact, 1L, Long::sum);
            }
        });
        result.put("impactDistribution", impactCount);

        return result;
    }

    @Override
    public void exportRiskAssessment(RiskAssessmentQueryDTO queryDTO, HttpServletResponse response) {
        List<TblRiskAssessment> list = this.selectRiskAssessmentList(queryDTO).getList();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("风险评估");
            String[] headers = {"风险ID", "企业ID", "风险类别", "风险项", "风险等级", "影响程度", "发生概率", "评估日期", "状态"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int rowNum = 1;
            for (TblRiskAssessment item : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getRiskId());
                row.createCell(1).setCellValue(item.getEnterpriseId());
                row.createCell(2).setCellValue(item.getRiskCategory());
                row.createCell(3).setCellValue(item.getRiskItem());
                row.createCell(4).setCellValue(item.getRiskLevel());
                row.createCell(5).setCellValue(item.getImpactDegree());
                row.createCell(6).setCellValue(item.getOccurrenceProbability());
                row.createCell(7).setCellValue(item.getAssessmentDate() != null ? sdf.format(item.getAssessmentDate()) : "");
                row.createCell(8).setCellValue(item.getStatus());
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("风险评估.xlsx", "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importRiskAssessment(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                try {
                    TblRiskAssessment entity = new TblRiskAssessment();
                    entity.setEnterpriseId(getCellValue(row.getCell(0)));
                    entity.setRiskCategory(getCellValue(row.getCell(1)));
                    entity.setRiskItem(getCellValue(row.getCell(2)));
                    entity.setRiskLevel(getCellValue(row.getCell(3)));
                    entity.setRiskDescription(getCellValue(row.getCell(4)));
                    entity.setImpactDegree(getCellValue(row.getCell(5)));
                    entity.setOccurrenceProbability(getCellValue(row.getCell(6)));
                    entity.setCountermeasures(getCellValue(row.getCell(7)));
                    entity.setStatus("PENDING");
                    entity.setCreateTime(new Date());
                    entity.setFDatasourcetype(1);
                    entity.setFImportbatches(file.getOriginalFilename());
                    this.save(entity);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("文件解析失败: " + e.getMessage());
        }
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        return result;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    @Override
    public void generateReport(String riskId, HttpServletResponse response) {
        TblRiskAssessment assessment = this.getById(riskId);
        if (assessment == null) {
            throw new RuntimeException("风险评估记录不存在");
        }
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("风险评估报告");
            String[][] reportData = {
                {"报告项", "内容"},
                {"风险ID", assessment.getRiskId()},
                {"企业ID", assessment.getEnterpriseId()},
                {"风险类别", assessment.getRiskCategory()},
                {"风险项", assessment.getRiskItem()},
                {"风险等级", assessment.getRiskLevel()},
                {"风险描述", assessment.getRiskDescription()},
                {"影响程度", assessment.getImpactDegree()},
                {"发生概率", assessment.getOccurrenceProbability()},
                {"应对措施", assessment.getCountermeasures()},
                {"评估日期", assessment.getAssessmentDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(assessment.getAssessmentDate()) : ""},
                {"状态", assessment.getStatus()}
            };
            for (int i = 0; i < reportData.length; i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(reportData[i][0]);
                row.createCell(1).setCellValue(reportData[i][1] != null ? reportData[i][1] : "");
            }
            sheet.setColumnWidth(0, 5000);
            sheet.setColumnWidth(1, 15000);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("风险评估报告_" + riskId + ".xlsx", "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("生成报告失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getHistory(String riskId) {
        TblRiskAssessment entity = this.getById(riskId);
        List<Map<String, Object>> historyList = new ArrayList<>();
        if (entity != null) {
            Map<String, Object> record = new HashMap<>();
            record.put("riskId", entity.getRiskId());
            record.put("riskCategory", entity.getRiskCategory());
            record.put("riskItem", entity.getRiskItem());
            record.put("riskLevel", entity.getRiskLevel());
            record.put("status", entity.getStatus());
            record.put("operationType", "CREATE");
            record.put("operationTime", entity.getCreateTime());
            record.put("remark", "创建风险评估记录");
            historyList.add(record);

            if (entity.getUpdateTime() != null) {
                Map<String, Object> updateRecord = new HashMap<>();
                updateRecord.put("riskId", entity.getRiskId());
                updateRecord.put("riskCategory", entity.getRiskCategory());
                updateRecord.put("riskItem", entity.getRiskItem());
                updateRecord.put("riskLevel", entity.getRiskLevel());
                updateRecord.put("status", entity.getStatus());
                updateRecord.put("operationType", "UPDATE");
                updateRecord.put("operationTime", entity.getUpdateTime());
                updateRecord.put("remark", "更新风险评估记录");
                historyList.add(updateRecord);
            }
        }
        return historyList;
    }
}

