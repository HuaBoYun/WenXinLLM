package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RiskIdentificationDTO;
import com.global.treasurer.dto.RiskIdentificationQueryDTO;
import com.global.treasurer.entity.TblRiskIdentification;
import com.global.treasurer.mapper.RiskIdentificationMapper;
import com.global.treasurer.service.IRiskIdentificationService;
import com.hbfk.util.user.UserProvider;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 风险识别Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
@Service
public class RiskIdentificationServiceImpl extends ServiceImpl<RiskIdentificationMapper, TblRiskIdentification>
        implements IRiskIdentificationService {
    @org.springframework.beans.factory.annotation.Autowired
    private com.hbfk.util.user.UserProvider userProvider;

    /**
     * 获取当前登录用户名
     */
    private String getLoginUserName() {
        try {
            com.hbfk.entity.TblStaffUtil user = userProvider.get();
            return user != null ? user.getUsername() : "system";
        } catch (Exception e) {
            return "system";
        }
    }

    private Long getLoginUserOrgId() {
        try {
            com.hbfk.entity.TblStaffUtil user = userProvider.get();
            if (user != null && user.getCurrentOrg() != null) {
                return user.getCurrentOrg().getOrgid().longValue();
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public PageInfo<TblRiskIdentification> selectRiskIdentificationList(RiskIdentificationQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getCurrent(), queryDTO.getSize());
        LambdaQueryWrapper<TblRiskIdentification> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getRiskCode()), TblRiskIdentification::getRiskCode, queryDTO.getRiskCode())
                .like(StringUtils.hasText(queryDTO.getRiskName()), TblRiskIdentification::getRiskName, queryDTO.getRiskName())
                .eq(StringUtils.hasText(queryDTO.getRiskType()), TblRiskIdentification::getRiskType, queryDTO.getRiskType())
                .eq(StringUtils.hasText(queryDTO.getRiskLevel()), TblRiskIdentification::getRiskLevel, queryDTO.getRiskLevel())
                .eq(StringUtils.hasText(queryDTO.getStatus()), TblRiskIdentification::getStatus, queryDTO.getStatus())
                .ge(queryDTO.getIdentificationDateStart() != null, TblRiskIdentification::getIdentificationDate, queryDTO.getIdentificationDateStart())
                .le(queryDTO.getIdentificationDateEnd() != null, TblRiskIdentification::getIdentificationDate, queryDTO.getIdentificationDateEnd())
                .eq(queryDTO.getOrgId() != null, TblRiskIdentification::getOrgId, queryDTO.getOrgId())
                .orderByDesc(TblRiskIdentification::getCreateTime);
        List<TblRiskIdentification> list = this.list(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblRiskIdentification selectRiskIdentificationById(Long identificationId) {
        return this.getById(identificationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRiskIdentification insertRiskIdentification(RiskIdentificationDTO dto) {
        TblRiskIdentification entity = new TblRiskIdentification();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getOrgId() == null) {
            entity.setOrgId(getLoginUserOrgId());
        }
        entity.setCreateTime(new Date());
        entity.setCreateBy(getLoginUserName());
        entity.setStatus("PENDING");
        this.save(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRiskIdentification updateRiskIdentification(RiskIdentificationDTO dto) {
        TblRiskIdentification entity = this.getById(dto.getRiskId());
        if (entity == null) {
            throw new RuntimeException("风险识别记录不存在");
        }
        Long existingOrgId = entity.getOrgId();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getOrgId() == null) {
            entity.setOrgId(existingOrgId != null ? existingOrgId : getLoginUserOrgId());
        }
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(getLoginUserName());
        this.updateById(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRiskIdentification(Long identificationId) {
        return this.removeById(identificationId);
    }

    @Override
    public void exportRiskIdentification(RiskIdentificationQueryDTO queryDTO, HttpServletResponse response) {
        List<TblRiskIdentification> list = this.selectRiskIdentificationList(queryDTO).getList();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("风险识别");
            String[] headers = {"风险编号", "风险名称", "风险类型", "风险等级", "影响程度", "发生概率", "识别人", "识别日期", "状态"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int rowNum = 1;
            for (TblRiskIdentification item : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getRiskCode());
                row.createCell(1).setCellValue(item.getRiskName());
                row.createCell(2).setCellValue(item.getRiskType());
                row.createCell(3).setCellValue(item.getRiskLevel());
                row.createCell(4).setCellValue(item.getImpactLevel());
                row.createCell(5).setCellValue(item.getProbability() != null ? item.getProbability().toString() : "");
                row.createCell(6).setCellValue(item.getIdentifier());
                row.createCell(7).setCellValue(item.getIdentificationDate() != null ? sdf.format(item.getIdentificationDate()) : "");
                row.createCell(8).setCellValue(item.getStatus());
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("风险识别.xlsx", "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchAssess(List<Long> ids) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        for (Long id : ids) {
            try {
                TblRiskIdentification entity = this.getById(id);
                if (entity != null) {
                    entity.setStatus("ASSESSING");
                    entity.setUpdateTime(new Date());
                    entity.setUpdateBy(getLoginUserName());
                    this.updateById(entity);
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
            }
        }
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", ids.size());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRiskIdentification closeRisk(Long identificationId, String reason) {
        TblRiskIdentification entity = this.getById(identificationId);
        if (entity == null) {
            throw new RuntimeException("风险识别记录不存在");
        }
        entity.setStatus("CLOSED");
        entity.setIdentificationRemark(reason);
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(getLoginUserName());
        this.updateById(entity);
        return entity;
    }

    @Override
    public List<Map<String, Object>> getHistory(Long identificationId) {
        TblRiskIdentification entity = this.getById(identificationId);
        List<Map<String, Object>> historyList = new ArrayList<>();
        if (entity != null) {
            Map<String, Object> record = new HashMap<>();
            record.put("riskId", entity.getRiskId());
            record.put("riskCode", entity.getRiskCode());
            record.put("riskName", entity.getRiskName());
            record.put("status", entity.getStatus());
            record.put("riskLevel", entity.getRiskLevel());
            record.put("operationType", "CREATE");
            record.put("operationTime", entity.getCreateTime());
            record.put("operator", entity.getCreateBy());
            record.put("remark", "创建风险识别记录");
            historyList.add(record);

            if (entity.getUpdateTime() != null) {
                Map<String, Object> updateRecord = new HashMap<>();
                updateRecord.put("riskId", entity.getRiskId());
                updateRecord.put("riskCode", entity.getRiskCode());
                updateRecord.put("riskName", entity.getRiskName());
                updateRecord.put("status", entity.getStatus());
                updateRecord.put("riskLevel", entity.getRiskLevel());
                updateRecord.put("operationType", "UPDATE");
                updateRecord.put("operationTime", entity.getUpdateTime());
                updateRecord.put("operator", entity.getUpdateBy());
                updateRecord.put("remark", entity.getIdentificationRemark());
                historyList.add(updateRecord);
            }
        }
        return historyList;
    }
}

