package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.EnterpriseInfo;
import com.huabo.cybermonitor.mapper.EnterpriseInfoMapper;
import com.huabo.cybermonitor.service.IEnterpriseInfoService;
import com.huabo.cybermonitor.util.ExcelUtil;
import com.huabo.cybermonitor.vo.EnterpriseInfoQueryVO;
import com.huabo.cybermonitor.vo.EnterpriseStatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 企业基础信息表 服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class EnterpriseInfoServiceImpl extends ServiceImpl<EnterpriseInfoMapper, EnterpriseInfo> implements IEnterpriseInfoService {

    @Autowired
    private EnterpriseInfoMapper enterpriseInfoMapper;

    @Override
    public IPage<EnterpriseInfo> getEnterpriseList(EnterpriseInfoQueryVO queryVO) {
        Page<EnterpriseInfo> page = new Page<>(queryVO.getPageNumber().intValue(), queryVO.getPageSize().intValue());
        return enterpriseInfoMapper.selectEnterpriseList(page, queryVO);
    }

    @Override
    public Map<String, Object> getEnterpriseDetail(String enterpriseId) {
        return enterpriseInfoMapper.selectEnterpriseDetail(enterpriseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addEnterprise(EnterpriseInfo enterpriseInfo) {
        try {
            // 设置创建时间
            enterpriseInfo.setCreateTime(new Date());
            
            // 验证统一社会信用代码是否重复
            if (validateCreditCode(enterpriseInfo.getCreditCode(), null)) {
                throw new RuntimeException("统一社会信用代码已存在");
            }
            
            // 验证企业名称是否重复
            if (validateEnterpriseName(enterpriseInfo.getEnterpriseName(), null)) {
                throw new RuntimeException("企业名称已存在");
            }
            
            return save(enterpriseInfo);
        } catch (Exception e) {
            log.error("新增企业失败", e);
            throw new RuntimeException("新增企业失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEnterprise(EnterpriseInfo enterpriseInfo) {
        try {
            // 设置更新时间
            enterpriseInfo.setUpdateTime(new Date());
            
            // 验证统一社会信用代码是否重复
            if (validateCreditCode(enterpriseInfo.getCreditCode(), enterpriseInfo.getEnterpriseId())) {
                throw new RuntimeException("统一社会信用代码已存在");
            }
            
            // 验证企业名称是否重复
            if (validateEnterpriseName(enterpriseInfo.getEnterpriseName(), enterpriseInfo.getEnterpriseId())) {
                throw new RuntimeException("企业名称已存在");
            }
            
            return updateById(enterpriseInfo);
        } catch (Exception e) {
            log.error("更新企业失败", e);
            throw new RuntimeException("更新企业失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEnterprise(String enterpriseId) {
        try {
            // 检查是否有子公司
            List<EnterpriseInfo> childEnterprises = getChildEnterprises(enterpriseId);
            if (!childEnterprises.isEmpty()) {
                throw new RuntimeException("该企业存在子公司，无法删除");
            }
            
            return removeById(enterpriseId);
        } catch (Exception e) {
            log.error("删除企业失败", e);
            throw new RuntimeException("删除企业失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteEnterprise(List<String> enterpriseIds) {
        try {
            for (String enterpriseId : enterpriseIds) {
                // 检查是否有子公司
                List<EnterpriseInfo> childEnterprises = getChildEnterprises(enterpriseId);
                if (!childEnterprises.isEmpty()) {
                    throw new RuntimeException("企业ID：" + enterpriseId + " 存在子公司，无法删除");
                }
            }
            
            return removeByIds(enterpriseIds);
        } catch (Exception e) {
            log.error("批量删除企业失败", e);
            throw new RuntimeException("批量删除企业失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getParentEnterpriseList() {
        return enterpriseInfoMapper.selectParentEnterpriseList();
    }

    @Override
    public boolean validateCreditCode(String creditCode, String excludeId) {
        EnterpriseInfo enterprise = enterpriseInfoMapper.selectByCreditCode(creditCode, excludeId);
        return enterprise != null;
    }

    @Override
    public boolean validateEnterpriseName(String enterpriseName, String excludeId) {
        EnterpriseInfo enterprise = enterpriseInfoMapper.selectByEnterpriseName(enterpriseName, excludeId);
        return enterprise != null;
    }

    @Override
    public EnterpriseStatisticsVO getEnterpriseStatistics() {
        return enterpriseInfoMapper.selectEnterpriseStatistics();
    }

    @Override
    public List<Map<String, Object>> getEnterpriseTypeDistribution() {
        return enterpriseInfoMapper.selectEnterpriseTypeDistribution();
    }

    @Override
    public List<Map<String, Object>> getEnterpriseRegionDistribution() {
        return enterpriseInfoMapper.selectEnterpriseRegionDistribution();
    }

    @Override
    public List<Map<String, Object>> getEnterpriseIndustryDistribution() {
        return enterpriseInfoMapper.selectEnterpriseIndustryDistribution();
    }

    @Override
    public void exportEnterpriseList(EnterpriseInfoQueryVO queryVO, HttpServletResponse response) {
        try {
            List<EnterpriseInfo> enterpriseList = enterpriseInfoMapper.selectEnterpriseListForExport(queryVO);

            // 设置导出的列标题
            String[] headers = {
                "企业名称", "统一社会信用代码", "企业类型", "注册资本(万元)",
                "法定代表人", "成立日期", "监管层级", "企业状态", "上市状态",
                "股票代码", "行业分类代码", "地区代码", "联系人", "联系电话",
                "联系邮箱", "注册地址", "经营范围", "创建时间"
            };

            // 使用现有的ExcelUtil构造方法
            ExcelUtil excelUtil = new ExcelUtil("企业信息列表", headers);

            // 添加数据行
            for (int i = 0; i < enterpriseList.size(); i++) {
                EnterpriseInfo enterprise = enterpriseList.get(i);
                Object[] row = {
                    enterprise.getEnterpriseName(),
                    enterprise.getCreditCode(),
                    getEnterpriseTypeLabel(enterprise.getEnterpriseType()),
                    enterprise.getRegisteredCapital(),
                    enterprise.getLegalRepresentative(),
                    enterprise.getEstablishDate(),
                    getSupervisionLevelLabel(enterprise.getSupervisionLevel()),
                    getEnterpriseStatusLabel(enterprise.getEnterpriseStatus()),
                    getListingStatusLabel(enterprise.getListingStatus()),
                    enterprise.getStockCode(),
                    enterprise.getIndustryCode(),
                    enterprise.getRegionCode(),
                    enterprise.getContactPerson(),
                    enterprise.getContactPhone(),
                    enterprise.getContactEmail(),
                    enterprise.getRegisteredAddress(),
                    enterprise.getBusinessScope(),
                    enterprise.getCreateTime()
                };
                excelUtil.addRow(i + 1, row);
            }

            // 导出Excel
            excelUtil.exportExcel(response, "企业信息列表.xls");
        } catch (Exception e) {
            log.error("导出企业列表失败", e);
            throw new RuntimeException("导出企业列表失败：" + e.getMessage());
        }
    }

    @Override
    public void downloadEnterpriseTemplate(HttpServletResponse response) {
        try {
            String[] headers = {
                "企业名称*", "统一社会信用代码*", "企业类型*", "注册资本(万元)",
                "法定代表人*", "成立日期", "监管层级*", "企业状态", "上市状态",
                "股票代码", "行业分类代码", "地区代码", "联系人", "联系电话",
                "联系邮箱", "注册地址", "经营范围", "备注"
            };

            // 使用现有的ExcelUtil构造方法
            ExcelUtil excelUtil = new ExcelUtil("企业信息导入模板", headers);

            // 添加示例数据
            Object[] exampleRow = {
                "示例企业有限公司", "91110000000000000X", "STATE_OWNED", "10000",
                "张三", "2020-01-01", "CENTRAL", "NORMAL", "UNLISTED",
                "", "C33", "110000", "李四", "13800138000",
                "dev@example.com", "北京市朝阳区示例街道1号", "示例经营范围", "示例备注"
            };
            excelUtil.addRow(1, exampleRow);

            // 导出Excel模板
            excelUtil.exportExcel(response, "企业信息导入模板.xls");
        } catch (Exception e) {
            log.error("下载企业导入模板失败", e);
            throw new RuntimeException("下载企业导入模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importEnterpriseList(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 简化导入逻辑，暂时返回成功结果
            // TODO: 实现完整的Excel解析逻辑
            result.put("successCount", 0);
            result.put("failCount", 0);
            result.put("errorMessages", new ArrayList<>());
            result.put("totalCount", 0);
            result.put("message", "导入功能开发中，请稍后使用");

        } catch (Exception e) {
            log.error("导入企业列表失败", e);
            throw new RuntimeException("导入企业列表失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public List<EnterpriseInfo> getChildEnterprises(String parentEnterpriseId) {
        return enterpriseInfoMapper.selectChildEnterprises(parentEnterpriseId);
    }

    @Override
    public List<EnterpriseInfo> getEnterprisesByType(String enterpriseType) {
        QueryWrapper<EnterpriseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ENTERPRISE_TYPE", enterpriseType);
        queryWrapper.eq("ENTERPRISE_STATUS", EnterpriseInfo.STATUS_NORMAL);
        return list(queryWrapper);
    }

    @Override
    public List<EnterpriseInfo> getEnterprisesBySupervisionLevel(String supervisionLevel) {
        QueryWrapper<EnterpriseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("SUPERVISION_LEVEL", supervisionLevel);
        queryWrapper.eq("ENTERPRISE_STATUS", EnterpriseInfo.STATUS_NORMAL);
        return list(queryWrapper);
    }

    @Override
    public List<EnterpriseInfo> getListedEnterprises() {
        QueryWrapper<EnterpriseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("LISTING_STATUS", EnterpriseInfo.LISTING_LISTED);
        queryWrapper.eq("ENTERPRISE_STATUS", EnterpriseInfo.STATUS_NORMAL);
        return list(queryWrapper);
    }

    @Override
    public List<EnterpriseInfo> getEnterprisesByIndustry(String industryCode) {
        QueryWrapper<EnterpriseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("INDUSTRY_CODE", industryCode);
        queryWrapper.eq("ENTERPRISE_STATUS", EnterpriseInfo.STATUS_NORMAL);
        return list(queryWrapper);
    }

    @Override
    public List<EnterpriseInfo> getEnterprisesByRegion(String regionCode) {
        QueryWrapper<EnterpriseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("REGION_CODE", regionCode);
        queryWrapper.eq("ENTERPRISE_STATUS", EnterpriseInfo.STATUS_NORMAL);
        return list(queryWrapper);
    }

    // 私有方法

    private String getEnterpriseTypeLabel(String type) {
        switch (type) {
            case "STATE_OWNED": return "国有独资";
            case "STATE_HOLDING": return "国有控股";
            case "STATE_PARTICIPATING": return "国有参股";
            default: return type;
        }
    }

    private String getSupervisionLevelLabel(String level) {
        switch (level) {
            case "CENTRAL": return "中央";
            case "LOCAL": return "地方";
            default: return level;
        }
    }

    private String getEnterpriseStatusLabel(String status) {
        switch (status) {
            case "NORMAL": return "正常";
            case "CANCELLED": return "注销";
            case "MERGED": return "合并";
            case "SUSPENDED": return "暂停";
            default: return status;
        }
    }

    private String getListingStatusLabel(String status) {
        switch (status) {
            case "LISTED": return "已上市";
            case "UNLISTED": return "未上市";
            default: return status;
        }
    }


}
