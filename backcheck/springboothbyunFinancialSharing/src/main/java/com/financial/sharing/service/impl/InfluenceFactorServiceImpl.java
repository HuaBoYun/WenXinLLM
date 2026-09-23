package com.financial.sharing.service.impl;

import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.exception.ServiceException;
import com.financial.sharing.service.InfluenceFactorService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.InfluenceFactorQueryParam;
import com.financial.sharing.vo.param.InfluenceFactorSaveParam;
import com.financial.sharing.vo.result.InfluenceFactorVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 影响因素定义服务实现类
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class InfluenceFactorServiceImpl implements InfluenceFactorService {

    @Autowired
    private DateBaseConfig dateBaseConfig;

    @Override
    public PageResult<InfluenceFactorVO> getInfluenceFactorPage(InfluenceFactorQueryParam param) {
        try {
            // 使用PageHelper分页
            com.github.pagehelper.PageHelper.startPage(param.getPageNo(), param.getPageSize());

            List<?> entities;

            
                com.financial.sharing.oracle.mapper.InfluenceFactorMapper mapper = dateBaseConfig.getOracleInfluenceFactorMapper();
                entities = mapper.selectInfluenceFactorPage(param);
            

            // 转换为VO
            List<InfluenceFactorVO> voList;
            
                voList = ((List<com.financial.sharing.oracle.entity.InfluenceFactorEntity>) entities).stream()
                        .map(this::convertOracleEntityToVO)
                        .collect(Collectors.toList());
            

            // 获取分页信息
            PageInfo<?> pageInfo = new PageInfo<>(entities);

            PageResult<InfluenceFactorVO> pageResult = new PageResult<>();
            pageResult.setTlist(voList);
            pageResult.setTotalRecord((int) pageInfo.getTotal());
            pageResult.setCurrentPage(param.getPageNo());
            pageResult.setPageSize(param.getPageSize());
            pageResult.setTotalPage(pageInfo.getPages());
            return pageResult;
        } catch (Exception e) {
            log.error("分页查询影响因素失败", e);
            throw new ServiceException("分页查询影响因素失败");
        }
    }

    @Override
    public InfluenceFactorVO getInfluenceFactorById(Long factorId) {
        if (factorId == null) {
            throw new ServiceException("影响因素ID不能为空");
        }

        try {

                com.financial.sharing.oracle.mapper.InfluenceFactorMapper mapper = dateBaseConfig.getOracleInfluenceFactorMapper();
                com.financial.sharing.oracle.entity.InfluenceFactorEntity entity = mapper.selectById(factorId);
                return entity != null ? convertOracleEntityToVO(entity) : null;

        } catch (Exception e) {
            log.error("根据ID查询影响因素失败，factorId: {}", factorId, e);
            throw new ServiceException("查询影响因素失败");
        }
    }

    @Override
    public InfluenceFactorVO getInfluenceFactorByCode(String factorCode, Long tenantId, Long bookId) {
        if (!StringUtils.hasText(factorCode) || tenantId == null || bookId == null) {
            throw new ServiceException("参数不能为空");
        }

        try {

                com.financial.sharing.oracle.mapper.InfluenceFactorMapper mapper = dateBaseConfig.getOracleInfluenceFactorMapper();
                com.financial.sharing.oracle.entity.InfluenceFactorEntity entity = mapper.selectByFactorCode(factorCode, tenantId, bookId);
                return entity != null ? convertOracleEntityToVO(entity) : null;

        } catch (Exception e) {
            log.error("根据编码查询影响因素失败，factorCode: {}", factorCode, e);
            throw new ServiceException("查询影响因素失败");
        }
    }

    @Override
    public List<InfluenceFactorVO> getInfluenceFactorsByType(Integer factorType, Long tenantId, Long bookId) {
        if (factorType == null || tenantId == null || bookId == null) {
            throw new ServiceException("参数不能为空");
        }

        try {

                com.financial.sharing.oracle.mapper.InfluenceFactorMapper mapper = dateBaseConfig.getOracleInfluenceFactorMapper();
                List<com.financial.sharing.oracle.entity.InfluenceFactorEntity> entities = mapper.selectByFactorType(factorType, tenantId, bookId);
                return entities.stream().map(this::convertOracleEntityToVO).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("根据类型查询影响因素失败，factorType: {}", factorType, e);
            throw new ServiceException("查询影响因素失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveInfluenceFactor(InfluenceFactorSaveParam param, Long userId) {
        if (param == null || userId == null) {
            throw new ServiceException("参数不能为空");
        }

        // 检查编码是否存在
        if (checkFactorCodeExists(param.getFactorCode(), param.getTenantId(), param.getBookId(), null)) {
            throw new ServiceException("影响因素编码已存在");
        }

        try {
            LocalDateTime now = LocalDateTime.now();


                com.financial.sharing.oracle.entity.InfluenceFactorEntity entity = new com.financial.sharing.oracle.entity.InfluenceFactorEntity();
                BeanUtils.copyProperties(param, entity);
                entity.setIsDeleted(0);
                entity.setCreateTime(now);
                entity.setUpdateTime(now);
                entity.setCreateBy(userId);
                entity.setUpdateBy(userId);

                // 设置默认值
                if (entity.getIsEnabled() == null) {
                    entity.setIsEnabled(1);
                }

                if (entity.getVersion() == null) {
                    entity.setVersion(1);
                }

                com.financial.sharing.oracle.mapper.InfluenceFactorMapper mapper = dateBaseConfig.getOracleInfluenceFactorMapper();
                mapper.insert(entity);
                return entity.getFactorId();
        } catch (Exception e) {
            log.error("保存影响因素失败", e);
            throw new ServiceException("保存影响因素失败");
        }
    }

    // 转换方法
    private InfluenceFactorVO convertMysqlEntityToVO(com.financial.sharing.mysql.entity.InfluenceFactorEntity entity) {
        InfluenceFactorVO vo = new InfluenceFactorVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置枚举值对应的名称
        vo.setFactorTypeName(getFactorTypeName(entity.getFactorType()));
        vo.setDataTypeName(getDataTypeName(entity.getDataType()));
        vo.setIsEnabledName(entity.getIsEnabled() == 1 ? "启用" : "禁用");

        return vo;
    }

    private InfluenceFactorVO convertOracleEntityToVO(com.financial.sharing.oracle.entity.InfluenceFactorEntity entity) {
        InfluenceFactorVO vo = new InfluenceFactorVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置枚举值对应的名称
        vo.setFactorTypeName(getFactorTypeName(entity.getFactorType()));
        vo.setDataTypeName(getDataTypeName(entity.getDataType()));
        vo.setIsEnabledName(entity.getIsEnabled() == 1 ? "启用" : "禁用");

        return vo;
    }
    
    private String getFactorTypeName(Integer factorType) {
        if (factorType == null) return "";
        switch (factorType) {
            case 1: return "税率因素";
            case 2: return "汇率因素";
            case 3: return "折旧率因素";
            case 4: return "费用率因素";
            case 5: return "其他因素";
            default: return "";
        }
    }

    private String getDataTypeName(String dataType) {
        if (dataType == null) return "";
        switch (dataType) {
            case "STRING": return "字符串";
            case "NUMBER": return "数字";
            case "BOOLEAN": return "布尔值";
            default: return dataType;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInfluenceFactor(InfluenceFactorSaveParam param, Long userId) {
        if (param == null || param.getFactorId() == null || userId == null) {
            throw new ServiceException("参数不能为空");
        }

        // 检查编码是否存在
        if (checkFactorCodeExists(param.getFactorCode(), param.getTenantId(), param.getBookId(), param.getFactorId())) {
            throw new ServiceException("影响因素编码已存在");
        }

        try {
            LocalDateTime now = LocalDateTime.now();


                com.financial.sharing.oracle.entity.InfluenceFactorEntity entity = new com.financial.sharing.oracle.entity.InfluenceFactorEntity();
                BeanUtils.copyProperties(param, entity);
                entity.setUpdateTime(now);
                entity.setUpdateBy(userId);

                com.financial.sharing.oracle.mapper.InfluenceFactorMapper mapper = dateBaseConfig.getOracleInfluenceFactorMapper();
                return mapper.updateById(entity) > 0;

        } catch (Exception e) {
            log.error("更新影响因素失败", e);
            throw new ServiceException("更新影响因素失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInfluenceFactor(Long factorId, Long userId) {
        if (factorId == null || userId == null) {
            throw new ServiceException("参数不能为空");
        }

        try {
            LocalDateTime now = LocalDateTime.now();


                com.financial.sharing.oracle.entity.InfluenceFactorEntity entity = new com.financial.sharing.oracle.entity.InfluenceFactorEntity();
                entity.setFactorId(factorId);
                entity.setIsDeleted(1);
                entity.setUpdateTime(now);
                entity.setUpdateBy(userId);

                com.financial.sharing.oracle.mapper.InfluenceFactorMapper mapper = dateBaseConfig.getOracleInfluenceFactorMapper();
                return mapper.updateById(entity) > 0;

        } catch (Exception e) {
            log.error("删除影响因素失败，factorId: {}", factorId, e);
            throw new ServiceException("删除影响因素失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteInfluenceFactors(List<Long> factorIds, Long userId) {
        if (CollectionUtils.isEmpty(factorIds) || userId == null) {
            throw new ServiceException("参数不能为空");
        }

        try {

                com.financial.sharing.oracle.mapper.InfluenceFactorMapper mapper = dateBaseConfig.getOracleInfluenceFactorMapper();
                return mapper.batchDelete(factorIds, userId) > 0;

        } catch (Exception e) {
            log.error("批量删除影响因素失败", e);
            throw new ServiceException("批量删除影响因素失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInfluenceFactorStatus(Long factorId, Integer isEnabled, Long userId) {
        if (factorId == null || isEnabled == null || userId == null) {
            throw new ServiceException("参数不能为空");
        }

        try {
            LocalDateTime now = LocalDateTime.now();


                com.financial.sharing.oracle.entity.InfluenceFactorEntity entity = new com.financial.sharing.oracle.entity.InfluenceFactorEntity();
                entity.setFactorId(factorId);
                entity.setIsEnabled(isEnabled);
                entity.setUpdateTime(now);
                entity.setUpdateBy(userId);

                com.financial.sharing.oracle.mapper.InfluenceFactorMapper mapper = dateBaseConfig.getOracleInfluenceFactorMapper();
                return mapper.updateById(entity) > 0;

        } catch (Exception e) {
            log.error("更新影响因素状态失败，factorId: {}", factorId, e);
            throw new ServiceException("更新影响因素状态失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateInfluenceFactorStatus(List<Long> factorIds, Integer isEnabled, Long userId) {
        if (CollectionUtils.isEmpty(factorIds) || isEnabled == null || userId == null) {
            throw new ServiceException("参数不能为空");
        }

        try {

                com.financial.sharing.oracle.mapper.InfluenceFactorMapper mapper = dateBaseConfig.getOracleInfluenceFactorMapper();
                return mapper.batchUpdateEnabled(factorIds, isEnabled, userId) > 0;

        } catch (Exception e) {
            log.error("批量更新影响因素状态失败", e);
            throw new ServiceException("批量更新影响因素状态失败");
        }
    }

    @Override
    public boolean checkFactorCodeExists(String factorCode, Long tenantId, Long bookId, Long excludeId) {
        if (!StringUtils.hasText(factorCode) || tenantId == null || bookId == null) {
            return false;
        }

        try {

                com.financial.sharing.oracle.mapper.InfluenceFactorMapper mapper = dateBaseConfig.getOracleInfluenceFactorMapper();
                return mapper.checkFactorCodeExists(factorCode, tenantId, bookId, excludeId) > 0;

        } catch (Exception e) {
            log.error("检查影响因素编码是否存在失败", e);
            return false;
        }
    }
}
