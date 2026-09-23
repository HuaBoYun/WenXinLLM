package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.financial.sharing.entity.TblExpenseParameter;
import com.financial.sharing.entity.TblExpenseParameterOption;
import com.financial.sharing.entity.TblExpenseParameterPermission;
import com.financial.sharing.mapper.TblExpenseParameterMapper;
import com.financial.sharing.mapper.TblExpenseParameterOptionMapper;
import com.financial.sharing.mapper.TblExpenseParameterPermissionMapper;
import com.financial.sharing.service.TblExpenseParameterService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 费用参数服务实现类
 *
 * @author Financial Sharing System
 * @since 2025-01-30
 */
@Slf4j
@Service
public class TblExpenseParameterServiceImpl implements TblExpenseParameterService {

    @Autowired
    private TblExpenseParameterMapper expenseParameterMapper;

    @Autowired
    private TblExpenseParameterOptionMapper expenseParameterOptionMapper;

    @Autowired
    private TblExpenseParameterPermissionMapper expenseParameterPermissionMapper;

    /**
     * 将 Boolean 或 Integer 转换为 Integer (0 或 1)
     * @param value 输入值
     * @param defaultValue 默认值
     * @return Integer 值
     */
    private Integer convertToInteger(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean) {
            return ((Boolean) value) ? 1 : 0;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof String) {
            String str = (String) value;
            if ("true".equalsIgnoreCase(str) || "1".equals(str)) {
                return 1;
            }
            if ("false".equalsIgnoreCase(str) || "0".equals(str)) {
                return 0;
            }
        }
        return defaultValue;
    }

    /**
     * 获取参数类型中文名称
     */
    private String getParameterTypeName(String parameterType) {
        if (parameterType == null) {
            return "";
        }
        switch (parameterType) {
            case "SYSTEM":
                return "系统参数";
            case "BUSINESS":
                return "业务参数";
            case "WORKFLOW":
                return "流程参数";
            case "UI":
                return "界面参数";
            case "SECURITY":
                return "安全参数";
            default:
                return parameterType;
        }
    }

    /**
     * 获取数据类型中文名称
     */
    private String getDataTypeName(String dataType) {
        if (dataType == null) {
            return "";
        }
        switch (dataType) {
            case "STRING":
                return "字符串";
            case "NUMBER":
                return "数字";
            case "BOOLEAN":
                return "布尔值";
            case "DATE":
                return "日期";
            case "JSON":
                return "JSON";
            default:
                return dataType;
        }
    }

    @Override
    public MyJsonBean<PageResult<TblExpenseParameter>> getList(Object param) {
        try {
            log.info("查询费用参数列表，参数: {}", param);

            // 解析查询参数
            Map<String, Object> paramMap = (Map<String, Object>) param;
            String parameterName = (String) paramMap.get("parameterName");
            String parameterType = (String) paramMap.get("parameterType");
            Integer isEnabled = (Integer) paramMap.get("isEnabled");
            Integer pageNo = paramMap.get("pageNo") != null ? (Integer) paramMap.get("pageNo") : 1;
            Integer pageSize = paramMap.get("pageSize") != null ? (Integer) paramMap.get("pageSize") : 10;

            // 构建查询条件
            LambdaQueryWrapper<TblExpenseParameter> wrapper = Wrappers.lambdaQuery();
            if (parameterName != null && !parameterName.trim().isEmpty()) {
                wrapper.like(TblExpenseParameter::getParameterName, parameterName);
            }
            if (parameterType != null && !parameterType.trim().isEmpty()) {
                wrapper.eq(TblExpenseParameter::getParameterType, parameterType);
            }
            if (isEnabled != null) {
                wrapper.eq(TblExpenseParameter::getIsEnabled, isEnabled);
            }
            wrapper.orderByDesc(TblExpenseParameter::getCreateTime);

            // 分页查询
            PageHelper.startPage(pageNo, pageSize);
            List<TblExpenseParameter> list = expenseParameterMapper.selectList(wrapper);
            PageInfo<TblExpenseParameter> pageInfo = new PageInfo<>(list);

            // 为每条记录添加中文名称字段和类型转换
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (TblExpenseParameter parameter : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("parameterId", parameter.getParameterId());
                map.put("parameterCode", parameter.getParameterCode());
                map.put("parameterName", parameter.getParameterName());
                map.put("parameterType", parameter.getParameterType());
                map.put("parameterTypeName", getParameterTypeName(parameter.getParameterType()));
                map.put("dataType", parameter.getDataType());
                map.put("dataTypeName", getDataTypeName(parameter.getDataType()));
                map.put("parameterValue", parameter.getParameterValue());
                map.put("defaultValue", parameter.getDefaultValue());
                // 将 Integer 转换为 Boolean 供前端使用
                map.put("isRequired", parameter.getIsRequired() != null && parameter.getIsRequired() == 1);
                map.put("isEditable", parameter.getIsEditable() != null && parameter.getIsEditable() == 1);
                map.put("isEnabled", parameter.getIsEnabled() != null && parameter.getIsEnabled() == 1);
                map.put("validationRule", parameter.getValidationRule());
                map.put("createTime", parameter.getCreateTime());
                map.put("createUser", parameter.getCreateUser());
                map.put("updateTime", parameter.getUpdateTime());
                map.put("updateUser", parameter.getUpdateUser());
                map.put("remark", parameter.getRemark());
                // 前端使用 description 字段，映射到 remark
                map.put("description", parameter.getRemark());
                resultList.add(map);
            }

            // 封装返回结果 - 使用原始类型避免泛型冲突
            PageResult pageResult = new PageResult();
            pageResult.setTotalRecord((int) pageInfo.getTotal());
            pageResult.setCurrentPage(pageInfo.getPageNum());
            pageResult.setTotalPage(pageInfo.getPages());
            pageResult.setPageSize(pageInfo.getPageSize());
            pageResult.setTlist(resultList);

            return MyJsonBean.successData(pageResult);
        } catch (Exception e) {
            log.error("查询费用参数列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<TblExpenseParameter> getById(String parameterId) {
        try {
            log.info("查询费用参数详情，parameterId: {}", parameterId);

            if (parameterId == null || parameterId.trim().isEmpty()) {
                return MyJsonBean.errorData("参数ID不能为空");
            }

            // 调用Mapper查询数据库
            TblExpenseParameter parameter = expenseParameterMapper.selectById(parameterId);

            if (parameter == null) {
                return MyJsonBean.errorData("参数不存在");
            }

            return MyJsonBean.successData(parameter);
        } catch (Exception e) {
            log.error("查询费用参数详情失败，parameterId: {}", parameterId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(Object param) {
        try {
            log.info("保存或更新费用参数，参数: {}", param);

            // 解析保存参数
            Map<String, Object> paramMap = (Map<String, Object>) param;

            TblExpenseParameter parameter = new TblExpenseParameter();
            parameter.setParameterId((String) paramMap.get("parameterId"));
            parameter.setParameterCode((String) paramMap.get("parameterCode"));
            parameter.setParameterName((String) paramMap.get("parameterName"));
            parameter.setParameterType((String) paramMap.get("parameterType"));
            parameter.setDataType((String) paramMap.get("dataType"));
            parameter.setParameterValue(paramMap.get("parameterValue") != null ? paramMap.get("parameterValue").toString() : null);
            parameter.setDefaultValue(paramMap.get("defaultValue") != null ? paramMap.get("defaultValue").toString() : null);
            parameter.setIsRequired(convertToInteger(paramMap.get("isRequired"), 0));
            parameter.setIsEditable(convertToInteger(paramMap.get("isEditable"), 1));
            parameter.setValidationRule((String) paramMap.get("validationRule"));
            parameter.setIsEnabled(convertToInteger(paramMap.get("isEnabled"), 1));
            // 前端使用 description 字段，后端映射到 remark
            String description = (String) paramMap.get("description");
            if (description == null || description.trim().isEmpty()) {
                description = (String) paramMap.get("remark");
            }
            parameter.setRemark(description);

            // 验证数据有效性
            if (parameter.getParameterCode() == null || parameter.getParameterCode().trim().isEmpty()) {
                return MyJsonBean.errorData("参数编码不能为空");
            }
            if (parameter.getParameterName() == null || parameter.getParameterName().trim().isEmpty()) {
                return MyJsonBean.errorData("参数名称不能为空");
            }
            if (parameter.getParameterType() == null || parameter.getParameterType().trim().isEmpty()) {
                return MyJsonBean.errorData("参数类型不能为空");
            }
            if (parameter.getDataType() == null || parameter.getDataType().trim().isEmpty()) {
                return MyJsonBean.errorData("数据类型不能为空");
            }

            // 判断是新增还是更新
            if (parameter.getParameterId() == null || parameter.getParameterId().trim().isEmpty()) {
                // 新增
                parameter.setParameterId(UUIDUtil.getUUID());
                parameter.setCreateTime(LocalDateTime.now());
                parameter.setCreateUser("SYSTEM"); // TODO: 从当前登录用户获取

                // 检查参数编码是否已存在
                TblExpenseParameter existing = expenseParameterMapper.selectByParameterCode(parameter.getParameterCode());
                if (existing != null) {
                    return MyJsonBean.errorData("参数编码已存在");
                }

                expenseParameterMapper.insert(parameter);
            } else {
                // 更新
                parameter.setUpdateTime(LocalDateTime.now());
                parameter.setUpdateUser("SYSTEM"); // TODO: 从当前登录用户获取

                // 检查参数是否存在
                TblExpenseParameter existing = expenseParameterMapper.selectById(parameter.getParameterId());
                if (existing == null) {
                    return MyJsonBean.errorData("参数不存在");
                }

                // 如果修改了参数编码，检查新编码是否已被其他记录使用
                if (!existing.getParameterCode().equals(parameter.getParameterCode())) {
                    TblExpenseParameter codeCheck = expenseParameterMapper.selectByParameterCode(parameter.getParameterCode());
                    if (codeCheck != null && !codeCheck.getParameterId().equals(parameter.getParameterId())) {
                        return MyJsonBean.errorData("参数编码已被其他记录使用");
                    }
                }

                expenseParameterMapper.updateById(parameter);
            }

            return MyJsonBean.successMsg("保存成功");
        } catch (Exception e) {
            log.error("保存或更新费用参数失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String parameterId) {
        try {
            log.info("删除费用参数，parameterId: {}", parameterId);

            if (parameterId == null || parameterId.trim().isEmpty()) {
                return MyJsonBean.errorData("参数ID不能为空");
            }

            // 验证参数是否存在
            TblExpenseParameter parameter = expenseParameterMapper.selectById(parameterId);
            if (parameter == null) {
                return MyJsonBean.errorData("参数不存在");
            }

            // 删除关联的选项
            LambdaQueryWrapper<TblExpenseParameterOption> optionWrapper = Wrappers.lambdaQuery();
            optionWrapper.eq(TblExpenseParameterOption::getParameterId, parameterId);
            expenseParameterOptionMapper.delete(optionWrapper);

            // 删除关联的权限
            LambdaQueryWrapper<TblExpenseParameterPermission> permissionWrapper = Wrappers.lambdaQuery();
            permissionWrapper.eq(TblExpenseParameterPermission::getParameterId, parameterId);
            expenseParameterPermissionMapper.delete(permissionWrapper);

            // 删除参数本身
            expenseParameterMapper.deleteById(parameterId);

            return MyJsonBean.successMsg("删除成功");
        } catch (Exception e) {
            log.error("删除费用参数失败，parameterId: {}", parameterId, e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean updateStatus(String parameterId, Integer isEnabled) {
        try {
            log.info("更新费用参数状态，parameterId: {}, isEnabled: {}", parameterId, isEnabled);

            if (parameterId == null || parameterId.trim().isEmpty()) {
                return MyJsonBean.errorData("参数ID不能为空");
            }

            if (isEnabled == null) {
                return MyJsonBean.errorData("状态值不能为空");
            }

            // 验证参数是否存在
            TblExpenseParameter parameter = expenseParameterMapper.selectById(parameterId);
            if (parameter == null) {
                return MyJsonBean.errorData("参数不存在");
            }

            // 更新状态
            parameter.setIsEnabled(isEnabled);
            parameter.setUpdateTime(LocalDateTime.now());
            parameter.setUpdateUser("SYSTEM"); // TODO: 从当前登录用户获取
            expenseParameterMapper.updateById(parameter);

            return MyJsonBean.successMsg("状态更新成功");
        } catch (Exception e) {
            log.error("更新费用参数状态失败，parameterId: {}", parameterId, e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<TblExpenseParameterOption>> getOptions(String parameterId) {
        try {
            log.info("获取参数选项，parameterId: {}", parameterId);

            if (parameterId == null || parameterId.trim().isEmpty()) {
                return MyJsonBean.errorData("参数ID不能为空");
            }

            // 查询选项列表
            List<TblExpenseParameterOption> options = expenseParameterOptionMapper.selectByParameterId(parameterId);

            return MyJsonBean.successData(options);
        } catch (Exception e) {
            log.error("获取参数选项失败，parameterId: {}", parameterId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<TblExpenseParameterPermission>> getPermissions(String parameterId) {
        try {
            log.info("获取参数权限，parameterId: {}", parameterId);

            if (parameterId == null || parameterId.trim().isEmpty()) {
                return MyJsonBean.errorData("参数ID不能为空");
            }

            // 查询权限列表
            List<TblExpenseParameterPermission> permissions = expenseParameterPermissionMapper.selectByParameterId(parameterId);

            return MyJsonBean.successData(permissions);
        } catch (Exception e) {
            log.error("获取参数权限失败，parameterId: {}", parameterId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOptions(String parameterId, List<TblExpenseParameterOption> options) {
        try {
            log.info("保存参数选项，parameterId: {}, options数量: {}", parameterId, options != null ? options.size() : 0);

            if (parameterId == null || parameterId.trim().isEmpty()) {
                return MyJsonBean.errorData("参数ID不能为空");
            }

            if (options == null || options.isEmpty()) {
                return MyJsonBean.errorData("选项列表不能为空");
            }

            // 验证参数是否存在
            TblExpenseParameter parameter = expenseParameterMapper.selectById(parameterId);
            if (parameter == null) {
                return MyJsonBean.errorData("参数不存在");
            }

            // 删除原有选项
            LambdaQueryWrapper<TblExpenseParameterOption> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(TblExpenseParameterOption::getParameterId, parameterId);
            expenseParameterOptionMapper.delete(wrapper);

            // 批量插入新选项
            for (TblExpenseParameterOption option : options) {
                if (option.getOptionId() == null || option.getOptionId().trim().isEmpty()) {
                    option.setOptionId(UUIDUtil.getUUID());
                }
                option.setParameterId(parameterId);
                option.setCreateTime(LocalDateTime.now());
                option.setCreateUser("SYSTEM"); // TODO: 从当前登录用户获取
                expenseParameterOptionMapper.insert(option);
            }

            return MyJsonBean.successMsg("选项保存成功");
        } catch (Exception e) {
            log.error("保存参数选项失败，parameterId: {}", parameterId, e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean savePermissions(String parameterId, List<TblExpenseParameterPermission> permissions) {
        try {
            log.info("保存参数权限，parameterId: {}, permissions数量: {}", parameterId, permissions != null ? permissions.size() : 0);

            if (parameterId == null || parameterId.trim().isEmpty()) {
                return MyJsonBean.errorData("参数ID不能为空");
            }

            if (permissions == null || permissions.isEmpty()) {
                return MyJsonBean.errorData("权限列表不能为空");
            }

            // 验证参数是否存在
            TblExpenseParameter parameter = expenseParameterMapper.selectById(parameterId);
            if (parameter == null) {
                return MyJsonBean.errorData("参数不存在");
            }

            // 删除原有权限
            LambdaQueryWrapper<TblExpenseParameterPermission> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(TblExpenseParameterPermission::getParameterId, parameterId);
            expenseParameterPermissionMapper.delete(wrapper);

            // 批量插入新权限
            for (TblExpenseParameterPermission permission : permissions) {
                if (permission.getPermissionId() == null || permission.getPermissionId().trim().isEmpty()) {
                    permission.setPermissionId(UUIDUtil.getUUID());
                }
                permission.setParameterId(parameterId);
                permission.setCreateTime(LocalDateTime.now());
                permission.setCreateUser("SYSTEM"); // TODO: 从当前登录用户获取
                expenseParameterPermissionMapper.insert(permission);
            }

            return MyJsonBean.successMsg("权限保存成功");
        } catch (Exception e) {
            log.error("保存参数权限失败，parameterId: {}", parameterId, e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }
}
