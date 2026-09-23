package com.financial.sharing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.oracle.entity.SystemParamsEntity;
import com.financial.sharing.oracle.mapper.SystemParamsMapper;
import com.financial.sharing.service.SystemParamsService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.SystemParamsQueryParam;
import com.financial.sharing.vo.param.SystemParamsSaveParam;
import com.financial.sharing.vo.result.SystemParamsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统参数配置Service实现类
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class SystemParamsServiceImpl implements SystemParamsService {

    @Autowired
    private SystemParamsMapper systemParamsMapper;

    private static final String PARAM_TYPE_STRING = "STRING";
    private static final String PARAM_TYPE_NUMBER = "NUMBER";
    private static final String PARAM_TYPE_BOOLEAN = "BOOLEAN";
    private static final String PARAM_TYPE_DATE = "DATE";

    @Override
    public PageResult<SystemParamsVO> getSystemParamsPage(SystemParamsQueryParam param) {
        // 使用PageHelper分页
        PageHelper.startPage(param.getPageNumber(), param.getPageSize());

        // 调用Mapper查询
        List<SystemParamsEntity> entityList = systemParamsMapper.selectSystemParamsList(param);

        // 使用PageInfo包装结果
        PageInfo<SystemParamsEntity> pageInfo = new PageInfo<>(entityList);

        // 转换为VO对象
        List<SystemParamsVO> voList = pageInfo.getList().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<SystemParamsVO>((int) pageInfo.getTotal(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getPageSize(), voList);
    }

    @Override
    public SystemParamsVO getSystemParamsById(Long id) {
        SystemParamsEntity entity = systemParamsMapper.selectById(id);
        return entity != null ? convertToVO(entity) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSystemParams(SystemParamsSaveParam param) {
        // 检查参数编码是否存在
        int exists = systemParamsMapper.checkParamCodeExists(param.getParamCode(),
                param.getTenantId(), param.getBookId(), null);
        if (exists > 0) {
            throw new RuntimeException("参数编码已存在：" + param.getParamCode());
        }

        SystemParamsEntity entity = new SystemParamsEntity();
        BeanUtils.copyProperties(param, entity);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setIsDeleted(0);

        return systemParamsMapper.insert(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSystemParams(SystemParamsSaveParam param) {
        if (param.getId() == null) {
            throw new RuntimeException("参数ID不能为空");
        }

        // 检查参数编码是否存在
        int exists = systemParamsMapper.checkParamCodeExists(param.getParamCode(),
                param.getTenantId(), param.getBookId(), param.getId());
        if (exists > 0) {
            throw new RuntimeException("参数编码已存在：" + param.getParamCode());
        }

        SystemParamsEntity entity = new SystemParamsEntity();
        BeanUtils.copyProperties(param, entity);
        entity.setUpdateTime(LocalDateTime.now());

        return systemParamsMapper.updateById(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSystemParams(Long id) {
        SystemParamsEntity entity = systemParamsMapper.selectById(id);
        if (entity == null) {
            throw new RuntimeException("参数不存在");
        }

        // 逻辑删除
        entity.setIsDeleted(1);
        entity.setUpdateTime(LocalDateTime.now());
        return systemParamsMapper.updateById(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSystemParams(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return true;
        }

        List<SystemParamsEntity> entities = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Long id : ids) {
            SystemParamsEntity entity = systemParamsMapper.selectById(id);
            if (entity != null) {
                entity.setIsDeleted(1);
                entity.setUpdateTime(now);
                entities.add(entity);
            }
        }
        if (!entities.isEmpty()) {
            for (SystemParamsEntity entity : entities) {
                systemParamsMapper.updateById(entity);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSystemParamsStatus(Long id, Integer isEnabled) {
        SystemParamsEntity entity = systemParamsMapper.selectById(id);
        if (entity == null) {
            throw new RuntimeException("参数不存在");
        }
        entity.setIsEnabled(isEnabled);
        entity.setUpdateTime(LocalDateTime.now());
        return systemParamsMapper.updateById(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateSystemParamsStatus(List<Long> ids, Integer isEnabled) {
        if (ids == null || ids.isEmpty()) {
            return true;
        }
        return systemParamsMapper.batchUpdateStatus(ids, isEnabled,
                ids.size() > 0 ? systemParamsMapper.selectById(ids.get(0)).getTenantId() : null) > 0;
    }

    @Override
    public String importSystemParams(MultipartFile file) {
        // TODO: 实现Excel导入功能
        return "导入功能开发中...";
    }

    @Override
    public void exportSystemParams(HttpServletResponse response, SystemParamsQueryParam param) {
        // TODO: 实现Excel导出功能
        throw new RuntimeException("导出功能开发中...");
    }

    @Override
    public String getParamValue(String paramCode, Long tenantId, Long bookId) {
        SystemParamsEntity entity = systemParamsMapper.getByParamCode(paramCode, tenantId, bookId);
        return entity != null ? entity.getParamValue() : null;
    }

    @Override
    public List<SystemParamsVO> getParamsByCategory(String categoryCode, Long tenantId, Long bookId) {
        List<SystemParamsEntity> entities = systemParamsMapper.getByCategoryCode(categoryCode, tenantId, bookId);
        return entities.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 实体转VO
     * @param entity 实体对象
     * @return VO对象
     */
    private SystemParamsVO convertToVO(SystemParamsEntity entity) {
        SystemParamsVO vo = new SystemParamsVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置枚举值名称
        vo.setIsRequiredName(entity.getIsRequired() == 1 ? "是" : "否");
        vo.setIsEnabledName(entity.getIsEnabled() == 1 ? "启用" : "禁用");
        vo.setParamTypeName(getParamTypeName(entity.getParamType()));
        vo.setCategoryName(getCategoryName(entity.getCategoryCode()));

        return vo;
    }

    /**
     * 获取参数类型名称
     * @param paramType 参数类型
     * @return 类型名称
     */
    private String getParamTypeName(String paramType) {
        switch (paramType) {
            case PARAM_TYPE_STRING:
                return "字符串";
            case PARAM_TYPE_NUMBER:
                return "数字";
            case PARAM_TYPE_BOOLEAN:
                return "布尔值";
            case PARAM_TYPE_DATE:
                return "日期";
            default:
                return paramType;
        }
    }

    /**
     * 获取分类名称
     * @param categoryCode 分类编码
     * @return 分类名称
     */
    private String getCategoryName(String categoryCode) {
        // TODO: 可以从字典表中获取分类名称
        switch (categoryCode) {
            case "SYSTEM":
                return "系统配置";
            case "BUSINESS":
                return "业务配置";
            case "UI":
                return "界面配置";
            case "SECURITY":
                return "安全配置";
            default:
                return categoryCode;
        }
    }
}