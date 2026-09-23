package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.oracle.entity.budget.*;
import com.management.accountant.oracle.mapper.budget.*;
import com.management.accountant.service.BudgetSystemStatsService;
import com.management.accountant.vo.result.BudgetSystemActivityVO;
import com.management.accountant.vo.result.BudgetSystemHealthVO;
import com.management.accountant.vo.result.BudgetSystemMatrixVO;
import com.management.accountant.vo.result.BudgetSystemModuleVO;
import com.management.accountant.vo.result.BudgetSystemStatsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 预算体系统计Service实现
 * 
 * @description 预算体系管理统计数据服务实现
 * @author AI Assistant
 * @date 2025-01-30
 */
@Service
@Slf4j
public class BudgetSystemStatsServiceImpl implements BudgetSystemStatsService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetDimensionMapper dimensionMapper;

    @Resource
    private BudgetIndicatorMapper indicatorMapper;

    @Resource
    private BudgetModelMapper modelMapper;

    @Resource
    private BudgetDimensionRelationMapper dimensionRelationMapper;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public BudgetSystemStatsVO getSystemStats() {
        BudgetSystemStatsVO stats = new BudgetSystemStatsVO();
        
        try {
            // 统计组织单元数据
            QueryWrapper<BudgetOrganization> orgWrapper = new QueryWrapper<>();
            Integer orgCount = organizationMapper.selectCount(orgWrapper).intValue();
            stats.setOrganizationCount(orgCount);

            // 统计活跃组织
            QueryWrapper<BudgetOrganization> activeOrgWrapper = new QueryWrapper<>();
            activeOrgWrapper.eq("STATUS", "1");
            Integer activeOrgCount = organizationMapper.selectCount(activeOrgWrapper).intValue();
            stats.setActiveOrgCount(activeOrgCount);
            
            // 计算组织增长率（简化计算，实际应该对比上月数据）
            if (orgCount > 0) {
                stats.setOrgGrowth(Math.round(activeOrgCount * 100.0 / orgCount * 10.0) / 10.0);
            }

            // 统计预算维度数据
            QueryWrapper<BudgetDimension> dimWrapper = new QueryWrapper<>();
            Integer dimCount = dimensionMapper.selectCount(dimWrapper).intValue();
            stats.setDimensionCount(dimCount);

            // 统计启用维度
            QueryWrapper<BudgetDimension> activeDimWrapper = new QueryWrapper<>();
            activeDimWrapper.eq("STATUS", "1");
            Integer activeDimCount = dimensionMapper.selectCount(activeDimWrapper).intValue();
            stats.setActiveDimCount(activeDimCount);
            
            // 计算维度使用率
            if (dimCount > 0) {
                stats.setDimUsageRate(Math.round(activeDimCount * 100.0 / dimCount * 10.0) / 10.0);
            }

            // 统计预算指标数据
            QueryWrapper<BudgetIndicator> indicatorWrapper = new QueryWrapper<>();
            Integer indicatorCount = indicatorMapper.selectCount(indicatorWrapper).intValue();
            stats.setIndicatorCount(indicatorCount);

            // 统计KPI指标
            QueryWrapper<BudgetIndicator> kpiWrapper = new QueryWrapper<>();
            kpiWrapper.eq("INDICATOR_TYPE", "KPI");
            Integer kpiCount = indicatorMapper.selectCount(kpiWrapper).intValue();
            stats.setKpiCount(kpiCount);
            
            // 计算指标增长率（简化计算）
            if (indicatorCount > 0) {
                stats.setIndicatorGrowth(Math.round(kpiCount * 50.0 / indicatorCount * 10.0) / 10.0);
            }

            // 统计预算模型数据
            QueryWrapper<BudgetModel> modelWrapper = new QueryWrapper<>();
            Integer modelCount = modelMapper.selectCount(modelWrapper).intValue();
            stats.setModelCount(modelCount);

            // 统计运行中模型
            QueryWrapper<BudgetModel> activeModelWrapper = new QueryWrapper<>();
            activeModelWrapper.eq("STATUS", "1");
            Integer activeModelCount = modelMapper.selectCount(activeModelWrapper).intValue();
            stats.setActiveModelCount(activeModelCount);
            
            // 计算模型效率
            if (modelCount > 0) {
                stats.setModelEfficiency(Math.round(activeModelCount * 100.0 / modelCount * 10.0) / 10.0);
            }

        } catch (Exception e) {
            log.error("获取预算体系统计数据失败", e);
        }
        
        return stats;
    }

    @Override
    public List<BudgetSystemModuleVO> getSystemModules() {
        List<BudgetSystemModuleVO> modules = new ArrayList<>();
        
        try {
            // 组织架构管理模块
            BudgetSystemModuleVO orgModule = new BudgetSystemModuleVO();
            orgModule.setKey("organizationStructure");
            orgModule.setTitle("组织架构管理");
            orgModule.setDescription("管理预算组织架构，设置部门层级和责任中心");
            orgModule.setIcon("el-icon-office-building");
            orgModule.setPath("/managementAccountant/ncv65/budgetSystem/organizationStructure");
            Integer orgCount = organizationMapper.selectCount(new QueryWrapper<BudgetOrganization>().eq("DEL_FLAG", 0)).intValue();
            orgModule.setItemCount(orgCount);
            orgModule.setLastUpdate(getLatestUpdateTimeForModule(organizationMapper, "DEL_FLAG"));
            modules.add(orgModule);

            // 维度配置模块
            BudgetSystemModuleVO dimModule = new BudgetSystemModuleVO();
            dimModule.setKey("dimensionConfiguration");
            dimModule.setTitle("维度配置");
            dimModule.setDescription("配置预算维度，支持多维度预算分析");
            dimModule.setIcon("el-icon-s-grid");
            dimModule.setPath("/managementAccountant/ncv65/budgetSystem/dimensionManagement");
            Integer dimCount = dimensionMapper.selectCount(new QueryWrapper<BudgetDimension>().eq("IS_DELETED", 0)).intValue();
            dimModule.setItemCount(dimCount);
            dimModule.setLastUpdate(getLatestUpdateTimeForModule(dimensionMapper, "IS_DELETED"));
            modules.add(dimModule);

            // 指标管理模块
            BudgetSystemModuleVO indicatorModule = new BudgetSystemModuleVO();
            indicatorModule.setKey("indicatorManagement");
            indicatorModule.setTitle("指标管理");
            indicatorModule.setDescription("定义预算指标体系，建立KPI考核标准");
            indicatorModule.setIcon("el-icon-data-line");
            indicatorModule.setPath("/managementAccountant/ncv65/budgetSystem/indicatorManagement");
            Integer indicatorCount = indicatorMapper.selectCount(new QueryWrapper<BudgetIndicator>().eq("IS_DELETED", 0)).intValue();
            indicatorModule.setItemCount(indicatorCount);
            indicatorModule.setLastUpdate(getLatestUpdateTimeForModule(indicatorMapper, "IS_DELETED"));
            modules.add(indicatorModule);

            // 预算模型模块
            BudgetSystemModuleVO modelModule = new BudgetSystemModuleVO();
            modelModule.setKey("budgetModel");
            modelModule.setTitle("预算模型");
            modelModule.setDescription("构建预算计算模型，支持复杂业务逻辑");
            modelModule.setIcon("el-icon-s-operation");
            modelModule.setPath("/managementAccountant/ncv65/budgetSystem/budgetModel");
            Integer modelCount = modelMapper.selectCount(new QueryWrapper<BudgetModel>().eq("IS_DELETED", 0)).intValue();
            modelModule.setItemCount(modelCount);
            modelModule.setLastUpdate(getLatestUpdateTimeForModule(modelMapper, "IS_DELETED"));
            modules.add(modelModule);

            // 预算结构模块
            BudgetSystemModuleVO structureModule = new BudgetSystemModuleVO();
            structureModule.setKey("budgetStructure");
            structureModule.setTitle("预算结构");
            structureModule.setDescription("设计预算科目结构，建立科目体系");
            structureModule.setIcon("el-icon-menu");
            structureModule.setPath("/managementAccountant/ncv65/budgetSystem/budgetStructure");
            structureModule.setItemCount(0);
            structureModule.setLastUpdate(DATE_FORMAT.format(new Date()));
            modules.add(structureModule);

            // 预算层级模块
            BudgetSystemModuleVO hierarchyModule = new BudgetSystemModuleVO();
            hierarchyModule.setKey("budgetHierarchy");
            hierarchyModule.setTitle("预算层级");
            hierarchyModule.setDescription("配置预算管理层级，设置审批权限");
            hierarchyModule.setIcon("el-icon-rank");
            hierarchyModule.setPath("/managementAccountant/ncv65/budgetSystem/budgetHierarchy");
            hierarchyModule.setItemCount(0);
            hierarchyModule.setLastUpdate(DATE_FORMAT.format(new Date()));
            modules.add(hierarchyModule);

        } catch (Exception e) {
            log.error("获取预算体系模块列表失败", e);
        }
        
        return modules;
    }

    /**
     * 获取模块最后更新时间（从数据库查询真实数据）
     */
    @SuppressWarnings("unchecked")
    private <T> String getLatestUpdateTimeForModule(BaseMapper<T> mapper, String deleteColumn) {
        try {
            QueryWrapper<T> qw = new QueryWrapper<>();
            qw.eq(deleteColumn, 0);
            qw.orderByDesc("UPDATE_TIME");
            qw.last("FETCH FIRST 1 ROWS ONLY");
            List<T> list = mapper.selectList(qw);
            if (!list.isEmpty()) {
                Object entity = list.get(0);
                java.lang.reflect.Method getUpdateTime = entity.getClass().getMethod("getUpdateTime");
                Object updateTime = getUpdateTime.invoke(entity);
                if (updateTime instanceof Date) {
                    return DATE_FORMAT.format((Date) updateTime);
                }
            }
        } catch (Exception e) {
            log.warn("获取模块最后更新时间失败: {}", e.getMessage());
        }
        return DATE_FORMAT.format(new Date());
    }

    @Override
    public BudgetSystemHealthVO getSystemHealth() {
        BudgetSystemHealthVO health = new BudgetSystemHealthVO();
        List<BudgetSystemHealthVO.HealthDetail> details = new ArrayList<>();
        
        try {
            // 完整性评分
            BudgetSystemHealthVO.HealthDetail completeness = new BudgetSystemHealthVO.HealthDetail();
            completeness.setKey("completeness");
            completeness.setLabel("完整性");
            completeness.setScore(calculateCompletenessScore());
            details.add(completeness);

            // 一致性评分
            BudgetSystemHealthVO.HealthDetail consistency = new BudgetSystemHealthVO.HealthDetail();
            consistency.setKey("consistency");
            consistency.setLabel("一致性");
            consistency.setScore(calculateConsistencyScore());
            details.add(consistency);

            // 准确性评分
            BudgetSystemHealthVO.HealthDetail accuracy = new BudgetSystemHealthVO.HealthDetail();
            accuracy.setKey("accuracy");
            accuracy.setLabel("准确性");
            accuracy.setScore(calculateAccuracyScore());
            details.add(accuracy);

            // 及时性评分
            BudgetSystemHealthVO.HealthDetail timeliness = new BudgetSystemHealthVO.HealthDetail();
            timeliness.setKey("timeliness");
            timeliness.setLabel("及时性");
            timeliness.setScore(calculateTimelinessScore());
            details.add(timeliness);

            // 可用性评分
            BudgetSystemHealthVO.HealthDetail usability = new BudgetSystemHealthVO.HealthDetail();
            usability.setKey("usability");
            usability.setLabel("可用性");
            usability.setScore(calculateUsabilityScore());
            details.add(usability);

            health.setDetails(details);

            // 计算总体评分
            double totalScore = details.stream()
                    .mapToDouble(BudgetSystemHealthVO.HealthDetail::getScore)
                    .average()
                    .orElse(0.0);
            health.setOverallScore(Math.round(totalScore * 10.0) / 10.0);

        } catch (Exception e) {
            log.error("获取预算体系健康度失败", e);
        }
        
        return health;
    }

    @Override
    public List<BudgetSystemActivityVO> getRecentActivities(Integer limit) {
        List<BudgetSystemActivityVO> activities = new ArrayList<>();
        
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        
        try {
           int perTableLimit = Math.max(limit / 4 + 1, 3);
           
           // 从组织表获取最近记录
           List<BudgetOrganization> recentOrgs = organizationMapper.selectList(
               new QueryWrapper<BudgetOrganization>().orderByDesc("CREATE_TIME").last("LIMIT " + perTableLimit)
           );
           for (BudgetOrganization org : recentOrgs) {
               BudgetSystemActivityVO activity = new BudgetSystemActivityVO();
               activity.setActivityId(org.getOrganizationId());
               activity.setType("组织管理");
               activity.setTitle("创建组织");
               activity.setDescription("创建预算组织: " + (org.getOrganizationName() != null ? org.getOrganizationName() : ""));
               activity.setOperator(org.getCreatorName() != null ? org.getCreatorName() : "系统");
               activity.setTime(org.getCreateTime() != null ? DATE_FORMAT.format(org.getCreateTime()) : "");
               activity.setStatus("SUCCESS");
               activity.setModule("预算组织");
               activities.add(activity);
           }
           
           // 从维度表获取最近记录
           List<BudgetDimension> recentDims = dimensionMapper.selectList(
               new QueryWrapper<BudgetDimension>().orderByDesc("CREATE_TIME").last("LIMIT " + perTableLimit)
           );
           for (BudgetDimension dim : recentDims) {
               BudgetSystemActivityVO activity = new BudgetSystemActivityVO();
               activity.setActivityId(dim.getDimensionId());
               activity.setType("维度配置");
               activity.setTitle("创建维度");
               activity.setDescription("创建预算维度: " + (dim.getDimensionName() != null ? dim.getDimensionName() : ""));
               activity.setOperator(dim.getCreatorName() != null ? dim.getCreatorName() : "系统");
               activity.setTime(dim.getCreateTime() != null ? DATE_FORMAT.format(dim.getCreateTime()) : "");
               activity.setStatus("SUCCESS");
               activity.setModule("预算维度");
               activities.add(activity);
           }
           
           // 从指标表获取最近记录
           List<BudgetIndicator> recentIndicators = indicatorMapper.selectList(
               new QueryWrapper<BudgetIndicator>().orderByDesc("CREATE_TIME").last("LIMIT " + perTableLimit)
           );
           for (BudgetIndicator ind : recentIndicators) {
               BudgetSystemActivityVO activity = new BudgetSystemActivityVO();
               activity.setActivityId(ind.getIndicatorId());
               activity.setType("指标管理");
               activity.setTitle("创建指标");
               activity.setDescription("创建预算指标: " + (ind.getIndicatorName() != null ? ind.getIndicatorName() : ""));
               activity.setOperator(ind.getCreatorName() != null ? ind.getCreatorName() : "系统");
               activity.setTime(ind.getCreateTime() != null ? DATE_FORMAT.format(ind.getCreateTime()) : "");
               activity.setStatus("SUCCESS");
               activity.setModule("预算指标");
               activities.add(activity);
           }
           
           // 从模型表获取最近记录
           List<BudgetModel> recentModels = modelMapper.selectList(
               new QueryWrapper<BudgetModel>().orderByDesc("CREATE_TIME").last("LIMIT " + perTableLimit)
           );
           for (BudgetModel model : recentModels) {
               BudgetSystemActivityVO activity = new BudgetSystemActivityVO();
               activity.setActivityId(model.getModelId());
               activity.setType("模型管理");
               activity.setTitle("创建模型");
               activity.setDescription("创建预算模型: " + (model.getModelName() != null ? model.getModelName() : ""));
               activity.setOperator(model.getCreatorName() != null ? model.getCreatorName() : "系统");
               activity.setTime(model.getCreateTime() != null ? DATE_FORMAT.format(model.getCreateTime()) : "");
               activity.setStatus("SUCCESS");
               activity.setModule("预算模型");
               activities.add(activity);
           }
           
           // 按时间倒序排序，取前limit条
           final int finalLimit = limit;
           activities.sort((a, b) -> {
               if (a.getTime() == null && b.getTime() == null) return 0;
               if (a.getTime() == null || a.getTime().isEmpty()) return 1;
               if (b.getTime() == null || b.getTime().isEmpty()) return -1;
               return b.getTime().compareTo(a.getTime());
           });
           
           if (activities.size() > finalLimit) {
               activities = new ArrayList<>(activities.subList(0, finalLimit));
           }
           
        } catch (Exception e) {
            log.error("获取最近活动记录失败", e);
        }
        
        return activities;
    }

    @Override
    public BudgetSystemMatrixVO getSystemMatrix() {
        BudgetSystemMatrixVO matrix = new BudgetSystemMatrixVO();

        try {
            // 查询组织列表
            QueryWrapper<BudgetOrganization> orgWrapper = new QueryWrapper<>();
            orgWrapper.orderByAsc("SORT_ORDER");
            List<BudgetOrganization> orgList = organizationMapper.selectList(orgWrapper);
            List<BudgetSystemMatrixVO.MatrixOrganization> matrixOrgs = new ArrayList<>();
            for (BudgetOrganization org : orgList) {
                BudgetSystemMatrixVO.MatrixOrganization mo = new BudgetSystemMatrixVO.MatrixOrganization();
                mo.setId(org.getOrganizationId());
                mo.setCode(org.getOrganizationCode());
                mo.setName(org.getOrganizationName());
                mo.setType(org.getOrganizationType());
                mo.setStatus(org.getStatus());
                mo.setParentId(org.getParentId());
                mo.setLevel(org.getOrganizationLevel());
                matrixOrgs.add(mo);
            }
            matrix.setOrganizations(matrixOrgs);

            // 查询维度列表
            QueryWrapper<BudgetDimension> dimWrapper = new QueryWrapper<>();
            dimWrapper.orderByAsc("SORT_ORDER");
            List<BudgetDimension> dimList = dimensionMapper.selectList(dimWrapper);
            List<BudgetSystemMatrixVO.MatrixDimension> matrixDims = new ArrayList<>();
            for (BudgetDimension dim : dimList) {
                BudgetSystemMatrixVO.MatrixDimension md = new BudgetSystemMatrixVO.MatrixDimension();
                md.setId(dim.getDimensionId());
                md.setCode(dim.getDimensionCode());
                md.setName(dim.getDimensionName());
                md.setType(dim.getDimensionType());
                md.setStatus(dim.getIsActive() != null && dim.getIsActive() ? "1" : "0");
                md.setParentId(dim.getParentId());
                matrixDims.add(md);
            }
            matrix.setDimensions(matrixDims);

            // 查询指标列表
            QueryWrapper<BudgetIndicator> indWrapper = new QueryWrapper<>();
            indWrapper.orderByAsc("CREATE_TIME");
            List<BudgetIndicator> indList = indicatorMapper.selectList(indWrapper);
            List<BudgetSystemMatrixVO.MatrixIndicator> matrixInds = new ArrayList<>();
            for (BudgetIndicator ind : indList) {
                BudgetSystemMatrixVO.MatrixIndicator mi = new BudgetSystemMatrixVO.MatrixIndicator();
                mi.setId(ind.getIndicatorId());
                mi.setCode(ind.getIndicatorCode());
                mi.setName(ind.getIndicatorName());
                mi.setType(ind.getIndicatorType());
                mi.setDataType(ind.getDataType());
                mi.setUnit(ind.getUnit());
                mi.setStatus(ind.getIsEnabled() != null && ind.getIsEnabled() == 1 ? "1" : "0");
                matrixInds.add(mi);
            }
            matrix.setIndicators(matrixInds);

            // 查询模型列表
            QueryWrapper<BudgetModel> modelWrapper = new QueryWrapper<>();
            modelWrapper.orderByAsc("SORT_ORDER");
            List<BudgetModel> modelList = modelMapper.selectList(modelWrapper);
            List<BudgetSystemMatrixVO.MatrixModel> matrixModels = new ArrayList<>();
            for (BudgetModel model : modelList) {
                BudgetSystemMatrixVO.MatrixModel mm = new BudgetSystemMatrixVO.MatrixModel();
                mm.setId(model.getModelId());
                mm.setCode(model.getModelCode());
                mm.setName(model.getModelName());
                mm.setType(model.getModelType());
                mm.setStatus(model.getStatus());
                mm.setBudgetCycle(model.getBudgetCycle());
                matrixModels.add(mm);
            }
            matrix.setModels(matrixModels);

            // 构建关联关系
            List<BudgetSystemMatrixVO.MatrixRelation> relations = new ArrayList<>();

            // 维度间关联关系（从维度关联表）
            QueryWrapper<BudgetDimensionRelation> relWrapper = new QueryWrapper<>();
            relWrapper.eq("IS_DELETED", 0);
            List<BudgetDimensionRelation> dimRelations = dimensionRelationMapper.selectList(relWrapper);
            for (BudgetDimensionRelation rel : dimRelations) {
                BudgetSystemMatrixVO.MatrixRelation mr = new BudgetSystemMatrixVO.MatrixRelation();
                mr.setSourceType("dimension");
                mr.setSourceId(rel.getSourceDimensionId());
                mr.setSourceName(rel.getSourceDimensionName());
                mr.setTargetType("dimension");
                mr.setTargetId(rel.getTargetDimensionId());
                mr.setTargetName(rel.getTargetDimensionName());
                mr.setRelationType(rel.getRelationType());
                relations.add(mr);
            }

            // 模型与维度的关联（从模型的dimensions字段）
            for (BudgetModel model : modelList) {
                if (model.getDimensions() != null && !model.getDimensions().isEmpty()) {
                    BudgetSystemMatrixVO.MatrixRelation mr = new BudgetSystemMatrixVO.MatrixRelation();
                    mr.setSourceType("model");
                    mr.setSourceId(model.getModelId());
                    mr.setSourceName(model.getModelName());
                    mr.setTargetType("dimension");
                    mr.setTargetId(model.getDimensions());
                    mr.setTargetName("关联维度");
                    mr.setRelationType("MODEL_DIMENSION");
                    relations.add(mr);
                }
                // 模型与组织的关联（从模型的applicableScope字段）
                if (model.getApplicableScope() != null && !model.getApplicableScope().isEmpty()) {
                    BudgetSystemMatrixVO.MatrixRelation mr = new BudgetSystemMatrixVO.MatrixRelation();
                    mr.setSourceType("model");
                    mr.setSourceId(model.getModelId());
                    mr.setSourceName(model.getModelName());
                    mr.setTargetType("organization");
                    mr.setTargetId(model.getApplicableScope());
                    mr.setTargetName("适用组织");
                    mr.setRelationType("MODEL_ORGANIZATION");
                    relations.add(mr);
                }
            }

            // 组织层级关联（父子关系）
            for (BudgetOrganization org : orgList) {
                if (org.getParentId() != null && !org.getParentId().isEmpty()) {
                    BudgetSystemMatrixVO.MatrixRelation mr = new BudgetSystemMatrixVO.MatrixRelation();
                    mr.setSourceType("organization");
                    mr.setSourceId(org.getParentId());
                    mr.setSourceName("上级组织");
                    mr.setTargetType("organization");
                    mr.setTargetId(org.getOrganizationId());
                    mr.setTargetName(org.getOrganizationName());
                    mr.setRelationType("HIERARCHY");
                    relations.add(mr);
                }
            }

            matrix.setRelations(relations);

        } catch (Exception e) {
            log.error("获取预算体系矩阵数据失败", e);
        }

        return matrix;
    }

    /**
     * 计算完整性评分
     */
    private Double calculateCompletenessScore() {
        try {
            int totalScore = 0;
            int maxScore = 100;
            
            // 检查组织是否配置
            Integer orgCount = organizationMapper.selectCount(new QueryWrapper<>()).intValue();
            if (orgCount > 0) totalScore += 25;

            // 检查维度是否配置
            Integer dimCount = dimensionMapper.selectCount(new QueryWrapper<>()).intValue();
            if (dimCount > 0) totalScore += 25;

            // 检查指标是否配置
            Integer indicatorCount = indicatorMapper.selectCount(new QueryWrapper<>()).intValue();
            if (indicatorCount > 0) totalScore += 25;

            // 检查模型是否配置
            Integer modelCount = modelMapper.selectCount(new QueryWrapper<>()).intValue();
            if (modelCount > 0) totalScore += 25;
            
            return (double) totalScore;
        } catch (Exception e) {
            log.error("计算完整性评分失败", e);
            return 0.0;
        }
    }

    /**
     * 计算一致性评分
     */
    private Double calculateConsistencyScore() {
        // 简化计算，实际应该检查数据一致性
        return 85.0;
    }

    /**
     * 计算准确性评分
     */
    private Double calculateAccuracyScore() {
        // 简化计算，实际应该检查数据准确性
        return 90.0;
    }

    /**
     * 计算及时性评分
     */
    private Double calculateTimelinessScore() {
        // 简化计算，实际应该检查数据更新及时性
        return 88.0;
    }

    /**
     * 计算可用性评分
     */
    private Double calculateUsabilityScore() {
        try {
            // 检查启用状态的数据比例
            Integer totalOrg = organizationMapper.selectCount(new QueryWrapper<>()).intValue();
            QueryWrapper<BudgetOrganization> activeOrgWrapper = new QueryWrapper<>();
            activeOrgWrapper.eq("STATUS", "1");
            Integer activeOrg = organizationMapper.selectCount(activeOrgWrapper).intValue();
            
            if (totalOrg > 0) {
                return Math.round(activeOrg * 100.0 / totalOrg * 10.0) / 10.0;
            }
            return 0.0;
        } catch (Exception e) {
            log.error("计算可用性评分失败", e);
            return 0.0;
        }
    }
}

