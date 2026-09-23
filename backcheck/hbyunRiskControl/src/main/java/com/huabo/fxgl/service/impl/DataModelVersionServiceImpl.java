package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.StringUtil;
import com.huabo.fxgl.dto.DataModelVersionCreateDTO;
import com.huabo.fxgl.dto.DataModelVersionQueryDTO;
import com.huabo.fxgl.entity.TblDataModel;
import com.huabo.fxgl.entity.TblDataModelVersion;
import com.huabo.fxgl.mapper.TblDataModelMapper;
import com.huabo.fxgl.mapper.TblDataModelVersionMapper;
import com.huabo.fxgl.service.DataModelVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 数据模型版本管理服务实现类
 *
 * @author AI Assistant
 * @since 2025-09-28
 */
@Slf4j
@Service
public class DataModelVersionServiceImpl extends ServiceImpl<TblDataModelVersionMapper, TblDataModelVersion> 
        implements DataModelVersionService {

    @Autowired
    private TblDataModelVersionMapper versionMapper;

    @Autowired
    private TblDataModelMapper modelMapper;

    @Override
    public IPage<TblDataModelVersion> getVersionPage(DataModelVersionQueryDTO queryDTO) {
        Page<TblDataModelVersion> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        return versionMapper.selectVersionPage(page, queryDTO.getModelId(), queryDTO.getStatus());
    }

    @Override
    public List<TblDataModelVersion> getVersionsByModelId(String modelId) {
        return versionMapper.selectByModelId(modelId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblDataModelVersion createVersion(DataModelVersionCreateDTO createDTO, String createUser) {
        try {
            // 检查模型是否存在
            TblDataModel model = modelMapper.selectById(createDTO.getModelId());
            if (model == null) {
                throw new RuntimeException("模型不存在");
            }

            // 生成版本号
            String versionNo = createDTO.getVersionNo();
            if (StringUtil.isEmpty(versionNo)) {
                versionNo = getNextVersionNo(createDTO.getModelId());
            }

            // 检查版本号是否已存在
            if (!isVersionNoAvailable(createDTO.getModelId(), versionNo)) {
                throw new RuntimeException("版本号已存在: " + versionNo);
            }

            // 生成版本ID
            String versionId = UUID.randomUUID().toString().replace("-", "");

            // 复制模型数据到版本表（优先从当前版本复制）
            int result = copyFromCurrentVersionOrModel(versionId, createDTO.getModelId(), versionNo,
                    createDTO.getChangeDescription(), createUser);

            if (result <= 0) {
                throw new RuntimeException("创建版本失败");
            }

            // 查询创建的版本
            TblDataModelVersion version = versionMapper.selectById(versionId);
            log.info("成功创建模型版本: modelId={}, versionNo={}, versionId={}", 
                    createDTO.getModelId(), versionNo, versionId);

            return version;

        } catch (Exception e) {
            log.error("创建模型版本失败: modelId={}, error={}", createDTO.getModelId(), e.getMessage(), e);
            throw new RuntimeException("创建版本失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteVersion(String versionId, String deleteUser) {
        try {
            TblDataModelVersion version = versionMapper.selectById(versionId);
            if (version == null) {
                throw new RuntimeException("版本不存在");
            }

            // 检查是否为当前版本
            if ("Y".equals(version.getIsCurrent())) {
                throw new RuntimeException("不能删除当前版本");
            }

            // 检查版本状态
            if ("PUBLISHED".equals(version.getStatus())) {
                throw new RuntimeException("不能删除已发布的版本");
            }

            int result = versionMapper.deleteById(versionId);
            log.info("成功删除模型版本: versionId={}, deleteUser={}", versionId, deleteUser);

            return result > 0;

        } catch (Exception e) {
            log.error("删除模型版本失败: versionId={}, error={}", versionId, e.getMessage(), e);
            throw new RuntimeException("删除版本失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishVersion(String versionId, String publishUser) {
        try {
            TblDataModelVersion version = versionMapper.selectById(versionId);
            if (version == null) {
                throw new RuntimeException("版本不存在");
            }

            if (!"TESTING".equals(version.getStatus()) && !"DRAFT".equals(version.getStatus())) {
                throw new RuntimeException("只有草稿或测试状态的版本才能发布");
            }

            int result = versionMapper.publishVersion(versionId, publishUser);
            log.info("成功发布模型版本: versionId={}, publishUser={}", versionId, publishUser);

            return result > 0;

        } catch (Exception e) {
            log.error("发布模型版本失败: versionId={}, error={}", versionId, e.getMessage(), e);
            throw new RuntimeException("发布版本失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean archiveVersion(String versionId, String archiveUser) {
        try {
            TblDataModelVersion version = versionMapper.selectById(versionId);
            if (version == null) {
                throw new RuntimeException("版本不存在");
            }

            // 检查是否为当前版本
            if ("Y".equals(version.getIsCurrent())) {
                throw new RuntimeException("不能归档当前版本");
            }

            int result = versionMapper.archiveVersion(versionId, archiveUser);
            log.info("成功归档模型版本: versionId={}, archiveUser={}", versionId, archiveUser);

            return result > 0;

        } catch (Exception e) {
            log.error("归档模型版本失败: versionId={}, error={}", versionId, e.getMessage(), e);
            throw new RuntimeException("归档版本失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setCurrentVersion(String versionId, String updateUser) {
        try {
            TblDataModelVersion version = versionMapper.selectById(versionId);
            if (version == null) {
                throw new RuntimeException("版本不存在");
            }

            if (!"PUBLISHED".equals(version.getStatus())) {
                throw new RuntimeException("只有已发布的版本才能设置为当前版本");
            }

            // 1. 更新版本表的IS_CURRENT标记
            int result = versionMapper.updateCurrentVersion(version.getModelId(), versionId);

            // 2. 同步版本数据到主模型表
            if (result > 0) {
                syncVersionToModel(version, updateUser);
            }

            log.info("成功设置当前版本: versionId={}, updateUser={}", versionId, updateUser);

            return result > 0;

        } catch (Exception e) {
            log.error("设置当前版本失败: versionId={}, error={}", versionId, e.getMessage(), e);
            throw new RuntimeException("设置当前版本失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rollbackToVersion(String versionId, String rollbackUser) {
        try {
            // 回滚实际上就是设置当前版本
            return setCurrentVersion(versionId, rollbackUser);

        } catch (Exception e) {
            log.error("回滚到版本失败: versionId={}, error={}", versionId, e.getMessage(), e);
            throw new RuntimeException("回滚失败: " + e.getMessage());
        }
    }

    @Override
    public TblDataModelVersion getVersionDetail(String versionId) {
        return versionMapper.selectById(versionId);
    }

    @Override
    public TblDataModelVersion getCurrentVersion(String modelId) {
        return versionMapper.selectCurrentVersion(modelId);
    }

    @Override
    public TblDataModelVersion getLatestVersion(String modelId) {
        return versionMapper.selectLatestVersion(modelId);
    }

    @Override
    public Map<String, Object> compareVersions(String sourceVersionId, String targetVersionId) {
        try {
            TblDataModelVersion sourceVersion = versionMapper.selectById(sourceVersionId);
            TblDataModelVersion targetVersion = versionMapper.selectById(targetVersionId);

            if (sourceVersion == null || targetVersion == null) {
                throw new RuntimeException("版本不存在");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("sourceVersion", sourceVersion);
            result.put("targetVersion", targetVersion);

            // 比较主要字段
            List<Map<String, Object>> differences = new ArrayList<>();
            
            compareField(differences, "模型名称", "modelName", sourceVersion.getModelName(), targetVersion.getModelName());
            compareField(differences, "SQL语句", "sqlStatement", sourceVersion.getSqlStatement(), targetVersion.getSqlStatement());
            compareField(differences, "业务含义", "businessMeaning", sourceVersion.getBusinessMeaning(), targetVersion.getBusinessMeaning());
            compareField(differences, "计算逻辑", "calculationLogic", sourceVersion.getCalculationLogic(), targetVersion.getCalculationLogic());
            compareField(differences, "阈值配置", "thresholdConfig", sourceVersion.getThresholdConfig(), targetVersion.getThresholdConfig());
            compareField(differences, "预警配置", "warningConfig", sourceVersion.getWarningConfig(), targetVersion.getWarningConfig());

            result.put("differences", differences);
            result.put("differenceCount", differences.size());

            return result;

        } catch (Exception e) {
            log.error("比较版本失败: sourceVersionId={}, targetVersionId={}, error={}", 
                    sourceVersionId, targetVersionId, e.getMessage(), e);
            throw new RuntimeException("比较版本失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getVersionStatistics(String modelId) {
        try {
            List<TblDataModelVersion> versions = versionMapper.selectByModelId(modelId);
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalCount", versions.size());
            
            // 按状态统计
            Map<String, Long> statusCount = new HashMap<>();
            statusCount.put("DRAFT", versions.stream().filter(v -> "DRAFT".equals(v.getStatus())).count());
            statusCount.put("TESTING", versions.stream().filter(v -> "TESTING".equals(v.getStatus())).count());
            statusCount.put("PUBLISHED", versions.stream().filter(v -> "PUBLISHED".equals(v.getStatus())).count());
            statusCount.put("ARCHIVED", versions.stream().filter(v -> "ARCHIVED".equals(v.getStatus())).count());
            
            statistics.put("statusCount", statusCount);
            
            // 当前版本
            TblDataModelVersion currentVersion = getCurrentVersion(modelId);
            statistics.put("currentVersion", currentVersion);
            
            // 最新版本
            TblDataModelVersion latestVersion = getLatestVersion(modelId);
            statistics.put("latestVersion", latestVersion);

            return statistics;

        } catch (Exception e) {
            log.error("获取版本统计失败: modelId={}, error={}", modelId, e.getMessage(), e);
            throw new RuntimeException("获取版本统计失败: " + e.getMessage());
        }
    }

    @Override
    public boolean isVersionNoAvailable(String modelId, String versionNo) {
        return versionMapper.countByVersionNo(modelId, versionNo) == 0;
    }

    @Override
    public String getNextVersionNo(String modelId) {
        String nextVersionNo = versionMapper.getNextVersionNo(modelId);
        return StringUtil.isEmpty(nextVersionNo) ? "v01" : nextVersionNo;
    }

    /**
     * 比较字段值
     */
    private void compareField(List<Map<String, Object>> differences, String fieldLabel, String fieldName,
                             Object sourceValue, Object targetValue) {
        String sourceStr = sourceValue == null ? "" : sourceValue.toString();
        String targetStr = targetValue == null ? "" : targetValue.toString();

        if (!Objects.equals(sourceStr, targetStr)) {
            Map<String, Object> diff = new HashMap<>();
            diff.put("fieldLabel", fieldLabel);
            diff.put("fieldName", fieldName);
            diff.put("sourceValue", sourceStr);
            diff.put("targetValue", targetStr);
            diff.put("diffType", sourceStr.isEmpty() ? "ADDED" : (targetStr.isEmpty() ? "DELETED" : "MODIFIED"));
            differences.add(diff);
        }
    }

    /**
     * 优先从当前版本复制数据，如果没有当前版本则从主模型表复制
     */
    private int copyFromCurrentVersionOrModel(String versionId, String modelId, String versionNo,
                                             String changeDescription, String createUser) {
        try {
            // 1. 先尝试从当前版本复制
            TblDataModelVersion currentVersion = versionMapper.selectCurrentVersion(modelId);

            if (currentVersion != null) {
                // 从当前版本复制
                log.info("从当前版本复制数据: modelId={}, currentVersionId={}", modelId, currentVersion.getVersionId());
                return copyFromVersion(versionId, currentVersion, versionNo, changeDescription, createUser);
            } else {
                // 从主模型表复制
                log.info("从主模型表复制数据: modelId={}", modelId);
                return versionMapper.copyFromModel(versionId, modelId, versionNo, changeDescription, createUser);
            }

        } catch (Exception e) {
            log.error("复制版本数据失败: modelId={}, error={}", modelId, e.getMessage(), e);
            throw new RuntimeException("复制版本数据失败: " + e.getMessage());
        }
    }

    /**
     * 从指定版本复制数据创建新版本
     */
    private int copyFromVersion(String newVersionId, TblDataModelVersion sourceVersion, String versionNo,
                               String changeDescription, String createUser) {
        try {
            TblDataModelVersion newVersion = new TblDataModelVersion();

            // 设置新版本信息
            newVersion.setVersionId(newVersionId);
            newVersion.setModelId(sourceVersion.getModelId());
            newVersion.setVersionNo(versionNo);
            newVersion.setChangeDescription(changeDescription);
            newVersion.setCreateUser(createUser);
            newVersion.setCreateTime(LocalDateTime.now());

            // 复制源版本的所有字段
            newVersion.setModelCode(sourceVersion.getModelCode());
            newVersion.setModelName(sourceVersion.getModelName());
            newVersion.setModelType(sourceVersion.getModelType());
            newVersion.setBusinessMeaning(sourceVersion.getBusinessMeaning());
            newVersion.setCalculationLogic(sourceVersion.getCalculationLogic());
            newVersion.setDataSourceId(sourceVersion.getDataSourceId());
            newVersion.setSqlStatement(sourceVersion.getSqlStatement()); // 关键：复制SQL语句
            newVersion.setWithClause(sourceVersion.getWithClause());
            newVersion.setSelectClause(sourceVersion.getSelectClause());
            newVersion.setFromClause(sourceVersion.getFromClause());
            newVersion.setWhereClause(sourceVersion.getWhereClause());
            newVersion.setGroupByClause(sourceVersion.getGroupByClause());
            newVersion.setHavingClause(sourceVersion.getHavingClause());
            newVersion.setOrderByClause(sourceVersion.getOrderByClause());
            newVersion.setDragConfig(sourceVersion.getDragConfig());
            newVersion.setThresholdConfig(sourceVersion.getThresholdConfig());
            newVersion.setWarningConfig(sourceVersion.getWarningConfig());
            newVersion.setTemplateId(sourceVersion.getTemplateId());
            newVersion.setParameterConfig(sourceVersion.getParameterConfig());
            newVersion.setTemplateType(sourceVersion.getTemplateType());

            // 设置新版本的默认状态
            newVersion.setStatus("DRAFT");
            newVersion.setIsEnabled("N");
            newVersion.setIsCurrent("N");

            // 保存新版本
            return versionMapper.insert(newVersion);

        } catch (Exception e) {
            log.error("从版本复制数据失败: sourceVersionId={}, error={}", sourceVersion.getVersionId(), e.getMessage(), e);
            throw new RuntimeException("从版本复制数据失败: " + e.getMessage());
        }
    }

    /**
     * 同步版本数据到主模型表
     */
    private void syncVersionToModel(TblDataModelVersion version, String updateUser) {
        try {
            // 使用原生SQL更新主模型表的关键字段
            String updateSql = "UPDATE TBL_DATA_MODEL SET " +
                    "MODEL_NAME = ?, " +
                    "BUSINESS_MEANING = ?, " +
                    "CALCULATION_LOGIC = ?, " +
                    "SQL_STATEMENT = ?, " +
                    "WITH_CLAUSE = ?, " +
                    "SELECT_CLAUSE = ?, " +
                    "FROM_CLAUSE = ?, " +
                    "WHERE_CLAUSE = ?, " +
                    "GROUP_BY_CLAUSE = ?, " +
                    "HAVING_CLAUSE = ?, " +
                    "ORDER_BY_CLAUSE = ?, " +
                    "DRAG_CONFIG = ?, " +
                    "THRESHOLD_CONFIG = ?, " +
                    "WARNING_CONFIG = ?, " +
                    "TEMPLATE_ID = ?, " +
                    "PARAMETER_CONFIG = ?, " +
                    "UPDATE_USER = ?, " +
                    "UPDATE_TIME = SYSDATE " +
                    "WHERE MODEL_ID = ?";

            // 执行更新
            int updateResult = versionMapper.updateModelFromVersion(
                    version.getModelName(),
                    version.getBusinessMeaning(),
                    version.getCalculationLogic(),
                    version.getSqlStatement(),
                    version.getWithClause(),
                    version.getSelectClause(),
                    version.getFromClause(),
                    version.getWhereClause(),
                    version.getGroupByClause(),
                    version.getHavingClause(),
                    version.getOrderByClause(),
                    version.getDragConfig(),
                    version.getThresholdConfig(),
                    version.getWarningConfig(),
                    version.getTemplateId(),
                    version.getParameterConfig(),
                    updateUser,
                    version.getModelId()
            );

            if (updateResult > 0) {
                log.info("成功同步版本数据到主模型表: modelId={}, versionId={}",
                        version.getModelId(), version.getVersionId());
            } else {
                log.warn("同步版本数据到主模型表失败: modelId={}, versionId={}",
                        version.getModelId(), version.getVersionId());
            }

        } catch (Exception e) {
            log.error("同步版本数据到主模型表异常: modelId={}, versionId={}, error={}",
                    version.getModelId(), version.getVersionId(), e.getMessage(), e);
            throw new RuntimeException("同步版本数据失败: " + e.getMessage());
        }
    }
}
