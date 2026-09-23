package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.entity.TblProxyDelegation;
import com.financial.sharing.entity.TblProxyDelegationHistory;
import com.financial.sharing.entity.TblProxyDelegationPermission;
import com.financial.sharing.mapper.TblProxyDelegationHistoryMapper;
import com.financial.sharing.mapper.TblProxyDelegationMapper;
import com.financial.sharing.mapper.TblProxyDelegationPermissionMapper;
import com.financial.sharing.service.TblProxyDelegationService;
import com.financial.sharing.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 代理委托服务实现类
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Service
public class TblProxyDelegationServiceImpl implements TblProxyDelegationService {

    @Autowired
    private TblProxyDelegationMapper proxyDelegationMapper;

    @Autowired
    private TblProxyDelegationPermissionMapper permissionMapper;

    @Autowired
    private TblProxyDelegationHistoryMapper historyMapper;

    @Override
    public Object getList(Object param) {
        try {
            Map<String, Object> paramMap = (Map<String, Object>) param;
            
            Integer pageNum = (Integer) paramMap.getOrDefault("pageNum", 0);
            Integer pageSize = (Integer) paramMap.getOrDefault("size", 10);
            
            Page<TblProxyDelegation> page = new Page<>(pageNum + 1, pageSize);
            LambdaQueryWrapper<TblProxyDelegation> wrapper = new LambdaQueryWrapper<>();
            
            // 添加查询条件（只在参数值非空时添加）
            String delegatorName = (String) paramMap.get("delegatorName");
            if (delegatorName != null && !delegatorName.trim().isEmpty()) {
                wrapper.like(TblProxyDelegation::getDelegatorName, delegatorName);
            }

            String proxyName = (String) paramMap.get("proxyName");
            if (proxyName != null && !proxyName.trim().isEmpty()) {
                wrapper.like(TblProxyDelegation::getProxyName, proxyName);
            }

            String delegationType = (String) paramMap.get("delegationType");
            if (delegationType != null && !delegationType.trim().isEmpty()) {
                wrapper.eq(TblProxyDelegation::getDelegationType, delegationType);
            }

            Object status = paramMap.get("status");
            if (status != null && !"".equals(status)) {
                wrapper.eq(TblProxyDelegation::getIsEnabled, status);
            }
            
            wrapper.orderByDesc(TblProxyDelegation::getCreateTime);
            
            IPage<TblProxyDelegation> result = proxyDelegationMapper.selectPage(page, wrapper);
            
            PageResult<TblProxyDelegation> pageResult = new PageResult<>();
            pageResult.setTlist(result.getRecords());
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize((int) result.getSize());
            pageResult.setCurrentPage((int) result.getCurrent());
            
            return pageResult;
        } catch (Exception e) {
            log.error("查询代理委托列表失败", e);
            throw new RuntimeException("查询代理委托列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object saveOrUpdate(Object param) {
        try {
            Map<String, Object> paramMap = (Map<String, Object>) param;

            TblProxyDelegation delegation = new TblProxyDelegation();
            String delegationId = (String) paramMap.get("delegationId");
            boolean isUpdate = false;
            TblProxyDelegation oldDelegation = null;

            if (delegationId != null && !delegationId.isEmpty()) {
                // 更新
                isUpdate = true;
                // 先保存旧数据用于记录变更（必须在修改之前保存）
                TblProxyDelegation temp = proxyDelegationMapper.selectById(delegationId);
                if (temp == null) {
                    throw new RuntimeException("代理委托不存在");
                }
                // 深拷贝旧数据
                oldDelegation = copyDelegation(temp);
                delegation = temp;
                delegation.setUpdateTime(LocalDateTime.now());
                delegation.setUpdateUser((String) paramMap.get("updateUser"));
            } else {
                // 新增
                delegationId = UUID.randomUUID().toString().replace("-", "");
                delegation.setDelegationId(delegationId);
                delegation.setCreateTime(LocalDateTime.now());
                delegation.setCreateUser((String) paramMap.get("createUser"));
            }

            // 设置字段
            if (paramMap.containsKey("delegationCode")) {
                delegation.setDelegationCode((String) paramMap.get("delegationCode"));
            }
            if (paramMap.containsKey("delegationType")) {
                delegation.setDelegationType((String) paramMap.get("delegationType"));
            }
            if (paramMap.containsKey("delegatorId")) {
                delegation.setDelegatorId((String) paramMap.get("delegatorId"));
            }
            if (paramMap.containsKey("delegatorName")) {
                delegation.setDelegatorName((String) paramMap.get("delegatorName"));
            }
            if (paramMap.containsKey("delegatorDept")) {
                delegation.setDelegatorDept((String) paramMap.get("delegatorDept"));
            }
            if (paramMap.containsKey("proxyId")) {
                delegation.setProxyId((String) paramMap.get("proxyId"));
            }
            if (paramMap.containsKey("proxyName")) {
                delegation.setProxyName((String) paramMap.get("proxyName"));
            }
            if (paramMap.containsKey("proxyDept")) {
                delegation.setProxyDept((String) paramMap.get("proxyDept"));
            }
            if (paramMap.containsKey("delegationScope")) {
                delegation.setDelegationScope((String) paramMap.get("delegationScope"));
            }
            if (paramMap.containsKey("permissionScope")) {
                Object permissionScope = paramMap.get("permissionScope");
                if (permissionScope instanceof List) {
                    // 将数组转换为逗号分隔的字符串
                    delegation.setPermissionScope(String.join(",", (List<String>) permissionScope));
                } else if (permissionScope instanceof String) {
                    delegation.setPermissionScope((String) permissionScope);
                }
            }
            if (paramMap.containsKey("amountLimit")) {
                Object amountLimit = paramMap.get("amountLimit");
                if (amountLimit instanceof Number) {
                    delegation.setAmountLimit(new java.math.BigDecimal(amountLimit.toString()));
                }
            }
            if (paramMap.containsKey("startDate")) {
                Object startDate = paramMap.get("startDate");
                if (startDate instanceof String) {
                    // 处理ISO 8601格式的日期字符串 "2025-12-31T16:00:00.000Z"
                    String dateStr = (String) startDate;
                    if (dateStr.contains("T")) {
                        dateStr = dateStr.substring(0, dateStr.indexOf("T"));
                    }
                    delegation.setStartDate(java.time.LocalDate.parse(dateStr));
                }
            }
            if (paramMap.containsKey("endDate")) {
                Object endDate = paramMap.get("endDate");
                if (endDate instanceof String) {
                    // 处理ISO 8601格式的日期字符串 "2026-12-30T16:00:00.000Z"
                    String dateStr = (String) endDate;
                    if (dateStr.contains("T")) {
                        dateStr = dateStr.substring(0, dateStr.indexOf("T"));
                    }
                    delegation.setEndDate(java.time.LocalDate.parse(dateStr));
                }
            }
            if (paramMap.containsKey("isEnabled")) {
                Object isEnabled = paramMap.get("isEnabled");
                // 处理Boolean到Integer的转换
                if (isEnabled instanceof Boolean) {
                    delegation.setIsEnabled((Boolean) isEnabled ? 1 : 0);
                } else if (isEnabled instanceof Integer) {
                    delegation.setIsEnabled((Integer) isEnabled);
                } else if (isEnabled instanceof String) {
                    delegation.setIsEnabled("true".equalsIgnoreCase((String) isEnabled) ? 1 : 0);
                }
            }
            if (paramMap.containsKey("description")) {
                delegation.setDescription((String) paramMap.get("description"));
            }
            if (paramMap.containsKey("remark")) {
                delegation.setRemark((String) paramMap.get("remark"));
            }

            if (delegation.getUpdateTime() != null) {
                proxyDelegationMapper.updateById(delegation);

                // 记录编辑操作历史
                if (isUpdate && oldDelegation != null) {
                    recordEditHistory(delegationId, oldDelegation, delegation);
                }
            } else {
                proxyDelegationMapper.insert(delegation);

                // 记录新增操作历史
                TblProxyDelegationHistory history = new TblProxyDelegationHistory();
                history.setHistoryId(UUID.randomUUID().toString().replace("-", ""));
                history.setDelegationId(delegationId);
                history.setOperationType("新增");
                history.setOperationTime(LocalDateTime.now());
                history.setOperationContent("创建代理委托：" + delegation.getDelegationCode());
                history.setOperator("SYSTEM"); // 实际应该从当前登录用户获取
                historyMapper.insert(history);
            }

            return delegationId;
        } catch (Exception e) {
            log.error("保存代理委托失败", e);
            throw new RuntimeException("保存代理委托失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object delete(String delegationId) {
        try {
            proxyDelegationMapper.deleteById(delegationId);
            return "删除成功";
        } catch (Exception e) {
            log.error("删除代理委托失败", e);
            throw new RuntimeException("删除代理委托失败: " + e.getMessage());
        }
    }

    @Override
    public Object getDetail(String delegationId) {
        try {
            return proxyDelegationMapper.selectById(delegationId);
        } catch (Exception e) {
            log.error("获取代理委托详情失败", e);
            throw new RuntimeException("获取代理委托详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object updateStatus(String delegationId, Integer status) {
        try {
            TblProxyDelegation delegation = proxyDelegationMapper.selectById(delegationId);
            if (delegation == null) {
                throw new RuntimeException("代理委托不存在");
            }
            delegation.setIsEnabled(status);
            delegation.setUpdateTime(LocalDateTime.now());
            proxyDelegationMapper.updateById(delegation);
            return "更新状态成功";
        } catch (Exception e) {
            log.error("更新代理委托状态失败", e);
            throw new RuntimeException("更新代理委托状态失败: " + e.getMessage());
        }
    }

    @Override
    public Object getPermissions(String delegationId) {
        try {
            return permissionMapper.selectByDelegationId(delegationId);
        } catch (Exception e) {
            log.error("获取代理委托权限失败", e);
            throw new RuntimeException("获取代理委托权限失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object savePermissions(String delegationId, Object permissions) {
        try {
            // 删除旧权限
            LambdaQueryWrapper<TblProxyDelegationPermission> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblProxyDelegationPermission::getDelegationId, delegationId);
            permissionMapper.delete(wrapper);

            // 插入新权限
            List<Map<String, Object>> permissionList = (List<Map<String, Object>>) permissions;
            for (Map<String, Object> permMap : permissionList) {
                TblProxyDelegationPermission permission = new TblProxyDelegationPermission();
                permission.setPermissionId(UUID.randomUUID().toString().replace("-", ""));
                permission.setDelegationId(delegationId);
                permission.setPermissionType((String) permMap.get("permissionType"));
                permission.setPermissionLevel((String) permMap.get("permissionLevel"));
                permission.setPermissionValue((String) permMap.get("permissionValue"));

                // 处理金额限制
                Object amountLimit = permMap.get("amountLimit");
                if (amountLimit != null) {
                    if (amountLimit instanceof Number) {
                        permission.setAmountLimit(new java.math.BigDecimal(amountLimit.toString()));
                    } else if (amountLimit instanceof String && !((String) amountLimit).isEmpty()) {
                        permission.setAmountLimit(new java.math.BigDecimal((String) amountLimit));
                    }
                }

                permission.setRemark((String) permMap.get("remark"));
                permission.setCreateTime(LocalDateTime.now());
                permission.setCreateUser((String) permMap.get("createUser"));
                permissionMapper.insert(permission);
            }

            return "保存权限范围成功";
        } catch (Exception e) {
            log.error("保存代理委托权限失败", e);
            throw new RuntimeException("保存代理委托权限失败: " + e.getMessage());
        }
    }

    @Override
    public Object getHistory(String delegationId) {
        try {
            return historyMapper.selectByDelegationId(delegationId);
        } catch (Exception e) {
            log.error("获取代理委托历史记录失败", e);
            throw new RuntimeException("获取代理委托历史记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object batchDelete(List<String> delegationIds) {
        try {
            for (String delegationId : delegationIds) {
                proxyDelegationMapper.deleteById(delegationId);
            }
            return "批量删除成功";
        } catch (Exception e) {
            log.error("批量删除代理委托失败", e);
            throw new RuntimeException("批量删除代理委托失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object activate(String delegationId) {
        return updateStatus(delegationId, 1, "激活");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object deactivate(String delegationId) {
        return updateStatus(delegationId, 0, "停用");
    }

    /**
     * 更新代理委托状态并记录历史
     */
    private Object updateStatus(String delegationId, Integer status, String operationType) {
        try {
            TblProxyDelegation delegation = proxyDelegationMapper.selectById(delegationId);
            if (delegation == null) {
                throw new RuntimeException("代理委托不存在");
            }

            Integer oldStatus = delegation.getIsEnabled();
            delegation.setIsEnabled(status);
            delegation.setUpdateTime(LocalDateTime.now());
            proxyDelegationMapper.updateById(delegation);

            // 记录操作历史
            TblProxyDelegationHistory history = new TblProxyDelegationHistory();
            history.setHistoryId(UUID.randomUUID().toString().replace("-", ""));
            history.setDelegationId(delegationId);
            history.setOperationType(operationType);
            history.setOperationTime(LocalDateTime.now());
            history.setOperationContent("状态从 " + (oldStatus == 1 ? "启用" : "禁用") + " 变更为 " + (status == 1 ? "启用" : "禁用"));
            history.setOperator("SYSTEM"); // 实际应该从当前登录用户获取
            historyMapper.insert(history);

            return operationType + "成功";
        } catch (Exception e) {
            log.error("更新代理委托状态失败", e);
            throw new RuntimeException("更新代理委托状态失败: " + e.getMessage());
        }
    }

    /**
     * 记录编辑操作历史
     */
    private void recordEditHistory(String delegationId, TblProxyDelegation oldData, TblProxyDelegation newData) {
        try {
            List<String> changes = new ArrayList<>();

            // 比较各个字段的变化
            if (!Objects.equals(oldData.getDelegationCode(), newData.getDelegationCode())) {
                changes.add("委托编号: " + oldData.getDelegationCode() + " → " + newData.getDelegationCode());
            }
            if (!Objects.equals(oldData.getDelegationType(), newData.getDelegationType())) {
                changes.add("委托类型: " + formatDelegationType(oldData.getDelegationType()) + " → " + formatDelegationType(newData.getDelegationType()));
            }
            if (!Objects.equals(oldData.getDelegatorName(), newData.getDelegatorName())) {
                changes.add("委托人: " + oldData.getDelegatorName() + " → " + newData.getDelegatorName());
            }
            if (!Objects.equals(oldData.getProxyName(), newData.getProxyName())) {
                changes.add("代理人: " + oldData.getProxyName() + " → " + newData.getProxyName());
            }
            if (!Objects.equals(oldData.getPermissionScope(), newData.getPermissionScope())) {
                changes.add("权限范围: " + oldData.getPermissionScope() + " → " + newData.getPermissionScope());
            }
            if (!Objects.equals(oldData.getAmountLimit(), newData.getAmountLimit())) {
                changes.add("金额限制: " + oldData.getAmountLimit() + " → " + newData.getAmountLimit());
            }
            if (!Objects.equals(oldData.getStartDate(), newData.getStartDate())) {
                changes.add("开始日期: " + oldData.getStartDate() + " → " + newData.getStartDate());
            }
            if (!Objects.equals(oldData.getEndDate(), newData.getEndDate())) {
                changes.add("结束日期: " + oldData.getEndDate() + " → " + newData.getEndDate());
            }
            if (!Objects.equals(oldData.getDescription(), newData.getDescription())) {
                changes.add("描述已修改");
            }

            // 如果有变更，记录到历史表
            if (!changes.isEmpty()) {
                TblProxyDelegationHistory history = new TblProxyDelegationHistory();
                history.setHistoryId(UUID.randomUUID().toString().replace("-", ""));
                history.setDelegationId(delegationId);
                history.setOperationType("编辑");
                history.setOperationTime(LocalDateTime.now());
                history.setOperationContent("修改内容: " + String.join("; ", changes));
                history.setOperator("SYSTEM"); // 实际应该从当前登录用户获取
                historyMapper.insert(history);
            }
        } catch (Exception e) {
            log.error("记录编辑历史失败", e);
            // 不抛出异常，避免影响主流程
        }
    }

    /**
     * 格式化委托类型显示
     */
    private String formatDelegationType(String type) {
        if (type == null) return "";
        switch (type) {
            case "FULL": return "全权委托";
            case "PARTIAL": return "部分委托";
            case "TEMPORARY": return "临时委托";
            case "EMERGENCY": return "紧急委托";
            default: return type;
        }
    }

    /**
     * 深拷贝代理委托对象
     */
    private TblProxyDelegation copyDelegation(TblProxyDelegation source) {
        if (source == null) return null;

        TblProxyDelegation copy = new TblProxyDelegation();
        copy.setDelegationId(source.getDelegationId());
        copy.setDelegationCode(source.getDelegationCode());
        copy.setDelegationType(source.getDelegationType());
        copy.setDelegatorId(source.getDelegatorId());
        copy.setDelegatorName(source.getDelegatorName());
        copy.setDelegatorDept(source.getDelegatorDept());
        copy.setProxyId(source.getProxyId());
        copy.setProxyName(source.getProxyName());
        copy.setProxyDept(source.getProxyDept());
        copy.setDelegationScope(source.getDelegationScope());
        copy.setPermissionScope(source.getPermissionScope());
        copy.setAmountLimit(source.getAmountLimit());
        copy.setStartDate(source.getStartDate());
        copy.setEndDate(source.getEndDate());
        copy.setIsEnabled(source.getIsEnabled());
        copy.setDescription(source.getDescription());
        copy.setRemark(source.getRemark());
        copy.setCreateTime(source.getCreateTime());
        copy.setCreateUser(source.getCreateUser());
        copy.setUpdateTime(source.getUpdateTime());
        copy.setUpdateUser(source.getUpdateUser());

        return copy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object suspend(String delegationId, Object suspendInfo) {
        try {
            TblProxyDelegation delegation = proxyDelegationMapper.selectById(delegationId);
            if (delegation == null) {
                throw new RuntimeException("代理委托不存在");
            }
            delegation.setIsEnabled(2); // 2表示暂停
            delegation.setUpdateTime(LocalDateTime.now());
            proxyDelegationMapper.updateById(delegation);
            return "暂停成功";
        } catch (Exception e) {
            log.error("暂停代理委托失败", e);
            throw new RuntimeException("暂停代理委托失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object resume(String delegationId) {
        return updateStatus(delegationId, 1);
    }

    @Override
    public Object getMyDelegations(String userId, String delegationType) {
        try {
            LambdaQueryWrapper<TblProxyDelegation> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblProxyDelegation::getProxyId, userId);
            if (delegationType != null && !delegationType.isEmpty()) {
                wrapper.eq(TblProxyDelegation::getDelegationType, delegationType);
            }
            wrapper.eq(TblProxyDelegation::getIsEnabled, 1);
            wrapper.orderByDesc(TblProxyDelegation::getCreateTime);
            return proxyDelegationMapper.selectList(wrapper);
        } catch (Exception e) {
            log.error("获取我的代理委托失败", e);
            throw new RuntimeException("获取我的代理委托失败: " + e.getMessage());
        }
    }

    @Override
    public Object getStatistics(String delegationId, String startDate, String endDate) {
        try {
            // 这里返回模拟统计数据，实际应该从业务表中统计
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("delegationId", delegationId);
            statistics.put("totalProcessed", 0);
            statistics.put("totalAmount", 0.0);
            statistics.put("averageAmount", 0.0);
            statistics.put("averageProcessTime", 0.0);
            statistics.put("statisticsDate", new Date());
            return statistics;
        } catch (Exception e) {
            log.error("获取代理委托统计失败", e);
            throw new RuntimeException("获取代理委托统计失败: " + e.getMessage());
        }
    }
}

