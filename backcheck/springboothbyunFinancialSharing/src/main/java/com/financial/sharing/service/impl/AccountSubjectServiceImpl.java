package com.financial.sharing.service.impl;

import com.financial.sharing.util.Java8Collections;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.service.AccountSubjectService;
import com.financial.sharing.vo.param.AccountSubjectQueryParam;
import com.financial.sharing.vo.param.AccountSubjectSaveParam;
import com.financial.sharing.vo.result.AccountSubjectVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会计科目服务实现类
 * 
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class AccountSubjectServiceImpl implements AccountSubjectService {

    @Autowired
    private DateBaseConfig dateBaseConfig;

    // 科目类型名称映射
    private static final Map<Integer, String> SUBJECT_TYPE_NAME_MAP = new HashMap<>();
    // 余额方向名称映射
    private static final Map<Integer, String> BALANCE_DIRECTION_NAME_MAP = new HashMap<>();

    static {
        SUBJECT_TYPE_NAME_MAP.put(1, "资产");
        SUBJECT_TYPE_NAME_MAP.put(2, "负债");
        SUBJECT_TYPE_NAME_MAP.put(3, "权益");
        SUBJECT_TYPE_NAME_MAP.put(4, "收入");
        SUBJECT_TYPE_NAME_MAP.put(5, "费用");

        BALANCE_DIRECTION_NAME_MAP.put(1, "借方");
        BALANCE_DIRECTION_NAME_MAP.put(2, "贷方");
    }

    @Override
    public PageInfo<AccountSubjectVO> getAccountSubjectPage(AccountSubjectQueryParam param) {
        try {
            // 参数验证
            if (param == null) {
                throw new IllegalArgumentException("查询参数不能为空");
            }

            // 设置默认值
            if (param.getPageNum() == null || param.getPageNum() <= 0) {
                param.setPageNum(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(10);
            }

            // 使用PageHelper分页
            PageHelper.startPage(param.getPageNum(), param.getPageSize());

            List<AccountSubjectVO> result;
            try {
                result = dateBaseConfig.getOracleAccountSubjectMapper().selectAccountSubjectPage(param);
            } catch (Exception e) {
                log.error("数据库查询异常", e);
                throw new RuntimeException("数据库查询失败：" + e.getMessage(), e);
            }

            // 安全的集合处理
            if (result == null) {
                result = Java8Collections.listOf();
            }

            // 使用PageInfo包装结果
            PageInfo<AccountSubjectVO> pageInfo = new PageInfo<>(result);

            // 填充显示名称
            if (pageInfo.getList() != null && !pageInfo.getList().isEmpty()) {
                for (AccountSubjectVO vo : pageInfo.getList()) {
                    try {
                        fillDisplayNames(vo);
                    } catch (Exception e) {
                        log.warn("填充显示名称失败，但不影响整体查询，vo={}", vo, e);
                        // 继续处理其他记录，不中断整体流程
                    }
                }
            }
            return pageInfo;
        } catch (IllegalArgumentException e) {
            throw e; // 重新抛出参数异常
        } catch (Exception e) {
            log.error("查询会计科目分页失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage(), e);
        }
    }

    @Override
    public AccountSubjectVO getAccountSubjectById(Long subjectId) {
        try {
            // 参数验证
            if (subjectId == null || subjectId <= 0) {
                throw new IllegalArgumentException("科目ID不能为空或小于等于0");
            }

            Object entity;
            try {
                entity = dateBaseConfig.getOracleAccountSubjectMapper().selectById(subjectId);
            } catch (Exception e) {
                log.error("根据ID查询会计科目失败，subjectId={}", subjectId, e);
                throw new RuntimeException("查询失败：" + e.getMessage(), e);
            }

            if (entity == null) {
                log.debug("未找到科目信息，subjectId={}", subjectId);
                return null;
            }

            AccountSubjectVO vo = new AccountSubjectVO();
            try {
                BeanUtils.copyProperties(entity, vo);
                fillDisplayNames(vo);
            } catch (Exception e) {
                log.warn("实体转换或填充显示名称失败，subjectId={}", subjectId, e);
                // 即使转换失败，也返回基础信息
            }
            return vo;
        } catch (IllegalArgumentException e) {
            throw e; // 重新抛出参数异常
        } catch (Exception e) {
            log.error("获取会计科目详情失败，subjectId={}", subjectId, e);
            throw new RuntimeException("获取详情失败：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "accountSubject", allEntries = true)
    public AccountSubjectVO saveAccountSubject(AccountSubjectSaveParam param) {
        // 检查科目编码是否存在
        if (checkAccountSubjectCodeExists(param.getSubjectCode(), param.getBookId(), param.getTenantId(), null)) {
            throw new RuntimeException("科目编码已存在");
        }

        // 计算科目级次
        Integer subjectLevel = calculateSubjectLevel(param.getParentSubjectId(), param.getBookId(), param.getTenantId());

        Object entity;
        
            com.financial.sharing.oracle.entity.AccountSubjectEntity oracleEntity = new com.financial.sharing.oracle.entity.AccountSubjectEntity();
            BeanUtils.copyProperties(param, oracleEntity);
            oracleEntity.setSubjectLevel(subjectLevel);
            oracleEntity.setIsLeaf(1); // 新增的科目默认为末级
            oracleEntity.setIsEnabled(param.getIsEnabled() != null ? param.getIsEnabled() : 1);
            oracleEntity.setCreateTime(LocalDateTime.now());
            oracleEntity.setUpdateTime(LocalDateTime.now());

            dateBaseConfig.getOracleAccountSubjectMapper().insert(oracleEntity);
            entity = oracleEntity;
        

        // 如果有上级科目，更新上级科目的末级标识
        if (param.getParentSubjectId() != null) {
            updateParentLeafFlag(param.getParentSubjectId());
        }

        AccountSubjectVO vo = new AccountSubjectVO();
        BeanUtils.copyProperties(entity, vo);
        fillDisplayNames(vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "accountSubject", allEntries = true)
    public AccountSubjectVO updateAccountSubject(AccountSubjectSaveParam param) {
        if (param.getSubjectId() == null) {
            throw new RuntimeException("科目ID不能为空");
        }

        // 检查科目编码是否存在（排除自己）
        if (checkAccountSubjectCodeExists(param.getSubjectCode(), param.getBookId(), param.getTenantId(), param.getSubjectId())) {
            throw new RuntimeException("科目编码已存在");
        }

        Object entity;

        com.financial.sharing.oracle.entity.AccountSubjectEntity oracleEntity = dateBaseConfig.getOracleAccountSubjectMapper().selectById(param.getSubjectId());
        if (oracleEntity == null) {
            throw new RuntimeException("会计科目不存在");
        }

        BeanUtils.copyProperties(param, oracleEntity);
        oracleEntity.setUpdateTime(LocalDateTime.now());

        dateBaseConfig.getOracleAccountSubjectMapper().updateById(oracleEntity);
        entity = oracleEntity;

        AccountSubjectVO vo = new AccountSubjectVO();
        BeanUtils.copyProperties(entity, vo);
        fillDisplayNames(vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAccountSubject(Long subjectId) {
        if (subjectId == null) {
            return false;
        }

        // 检查是否有子科目
        int childrenCount;
        
            childrenCount = dateBaseConfig.getOracleAccountSubjectMapper().countChildrenByParentId(subjectId, null);
        

        if (childrenCount > 0) {
            throw new RuntimeException("存在下级科目，不能删除");
        }

        // 执行删除
        int result;
        
            result = dateBaseConfig.getOracleAccountSubjectMapper().deleteById(subjectId);
        

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteAccountSubjects(List<Long> subjectIds) {
        if (Java8Collections.isEmpty(subjectIds)) {
            return false;
        }

        int result;
        
            result = dateBaseConfig.getOracleAccountSubjectMapper().batchDelete(subjectIds, null);
        

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAccountSubjectStatus(Long subjectId, Integer isEnabled) {
        if (subjectId == null) {
            return false;
        }

        int result;
        
            result = dateBaseConfig.getOracleAccountSubjectMapper().batchUpdateStatus(Java8Collections.listOf(subjectId), isEnabled, null);
        

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateAccountSubjectStatus(List<Long> subjectIds, Integer isEnabled) {
        if (Java8Collections.isEmpty(subjectIds)) {
            return false;
        }

        int result;
        
            result = dateBaseConfig.getOracleAccountSubjectMapper().batchUpdateStatus(subjectIds, isEnabled, null);
        

        return result > 0;
    }

    @Override
    public boolean checkAccountSubjectCodeExists(String subjectCode, Long bookId, Long tenantId, Long excludeId) {
        if (!StringUtils.hasText(subjectCode) || bookId == null || tenantId == null) {
            return false;
        }

        Object entity;
        
            entity = dateBaseConfig.getOracleAccountSubjectMapper().selectBySubjectCode(subjectCode, bookId, tenantId, excludeId);
        

        return entity != null;
    }

    @Override
    public List<AccountSubjectVO> getAccountSubjectTree(Long bookId, Long tenantId) {
        if (bookId == null || tenantId == null) {
            return Java8Collections.listOf();
        }

        List<AccountSubjectVO> result;

            result = dateBaseConfig.getOracleAccountSubjectMapper().selectAccountSubjectTree(bookId, tenantId);


        if (!Java8Collections.isEmpty(result)) {
            result.forEach(this::fillDisplayNames);
        }
        return result;
    }

    @Override
    public List<AccountSubjectVO> getAccountSubjectsByType(Integer subjectType, Long bookId, Long tenantId) {
        if (subjectType == null || bookId == null || tenantId == null) {
            return Java8Collections.listOf();
        }

        List<AccountSubjectVO> result;

            result = dateBaseConfig.getOracleAccountSubjectMapper().selectBySubjectType(subjectType, bookId, tenantId);


        if (!Java8Collections.isEmpty(result)) {
            result.forEach(this::fillDisplayNames);
        }
        return result;
    }

    @Override
    public List<AccountSubjectVO> getAccountSubjectsByParentId(Long parentSubjectId, Long bookId, Long tenantId) {
        if (bookId == null || tenantId == null) {
            return Java8Collections.listOf();
        }

        List<AccountSubjectVO> result;

            result = dateBaseConfig.getOracleAccountSubjectMapper().selectByParentId(parentSubjectId, bookId, tenantId);


        if (!Java8Collections.isEmpty(result)) {
            result.forEach(this::fillDisplayNames);
        }
        return result;
    }

    /**
     * 填充显示名称
     */
    private void fillDisplayNames(AccountSubjectVO vo) {
        if (vo == null) {
            return;
        }
        vo.setSubjectTypeName(SUBJECT_TYPE_NAME_MAP.get(vo.getSubjectType()));
        vo.setBalanceDirectionName(BALANCE_DIRECTION_NAME_MAP.get(vo.getBalanceDirection()));
    }

    /**
     * 计算科目级次
     */
    private Integer calculateSubjectLevel(Long parentSubjectId, Long bookId, Long tenantId) {
        if (parentSubjectId == null) {
            return 1; // 顶级科目
        }

        AccountSubjectVO parent = getAccountSubjectById(parentSubjectId);
        if (parent == null) {
            return 1;
        }
        return parent.getSubjectLevel() + 1;
    }

    /**
     * 更新上级科目的末级标识
     */
    private void updateParentLeafFlag(Long parentSubjectId) {
        if (parentSubjectId == null) {
            return;
        }

        
            dateBaseConfig.getOracleAccountSubjectMapper().updateLeafFlag(parentSubjectId, 0, null);
        
    }

    @Override
    @CacheEvict(value = "accountSubject", allEntries = true)
    public void refreshCache(Long bookId) {
        log.info("刷新科目缓存，bookId: {}", bookId);
        // 缓存已被@CacheEvict注解清除
    }
}
