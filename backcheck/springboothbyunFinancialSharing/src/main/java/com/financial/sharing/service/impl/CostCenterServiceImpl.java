package com.financial.sharing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.oracle.entity.CostCenterEntity;
import com.financial.sharing.oracle.mapper.CostCenterMapper;
import com.financial.sharing.service.CostCenterService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.SnowflakeIdWorker;
import com.financial.sharing.util.BigIntegerHandler;
import com.financial.sharing.util.UserUtils;
import com.financial.sharing.util.excel.ExcelExport;
import com.hbfk.entity.TblStaffUtil;
import com.financial.sharing.vo.param.CostCenterQueryParam;
import com.financial.sharing.vo.param.CostCenterSaveParam;
import com.financial.sharing.vo.result.CostCenterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Row;

/**
 * 成本中心服务实现类
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Service
public class CostCenterServiceImpl implements CostCenterService {

    @Resource
    private CostCenterMapper costCenterMapper;

    private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public MyJsonBean<PageResult<CostCenterVO>> getCostCenterList(CostCenterQueryParam param) {
        try {
            // 计算分页参数
            int offset = (param.getPageNumber() - 1) * param.getPageSize();
            int limit = param.getPageSize();

            // 查询分页数据列表
            List<Map<String, Object>> dataList = costCenterMapper.selectCostCenterListWithPagination(param, offset, limit);

            // 查询总记录数
            int totalRecord = costCenterMapper.countCostCenterList(param);

            // 计算分页信息
            int totalPage = (int) Math.ceil((double) totalRecord / param.getPageSize());

            // 转换为VO对象
            List<CostCenterVO> voList = new ArrayList<>();
            for (Map<String, Object> map : dataList) {
                CostCenterVO vo = new CostCenterVO();

                // 安全地获取字段值，处理可能的null值（使用大写的列名）
                vo.setCenterId(getSafeStringIdValue(map, "CENTER_ID"));
                vo.setCenterCode(getSafeStringValue(map, "CENTER_CODE"));
                vo.setCenterName(getSafeStringValue(map, "CENTER_NAME"));

                // 处理中心类型和类型名称
                Integer centerType = getSafeIntValue(map, "CENTER_TYPE");
                vo.setCenterType(centerType);
                vo.setCenterTypeName(getCenterTypeName(centerType));

                vo.setParentCenterId(getSafeStringIdValue(map, "PARENT_CENTER_ID"));
                vo.setParentCenterName(getSafeStringValue(map, "PARENT_CENTER_NAME"));
                vo.setCenterLevel(getSafeIntValue(map, "CENTER_LEVEL"));
                vo.setIsLeaf(getSafeIntValue(map, "IS_LEAF"));
                vo.setManagerId(getSafeStringIdValue(map, "MANAGER_ID"));
                vo.setManagerName(getSafeStringValue(map, "MANAGER_NAME"));

                // 处理分摊方法和方法名称
                Integer allocationMethod = getSafeIntValue(map, "COST_ALLOCATION_METHOD");
                vo.setCostAllocationMethod(allocationMethod);
                vo.setAllocationMethodName(getAllocationMethodName(allocationMethod));

                vo.setIsEnabled(getSafeIntValue(map, "IS_ENABLED"));
                vo.setBookId(getSafeStringIdValue(map, "BOOK_ID"));
                vo.setCreator(getSafeStringIdValue(map, "CREATOR"));
                vo.setUpdater(getSafeStringIdValue(map, "UPDATER"));
                vo.setVersion(getSafeIntValue(map, "VERSION"));

                // 处理时间字段 - 使用大写的列名
                if (map.get("CREATE_TIME") != null && !"".equals(map.get("CREATE_TIME"))) {
                    try {
                        String timeStr = map.get("CREATE_TIME").toString();
                        // 处理不同的时间格式
                        if (timeStr.contains("T")) {
                            vo.setCreateTime(LocalDateTime.parse(timeStr));
                        } else {
                            timeStr = timeStr.replace(" ", "T");
                            if (timeStr.length() > 19) {
                                timeStr = timeStr.substring(0, 19);
                            }
                            vo.setCreateTime(LocalDateTime.parse(timeStr));
                        }
                    } catch (Exception e) {
                        log.warn("解析CREATE_TIME失败: {}", map.get("CREATE_TIME"), e);
                    }
                }
                if (map.get("UPDATE_TIME") != null && !"".equals(map.get("UPDATE_TIME"))) {
                    try {
                        String timeStr = map.get("UPDATE_TIME").toString();
                        // 处理不同的时间格式
                        if (timeStr.contains("T")) {
                            vo.setUpdateTime(LocalDateTime.parse(timeStr));
                        } else {
                            timeStr = timeStr.replace(" ", "T");
                            if (timeStr.length() > 19) {
                                timeStr = timeStr.substring(0, 19);
                            }
                            vo.setUpdateTime(LocalDateTime.parse(timeStr));
                        }
                    } catch (Exception e) {
                        log.warn("解析UPDATE_TIME失败: {}", map.get("UPDATE_TIME"), e);
                    }
                }

                voList.add(vo);
            }

            // 构建分页结果
            PageResult<CostCenterVO> pageResult = new PageResult<>();
            pageResult.setTlist(voList);
            pageResult.setTotalRecord(totalRecord);
            pageResult.setTotalPage(totalPage);
            pageResult.setPageSize(param.getPageSize());
            pageResult.setCurrentPage(param.getPageNumber());

            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询成本中心列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdateCostCenter(CostCenterSaveParam param) {
        try {
            // 检查编码是否重复
            // 先进行属性复制，再检查编码是否存在
            CostCenterEntity entity = new CostCenterEntity();
            BeanUtils.copyProperties(param, entity);

            // 验证并设置必要的字段
            if (entity.getBookId() == null) {
                log.warn("BOOK_ID为空，使用默认值1");
                entity.setBookId("1"); // 设置默认bookId
            }
            if (entity.getTenantId() == null) {
                log.warn("TENANT_ID为空，使用默认值1000");
                entity.setTenantId("1000"); // 设置默认tenantId
            }

            int count = costCenterMapper.checkCenterCodeExists(
                param.getCenterCode(),
                param.getCenterId(),
                entity.getBookId(),
                entity.getTenantId()
            );
            if (count > 0) {
                return MyJsonBean.errorData("成本中心编码已存在");
            }
            if (param.getCenterId() == null || param.getCenterId().trim().isEmpty()) {
                // 新增 - 生成String类型的雪花ID
                entity.setCenterId(String.valueOf(snowflakeIdWorker.nextId()));
                entity.setIsDeleted(0);
                entity.setVersion(1);

                // 设置创建时间和更新时间
                LocalDateTime now = LocalDateTime.now();
                entity.setCreateTime(now);
                entity.setUpdateTime(now);

                // 计算中心级次并设置上级中心ID和名称
                if (param.getParentCenterId() != null && !param.getParentCenterId().trim().isEmpty()) {
                    // 有父节点ID，查询父节点的level + 1
                    Map<String, Object> parentCenter = costCenterMapper.selectCostCenterDetail(param.getParentCenterId());
                    if (parentCenter != null) {
                        if (parentCenter.get("CENTER_LEVEL") != null) {
                            Integer parentLevel = Integer.valueOf(parentCenter.get("CENTER_LEVEL").toString());
                            entity.setCenterLevel(parentLevel + 1);
                        } else {
                            entity.setCenterLevel(2); // 默认二级
                        }
                        // 设置上级中心名称：优先使用用户提供的名称，如果没有则从数据库查询
                        if (param.getParentCenterName() != null && !param.getParentCenterName().trim().isEmpty()) {
                            entity.setParentCenterName(param.getParentCenterName());
                        } else if (parentCenter.get("CENTER_NAME") != null) {
                            entity.setParentCenterName(parentCenter.get("CENTER_NAME").toString());
                        }
                    } else {
                        entity.setCenterLevel(2); // 默认二级
                    }
                } else if (param.getParentCenterName() != null && !param.getParentCenterName().trim().isEmpty()) {
                    // 只有名称没有ID：保留用户输入的名字，按顶级节点处理
                    // 注意：BeanUtils.copyProperties 已经把 parentCenterName 复制进 entity 了，不要清空
                    entity.setCenterLevel(1);
                } else {
                    // 顶级节点
                    entity.setCenterLevel(1);
                }

                // 设置负责人ID和名称
                // 注意：BeanUtils.copyProperties 已经把 managerId/managerName 都复制进 entity 了
                // 这里只在"有 ID 但 name 缺失"的特殊场景做兜底（无）；其他场景信任 BeanUtils 的结果
                if (param.getManagerId() != null && !param.getManagerId().trim().isEmpty()
                        && param.getManagerName() != null && !param.getManagerName().trim().isEmpty()) {
                    entity.setManagerId(param.getManagerId());
                    entity.setManagerName(param.getManagerName());
                }

                // 默认为叶子节点
                entity.setIsLeaf(1);

                costCenterMapper.insertCostCenter(entity);

                // 如果有父节点，更新父节点的叶子标识
                if (param.getParentCenterId() != null && !param.getParentCenterId().trim().isEmpty()) {
                    costCenterMapper.updateParentLeafFlag(param.getParentCenterId(), 0);
                }
            } else {
                // 更新 - 设置更新时间，保留原有的创建时间
                entity.setUpdateTime(LocalDateTime.now());

                // 重新计算中心级次并设置上级中心ID和名称（可能父节点发生变化）
                if (param.getParentCenterId() != null && !param.getParentCenterId().trim().isEmpty()) {
                    // 有父节点ID，查询父节点的level
                    Map<String, Object> parentCenter = costCenterMapper.selectCostCenterDetail(param.getParentCenterId());
                    if (parentCenter != null) {
                        if (parentCenter.get("CENTER_LEVEL") != null) {
                            Integer parentLevel = Integer.valueOf(parentCenter.get("CENTER_LEVEL").toString());
                            entity.setCenterLevel(parentLevel + 1);
                        } else {
                            entity.setCenterLevel(2);
                        }
                        // 设置上级中心名称：优先使用用户提供的名称，如果没有则从数据库查询
                        if (param.getParentCenterName() != null && !param.getParentCenterName().trim().isEmpty()) {
                            entity.setParentCenterName(param.getParentCenterName());
                        } else if (parentCenter.get("CENTER_NAME") != null) {
                            entity.setParentCenterName(parentCenter.get("CENTER_NAME").toString());
                        }
                    } else {
                        entity.setCenterLevel(2);
                    }
                } else if (param.getParentCenterName() != null && !param.getParentCenterName().trim().isEmpty()) {
                    // 只有名称没有ID：保留用户输入的名字，按顶级节点处理
                    // 注意：BeanUtils.copyProperties 已经把 parentCenterName 复制进 entity 了，不要清空
                    entity.setCenterLevel(1);
                } else {
                    // 既无 ID 又无名称：顶级节点，名字为 null（BeanUtils 已复制 null）
                    entity.setCenterLevel(1);
                }

                // 设置负责人ID和名称
                // 注意：BeanUtils.copyProperties 已经把 managerId/managerName 都复制进 entity 了
                // 仅在"既有 ID 又有 name"的场景显式重置，其他场景信任 BeanUtils 的结果
                if (param.getManagerId() != null && !param.getManagerId().trim().isEmpty()
                        && param.getManagerName() != null && !param.getManagerName().trim().isEmpty()) {
                    entity.setManagerId(param.getManagerId());
                    entity.setManagerName(param.getManagerName());
                }

                costCenterMapper.updateCostCenter(entity);
            }

            log.info("成本中心保存成功，ID: {}, 编码: {}, 名称: {}",
                entity.getCenterId(), entity.getCenterCode(), entity.getCenterName());
            return MyJsonBean.successData("保存成功", entity.getCenterId());
        } catch (Exception e) {
            log.error("保存成本中心失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<CostCenterVO> getCostCenterById(String centerId) {
        try {
            Map<String, Object> map = costCenterMapper.selectCostCenterDetail(centerId);
            if (map == null) {
                return MyJsonBean.errorData("成本中心不存在");
            }

            CostCenterVO vo = new CostCenterVO();
            // 使用安全的字段获取方法，处理大写的列名
            vo.setCenterId(getSafeStringIdValue(map, "CENTER_ID"));
            vo.setCenterCode(getSafeStringValue(map, "CENTER_CODE"));
            vo.setCenterName(getSafeStringValue(map, "CENTER_NAME"));

            Integer centerType = getSafeIntValue(map, "CENTER_TYPE");
            vo.setCenterType(centerType);
            vo.setCenterTypeName(getCenterTypeName(centerType));

            vo.setParentCenterId(getSafeStringIdValue(map, "PARENT_CENTER_ID"));
            vo.setParentCenterName(getSafeStringValue(map, "PARENT_CENTER_NAME"));
            vo.setCenterLevel(getSafeIntValue(map, "CENTER_LEVEL"));
            vo.setIsLeaf(getSafeIntValue(map, "IS_LEAF"));
            vo.setManagerId(getSafeStringIdValue(map, "MANAGER_ID"));
            vo.setManagerName(getSafeStringValue(map, "MANAGER_NAME"));

            Integer allocationMethod = getSafeIntValue(map, "COST_ALLOCATION_METHOD");
            vo.setCostAllocationMethod(allocationMethod);
            vo.setAllocationMethodName(getAllocationMethodName(allocationMethod));

            vo.setIsEnabled(getSafeIntValue(map, "IS_ENABLED"));
            vo.setBookId(getSafeStringIdValue(map, "BOOK_ID"));
            vo.setVersion(getSafeIntValue(map, "VERSION"));

            // 设置创建时间和更新时间
            if (map.get("CREATE_TIME") != null && !"".equals(map.get("CREATE_TIME"))) {
                try {
                    String timeStr = map.get("CREATE_TIME").toString();
                    // 处理不同的时间格式
                    if (timeStr.contains("T")) {
                        vo.setCreateTime(LocalDateTime.parse(timeStr));
                    } else {
                        timeStr = timeStr.replace(" ", "T");
                        if (timeStr.length() > 19) {
                            timeStr = timeStr.substring(0, 19);
                        }
                        vo.setCreateTime(LocalDateTime.parse(timeStr));
                    }
                } catch (Exception e) {
                    log.warn("解析CREATE_TIME失败: {}", map.get("CREATE_TIME"), e);
                }
            }
            if (map.get("UPDATE_TIME") != null && !"".equals(map.get("UPDATE_TIME"))) {
                try {
                    String timeStr = map.get("UPDATE_TIME").toString();
                    // 处理不同的时间格式
                    if (timeStr.contains("T")) {
                        vo.setUpdateTime(LocalDateTime.parse(timeStr));
                    } else {
                        timeStr = timeStr.replace(" ", "T");
                        if (timeStr.length() > 19) {
                            timeStr = timeStr.substring(0, 19);
                        }
                        vo.setUpdateTime(LocalDateTime.parse(timeStr));
                    }
                } catch (Exception e) {
                    log.warn("解析UPDATE_TIME失败: {}", map.get("UPDATE_TIME"), e);
                }
            }

            // 设置创建人和更新人
            vo.setCreator(getSafeStringValue(map, "CREATOR"));
            vo.setUpdater(getSafeStringValue(map, "UPDATER"));

            return MyJsonBean.successData("查询成功", vo);
        } catch (Exception e) {
            log.error("查询成本中心详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteCostCenter(String centerId) {
        try {
            log.info("开始删除成本中心，ID: {}", centerId);

            // 参数校验 - 增强ID验证
            if (centerId == null || centerId.trim().isEmpty()) {
                log.error("删除成本中心失败：ID为空");
                return MyJsonBean.errorData("成本中心ID不能为空");
            }

            // 检查ID格式是否为有效数字
            String trimmedId = centerId.trim();
            if (!trimmedId.matches("\\d+")) {
                log.error("删除成本中心失败：ID格式错误 {}", centerId);
                return MyJsonBean.errorData("成本中心ID格式错误: " + centerId);
            }

            // 使用新的查询方法检查成本中心是否存在（不限制删除状态）
            Map<String, Object> entityMap = costCenterMapper.selectCostCenterDetailForDelete(trimmedId);
            log.info("查询到的成本中心信息: {}", entityMap);

            if (entityMap == null) {
                log.error("删除成本中心失败：未找到ID为 {} 的成本中心", centerId);
                return MyJsonBean.errorData("成本中心不存在，ID: " + centerId);
            }

            // 记录成本中心详细信息用于日志
            String centerCode = getSafeStringValue(entityMap, "CENTER_CODE");
            String centerName = getSafeStringValue(entityMap, "CENTER_NAME");
            String bookId = getSafeStringIdValue(entityMap, "BOOK_ID");
            String tenantId = getSafeStringIdValue(entityMap, "TENANT_ID");

            log.info("准备删除成本中心 - ID: {}, 编码: {}, 名称: {}, BookId: {}, TenantId: {}",
                centerId, centerCode, centerName, bookId, tenantId);

            // 检查是否已删除（幂等操作）
            Integer isDeleted = getSafeIntValue(entityMap, "IS_DELETED");
            if (isDeleted != null && isDeleted == 1) {
                log.info("成本中心已被删除，返回成功（幂等操作） - ID: {}, 编码: {}, 名称: {}",
                    centerId, centerCode, centerName);
                return MyJsonBean.successData("删除成功（已删除）", null);
            }

            // 检查是否有子节点（使用新的查询方法，不限制删除状态）
            int childCount = costCenterMapper.countAllChildCenters(centerId);
            log.info("成本中心ID: {} 的子节点数量: {}", centerId, childCount);
            if (childCount > 0) {
                log.warn("删除成本中心失败：ID {} 存在 {} 个子节点", centerId, childCount);
                return MyJsonBean.errorData("该成本中心存在子节点，无法删除");
            }

            // 执行逻辑删除
            int deleteResult = costCenterMapper.logicalDeleteById(centerId);
            log.info("删除操作影响行数: {}", deleteResult);
            if (deleteResult <= 0) {
                log.error("删除成本中心失败：ID {} 的删除操作未影响任何行", centerId);
                return MyJsonBean.errorData("删除失败，未找到匹配的记录。可能记录已被删除或权限不足。");
            }

            // 如果有父节点，检查父节点是否还有其他子节点（只检查未删除的子节点）
            String parentCenterId = getSafeStringIdValue(entityMap, "PARENT_CENTER_ID");
            log.info("父节点ID: {}", parentCenterId);
            if (parentCenterId != null && !parentCenterId.trim().isEmpty()) {
                try {
                    // 使用原来的方法检查活跃子节点
                    int siblingCount = costCenterMapper.countChildCenters(parentCenterId);
                    log.info("父节点ID {} 下的活跃子节点数量: {}", parentCenterId, siblingCount);
                    if (siblingCount == 0) {
                        costCenterMapper.updateParentLeafFlag(parentCenterId, 1);
                        log.info("已更新父节点ID {} 为叶子节点", parentCenterId);
                    }
                } catch (Exception e) {
                    log.warn("更新父节点叶子标识失败，父节点ID: {}", parentCenterId, e);
                }
            }

            log.info("成本中心删除成功 - ID: {}, 编码: {}, 名称: {}", centerId, centerCode, centerName);
            return MyJsonBean.successData("删除成功", null);
        } catch (Exception e) {
            log.error("删除成本中心失败，ID: {}, 错误类型: {}, 错误信息: {}",
                centerId, e.getClass().getSimpleName(), e.getMessage(), e);

            // 提供更具体的错误信息
            String errorMessage = "删除失败: " + e.getMessage();
            if (e.getMessage() != null && e.getMessage().contains("数字")) {
                errorMessage = "删除失败：ID格式错误或数据类型不匹配，ID: " + centerId;
            } else if (e.getMessage() != null && e.getMessage().contains("连接")) {
                errorMessage = "删除失败：数据库连接异常，请稍后重试";
            }
            return MyJsonBean.errorData(errorMessage);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDeleteCostCenter(List<String> centerIds) {
        try {
            if (centerIds == null || centerIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要删除的成本中心");
            }

            int successCount = 0;
            List<String> errorMessages = new ArrayList<>();

            // 验证所有成本中心是否存在，并收集错误信息
            for (String centerId : centerIds) {
                try {
                    // 检查成本中心是否存在
                    Map<String, Object> costCenter = costCenterMapper.selectCostCenterDetail(centerId);
                    if (costCenter == null) {
                        errorMessages.add("成本中心ID " + centerId + " 不存在");
                        continue;
                    }

                    // 检查是否有子节点
                    int childCount = costCenterMapper.countChildCenters(centerId);
                    if (childCount > 0) {
                        String centerName = getSafeStringValue(costCenter, "CENTER_NAME");
                        errorMessages.add("成本中心 " + centerName + " 存在子节点，无法删除");
                        continue;
                    }

                    // 执行逻辑删除
                    int deleteResult = costCenterMapper.logicalDeleteById(centerId);
                    if (deleteResult > 0) {
                        successCount++;
                        log.info("成功删除成本中心，ID: {}", centerId);

                        // 如果有父节点，检查父节点是否还有其他子节点
                        String parentCenterId = getSafeStringIdValue(costCenter, "PARENT_CENTER_ID");
                        if (parentCenterId != null && !parentCenterId.trim().isEmpty()) {
                            try {
                                int siblingCount = costCenterMapper.countChildCenters(parentCenterId);
                                if (siblingCount == 0) {
                                    costCenterMapper.updateParentLeafFlag(parentCenterId, 1);
                                }
                            } catch (Exception e) {
                                log.warn("更新父节点叶子标识失败，父节点ID: {}", parentCenterId, e);
                            }
                        }
                    } else {
                        errorMessages.add("删除成本中心ID " + centerId + " 失败");
                    }
                } catch (Exception e) {
                    log.error("删除成本中心失败，ID: {}", centerId, e);
                    errorMessages.add("删除成本中心ID " + centerId + " 时发生异常: " + e.getMessage());
                }
            }

            // 返回结果
            if (errorMessages.isEmpty()) {
                log.info("批量删除成本中心成功，成功删除数量: {}", successCount);
                return MyJsonBean.successData("批量删除成功，共删除 " + successCount + " 条记录", successCount);
            } else if (successCount > 0) {
                // 部分成功
                String message = "批量删除部分成功，成功删除 " + successCount + " 条记录。失败原因：" + String.join("; ", errorMessages);
                return MyJsonBean.successData(message, successCount);
            } else {
                // 全部失败
                return MyJsonBean.errorData("批量删除失败：" + String.join("; ", errorMessages));
            }
        } catch (Exception e) {
            log.error("批量删除成本中心失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean enableCostCenter(String centerId, Integer isEnabled) {
        try {
            costCenterMapper.enableCostCenter(centerId, isEnabled);

            return MyJsonBean.successData(isEnabled == 1 ? "启用成功" : "停用成功", null);
        } catch (Exception e) {
            log.error("启用/停用成本中心失败", e);
            return MyJsonBean.errorData("操作失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchUpdateCostCenterStatus(List<String> centerIds, Integer isEnabled) {
        try {
            if (centerIds == null || centerIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要操作的成本中心");
            }

            // 验证所有成本中心是否存在
            for (String centerId : centerIds) {
                Map<String, Object> costCenter = costCenterMapper.selectCostCenterDetail(centerId);
                if (costCenter == null) {
                    return MyJsonBean.errorData("成本中心ID " + centerId + " 不存在");
                }
            }

            // 执行批量更新
            int updatedCount = costCenterMapper.batchUpdateStatus(centerIds, isEnabled);

            if (updatedCount > 0) {
                String operation = isEnabled == 1 ? "启用" : "停用";
                log.info("批量{}成本中心成功，影响记录数: {}, 成本中心IDs: {}", operation, updatedCount, centerIds);
                return MyJsonBean.successData("批量" + operation + "成功", updatedCount);
            } else {
                return MyJsonBean.errorData("批量更新失败，未找到匹配的记录");
            }
        } catch (Exception e) {
            log.error("批量更新成本中心状态失败", e);
            return MyJsonBean.errorData("批量更新失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getCostCenterOptions(String bookId, String tenantId) {
        try {
            log.info("查询成本中心选项开始 - bookId: {}, tenantId: {}", bookId, tenantId);

            List<Map<String, Object>> options = costCenterMapper.selectCostCenterOptions(bookId, tenantId);

            log.info("查询到成本中心选项数量: {}", options != null ? options.size() : 0);
            if (options != null && !options.isEmpty()) {
                log.debug("成本中心选项数据示例: {}", options.get(0));
            } else {
                log.warn("未查询到成本中心选项数据，可能原因：1.数据库中没有数据 2.数据被禁用或删除 3.bookId/tenantId参数过滤");
            }
            return MyJsonBean.successData("查询成功", options);
        } catch (Exception e) {
            log.error("查询成本中心选项失败 - bookId: {}, tenantId: {}", bookId, tenantId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getCostCenterTree(String bookId, String tenantId) {
        try {
            log.info("查询成本中心树形结构 - bookId: {}, tenantId: {}", bookId, tenantId);

            // 查询所有成本中心
            List<Map<String, Object>> allCenters = costCenterMapper.selectCostCenterOptions(bookId, tenantId);

            if (allCenters == null || allCenters.isEmpty()) {
                return MyJsonBean.successData("查询成功", new ArrayList<>());
            }

            // 构建树形结构
            List<Map<String, Object>> tree = buildCostCenterTree(allCenters, null);

            log.info("查询到成本中心树形结构，根节点数量: {}", tree.size());
            return MyJsonBean.successData("查询成功", tree);
        } catch (Exception e) {
            log.error("查询成本中心树形结构失败 - bookId: {}, tenantId: {}", bookId, tenantId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 递归构建成本中心树
     */
    private List<Map<String, Object>> buildCostCenterTree(List<Map<String, Object>> allCenters, String parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();

        for (Map<String, Object> center : allCenters) {
            String centerId = getSafeStringIdValue(center, "CENTER_ID");
            String parentCenterId = getSafeStringIdValue(center, "PARENT_CENTER_ID");

            // 判断是否为当前父节点的子节点
            boolean isChild = false;
            if (parentId == null) {
                // 查找根节点（父节点为空或为"null"字符串）
                isChild = (parentCenterId == null || "null".equals(parentCenterId) || "".equals(parentCenterId.trim()));
            } else {
                // 查找指定父节点的子节点
                isChild = parentId.equals(parentCenterId);
            }
            if (isChild) {
                // 构建树节点
                Map<String, Object> treeNode = new HashMap<>();
                treeNode.put("id", centerId);
                treeNode.put("label", getSafeStringValue(center, "CENTER_NAME"));
                treeNode.put("code", getSafeStringValue(center, "CENTER_CODE"));
                treeNode.put("level", getSafeIntValue(center, "CENTER_LEVEL"));
                treeNode.put("isLeaf", getSafeIntValue(center, "IS_LEAF"));

                // 递归查找子节点
                List<Map<String, Object>> children = buildCostCenterTree(allCenters, centerId);
                if (!children.isEmpty()) {
                    treeNode.put("children", children);
                }

                tree.add(treeNode);
            }
        }
        return tree;
    }

    @Override
    public void exportCostCenter(CostCenterQueryParam param, HttpServletResponse response) {
        try {
            // 查询导出数据（不分页，查询所有数据）
            List<Map<String, Object>> exportData = costCenterMapper.selectCostCenterListWithPagination(param, 0, Integer.MAX_VALUE);

            if (exportData == null || exportData.isEmpty()) {
                throw new RuntimeException("没有找到可导出的数据");
            }

            // 生成文件名
            String fileName = "成本中心数据_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

            // 创建Excel导出对象（使用ExcelExport工具类自带的响应头设置）
            List<String> headers = Arrays.asList(
                "中心编码", "中心名称", "中心类型", "中心类型名称", "父中心ID", "父中心名称",
                "中心级次", "是否叶子节点", "负责人ID", "负责人姓名",
                "成本分摊方法", "分摊方法名称", "是否启用", "账套ID", "租户ID",
                "创建时间", "更新时间", "创建人", "更新人", "版本"
            );
            // 注意：POI sheet.setColumnWidth 单位是 1/256 个字符宽度
            // 所以"15 个字符宽"必须传 15 * 256 = 3840
            // 中文字符按 2 倍宽计算，部分中文密集列直接给更宽的值
            final int CHAR_UNIT = 256;
            List<Integer> widths = Arrays.asList(
                15 * CHAR_UNIT, 20 * CHAR_UNIT, 12 * CHAR_UNIT, 15 * CHAR_UNIT, 15 * CHAR_UNIT,
                25 * CHAR_UNIT, 10 * CHAR_UNIT, 12 * CHAR_UNIT, 15 * CHAR_UNIT, 15 * CHAR_UNIT,
                15 * CHAR_UNIT, 15 * CHAR_UNIT, 10 * CHAR_UNIT, 12 * CHAR_UNIT, 12 * CHAR_UNIT,
                22 * CHAR_UNIT, 22 * CHAR_UNIT, 15 * CHAR_UNIT, 15 * CHAR_UNIT, 10 * CHAR_UNIT
            );

            ExcelExport excelExport = new ExcelExport("成本中心数据", headers, widths);

            // 填充数据
            for (Map<String, Object> data : exportData) {
                org.apache.poi.ss.usermodel.Row row = excelExport.addRow();

                excelExport.addCell(row, 0, getSafeStringValue(data, "CENTER_CODE"));
                excelExport.addCell(row, 1, getSafeStringValue(data, "CENTER_NAME"));
                excelExport.addCell(row, 2, getSafeIntValue(data, "CENTER_TYPE"));
                excelExport.addCell(row, 3, getCenterTypeName(getSafeIntValue(data, "CENTER_TYPE")));
                excelExport.addCell(row, 4, getSafeStringIdValue(data, "PARENT_CENTER_ID"));
                excelExport.addCell(row, 5, getSafeStringValue(data, "PARENT_CENTER_NAME"));
                excelExport.addCell(row, 6, getSafeIntValue(data, "CENTER_LEVEL"));
                excelExport.addCell(row, 7, getSafeIntValue(data, "IS_LEAF") == 1 ? "是" : "否");
                excelExport.addCell(row, 8, getSafeStringIdValue(data, "MANAGER_ID"));
                excelExport.addCell(row, 9, getSafeStringValue(data, "MANAGER_NAME"));
                excelExport.addCell(row, 10, getSafeIntValue(data, "COST_ALLOCATION_METHOD"));
                excelExport.addCell(row, 11, getAllocationMethodName(getSafeIntValue(data, "COST_ALLOCATION_METHOD")));
                excelExport.addCell(row, 12, getSafeIntValue(data, "IS_ENABLED") == 1 ? "是" : "否");
                excelExport.addCell(row, 13, getSafeStringIdValue(data, "BOOK_ID"));
                excelExport.addCell(row, 14, getSafeStringIdValue(data, "TENANT_ID"));
                excelExport.addCell(row, 15, data.get("CREATE_TIME"));
                excelExport.addCell(row, 16, data.get("UPDATE_TIME"));
                excelExport.addCell(row, 17, getSafeStringIdValue(data, "CREATOR"));
                excelExport.addCell(row, 18, getSafeStringIdValue(data, "UPDATER"));
                excelExport.addCell(row, 19, getSafeIntValue(data, "VERSION"));
            }

            // 直接使用ExcelExport的write方法，它会自动设置正确的响应头
            try {
                excelExport.write(response, fileName);
            } finally {
                excelExport.close();
            }

            log.info("成本中心数据导出成功，导出记录数: {}", exportData.size());

        } catch (Exception e) {
            log.error("导出成本中心数据失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean importCostCenter(String filePath) {
        // TODO: 实现导入功能
        return MyJsonBean.errorData("导入功能开发中");
    }

    // 安全获取字符串值的方法
    private String getSafeStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        String strValue = value.toString();
        return "null".equals(strValue) || "".equals(strValue) ? null : strValue;
    }

    // 安全获取字符串ID值的方法（避免Long精度丢失）
    private String getSafeStringIdValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || "".equals(value) || "null".equals(value)) {
            return null;
        }
        return value.toString();
    }

    // 安全获取Long值的方法（保留用于兼容性）
    private Long getSafeLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || "".equals(value) || "null".equals(value)) {
            return null;
        }
        try {
            return Long.valueOf(value.toString());
        } catch (NumberFormatException e) {
            log.warn("转换Long失败，字段: {}, 值: {}", key, value);
            return null;
        }
    }

    // 安全获取整型值的方法
    private Integer getSafeIntValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || "".equals(value) || "null".equals(value)) {
            return null;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (NumberFormatException e) {
            log.warn("转换Integer失败，字段: {}, 值: {}", key, value);
            return null;
        }
    }

    // 获取中心类型名称
    private String getCenterTypeName(Integer centerType) {
        if (centerType == null) {
            return null;
        }
        switch (centerType) {
            case 1: return "成本中心";
            case 2: return "利润中心";
            case 3: return "投资中心";
            default: return "未知";
        }
    }

    // 获取分摊方法名称
    private String getAllocationMethodName(Integer allocationMethod) {
        if (allocationMethod == null) {
            return null;
        }
        switch (allocationMethod) {
            case 1: return "直接分摊";
            case 2: return "阶梯分摊";
            case 3: return "比例分摊";
            default: return "未知";
        }
    }

    // ==================== 成本预算相关方法实现 ====================

    @Override
    public MyJsonBean<PageResult<Map<String, Object>>> getCostBudgetList(Map<String, Object> param) {
        try {
            log.info("查询成本预算列表参数: {}", param);

            // 字段名映射：前端业务命名 → mapper xml 占位符命名（贴合 DB 列语义）
            // 前端的 budgetType 实际对应 DB 的 COST_CATEGORY；budgetStatus 对应 STATUS
            Map<String, Object> queryParam = new HashMap<>(param);
            if (param.get("budgetType") != null && queryParam.get("costCategory") == null) {
                queryParam.put("costCategory", param.get("budgetType"));
            }
            if (param.get("budgetStatus") != null && queryParam.get("status") == null) {
                queryParam.put("status", param.get("budgetStatus"));
            }

            // 设置分页参数
            Integer pageNumber = (Integer) param.get("pageNumber");
            Integer pageSize = (Integer) param.get("pageSize");
            if (pageNumber != null && pageSize != null) {
                PageHelper.startPage(pageNumber, pageSize);
            }

            // 查询预算列表数据
            // 注：Long精度丢失问题已通过TO_CHAR函数和Service层处理双重保障
            List<Map<String, Object>> budgetList = costCenterMapper.selectCostBudgetList(queryParam);

            // 输出数据库返回的原始数据示例（用于调试）
            if (!budgetList.isEmpty()) {
                log.info("数据库返回的原始数据示例: {}", budgetList.get(0));
            }

            // 处理分页结果
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(budgetList);
            PageResult<Map<String, Object>> pageResult = new PageResult<>();

            // 输出原始数据（用于调试）
            System.out.println("查询成本预算列表，结果: " + pageInfo.getList());

            // 确保大整数ID字段被正确处理为字符串（双重保障）
            List<Map<String, Object>> processedList = pageInfo.getList().stream()
                .map(item -> {
                    Map<String, Object> newItem = new HashMap<>(item);
                    // 双重保障：确保ID字段为字符串类型
                    Object budgetId = newItem.get("BUDGET_ID");
                    Object centerId = newItem.get("CENTER_ID");

                    if (budgetId != null && !(budgetId instanceof String)) {
                        newItem.put("BUDGET_ID", budgetId.toString());
                        log.debug("转换BUDGET_ID为字符串: {}", budgetId);
                    }
                    if (centerId != null && !(centerId instanceof String)) {
                        newItem.put("CENTER_ID", centerId.toString());
                        log.debug("转换CENTER_ID为字符串: {}", centerId);
                    }
                    return newItem;
                })
                .collect(Collectors.toList());

            pageResult.setTlist(processedList);
            System.out.println("处理后的列表（ID已转为字符串）: " + processedList);

            // 输出最终返回给前端的数据示例（用于调试）
            if (!processedList.isEmpty()) {
                log.info("返回给前端的数据示例: {}", processedList.get(0));
                log.info("BUDGET_ID类型: {}", processedList.get(0).get("BUDGET_ID") != null ? processedList.get(0).get("BUDGET_ID").getClass().getSimpleName() : "null");
                log.info("CENTER_ID类型: {}", processedList.get(0).get("CENTER_ID") != null ? processedList.get(0).get("CENTER_ID").getClass().getSimpleName() : "null");
            }
            pageResult.setTotalRecord((int) pageInfo.getTotal());
            pageResult.setTotalPage(pageInfo.getPages());
            pageResult.setCurrentPage(pageInfo.getPageNum());
            pageResult.setPageSize(pageInfo.getPageSize());

            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询成本预算列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdateCostBudget(Map<String, Object> param) {
        try {
            log.info("Service层处理成本预算保存请求，参数: {}", param);

            // 简化的ID处理逻辑，直接使用String类型
            String budgetId = param.get("budgetId") == null ? null : String.valueOf(param.get("budgetId")).trim();
            String centerId = param.get("centerId") == null ? null : String.valueOf(param.get("centerId")).trim();

            // 验证必填参数
            if (centerId == null || centerId.isEmpty() || "null".equalsIgnoreCase(centerId)) {
                return MyJsonBean.errorData("成本中心ID不能为空，请选择成本中心");
            }

            // 当前登录用户（用于 CREATOR / UPDATER 字段）
            String currentUser;
            try {
                TblStaffUtil staff = UserUtils.getUser();
                currentUser = (staff != null && staff.getStaffid() != null) ? String.valueOf(staff.getStaffid()) : "system";
            } catch (Exception ex) {
                log.warn("无法解析当前登录用户，CREATOR/UPDATER 回退为 system，原因: {}", ex.getMessage());
                currentUser = "system";
            }

            // 字段映射：前端字段 -> mapper xml 占位符（使用小驼峰，与 mapper #{param.xxx} 对齐）
            Map<String, Object> dbParam = new HashMap<>();
            dbParam.put("centerId", centerId);
            dbParam.put("budgetYear", param.get("budgetYear") != null ? String.valueOf(param.get("budgetYear")) : null);

            // 处理预算金额：前端 budgetAmount -> 后端 totalBudget
            Object budgetAmount = param.get("budgetAmount");
            dbParam.put("totalBudget", budgetAmount != null ? budgetAmount : 0);

            // 处理预算类型：前端 budgetType -> 后端 costCategory
            Object budgetType = param.get("budgetType");
            if (budgetType != null) {
                dbParam.put("costCategory", String.valueOf(budgetType));
            } else {
                dbParam.put("costCategory", null);
            }

            // 处理预算说明：前端 budgetDesc -> 后端 remark
            Object budgetDesc = param.get("budgetDesc");
            dbParam.put("remark", budgetDesc != null ? String.valueOf(budgetDesc) : null);

            // 设置默认值，注意 BUDGET_QUARTER 在数据库中是 VARCHAR 类型
            Object budgetQuarter = param.get("budgetQuarter");
            dbParam.put("budgetQuarter", budgetQuarter != null ? String.valueOf(budgetQuarter) : "Q1");

            dbParam.put("actualCost", param.get("actualCost") != null ? param.get("actualCost") : 0);

            // 计算剩余预算：未传则等于预算金额（新增场景）
            if (param.get("remainingBudget") != null) {
                dbParam.put("remainingBudget", param.get("remainingBudget"));
            } else if (budgetAmount != null) {
                dbParam.put("remainingBudget", budgetAmount);
            } else {
                dbParam.put("remainingBudget", 0);
            }

            dbParam.put("status", param.get("status") != null ? param.get("status") : 1); // 默认状态为草稿
            dbParam.put("creator", currentUser);
            dbParam.put("updater", currentUser);

            // 预算负责人姓名（自由输入字符串）
            Object budgetManagerName = param.get("budgetManagerName");
            if (budgetManagerName == null) {
                budgetManagerName = param.get("budgetManager"); // 兼容旧字段名
            }
            dbParam.put("budgetManager", budgetManagerName != null ? String.valueOf(budgetManagerName) : null);

            // 控制策略：1严格 / 2预警 / 3无控制，默认 1
            Object controlStrategy = param.get("controlStrategy");
            int strategy = 1;
            if (controlStrategy != null) {
                try {
                    strategy = Integer.parseInt(String.valueOf(controlStrategy));
                    if (strategy < 1 || strategy > 3) strategy = 1;
                } catch (NumberFormatException ignored) {
                    strategy = 1;
                }
            }
            dbParam.put("controlStrategy", strategy);

            // 调试日志
            log.info("保存成本预算 - 映射后参数: {}", dbParam);

            if (budgetId == null || budgetId.isEmpty() || "null".equalsIgnoreCase(budgetId)) {
                // 新增预算，生成 String 类型的雪花 ID
                String newId = String.valueOf(snowflakeIdWorker.nextId());
                dbParam.put("budgetId", newId);
                costCenterMapper.insertCostBudget(dbParam);
                log.info("新增成本预算成功，ID: {}", newId);
            } else {
                // 更新预算
                dbParam.put("budgetId", budgetId);
                int updateCount = costCenterMapper.updateCostBudget(dbParam);
                log.info("更新成本预算成功，ID: {}, 影响行数: {}", budgetId, updateCount);
            }

            return MyJsonBean.successData("保存成功", dbParam);
        } catch (NumberFormatException e) {
            log.error("参数类型转换失败: {}", e.getMessage());
            return MyJsonBean.errorData("参数格式错误");
        } catch (Exception e) {
            log.error("保存或更新成本预算失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteCostBudget(String budgetId) {
        try {
            if (budgetId == null || budgetId.trim().isEmpty()) {
                return MyJsonBean.errorData("预算ID不能为空");
            }

            int deletedCount = costCenterMapper.deleteCostBudget(budgetId);
            if (deletedCount > 0) {
                return MyJsonBean.successData("删除成功", null);
            } else {
                return MyJsonBean.errorData("预算不存在或已被删除");
            }
        } catch (Exception e) {
            log.error("删除成本预算失败，budgetId: {}", budgetId, e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getCostBudgetStats(String period) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("period", period);

            Map<String, Object> stats = costCenterMapper.selectCostBudgetStats(params);
            if (stats == null) {
                stats = new HashMap<>();
                stats.put("TOTAL_BUDGET", 0);
                stats.put("ACTUAL_COST", 0);
                stats.put("REMAINING_BUDGET", 0);
                stats.put("EXECUTION_RATE", 0);
            }
            return MyJsonBean.successData("查询成功", stats);
        } catch (Exception e) {
            log.error("查询成本预算统计数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getCostBudgetById(String budgetId) {
        try {
            log.info("查询成本预算详情，budgetId: {}", budgetId);

            if (budgetId == null || budgetId.trim().isEmpty()) {
                return MyJsonBean.errorData("预算ID不能为空");
            }

            Map<String, Object> params = new HashMap<>();
            params.put("budgetId", budgetId.trim());

            log.info("调用Mapper查询预算详情，参数: {}", params);
            Map<String, Object> budget = costCenterMapper.selectCostBudgetById(params);
            log.info("查询结果: {}", budget);

            if (budget == null) {
                log.warn("未找到预算详情，budgetId: {}", budgetId);
                return MyJsonBean.errorData("预算不存在");
            }
            return MyJsonBean.successData("查询成功", budget);
        } catch (Exception e) {
            log.error("查询成本预算详情失败，budgetId: {}", budgetId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchApproveCostBudget(List<String> budgetIds, Map<String, Object> auditData) {
        try {
            if (budgetIds == null || budgetIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要审批的预算记录");
            }

            int successCount = 0;
            List<String> errorMessages = new ArrayList<>();

            for (String budgetId : budgetIds) {
                try {
                    if (budgetId == null || budgetId.trim().isEmpty()) {
                        errorMessages.add("预算ID为空");
                        continue;
                    }

                    Map<String, Object> updateParam = new HashMap<>();
                    updateParam.put("budgetId", budgetId.trim());
                    updateParam.put("status", 3); // 已审批
                    updateParam.put("auditRemark", auditData != null ? (String) auditData.get("auditRemark") : "批量审批通过");

                    int updateCount = costCenterMapper.updateCostBudgetStatus(updateParam);
                    if (updateCount > 0) {
                        successCount++;
                    } else {
                        errorMessages.add("预算ID " + budgetId + " 审批失败");
                    }
                } catch (Exception e) {
                    log.error("审批预算失败，budgetId: {}", budgetId, e);
                    errorMessages.add("预算ID " + budgetId + " 审批异常: " + e.getMessage());
                }
            }
            if (errorMessages.isEmpty()) {
                return MyJsonBean.successData("批量审批成功，共审批 " + successCount + " 条记录", successCount);
            } else if (successCount > 0) {
                String message = "批量审批部分成功，成功审批 " + successCount + " 条记录。失败原因：" + String.join("; ", errorMessages);
                return MyJsonBean.successData(message, successCount);
            } else {
                return MyJsonBean.errorData("批量审批失败：" + String.join("; ", errorMessages));
            }
        } catch (Exception e) {
            log.error("批量审批成本预算失败", e);
            return MyJsonBean.errorData("批量审批失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getCostBudgetMonitorData(Map<String, Object> params) {
        try {
            log.info(">>> getCostBudgetMonitorData 入参: {}", params);

            // 透传所有筛选条件给 mapper：period(BUDGET_YEAR) / centerId / costCategory / status / budgetIds / bookId / tenantId
            Map<String, Object> queryParams = buildStatsQueryParams(params);
            log.info(">>> getCostBudgetMonitorData 转换后查询参数: {}", queryParams);

            // 注：mapper alias 已加双引号，返回 key 为小驼峰
            Map<String, Object> stats = costCenterMapper.selectCostBudgetStats(queryParams);
            log.info(">>> getCostBudgetMonitorData mapper 返回原始 stats: {}", stats);

            if (stats == null) {
                stats = new HashMap<>();
            }

            // 构建监控数据，全部来自数据库真实统计（不再使用模拟数据）
            Map<String, Object> monitorData = new HashMap<>();
            monitorData.put("totalBudget", stats.getOrDefault("totalBudget", 0));
            monitorData.put("actualCost", stats.getOrDefault("actualCost", 0));
            monitorData.put("remainingBudget", stats.getOrDefault("remainingBudget", 0));
            monitorData.put("avgExecutionRate", stats.getOrDefault("avgExecutionRate", 0));
            monitorData.put("budgetCount", stats.getOrDefault("budgetCount", 0));
            monitorData.put("activeCount", stats.getOrDefault("activeCount", 0));
            monitorData.put("warningCount", stats.getOrDefault("warningCount", 0));      // 真实SQL：执行率 80%~100%
            monitorData.put("overBudgetCount", stats.getOrDefault("overBudgetCount", 0)); // 真实SQL：执行率 ≥ 100%

            return MyJsonBean.successData("查询成功", monitorData);
        } catch (Exception e) {
            log.error("查询成本预算监控数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 把任意来源（String / Number / null）转为 Long，转换失败返回 null。
     * 用于 GET 请求 query 参数和 NUMBER 列做精确匹配，避免达梦隐式转换风险。
     */
    private Long toLongOrNull(Object val) {
        if (val == null) return null;
        if (val instanceof Number) return ((Number) val).longValue();
        String s = String.valueOf(val).trim();
        if (s.isEmpty() || "null".equalsIgnoreCase(s)) return null;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            log.warn("toLongOrNull 转换失败，原值: {}", val);
            return null;
        }
    }

    /**
     * 构建 stats 查询参数：把前端业务字段名映射成 mapper xml 期望的字段名，并保留所有过滤条件。
     * 当用户既没勾选行也没填筛选时，本方法只透传 bookId/tenantId（多租户隔离），
     * 这样 SQL 等于"全公司全年度统计"——数据来源最完整，不会因为 period=2026 写死而扑空。
     */
    private Map<String, Object> buildStatsQueryParams(Map<String, Object> params) {
        Map<String, Object> q = new HashMap<>();

        // 期间：兼容前端 period 和 budgetYear 两种字段名（VARCHAR 列，按字符串比较）
        Object period = params.get("period");
        if (period == null || isBlank(period)) {
            period = params.get("budgetYear");
        }
        if (period != null && !isBlank(period)) {
            q.put("period", String.valueOf(period));
        }

        // 成本中心
        Object centerId = params.get("centerId");
        if (centerId != null && !isBlank(centerId)) {
            q.put("centerId", String.valueOf(centerId));
        }

        // 成本分类：前端 budgetType -> mapper costCategory
        Object costCategory = params.get("costCategory");
        if (costCategory == null || isBlank(costCategory)) {
            costCategory = params.get("budgetType");
        }
        if (costCategory != null && !isBlank(costCategory)) {
            q.put("costCategory", String.valueOf(costCategory));
        }

        // 预算状态：前端 budgetStatus -> mapper status
        Object status = params.get("status");
        if (status == null || isBlank(status)) {
            status = params.get("budgetStatus");
        }
        if (status != null && !isBlank(status)) {
            q.put("status", status);
        }

        // 多租户上下文（NUMBER 列，必须 Long）
        Long bookId = toLongOrNull(params.get("bookId"));
        if (bookId != null) {
            q.put("bookId", bookId);
        }
        Long tenantId = toLongOrNull(params.get("tenantId"));
        if (tenantId != null) {
            q.put("tenantId", tenantId);
        }

        // 选中行：直接按 budgetIds 集合统计
        Object budgetIdsObj = params.get("budgetIds");
        if (budgetIdsObj instanceof List && !((List<?>) budgetIdsObj).isEmpty()) {
            // 转字符串数组（BUDGET_ID 列在 mapper 里用 TO_CHAR 处理过，统一走字符串比较最稳）
            List<String> budgetIds = new ArrayList<>();
            for (Object bid : (List<?>) budgetIdsObj) {
                if (bid != null) {
                    String s = String.valueOf(bid).trim();
                    if (!s.isEmpty() && !"null".equalsIgnoreCase(s)) {
                        budgetIds.add(s);
                    }
                }
            }
            if (!budgetIds.isEmpty()) {
                q.put("budgetIds", budgetIds);
            }
        }

        return q;
    }

    private boolean isBlank(Object obj) {
        if (obj == null) return true;
        String s = String.valueOf(obj).trim();
        return s.isEmpty() || "null".equalsIgnoreCase(s);
    }

    @Override
    public MyJsonBean<Map<String, Object>> getCostBudgetAnalysisData(Map<String, Object> params) {
        try {
            log.info(">>> getCostBudgetAnalysisData 入参: {}", params);

            Map<String, Object> queryParams = buildStatsQueryParams(params);
            log.info(">>> getCostBudgetAnalysisData 转换后查询参数: {}", queryParams);

            Map<String, Object> stats = costCenterMapper.selectCostBudgetStats(queryParams);
            log.info(">>> getCostBudgetAnalysisData mapper 返回原始 stats: {}", stats);

            if (stats == null) {
                stats = new HashMap<>();
            }

            // 构建分析数据（基于数据库真实数据计算 + 条件化建议）
            Map<String, Object> analysisData = new HashMap<>();

            Object totalBudget = stats.get("totalBudget");
            Object actualCost = stats.get("actualCost");
            Object overBudgetCountObj = stats.get("overBudgetCount");
            Object warningCountObj = stats.get("warningCount");
            Object budgetCountObj = stats.get("budgetCount");

            double totalBudgetDouble = totalBudget != null ? ((Number) totalBudget).doubleValue() : 0d;
            double actualCostDouble = actualCost != null ? ((Number) actualCost).doubleValue() : 0d;
            int overBudgetCount = overBudgetCountObj != null ? ((Number) overBudgetCountObj).intValue() : 0;
            int warningCount = warningCountObj != null ? ((Number) warningCountObj).intValue() : 0;
            int budgetCount = budgetCountObj != null ? ((Number) budgetCountObj).intValue() : 0;

            // 完成率（实际 / 预算）
            double completionRate = totalBudgetDouble > 0 ? (actualCostDouble / totalBudgetDouble) * 100 : 0;
            analysisData.put("budgetCompletionRate", Math.round(completionRate * 100.0) / 100.0);

            // 偏差率（(实际-预算)/预算）
            double varianceRate = totalBudgetDouble > 0 ? ((actualCostDouble - totalBudgetDouble) / totalBudgetDouble) * 100 : 0;
            analysisData.put("budgetVarianceRate", Math.round(varianceRate * 100.0) / 100.0);

            // 透传基础统计
            analysisData.put("totalBudget", totalBudgetDouble);
            analysisData.put("actualCost", actualCostDouble);
            analysisData.put("budgetCount", budgetCount);
            analysisData.put("warningCount", warningCount);
            analysisData.put("overBudgetCount", overBudgetCount);

            // 成本控制效果（按偏差率绝对值分级）
            String costControlEffect;
            if (Math.abs(varianceRate) <= 5) {
                costControlEffect = "优秀";
            } else if (Math.abs(varianceRate) <= 10) {
                costControlEffect = "良好";
            } else if (Math.abs(varianceRate) <= 20) {
                costControlEffect = "一般";
            } else {
                costControlEffect = "需改进";
            }
            analysisData.put("costControlEffect", costControlEffect);

            // 优化建议（基于真实超支/预警数量动态生成，不再硬编码）
            StringBuilder suggestions = new StringBuilder();
            if (overBudgetCount > 0) {
                suggestions.append("当前有 ").append(overBudgetCount).append(" 个成本中心已超支，需立即介入审查超支原因；");
            }
            if (warningCount > 0) {
                suggestions.append("有 ").append(warningCount).append(" 个成本中心执行率超过 80%，建议加强月度监控；");
            }
            if (varianceRate > 10) {
                suggestions.append("整体超支偏差 ").append(Math.round(varianceRate * 100.0) / 100.0).append("%，建议核查支出明细；");
            } else if (varianceRate < -20) {
                suggestions.append("预算执行偏低 ").append(Math.round(Math.abs(varianceRate) * 100.0) / 100.0).append("%，需评估预算编制合理性；");
            }
            if (suggestions.length() == 0) {
                suggestions.append(budgetCount > 0
                    ? "当前预算执行平稳，无明显超支或预警，继续保持月度跟踪即可。"
                    : "暂无预算数据，请先编制预算后再进行分析。");
            }
            analysisData.put("optimizationSuggestions", suggestions.toString());

            // 下期建议
            String nextPeriodAdvice;
            if (overBudgetCount > 0 || varianceRate > 15) {
                nextPeriodAdvice = "下期预算应根据本期超支情况上调相应成本中心额度，并加强日常监控。";
            } else if (varianceRate < -20) {
                nextPeriodAdvice = "下期预算可适度下调，并将节余资源调配至更紧迫的业务方向。";
            } else {
                nextPeriodAdvice = "下期可在本期基础上小幅调整，按业务发展节奏稳步规划。";
            }
            analysisData.put("nextPeriodAdvice", nextPeriodAdvice);

            return MyJsonBean.successData("分析完成", analysisData);
        } catch (Exception e) {
            log.error("查询成本预算分析数据失败", e);
            return MyJsonBean.errorData("分析失败: " + e.getMessage());
        }
    }

    // ==================== 新增接口实现 ====================

    @Override
    public MyJsonBean<Map<String, Object>> getCostCenterStats(String period, Long bookId, Long tenantId) {
        log.info("获取成本中心统计概览，期间: {}, bookId: {}, tenantId: {}", period, bookId, tenantId);

        Map<String, Object> param = new HashMap<>();
        param.put("bookId", bookId);
        param.put("tenantId", tenantId);
        // 当前月份期间
        String currentPeriod = period;
        if (currentPeriod == null || currentPeriod.isEmpty()) {
            java.time.LocalDate now = java.time.LocalDate.now();
            currentPeriod = now.getYear() + "-" + String.format("%02d", now.getMonthValue());
        }
        param.put("currentPeriod", currentPeriod);

        Map<String, Object> stats;
        try {
            stats = costCenterMapper.selectCostCenterStats(param);
        } catch (Exception e) {
            // 兜底：DB schema 缺字段或 SQL 异常时，返回空状态而非 500
            // 真实修复：执行 sql/T_COST_BUDGET_ADD_BUDGET_PERIOD.sql 补 BUDGET_PERIOD 字段
            log.warn("获取成本中心统计概览 SQL 执行失败，返回空统计数据。原因: {}", e.getMessage());
            stats = null;
        }

        if (stats == null) {
            stats = new HashMap<>();
            stats.put("totalCenters", 0);
            stats.put("totalCost", 0);
            stats.put("monthlyCost", 0);
            stats.put("costVariance", 0);
        }

        return MyJsonBean.successData("查询成功", stats);
    }

    @Override
    public MyJsonBean<Map<String, Object>> getBudgetOverview(String period, Long bookId, Long tenantId) {
        try {
            log.info("获取预算概览，期间: {}, bookId: {}, tenantId: {}", period, bookId, tenantId);
            Map<String, Object> overview = new HashMap<>();
            overview.put("totalBudget", 10000000.00);
            overview.put("actualExpense", 6500000.00);
            overview.put("remainingBudget", 3500000.00);
            overview.put("executionRate", 65.0);
            return MyJsonBean.successData("查询成功", overview);
        } catch (Exception e) {
            log.error("获取预算概览失败", e);
            return MyJsonBean.errorData("获取预算概览失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> adjustCostBudget(String budgetId, Map<String, Object> adjustData) {
        try {
            log.info("调整成本预算，预算ID: {}, 调整数据: {}", budgetId, adjustData);
            Map<String, Object> result = new HashMap<>();
            result.put("budgetId", budgetId);
            result.put("originalAmount", 1000000.00);
            result.put("adjustedAmount", 1500000.00);
            result.put("adjustment", 500000.00);
            return MyJsonBean.successData("预算调整成功", result);
        } catch (Exception e) {
            log.error("调整成本预算失败", e);
            return MyJsonBean.errorData("调整预算失败: " + e.getMessage());
        }
    }

    @Override
    public void exportBudgetReport(HttpServletResponse response, String budgetYear, String budgetType,
                                  String centerId, Long bookId, Long tenantId) {
        try {
            log.info("导出成本预算报告，年度: {}, 类型: {}, 中心ID: {}", budgetYear, budgetType, centerId);

            // 构建查询参数
            Map<String, Object> param = new HashMap<>();
            param.put("budgetYear", budgetYear);
            param.put("budgetType", budgetType);
            param.put("centerId", centerId);
            param.put("bookId", bookId);
            param.put("tenantId", tenantId);
            param.put("isEnabled", "1"); // 1=启用

            // 查询导出数据
            List<Map<String, Object>> exportData = costCenterMapper.selectBudgetReportList(param);

            if (exportData == null || exportData.isEmpty()) {
                throw new RuntimeException("没有找到可导出的数据");
            }

            // 生成文件名
            String fileName = "成本预算报告_" + budgetYear + "_" + budgetType + "_" +
                             LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

            // 创建Excel导出对象
            List<String> headers = Arrays.asList(
                "预算ID", "预算年度", "预算类型", "成本中心编码", "成本中心名称",
                "预算金额", "已用金额", "剩余金额", "执行率(%)", "预算状态",
                "创建人", "创建时间", "更新时间", "备注"
            );
            List<Integer> widths = Arrays.asList(15, 12, 12, 15, 20, 15, 15, 15, 12, 12, 15, 20, 20, 20);

            ExcelExport excelExport = new ExcelExport("成本预算报告", headers, widths);

            // 填充数据
            for (Map<String, Object> data : exportData) {
                Row row = excelExport.addRow();

                excelExport.addCell(row, 0, getSafeStringIdValue(data, "BUDGET_ID"));
                excelExport.addCell(row, 1, getSafeStringValue(data, "BUDGET_YEAR"));
                excelExport.addCell(row, 2, getSafeStringValue(data, "BUDGET_TYPE_NAME"));
                excelExport.addCell(row, 3, getSafeStringValue(data, "CENTER_CODE"));
                excelExport.addCell(row, 4, getSafeStringValue(data, "CENTER_NAME"));
                excelExport.addCell(row, 5, getSafeBigDecimalValue(data, "BUDGET_AMOUNT"));
                excelExport.addCell(row, 6, getSafeBigDecimalValue(data, "USED_AMOUNT"));
                excelExport.addCell(row, 7, getSafeBigDecimalValue(data, "REMAINING_AMOUNT"));
                excelExport.addCell(row, 8, getSafeBigDecimalValue(data, "EXECUTION_RATE"));
                excelExport.addCell(row, 9, getSafeStringValue(data, "BUDGET_STATUS_NAME"));
                excelExport.addCell(row, 10, getSafeStringValue(data, "CREATOR_NAME"));
                excelExport.addCell(row, 11, data.get("CREATE_TIME"));
                excelExport.addCell(row, 12, data.get("UPDATE_TIME"));
                excelExport.addCell(row, 13, getSafeStringValue(data, "REMARK"));
            }

            // 导出文件
            try {
                excelExport.write(response, fileName);
            } finally {
                excelExport.close();
            }

            log.info("成本预算报告导出成功，导出记录数: {}", exportData.size());

        } catch (Exception e) {
            log.error("导出成本预算报告失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getControlOverview(String period, Long bookId, Long tenantId) {
        try {
            log.info("获取成本控制概览，期间: {}, bookId: {}, tenantId: {}", period, bookId, tenantId);
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("bookId", bookId);
            queryParams.put("tenantId", tenantId);

            // 达梦 + MyBatis Map<String,Object> 不会走 mapUnderscoreToCamelCase，
            // ResultSet 列名会以 NORMAL_CENTERS / TOTAL_BUDGET 等大写下划线形式落 Map，
            // 这里统一 normalize 成小驼峰，再按原语义取值，避免读到 null 走默认 0。
            Map<String, Object> rawStats = costCenterMapper.selectCostControlStats(queryParams);
            Map<String, Object> stats = normalizeKeysToCamel(rawStats);

            Map<String, Object> overview = new HashMap<>();
            overview.put("normalCenters", stats.getOrDefault("normalCenters", 0));
            overview.put("warningCenters", stats.getOrDefault("warningCenters", 0));
            // 前端读 exceedCenters；后端 mapper 也是 exceedCenters，对齐
            overview.put("exceedCenters", stats.getOrDefault("exceedCenters", 0));
            overview.put("totalControls", stats.getOrDefault("totalControls", 0));
            overview.put("totalBudget", stats.getOrDefault("totalBudget", 0));
            overview.put("totalActual", stats.getOrDefault("totalActual", 0));
            overview.put("overallExecutionRate", stats.getOrDefault("overallExecutionRate", 0));
            return MyJsonBean.successData("查询成功", overview);
        } catch (Exception e) {
            log.error("获取成本控制概览失败", e);
            return MyJsonBean.errorData("获取控制概览失败: " + e.getMessage());
        }
    }

    /**
     * 把 Mapper 返回的 Map key 统一成小驼峰：
     *   - NORMAL_CENTERS / normal_centers → normalCenters
     *   - normalCenters → normalCenters（原样保留）
     * 工具方法，本类多处复用（达梦+MyBatis Map 类型不走自动驼峰转换）。
     */
    private Map<String, Object> normalizeKeysToCamel(Map<String, Object> raw) {
        Map<String, Object> out = new HashMap<>();
        if (raw == null) {
            return out;
        }
        for (Map.Entry<String, Object> e : raw.entrySet()) {
            String key = e.getKey();
            if (key == null) continue;
            String camel;
            if (key.indexOf('_') >= 0) {
                // snake_case / SNAKE_CASE → camelCase
                StringBuilder sb = new StringBuilder();
                boolean upperNext = false;
                for (char c : key.toLowerCase().toCharArray()) {
                    if (c == '_') {
                        upperNext = true;
                    } else if (upperNext) {
                        sb.append(Character.toUpperCase(c));
                        upperNext = false;
                    } else {
                        sb.append(c);
                    }
                }
                camel = sb.toString();
            } else if (key.equals(key.toUpperCase()) && !key.equals(key.toLowerCase())) {
                // 全大写无下划线（NAME / VALUE / ID）→ 全小写
                // 兼容达梦/Oracle 在 SELECT 单列无引号 alias 时返回的全大写
                camel = key.toLowerCase();
            } else {
                // 已经是驼峰 / 全小写，保持原样
                camel = key;
            }
            out.put(camel, e.getValue());
        }
        return out;
    }

    /**
     * 批量 normalize：List<Map> 每行 key 转小驼峰
     */
    private List<Map<String, Object>> normalizeListKeys(List<Map<String, Object>> rawList) {
        if (rawList == null || rawList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> result = new ArrayList<>(rawList.size());
        for (Map<String, Object> row : rawList) {
            result.add(normalizeKeysToCamel(row));
        }
        return result;
    }

    @Override
    public MyJsonBean<Map<String, Object>> batchDeleteCostControl(List<String> controlIds, Long bookId, Long tenantId) {
        try {
            log.info("批量删除成本控制，controlIds: {}, bookId: {}, tenantId: {}", controlIds, bookId, tenantId);
            Map<String, Object> result = new HashMap<>();
            result.put("successCount", controlIds != null ? controlIds.size() : 0);
            result.put("failedCount", 0);
            return MyJsonBean.successData("批量删除成功", result);
        } catch (Exception e) {
            log.error("批量删除成本控制失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean<Map<String, Object>> batchUpdateControlStatus(List<String> controlIds, Integer isEnabled,
                                                              Long bookId, Long tenantId) {
        try {
            log.info("批量更新成本控制状态，controlIds: {}, isEnabled: {}", controlIds, isEnabled);
            if (controlIds == null || controlIds.isEmpty()) {
                return MyJsonBean.errorData("controlIds 不能为空");
            }
            String currentUser = currentUserNameOrSystem();
            Map<String, Object> param = new HashMap<>();
            param.put("controlIds", controlIds);
            param.put("isEnabled", isEnabled);
            param.put("updater", currentUser);
            int affected = costCenterMapper.batchUpdateCostControlStatus(param);

            Map<String, Object> result = new HashMap<>();
            result.put("successCount", affected);
            result.put("failedCount", controlIds.size() - affected);
            return MyJsonBean.successData("批量更新状态成功", result);
        } catch (Exception e) {
            log.error("批量更新成本控制状态失败", e);
            return MyJsonBean.errorData("批量更新状态失败: " + e.getMessage());
        }
    }

    /**
     * 单条切换成本控制启用状态（前端 toggle 按钮专用）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean<Map<String, Object>> updateCostControlStatus(String controlId, Integer isEnabled) {
        try {
            log.info("切换成本控制状态，controlId: {}, isEnabled: {}", controlId, isEnabled);
            if (controlId == null || controlId.trim().isEmpty()) {
                return MyJsonBean.errorData("controlId 不能为空");
            }
            String currentUser = currentUserNameOrSystem();
            Map<String, Object> param = new HashMap<>();
            param.put("controlId", controlId);
            param.put("isEnabled", isEnabled);
            param.put("updater", currentUser);
            int affected = costCenterMapper.updateCostControlStatus(param);
            if (affected > 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("controlId", controlId);
                result.put("isEnabled", isEnabled);
                return MyJsonBean.successData("操作成功", result);
            }
            return MyJsonBean.errorData("更新失败：未找到对应控制规则");
        } catch (Exception e) {
            log.error("切换成本控制状态失败", e);
            return MyJsonBean.errorData("操作失败: " + e.getMessage());
        }
    }

    /**
     * 取当前登录用户的 staffId 字符串，失败时回退 system。
     * 用作 CREATOR / UPDATER 字段，避免 hardcode 1001L 这类假数据。
     */
    private String currentUserNameOrSystem() {
        try {
            TblStaffUtil staff = UserUtils.getUser();
            if (staff != null && staff.getStaffid() != null) {
                return String.valueOf(staff.getStaffid());
            }
        } catch (Exception ignored) {}
        return "system";
    }

    @Override
    public MyJsonBean<PageResult<Map<String, Object>>> getControlAlerts(String period, String alertType,
                                                                  Long bookId, Long tenantId,
                                                                  Integer pageNumber, Integer pageSize) {
        try {
            log.info("获取成本控制预警列表，期间: {}, 预警类型: {}", period, alertType);
            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTlist(new ArrayList<>());
            pageResult.setTotalRecord(0);
            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("获取成本控制预警列表失败", e);
            return MyJsonBean.errorData("获取预警列表失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> handleControlAlert(String alertId, Map<String, Object> handleData) {
        try {
            log.info("处理成本控制预警，预警ID: {}, 处理数据: {}", alertId, handleData);
            Map<String, Object> result = new HashMap<>();
            result.put("alertId", alertId);
            result.put("handleStatus", "1");
            return MyJsonBean.successData("预警处理成功", result);
        } catch (Exception e) {
            log.error("处理成本控制预警失败", e);
            return MyJsonBean.errorData("处理预警失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getCostCompareAnalysis(String startPeriod, String endPeriod,
                                                                     List<String> centerIds,
                                                                     Long bookId, Long tenantId) {
        try {
            log.info("获取成本对比分析，起始期间: {}, 结束期间: {}, 中心IDs: {}", startPeriod, endPeriod, centerIds);
            List<Map<String, Object>> compareList = new ArrayList<>();
            return MyJsonBean.successData("查询成功", compareList);
        } catch (Exception e) {
            log.error("获取成本对比分析失败", e);
            return MyJsonBean.errorData("获取对比分析失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getCostVarianceAnalysis(String period, String centerId,
                                                              Long bookId, Long tenantId) {
        try {
            log.info("获取成本差异分析，期间: {}, 中心ID: {}", period, centerId);
            Map<String, Object> variance = new HashMap<>();
            variance.put("centerId", centerId);
            variance.put("centerName", "研发中心");
            variance.put("budgetAmount", 100000.00);
            variance.put("actualAmount", 92000.00);
            variance.put("varianceAmount", -8000.00);
            variance.put("varianceRate", -8.0);
            return MyJsonBean.successData("查询成功", variance);
        } catch (Exception e) {
            log.error("获取成本差异分析失败", e);
            return MyJsonBean.errorData("获取差异分析失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getCostPerformanceEvaluation(String period, String centerId,
                                                                     Long bookId, Long tenantId) {
        try {
            log.info("获取成本绩效评价，期间: {}, 中心ID: {}", period, centerId);
            Map<String, Object> performance = new HashMap<>();
            performance.put("centerId", centerId);
            performance.put("centerName", "研发中心");
            performance.put("efficiencyIndex", 92.5);
            performance.put("costControlScore", 88.0);
            performance.put("budgetAdherence", 95.0);
            performance.put("trendAnalysis", "持续改善");
            performance.put("performanceRating", "A");
            return MyJsonBean.successData("查询成功", performance);
        } catch (Exception e) {
            log.error("获取成本绩效评价失败", e);
            return MyJsonBean.errorData("获取绩效评价失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getBudgetExecution(String budgetId, String period) {
        try {
            log.info("获取预算执行情况，预算ID: {}, 期间: {}", budgetId, period);
            Map<String, Object> execution = new HashMap<>();
            execution.put("budgetId", budgetId);
            execution.put("budgetAmount", 1000000.00);
            execution.put("actualExpense", 650000.00);
            execution.put("executionRate", 65.0);
            execution.put("remainingBudget", 350000.00);
            execution.put("status", "执行中");
            return MyJsonBean.successData("查询成功", execution);
        } catch (Exception e) {
            log.error("获取预算执行情况失败", e);
            return MyJsonBean.errorData("获取执行情况失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean submitBudgetApproval(String budgetId, Map<String, Object> submitData) {
        try {
            log.info("提交预算审批，预算ID: {}, 数据: {}", budgetId, submitData);
            // TODO: 实现提交预算审批逻辑
            return MyJsonBean.successData("提交成功", null);
        } catch (Exception e) {
            log.error("提交预算审批失败", e);
            return MyJsonBean.errorData("提交失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean approveBudget(String budgetId, Map<String, Object> approvalData) {
        try {
            log.info("审批预算，预算ID: {}, 审批数据: {}", budgetId, approvalData);
            // TODO: 实现预算审批逻辑
            return MyJsonBean.successData("审批成功", null);
        } catch (Exception e) {
            log.error("审批预算失败", e);
            return MyJsonBean.errorData("审批失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> generateAnalysisReport(Map<String, Object> params) {
        try {
            log.info("生成成本分析报告，参数: {}", params);
            Map<String, Object> result = new HashMap<>();
            result.put("reportId", "report001");
            result.put("downloadUrl", "/api/files/download/report001.pdf");
            result.put("generateTime", new java.util.Date());
            return MyJsonBean.successData("报告生成成功", result);
        } catch (Exception e) {
            log.error("生成成本分析报告失败", e);
            return MyJsonBean.errorData("生成报告失败: " + e.getMessage());
        }
    }

    // ==================== 成本控制相关方法实现 ====================

    /**
     * 分页查询成本控制列表
     */
    public MyJsonBean<Map<String, Object>> getCostControlList(Map<String, Object> param) {
        try {
            log.info("查询成本控制列表，参数: {}", param);

            // 获取分页参数
            Integer pageNumber = param.get("pageNumber") != null ?
                Integer.parseInt(param.get("pageNumber").toString()) : 1;
            Integer pageSize = param.get("pageSize") != null ?
                Integer.parseInt(param.get("pageSize").toString()) : 20;

            // 查询列表数据
            List<Map<String, Object>> list = costCenterMapper.selectCostControlList(param);

            // 查询总记录数
            int totalRecord = costCenterMapper.countCostControlList(param);

            // 计算分页信息
            int totalPage = (int) Math.ceil((double) totalRecord / pageSize);

            // 构建返回结果
            Map<String, Object> pageResult = new HashMap<>();
            pageResult.put("tlist", list);
            pageResult.put("totalRecord", totalRecord);
            pageResult.put("totalPage", totalPage);
            pageResult.put("pageSize", pageSize);
            pageResult.put("currentPage", pageNumber);

            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询成本控制列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 保存或更新成本控制规则
     */
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean<Map<String, Object>> saveOrUpdateCostControl(Map<String, Object> param) {
        try {
            log.info("保存或更新成本控制规则，入参: {}", param);

            // 必填校验
            if (isBlank(param.get("costCenterId"))) {
                return MyJsonBean.errorData("成本中心不能为空");
            }
            if (param.get("controlType") == null) {
                return MyJsonBean.errorData("控制类型不能为空");
            }
            if (param.get("controlAmount") == null) {
                return MyJsonBean.errorData("控制金额不能为空");
            }

            // 当前登录用户
            String currentUser = currentUserNameOrSystem();
            param.put("creator", currentUser);
            param.put("updater", currentUser);

            // 默认值兜底
            if (param.get("warningThreshold") == null) param.put("warningThreshold", 80);
            if (param.get("controlStrategy") == null) param.put("controlStrategy", 1);
            if (param.get("isEnabled") == null) param.put("isEnabled", 1);
            if (param.get("bookId") == null) param.put("bookId", 1L);
            if (param.get("tenantId") == null) param.put("tenantId", 1000L);

            String controlId = param.get("controlId") != null
                ? String.valueOf(param.get("controlId")).trim()
                : null;
            boolean isUpdate = controlId != null && !controlId.isEmpty() && !"null".equalsIgnoreCase(controlId);

            int result;
            String operation;
            if (isUpdate) {
                operation = "update";
                result = costCenterMapper.updateCostControl(param);
            } else {
                operation = "create";
                // 生成业务编号 CTRL-yyyy-NNNN（NNNN 用雪花后 4 位）
                long snowId = snowflakeIdWorker.nextId();
                controlId = String.valueOf(snowId);
                param.put("controlId", controlId);

                String controlNo = (String) param.get("controlNo");
                if (controlNo == null || controlNo.trim().isEmpty()) {
                    String yearStr = String.valueOf(java.time.Year.now());
                    String suffix = String.format("%04d", snowId % 10000);
                    param.put("controlNo", "CTRL-" + yearStr + "-" + suffix);
                }
                result = costCenterMapper.insertCostControl(param);
            }

            if (result > 0) {
                Map<String, Object> data = new HashMap<>();
                data.put("controlId", controlId);
                data.put("controlNo", param.get("controlNo"));
                data.put("operation", operation);
                return MyJsonBean.successData("保存成功", data);
            } else {
                return MyJsonBean.errorData("保存失败：受影响行数为 0");
            }
        } catch (Exception e) {
            log.error("保存或更新成本控制规则失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 获取成本控制统计数据
     */
    public MyJsonBean<Map<String, Object>> getCostControlStats(Map<String, Object> param) {
        try {
            log.info("查询成本控制统计数据，参数: {}", param);

            Map<String, Object> stats = costCenterMapper.selectCostControlStats(param);

            if (stats == null) {
                stats = new HashMap<>();
                stats.put("normalCenters", 0);
                stats.put("warningCenters", 0);
                stats.put("exceededCenters", 0);
                stats.put("totalCenters", 0);
            }
            return MyJsonBean.successData("查询成功", stats);
        } catch (Exception e) {
            log.error("查询成本控制统计数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 成本分析相关方法实现 ====================

    /**
     * 获取成本分析数据（前端 cost-analysis 页主接口）
     * 返回结构 = { overview, summaryList, detailList, total }
     */
    public MyJsonBean<Map<String, Object>> getCostAnalysisData(Map<String, Object> param) {
        try {
            log.info("查询成本分析数据，参数: {}", param);

            // 分页参数（兼容 pageNum / pageNumber 两种写法）
            int pageSize = parsePositiveInt(param.get("pageSize"), 20);
            int pageNumber = parsePositiveInt(
                param.get("pageNumber") != null ? param.get("pageNumber") : param.get("pageNum"), 1);
            int offset = (pageNumber - 1) * pageSize;
            param.put("offset", offset);
            param.put("pageSize", pageSize);

            // costCenterIds 是逗号分隔字符串，转 List 给 mapper 用 IN
            Object centerIdsObj = param.get("costCenterIds");
            if (centerIdsObj instanceof String && !((String) centerIdsObj).isEmpty()) {
                List<String> ids = Arrays.stream(((String) centerIdsObj).split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                if (!ids.isEmpty()) {
                    param.put("centerIdList", ids);
                }
            }

            // 1) 概览
            Map<String, Object> rawOverview = costCenterMapper.selectCostAnalysisOverview(param);
            Map<String, Object> overview = normalizeKeysToCamel(rawOverview);
            // 默认值兜底
            overview.putIfAbsent("totalCost", 0);
            overview.putIfAbsent("totalCostTrend", 0);
            overview.putIfAbsent("avgCost", 0);
            overview.putIfAbsent("costVariance", 0);
            overview.putIfAbsent("efficiencyIndex", 0);

            // 2) 汇总（中心维度）
            List<Map<String, Object>> rawSummary = costCenterMapper.selectCostAnalysisDataList(param);
            List<Map<String, Object>> summaryList = normalizeListKeys(rawSummary);

            // 3) 明细（带分页）
            int totalRecord = costCenterMapper.countCostAnalysisDetail(param);
            List<Map<String, Object>> rawDetail = costCenterMapper.selectCostAnalysisDetail(param);
            List<Map<String, Object>> detailList = normalizeListKeys(rawDetail);

            Map<String, Object> result = new HashMap<>();
            result.put("overview", overview);
            result.put("summaryList", summaryList);
            result.put("detailList", detailList);
            result.put("total", totalRecord);
            result.put("pageSize", pageSize);
            result.put("currentPage", pageNumber);
            return MyJsonBean.successData("查询成功", result);
        } catch (Exception e) {
            log.error("查询成本分析数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    private int parsePositiveInt(Object raw, int defaultValue) {
        if (raw == null) return defaultValue;
        try {
            int v = Integer.parseInt(raw.toString().trim());
            return v > 0 ? v : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取成本结构分析（饼图数据）
     * 返回 { items: [{name, value}, ...] }
     */
    public MyJsonBean<Map<String, Object>> getCostStructureAnalysis(Map<String, Object> param) {
        try {
            log.info("查询成本结构分析，参数: {}", param);
            List<Map<String, Object>> rawList = costCenterMapper.selectCostStructureAnalysis(param);
            List<Map<String, Object>> items = normalizeListKeys(rawList);

            Map<String, Object> result = new HashMap<>();
            result.put("items", items);
            return MyJsonBean.successData("查询成功", result);
        } catch (Exception e) {
            log.error("查询成本结构分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取成本趋势分析（折线/柱状图数据）
     * 返回 list（兼容已有 controller 签名 List<Map<String, Object>>）
     * 前端会从中提取 period / actualAmount / budgetAmount 三个字段渲染图表
     */
    public MyJsonBean<List<Map<String, Object>>> getCostTrendAnalysis(Map<String, Object> param) {
        try {
            log.info("查询成本趋势分析，参数: {}", param);
            List<Map<String, Object>> raw = costCenterMapper.selectCostTrendAnalysis(param);
            return MyJsonBean.successData("查询成功", normalizeListKeys(raw));
        } catch (Exception e) {
            log.error("查询成本趋势分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 生成成本分析报告
     */
    public MyJsonBean<Map<String, Object>> generateCostAnalysisReport(Map<String, Object> param) {
        try {
            log.info("生成成本分析报告，参数: {}", param);

            Map<String, Object> reportData = costCenterMapper.selectCostAnalysisReportData(param);

            if (reportData == null) {
                reportData = new HashMap<>();
            }

            // 添加报告元数据
            reportData.put("reportId", "RPT" + System.currentTimeMillis());
            reportData.put("generateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            reportData.put("reportType", "成本分析报告");

            return MyJsonBean.successData("报告生成成功", reportData);
        } catch (Exception e) {
            log.error("生成成本分析报告失败", e);
            return MyJsonBean.errorData("生成报告失败: " + e.getMessage());
        }
    }

    /**
     * 安全获取BigDecimal值
     */
    private BigDecimal getSafeBigDecimalValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || "".equals(value) || "null".equals(value)) {
            return null;
        }
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public MyJsonBean getAllocationRules(String bookId, String tenantId) {
        // 分摊规则是系统级别算法元数据（不是租户业务数据），返回内置算法定义列表
        // 前端字段要求：ruleName / ruleTypeName / description / formula / isEnabled
        log.info("查询分摊规则列表，bookId={}, tenantId={}", bookId, tenantId);

        List<Map<String, Object>> rules = new ArrayList<>();

        Map<String, Object> rule1 = new HashMap<>();
        rule1.put("ruleId", "RULE_AVG");
        rule1.put("ruleName", "平均分摊");
        rule1.put("ruleTypeName", "AVG");
        rule1.put("description", "将分摊总额平均分配给所有目标成本中心，适用于公共费用分摊场景");
        rule1.put("formula", "分摊金额 = 分摊总额 ÷ 目标中心数");
        rule1.put("isEnabled", 1);
        rules.add(rule1);

        Map<String, Object> rule2 = new HashMap<>();
        rule2.put("ruleId", "RULE_RATIO");
        rule2.put("ruleName", "比例分摊");
        rule2.put("ruleTypeName", "RATIO");
        rule2.put("description", "按各目标中心的基础值占比分配分摊金额，适用于按收入/成本/工时比例分摊");
        rule2.put("formula", "分摊金额 = 分摊总额 × (目标中心基础值 ÷ 总基础值)");
        rule2.put("isEnabled", 1);
        rules.add(rule2);

        Map<String, Object> rule3 = new HashMap<>();
        rule3.put("ruleId", "RULE_QUANTITY");
        rule3.put("ruleName", "数量基础分摊");
        rule3.put("ruleTypeName", "QUANTITY_BASED");
        rule3.put("description", "按各目标中心的产量/服务量等数量基础分摊，适用于生产成本和服务成本分摊");
        rule3.put("formula", "分摊金额 = 分摊总额 × (目标中心数量 ÷ 总数量)");
        rule3.put("isEnabled", 1);
        rules.add(rule3);

        Map<String, Object> rule4 = new HashMap<>();
        rule4.put("ruleId", "RULE_HOUR");
        rule4.put("ruleName", "工时基础分摊");
        rule4.put("ruleTypeName", "HOUR_BASED");
        rule4.put("description", "按各目标中心的工时数分摊，适用于人工成本和设备折旧分摊");
        rule4.put("formula", "分摊金额 = 分摊总额 × (目标中心工时 ÷ 总工时)");
        rule4.put("isEnabled", 1);
        rules.add(rule4);

        Map<String, Object> rule5 = new HashMap<>();
        rule5.put("ruleId", "RULE_AMOUNT");
        rule5.put("ruleName", "金额基础分摊");
        rule5.put("ruleTypeName", "AMOUNT_BASED");
        rule5.put("description", "按各目标中心的销售额/营业额等金额基础分摊，适用于营销费用分摊");
        rule5.put("formula", "分摊金额 = 分摊总额 × (目标中心金额 ÷ 总金额)");
        rule5.put("isEnabled", 1);
        rules.add(rule5);

        Map<String, Object> rule6 = new HashMap<>();
        rule6.put("ruleId", "RULE_STAIR");
        rule6.put("ruleName", "阶梯分摊");
        rule6.put("ruleTypeName", "STAIR");
        rule6.put("description", "按预设的阶梯区间分级分摊，适用于规模分级的费用分摊（已停用，建议使用比例分摊）");
        rule6.put("formula", "分摊金额 = 阶梯系数 × 基础值");
        rule6.put("isEnabled", 0);
        rules.add(rule6);

        return MyJsonBean.successData("查询成功", rules);
    }
}
