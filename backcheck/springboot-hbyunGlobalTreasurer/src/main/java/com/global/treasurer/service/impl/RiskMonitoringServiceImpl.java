package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RiskMonitoringDTO;
import com.global.treasurer.dto.RiskMonitoringQueryDTO;
import com.global.treasurer.entity.TblRiskMonitoring;
import com.global.treasurer.mapper.RiskMonitoringMapper;
import com.global.treasurer.service.IRiskMonitoringService;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 风险监控Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-03-24
 */
@Service
public class RiskMonitoringServiceImpl extends ServiceImpl<RiskMonitoringMapper, TblRiskMonitoring>
        implements IRiskMonitoringService {

    @Autowired
    private UserProvider userProvider;

    private String getLoginUserName() {
        try {
            com.hbfk.entity.TblStaffUtil user = userProvider.get();
            return user != null ? user.getUsername() : "system";
        } catch (Exception e) {
            return "system";
        }
    }

    @Override
    public PageInfo<TblRiskMonitoring> selectRiskMonitoringList(RiskMonitoringQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        LambdaQueryWrapper<TblRiskMonitoring> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getMonitoringNo()), TblRiskMonitoring::getMonitoringNo, queryDTO.getMonitoringNo())
                .eq(queryDTO.getRiskTypeId() != null, TblRiskMonitoring::getRiskTypeId, queryDTO.getRiskTypeId())
                .eq(StringUtils.hasText(queryDTO.getRiskStatus()), TblRiskMonitoring::getRiskStatus, queryDTO.getRiskStatus())
                .eq(queryDTO.getOrgId() != null, TblRiskMonitoring::getOrgId, queryDTO.getOrgId());
        if (StringUtils.hasText(queryDTO.getMonitoringDateStart())) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                wrapper.ge(TblRiskMonitoring::getMonitoringDate, sdf.parse(queryDTO.getMonitoringDateStart()));
            } catch (Exception ignored) {}
        }
        if (StringUtils.hasText(queryDTO.getMonitoringDateEnd())) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(queryDTO.getMonitoringDateEnd()));
                cal.add(Calendar.DAY_OF_MONTH, 1);
                wrapper.lt(TblRiskMonitoring::getMonitoringDate, cal.getTime());
            } catch (Exception ignored) {}
        }
        wrapper.orderByDesc(TblRiskMonitoring::getCreateTime);
        List<TblRiskMonitoring> list = this.list(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblRiskMonitoring selectRiskMonitoringById(Long recordId) {
        return this.getById(recordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRiskMonitoring insertRiskMonitoring(RiskMonitoringDTO dto) {
        TblRiskMonitoring entity = new TblRiskMonitoring();
        BeanUtils.copyProperties(dto, entity);
        entity.setCreateTime(new Date());
        entity.setCreateBy(getLoginUserName());
        if (!StringUtils.hasText(entity.getMonitoringNo())) {
            entity.setMonitoringNo("MON-" + System.currentTimeMillis());
        }
        if (!StringUtils.hasText(entity.getRiskStatus())) {
            entity.setRiskStatus("NORMAL");
        }
        this.save(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRiskMonitoring updateRiskMonitoring(RiskMonitoringDTO dto) {
        TblRiskMonitoring entity = this.getById(dto.getRecordId());
        if (entity == null) throw new RuntimeException("风险监控记录不存在");
        BeanUtils.copyProperties(dto, entity);
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(getLoginUserName());
        this.updateById(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRiskMonitoring(Long recordId) {
        return this.removeById(recordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteRiskMonitorings(List<Long> recordIds) {
        return this.removeByIds(recordIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRiskMonitoring triggerAlert(Long recordId, String alertMessage) {
        TblRiskMonitoring entity = this.getById(recordId);
        if (entity == null) throw new RuntimeException("风险监控记录不存在");
        entity.setAlertTriggered(true);
        entity.setAlertMessage(alertMessage);
        entity.setRiskStatus("WARNING");
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(getLoginUserName());
        this.updateById(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRiskMonitoring handleMonitoring(Long recordId, String actionTaken) {
        TblRiskMonitoring entity = this.getById(recordId);
        if (entity == null) throw new RuntimeException("风险监控记录不存在");
        entity.setActionTaken(actionTaken);
        entity.setHandledAt(new Date());
        entity.setRiskStatus("NORMAL");
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(getLoginUserName());
        this.updateById(entity);
        return entity;
    }

    @Override
    public Map<String, Object> getOverview(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<TblRiskMonitoring> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(orgId != null, TblRiskMonitoring::getOrgId, orgId);
        List<TblRiskMonitoring> all = this.list(wrapper);
        result.put("total", all.size());
        result.put("normal", all.stream().filter(r -> "NORMAL".equals(r.getRiskStatus())).count());
        result.put("warning", all.stream().filter(r -> "WARNING".equals(r.getRiskStatus())).count());
        result.put("critical", all.stream().filter(r -> "CRITICAL".equals(r.getRiskStatus()) || "BREACH".equals(r.getRiskStatus())).count());
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void exportRiskMonitoring(Map<String, Object> body, HttpServletResponse response) {
        try {
            List<TblRiskMonitoring> dataList;
            List<?> ids = (List<?>) body.get("ids");
            if (ids != null && !ids.isEmpty()) {
                List<Long> recordIds = new ArrayList<>();
                for (Object id : ids) {
                    recordIds.add(Long.valueOf(id.toString()));
                }
                dataList = this.listByIds(recordIds);
            } else {
                RiskMonitoringQueryDTO queryDTO = new RiskMonitoringQueryDTO();
                if (body.get("pageNum") != null) queryDTO.setPageNum(Integer.valueOf(body.get("pageNum").toString()));
                if (body.get("pageSize") != null) queryDTO.setPageSize(Integer.valueOf(body.get("pageSize").toString()));
                if (body.get("monitoringNo") != null) queryDTO.setMonitoringNo(body.get("monitoringNo").toString());
                if (body.get("riskStatus") != null) queryDTO.setRiskStatus(body.get("riskStatus").toString());
                if (body.get("orgId") != null) queryDTO.setOrgId(Long.valueOf(body.get("orgId").toString()));
                if (body.get("monitoringDateStart") != null) queryDTO.setMonitoringDateStart(body.get("monitoringDateStart").toString());
                if (body.get("monitoringDateEnd") != null) queryDTO.setMonitoringDateEnd(body.get("monitoringDateEnd").toString());
                PageInfo<TblRiskMonitoring> pageInfo = selectRiskMonitoringList(queryDTO);
                dataList = pageInfo.getList();
            }
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=risk_monitoring.xlsx");
            org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.xssf.usermodel.XSSFSheet sheet = workbook.createSheet("风险监控");
            String[] headers = {"监控编号", "风险类型", "风险状态", "当前风险值", "阈值", "预警值", "临界值", "是否触发警报", "监控日期", "处理措施", "备注"};
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int rowIdx = 1;
            for (TblRiskMonitoring r : dataList) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(nvl(r.getMonitoringNo()));
                row.createCell(1).setCellValue(nvl(r.getRiskTypeName()));
                row.createCell(2).setCellValue(statusText(r.getRiskStatus()));
                row.createCell(3).setCellValue(r.getCurrentRiskValue() != null ? r.getCurrentRiskValue().doubleValue() : 0);
                row.createCell(4).setCellValue(r.getThresholdValue() != null ? r.getThresholdValue().doubleValue() : 0);
                row.createCell(5).setCellValue(r.getWarningValue() != null ? r.getWarningValue().doubleValue() : 0);
                row.createCell(6).setCellValue(r.getCriticalValue() != null ? r.getCriticalValue().doubleValue() : 0);
                row.createCell(7).setCellValue(r.getAlertTriggered() != null && r.getAlertTriggered() ? "是" : "否");
                row.createCell(8).setCellValue(r.getMonitoringDate() != null ? sdf.format(r.getMonitoringDate()) : "");
                row.createCell(9).setCellValue(nvl(r.getActionTaken()));
                row.createCell(10).setCellValue(nvl(r.getRemark()));
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    private String statusText(String status) {
        if (status == null) return "";
        switch (status) {
            case "NORMAL": return "正常";
            case "WARNING": return "预警";
            case "CRITICAL": return "严重";
            case "BREACH": return "违规";
            default: return status;
        }
    }

    private String nvl(String s) { return s == null ? "" : s; }
}

