package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.sharing.entity.TblGeneralStandard;
import com.financial.sharing.entity.TblGeneralStandardLevel;
import com.financial.sharing.entity.TblGeneralStandardCondition;
import com.financial.sharing.mapper.TblGeneralStandardMapper;
import com.financial.sharing.mapper.TblGeneralStandardLevelMapper;
import com.financial.sharing.mapper.TblGeneralStandardConditionMapper;
import com.financial.sharing.service.TblGeneralStandardService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.hbfk.util.RandowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 通用标准服务实现类
 *
 * @author Financial Sharing System
 * @since 2025-01-30
 */
@Slf4j
@Service
public class TblGeneralStandardServiceImpl implements TblGeneralStandardService {

    @Autowired
    private TblGeneralStandardMapper generalStandardMapper;

    @Autowired
    private TblGeneralStandardLevelMapper generalStandardLevelMapper;

    @Autowired
    private TblGeneralStandardConditionMapper conditionMapper;

    @Override
    public MyJsonBean<PageResult<TblGeneralStandard>> getList(Object param) {
        try {
            log.info("查询通用标准列表，参数: {}", param);

            // 解析查询参数
            Map<String, Object> paramMap = (Map<String, Object>) param;
            String orgId = (String) paramMap.get("orgId");
            String standardName = (String) paramMap.get("standardName");
            String standardType = (String) paramMap.get("standardType");
            Integer isEnabled = (Integer) paramMap.get("isEnabled");
            Integer pageNo = (Integer) paramMap.getOrDefault("pageNo", 1);
            Integer pageSize = (Integer) paramMap.getOrDefault("pageSize", 10);

            // 创建分页对象
            Page<TblGeneralStandard> page = new Page<>(pageNo, pageSize);

            // 构建查询条件
            QueryWrapper<TblGeneralStandard> wrapper = new QueryWrapper<>();

            // 标准名称模糊查询
            if (!StringUtils.isEmpty(standardName)) {
                wrapper.lambda().like(TblGeneralStandard::getStandardName, standardName);
            }

            // 标准类型精确查询
            if (!StringUtils.isEmpty(standardType)) {
                wrapper.lambda().eq(TblGeneralStandard::getStandardType, standardType);
            }

            // 是否启用查询
            if (isEnabled != null) {
                wrapper.lambda().eq(TblGeneralStandard::getIsEnabled, isEnabled);
            }

            // 按创建时间倒序排列
            wrapper.lambda().orderByDesc(TblGeneralStandard::getCreateTime);

            // 执行分页查询
            IPage<TblGeneralStandard> pageResult = generalStandardMapper.selectPage(page, wrapper);

            // 封装返回结果
            PageResult<TblGeneralStandard> result = new PageResult<>();
            result.setTotalRecord((int) pageResult.getTotal());
            result.setCurrentPage((int) pageResult.getCurrent());
            result.setTotalPage((int) pageResult.getPages());
            result.setPageSize((int) pageResult.getSize());
            result.setTlist(pageResult.getRecords());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询通用标准列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<TblGeneralStandard> getById(String standardId) {
        try {
            log.info("查询通用标准详情，standardId: {}", standardId);

            if (standardId == null || standardId.trim().isEmpty()) {
                return MyJsonBean.errorData("标准ID不能为空");
            }

            // TODO: 实现详情查询逻辑
            // 1. 调用Mapper查询数据库
            // 2. 封装返回结果

            TblGeneralStandard standard = new TblGeneralStandard();
            standard.setStandardId(standardId);

            return MyJsonBean.successData(standard);
        } catch (Exception e) {
            log.error("查询通用标准详情失败，standardId: {}", standardId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(Object param) {
        try {
            log.info("保存或更新通用标准，参数: {}", param);

            // 解析保存参数
            Map<String, Object> paramMap = (Map<String, Object>) param;

            // 创建实体对象
            TblGeneralStandard standard = new TblGeneralStandard();

            String standardId = (String) paramMap.get("standardId");
            standard.setStandardCode((String) paramMap.get("standardCode"));
            standard.setStandardName((String) paramMap.get("standardName"));
            standard.setStandardType((String) paramMap.get("standardType"));
            standard.setLevelCount((Integer) paramMap.get("levelCount"));
            standard.setApplicableScope((String) paramMap.get("applicableScope"));
            standard.setDescription((String) paramMap.get("description"));
            standard.setRemark((String) paramMap.get("remark"));

            // 处理日期字段
            Object effectiveDateObj = paramMap.get("effectiveDate");
            if (effectiveDateObj != null) {
                if (effectiveDateObj instanceof String) {
                    String dateStr = (String) effectiveDateObj;
                    // 处理 ISO 8601 格式的日期字符串
                    if (dateStr.contains("T")) {
                        standard.setEffectiveDate(LocalDate.parse(dateStr.substring(0, 10)));
                    } else {
                        standard.setEffectiveDate(LocalDate.parse(dateStr));
                    }
                } else if (effectiveDateObj instanceof Date) {
                    standard.setEffectiveDate(((Date) effectiveDateObj).toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate());
                }
            }

            Object expiryDateObj = paramMap.get("expiryDate");
            if (expiryDateObj != null) {
                if (expiryDateObj instanceof String) {
                    String dateStr = (String) expiryDateObj;
                    if (dateStr.contains("T")) {
                        standard.setExpiryDate(LocalDate.parse(dateStr.substring(0, 10)));
                    } else {
                        standard.setExpiryDate(LocalDate.parse(dateStr));
                    }
                } else if (expiryDateObj instanceof Date) {
                    standard.setExpiryDate(((Date) expiryDateObj).toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate());
                }
            }

            // 处理布尔值
            Object isEnabledObj = paramMap.get("isEnabled");
            if (isEnabledObj != null) {
                if (isEnabledObj instanceof Boolean) {
                    standard.setIsEnabled(((Boolean) isEnabledObj) ? 1 : 0);
                } else if (isEnabledObj instanceof Integer) {
                    standard.setIsEnabled((Integer) isEnabledObj);
                }
            } else {
                standard.setIsEnabled(1); // 默认启用
            }

            LocalDateTime now = LocalDateTime.now();

            // 判断是新增还是更新
            if (standardId == null || standardId.trim().isEmpty()) {
                // 新增
                standard.setStandardId(RandowUtil.uuId());
                standard.setCreateTime(now);
                standard.setCreateUser("SYSTEM"); // TODO: 从当前登录用户获取
                standard.setUpdateTime(now);
                standard.setUpdateUser("SYSTEM");

                generalStandardMapper.insert(standard);
                log.info("新增通用标准成功，standardId: {}", standard.getStandardId());
            } else {
                // 更新
                standard.setStandardId(standardId);
                standard.setUpdateTime(now);
                standard.setUpdateUser("SYSTEM"); // TODO: 从当前登录用户获取

                generalStandardMapper.updateById(standard);
                log.info("更新通用标准成功，standardId: {}", standardId);
            }

            return MyJsonBean.successMsg("保存成功");
        } catch (Exception e) {
            log.error("保存或更新通用标准失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String standardId) {
        try {
            log.info("删除通用标准，standardId: {}", standardId);

            if (standardId == null || standardId.trim().isEmpty()) {
                return MyJsonBean.errorData("标准ID不能为空");
            }

            // TODO: 实现删除逻辑
            // 1. 验证标准是否存在
            // 2. 验证是否可以删除（未被引用等）
            // 3. 删除关联的级别配置和适用条件
            // 4. 调用Mapper删除数据
            // 5. 返回操作结果

            return MyJsonBean.successMsg("删除成功");
        } catch (Exception e) {
            log.error("删除通用标准失败，standardId: {}", standardId, e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean updateStatus(String standardId, Integer isEnabled) {
        try {
            log.info("更新通用标准状态，standardId: {}, isEnabled: {}", standardId, isEnabled);

            if (standardId == null || standardId.trim().isEmpty()) {
                return MyJsonBean.errorData("标准ID不能为空");
            }

            if (isEnabled == null) {
                return MyJsonBean.errorData("状态值不能为空");
            }

            // TODO: 实现状态更新逻辑
            // 1. 验证标准是否存在
            // 2. 设置更新时间
            // 3. 调用Mapper更新状态
            // 4. 返回操作结果

            return MyJsonBean.successMsg("状态更新成功");
        } catch (Exception e) {
            log.error("更新通用标准状态失败，standardId: {}", standardId, e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean copyStandard(String standardId) {
        try {
            log.info("复制通用标准，standardId: {}", standardId);

            if (standardId == null || standardId.trim().isEmpty()) {
                return MyJsonBean.errorData("标准ID不能为空");
            }

            // TODO: 实现标准复制逻辑
            // 1. 验证原标准是否存在
            // 2. 复制标准主表数据（生成新的ID）
            // 3. 复制级别配置数据
            // 4. 复制适用条件数据
            // 5. 返回新标准信息

            Map<String, Object> result = new HashMap<>();
            result.put("newStandardId", "");
            result.put("standardName", "");

            return MyJsonBean.successData("复制成功", result);
        } catch (Exception e) {
            log.error("复制通用标准失败，standardId: {}", standardId, e);
            return MyJsonBean.errorData("复制失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getLevelConfigs(String standardId) {
        try {
            log.info("获取标准级别配置，standardId: {}", standardId);

            if (standardId == null || standardId.trim().isEmpty()) {
                return MyJsonBean.errorData("标准ID不能为空");
            }

            // 查询级别配置数据
            QueryWrapper<TblGeneralStandardLevel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("STANDARD_ID", standardId);
            queryWrapper.orderByAsc("SORT_ORDER", "LEVEL_CODE");

            List<TblGeneralStandardLevel> levelList = generalStandardLevelMapper.selectList(queryWrapper);

            // 转换为前端需要的格式
            List<Map<String, Object>> levelConfigs = new ArrayList<>();
            for (TblGeneralStandardLevel level : levelList) {
                Map<String, Object> levelMap = new HashMap<>();
                levelMap.put("levelId", level.getLevelId());
                levelMap.put("standardId", level.getStandardId());

                // 将 levelCode (LEVEL_001) 转换为 levelNumber (1)
                String levelCode = level.getLevelCode();
                if (levelCode != null && levelCode.startsWith("LEVEL_")) {
                    try {
                        int levelNumber = Integer.parseInt(levelCode.substring(6));
                        levelMap.put("levelNumber", levelNumber);
                    } catch (NumberFormatException e) {
                        levelMap.put("levelNumber", 1);
                    }
                } else {
                    levelMap.put("levelNumber", 1);
                }

                levelMap.put("levelName", level.getLevelName());

                // 将 standardValue (BigDecimal) 转换为 standardAmount (number)
                if (level.getStandardValue() != null) {
                    levelMap.put("standardAmount", level.getStandardValue().doubleValue());
                } else {
                    levelMap.put("standardAmount", 0);
                }

                levelMap.put("unit", level.getUnit());
                levelMap.put("description", level.getRemark() != null ? level.getRemark() : "");
                levelMap.put("remark", level.getRemark() != null ? level.getRemark() : "");

                levelConfigs.add(levelMap);
            }

            log.info("查询到 {} 条级别配置数据", levelConfigs.size());
            return MyJsonBean.successData(levelConfigs);
        } catch (Exception e) {
            log.error("获取标准级别配置失败，standardId: {}", standardId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveLevelConfigs(String standardId, Object levels) {
        try {
            log.info("保存标准级别配置，standardId: {}", standardId);

            if (standardId == null || standardId.trim().isEmpty()) {
                return MyJsonBean.errorData("标准ID不能为空");
            }

            if (levels == null) {
                return MyJsonBean.errorData("级别配置数据不能为空");
            }

            // 1. 验证标准是否存在
            TblGeneralStandard standard = generalStandardMapper.selectById(standardId);
            if (standard == null) {
                return MyJsonBean.errorData("标准不存在");
            }

            // 2. 解析级别配置数据
            List<Map<String, Object>> levelList;
            try {
                if (levels instanceof List) {
                    levelList = (List<Map<String, Object>>) levels;
                } else if (levels instanceof String) {
                    // 如果是字符串，尝试解析为JSON
                    ObjectMapper objectMapper = new ObjectMapper();
                    levelList = objectMapper.readValue((String) levels,
                        new TypeReference<List<Map<String, Object>>>() {});
                } else {
                    return MyJsonBean.errorData("级别配置数据格式不正确");
                }
            } catch (Exception e) {
                log.error("解析级别配置数据失败", e);
                return MyJsonBean.errorData("级别配置数据格式不正确: " + e.getMessage());
            }

            if (levelList == null || levelList.isEmpty()) {
                return MyJsonBean.errorData("级别配置数据不能为空");
            }

            // 3. 删除原有的级别配置
            QueryWrapper<TblGeneralStandardLevel> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("STANDARD_ID", standardId);
            generalStandardLevelMapper.delete(deleteWrapper);
            log.info("删除原有级别配置，standardId: {}", standardId);

            // 4. 构建新的级别配置列表
            List<TblGeneralStandardLevel> newLevels = new ArrayList<>();
            for (int i = 0; i < levelList.size(); i++) {
                Map<String, Object> levelData = levelList.get(i);

                TblGeneralStandardLevel level = new TblGeneralStandardLevel();

                // 生成新的级别ID
                level.setLevelId(RandowUtil.uuId());
                level.setStandardId(standardId);

                // 级别编码：LEVEL_001, LEVEL_002, ...
                Integer levelNumber = (Integer) levelData.get("levelNumber");
                if (levelNumber == null) {
                    levelNumber = i + 1;
                }
                level.setLevelCode(String.format("LEVEL_%03d", levelNumber));

                // 级别名称
                String levelName = (String) levelData.get("levelName");
                if (levelName == null || levelName.trim().isEmpty()) {
                    levelName = levelNumber + "级标准";
                }
                level.setLevelName(levelName);

                // 标准金额 -> 标准值
                Object standardAmountObj = levelData.get("standardAmount");
                if (standardAmountObj != null) {
                    BigDecimal standardValue;
                    if (standardAmountObj instanceof BigDecimal) {
                        standardValue = (BigDecimal) standardAmountObj;
                    } else if (standardAmountObj instanceof Number) {
                        standardValue = new BigDecimal(standardAmountObj.toString());
                    } else {
                        standardValue = new BigDecimal(standardAmountObj.toString());
                    }
                    level.setStandardValue(standardValue);
                } else {
                    level.setStandardValue(BigDecimal.ZERO);
                }

                // 单位（可选）
                String unit = (String) levelData.get("unit");
                level.setUnit(unit != null ? unit : "元");

                // 排序号
                level.setSortOrder(levelNumber);

                // 备注：合并 description 和 remark
                String description = (String) levelData.get("description");
                String remark = (String) levelData.get("remark");
                StringBuilder remarkBuilder = new StringBuilder();
                if (description != null && !description.trim().isEmpty()) {
                    remarkBuilder.append(description);
                }
                if (remark != null && !remark.trim().isEmpty()) {
                    if (remarkBuilder.length() > 0) {
                        remarkBuilder.append("; ");
                    }
                    remarkBuilder.append(remark);
                }
                level.setRemark(remarkBuilder.toString());

                // 创建时间和创建人
                level.setCreateTime(LocalDateTime.now());
                level.setCreateUser("SYSTEM"); // TODO: 从当前用户上下文获取

                newLevels.add(level);
            }

            // 5. 批量插入新的级别配置
            for (TblGeneralStandardLevel level : newLevels) {
                generalStandardLevelMapper.insert(level);
            }

            log.info("保存标准级别配置成功，standardId: {}, 级别数量: {}", standardId, newLevels.size());
            return MyJsonBean.successData("保存成功", newLevels.size());
        } catch (Exception e) {
            log.error("保存标准级别配置失败，standardId: {}", standardId, e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getConditions(String standardId) {
        try {
            log.info("获取标准适用条件，standardId: {}", standardId);

            if (standardId == null || standardId.trim().isEmpty()) {
                return MyJsonBean.errorData("标准ID不能为空");
            }

            // 查询适用条件数据
            QueryWrapper<TblGeneralStandardCondition> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("STANDARD_ID", standardId);
            queryWrapper.orderByAsc("PRIORITY");

            List<TblGeneralStandardCondition> conditionList = conditionMapper.selectList(queryWrapper);

            // 转换为前端需要的格式
            List<Map<String, Object>> conditions = new ArrayList<>();
            for (TblGeneralStandardCondition condition : conditionList) {
                Map<String, Object> conditionMap = new HashMap<>();
                conditionMap.put("conditionId", condition.getConditionId());
                conditionMap.put("standardId", condition.getStandardId());
                conditionMap.put("conditionExpression", condition.getConditionExpression());
                conditionMap.put("conditionDesc", condition.getConditionDesc());
                conditionMap.put("priority", condition.getPriority());
                conditionMap.put("remark", condition.getRemark());
                conditions.add(conditionMap);
            }

            return MyJsonBean.successData(conditions);
        } catch (Exception e) {
            log.error("获取标准适用条件失败，standardId: {}", standardId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveConditions(String standardId, Object conditions) {
        try {
            log.info("保存适用条件，standardId: {}, conditions: {}", standardId, conditions);

            // 验证输入
            if (standardId == null || standardId.trim().isEmpty()) {
                return MyJsonBean.errorData("标准ID不能为空");
            }

            if (conditions == null) {
                return MyJsonBean.errorData("适用条件数据不能为空");
            }

            // 验证标准是否存在
            TblGeneralStandard standard = generalStandardMapper.selectById(standardId);
            if (standard == null) {
                return MyJsonBean.errorData("标准不存在");
            }

            // 解析条件数据
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> conditionList = objectMapper.convertValue(
                conditions,
                new TypeReference<List<Map<String, Object>>>() {}
            );

            // 删除该标准的所有旧适用条件
            QueryWrapper<TblGeneralStandardCondition> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("STANDARD_ID", standardId);
            conditionMapper.delete(deleteWrapper);
            log.info("删除标准 {} 的旧适用条件", standardId);

            // 批量插入新的适用条件
            LocalDateTime now = LocalDateTime.now();
            for (Map<String, Object> conditionData : conditionList) {
                TblGeneralStandardCondition condition = new TblGeneralStandardCondition();

                // 生成新的条件ID
                condition.setConditionId(RandowUtil.uuId());
                condition.setStandardId(standardId);

                // 设置条件表达式
                String conditionExpression = (String) conditionData.get("conditionExpression");
                if (conditionExpression == null || conditionExpression.trim().isEmpty()) {
                    continue; // 跳过空的条件表达式
                }
                condition.setConditionExpression(conditionExpression);

                // 设置条件描述
                condition.setConditionDesc((String) conditionData.get("conditionDesc"));

                // 设置优先级
                Object priorityObj = conditionData.get("priority");
                if (priorityObj != null) {
                    condition.setPriority(Integer.parseInt(priorityObj.toString()));
                }

                // 设置备注
                condition.setRemark((String) conditionData.get("remark"));

                // 设置创建信息
                condition.setCreateTime(now);
                condition.setCreateUser("SYSTEM"); // TODO: 从当前登录用户获取

                conditionMapper.insert(condition);
            }

            log.info("保存适用条件成功，standardId: {}, 条件数量: {}", standardId, conditionList.size());
            return MyJsonBean.successMsg("保存成功");
        } catch (Exception e) {
            log.error("保存适用条件失败，standardId: {}", standardId, e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> calculate(String standardId, Map<String, Object> params) {
        try {
            log.info("标准计算，standardId: {}, params: {}", standardId, params);

            if (standardId == null || standardId.trim().isEmpty()) {
                return MyJsonBean.errorData("标准ID不能为空");
            }

            if (params == null || params.isEmpty()) {
                return MyJsonBean.errorData("计算参数不能为空");
            }

            // TODO: 实现标准计算逻辑
            // 1. 查询标准配置
            // 2. 根据参数匹配级别
            // 3. 计算结果
            // 4. 返回计算结果

            Map<String, Object> result = new HashMap<>();
            result.put("matchedLevel", "");
            result.put("calculatedValue", "");
            result.put("limitAmount", "");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("标准计算失败，standardId: {}", standardId, e);
            return MyJsonBean.errorData("计算失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getStatistics(String standardId) {
        try {
            log.info("获取标准使用统计，standardId: {}", standardId);

            if (standardId == null || standardId.trim().isEmpty()) {
                return MyJsonBean.errorData("标准ID不能为空");
            }

            // TODO: 实现使用统计查询逻辑
            // 1. 统计标准被引用次数
            // 2. 统计标准使用次数
            // 3. 统计各级别使用情况
            // 4. 封装返回结果

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("referenceCount", 0);
            statistics.put("usageCount", 0);
            statistics.put("levelStatistics", new ArrayList<>());

            return MyJsonBean.successData(statistics);
        } catch (Exception e) {
            log.error("获取标准使用统计失败，standardId: {}", standardId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}
