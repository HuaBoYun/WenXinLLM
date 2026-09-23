package com.financial.sharing.enterpriseReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.enterpriseReport.dto.ReportDataQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblReportData;
import com.financial.sharing.enterpriseReport.mapper.ReportDataMapper;
import com.financial.sharing.enterpriseReport.service.ReportDataService;
import com.hbfk.util.user.UserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 报表数据Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class ReportDataServiceImpl extends ServiceImpl<ReportDataMapper, TblReportData> 
        implements ReportDataService {

    @Override
    public List<TblReportData> getList(ReportDataQueryParam param) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        return baseMapper.selectReportDataList(
            tenantId,
            param.getTaskId(),
            param.getTemplateId(),
            param.getIndicatorId(),
            param.getOrgId(),
            param.getPeriod(),
            param.getDataSource()
        );
    }

    @Override
    public TblReportData getDetail(String dataId) {
        TblReportData reportData = this.getById(dataId);
        if (reportData != null) {
            // 补充关联信息
            String tenantId = UserUtils.getUser().getOrgid().toString();
            List<TblReportData> list = baseMapper.selectReportDataList(
                tenantId, null, null, null, null, null, null
            );
            for (TblReportData data : list) {
                if (data.getDataId().equals(dataId)) {
                    reportData.setTaskName(data.getTaskName());
                    reportData.setTemplateName(data.getTemplateName());
                    reportData.setIndicatorName(data.getIndicatorName());
                    reportData.setOrgName(data.getOrgName());
                    break;
                }
            }
        }
        return reportData;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveReportData(TblReportData reportData) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();
        
        reportData.setTenantId(tenantId);
        
        if (StringUtils.isEmpty(reportData.getDataId())) {
            // 新增
            reportData.setCreateUser(userId);
            reportData.setCreateTime(now);
            reportData.setUpdateUser(userId);
            reportData.setUpdateTime(now);
            
            // 设置默认数据来源
            if (StringUtils.isEmpty(reportData.getDataSource())) {
                reportData.setDataSource("MANUAL");
            }
            
            // 设置默认可编辑
            if (StringUtils.isEmpty(reportData.getIsEditable())) {
                reportData.setIsEditable("Y");
            }
            
            return this.save(reportData);
        } else {
            // 修改
            reportData.setUpdateUser(userId);
            reportData.setUpdateTime(now);
            
            return this.updateById(reportData);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveReportData(List<TblReportData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return true;
        }
        
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();
        
        // 分离新增和修改的数据
        for (TblReportData reportData : dataList) {
            reportData.setTenantId(tenantId);
            
            if (StringUtils.isEmpty(reportData.getDataId())) {
                // 新增
                reportData.setDataId(UUID.randomUUID().toString().replace("-", ""));
                reportData.setCreateUser(userId);
                reportData.setCreateTime(now);
                reportData.setUpdateUser(userId);
                reportData.setUpdateTime(now);
                
                // 设置默认值
                if (StringUtils.isEmpty(reportData.getDataSource())) {
                    reportData.setDataSource("MANUAL");
                }
                if (StringUtils.isEmpty(reportData.getIsEditable())) {
                    reportData.setIsEditable("Y");
                }
            } else {
                // 修改
                reportData.setUpdateUser(userId);
                reportData.setUpdateTime(now);
            }
        }
        
        // 批量保存
        return this.saveOrUpdateBatch(dataList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteReportData(String dataId) {
        TblReportData reportData = this.getById(dataId);
        if (reportData == null) {
            throw new RuntimeException("数据不存在");
        }
        
        return this.removeById(dataId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCondition(String taskId, String templateId, String orgId, String period) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        int count = baseMapper.deleteByCondition(taskId, templateId, orgId, period, tenantId);
        
        return count > 0;
    }

    @Override
    public List<TblReportData> getDataByCondition(String taskId, String templateId, String orgId, String period) {
        String tenantId = UserUtils.getUser().getOrgid().toString();

        QueryWrapper<TblReportData> wrapper = new QueryWrapper<>();
        wrapper.eq("TENANT_ID", tenantId);

        if (!StringUtils.isEmpty(taskId)) {
            wrapper.eq("TASK_ID", taskId);
        }
        if (!StringUtils.isEmpty(templateId)) {
            wrapper.eq("TEMPLATE_ID", templateId);
        }
        if (!StringUtils.isEmpty(orgId)) {
            wrapper.eq("ORG_ID", orgId);
        }
        if (!StringUtils.isEmpty(period)) {
            wrapper.eq("PERIOD", period);
        }

        wrapper.orderByDesc("CREATE_TIME");

        return this.list(wrapper);
    }
}


