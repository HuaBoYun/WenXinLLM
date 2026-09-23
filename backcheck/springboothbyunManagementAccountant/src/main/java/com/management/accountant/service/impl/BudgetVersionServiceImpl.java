package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.oracle.entity.budget.BudgetVersion;
import com.management.accountant.oracle.mapper.budget.BudgetVersionMapper;
import com.management.accountant.oracle.service.budget.BudgetVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 预算版本 ServiceImpl
 */
@Service
@Slf4j
public class BudgetVersionServiceImpl extends ServiceImpl<BudgetVersionMapper, BudgetVersion>
        implements BudgetVersionService {

    @Override
    public BudgetVersion getByVersionCode(String versionCode) {
        QueryWrapper<BudgetVersion> w = new QueryWrapper<>();
        w.eq("VERSION_CODE", versionCode).eq("DEL_FLAG", 0);
        return baseMapper.selectOne(w);
    }

    @Override
    public BudgetVersion getCurrentVersion(Integer fiscalYear) {
        QueryWrapper<BudgetVersion> w = new QueryWrapper<>();
        if (fiscalYear != null) w.eq("FISCAL_YEAR", fiscalYear);
        w.eq("IS_CURRENT", 1).eq("DEL_FLAG", 0).last("FETCH FIRST 1 ROWS ONLY");
        return baseMapper.selectOne(w);
    }

    @Override
    public List<BudgetVersion> getByFiscalYear(Integer fiscalYear) {
        QueryWrapper<BudgetVersion> w = new QueryWrapper<>();
        w.eq("FISCAL_YEAR", fiscalYear).eq("DEL_FLAG", 0).orderByDesc("VERSION_NUMBER");
        return baseMapper.selectList(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createVersion(BudgetVersion version) {
        if (version == null) return false;
        if (version.getDelFlag() == null) version.setDelFlag(0);
        // 自动生成版本号 VERSION_NUMBER（非空约束）
        if (!StringUtils.hasText(version.getVersionNumber())) {
            version.setVersionNumber(generateVersionNumber(version.getFiscalYear()));
        }
        // 自动生成版本编码 VERSION_CODE
        if (!StringUtils.hasText(version.getVersionCode())) {
            version.setVersionCode("VER" + System.currentTimeMillis());
        }
        // 默认状态
        if (!StringUtils.hasText(version.getVersionStatus())) {
            version.setVersionStatus("DRAFT");
        }
        version.setCreateTime(new Date());
        version.setUpdateTime(new Date());
        return save(version);
    }

    /**
     * 根据财年自动生成版本号，格式: V{年份}.{序号}
     */
    private String generateVersionNumber(Integer fiscalYear) {
        int year = fiscalYear != null ? fiscalYear : Calendar.getInstance().get(Calendar.YEAR);
        QueryWrapper<BudgetVersion> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (fiscalYear != null) w.eq("FISCAL_YEAR", fiscalYear);
        w.likeRight("VERSION_NUMBER", "V" + year);
        long count = baseMapper.selectCount(w);
        return "V" + year + "." + (count + 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateVersion(BudgetVersion version) {
        if (version == null || !StringUtils.hasText(version.getVersionId())) return false;
        version.setUpdateTime(new Date());
        return updateById(version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setCurrentVersion(String versionId) {
        // 先把所有版本的 IS_CURRENT 置 0
        BudgetVersion clearAll = new BudgetVersion();
        clearAll.setIsCurrent(false);
        clearAll.setUpdateTime(new Date());
        update(clearAll, new QueryWrapper<BudgetVersion>().eq("DEL_FLAG", 0));
        // 再激活目标版本
        BudgetVersion target = new BudgetVersion();
        target.setVersionId(versionId);
        target.setIsCurrent(true);
        target.setIsActive(true);
        target.setVersionStatus("ACTIVE");
        target.setUpdateTime(new Date());
        return updateById(target);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean archiveVersion(String versionId) {
        BudgetVersion v = new BudgetVersion();
        v.setVersionId(versionId);
        v.setVersionStatus("ARCHIVED");
        v.setUpdateTime(new Date());
        return updateById(v);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lockVersion(String versionId) {
        BudgetVersion v = new BudgetVersion();
        v.setVersionId(versionId);
        v.setVersionStatus("LOCKED");
        v.setUpdateTime(new Date());
        return updateById(v);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetVersion copyVersion(String versionId, String newVersionName) {
        BudgetVersion src = getById(versionId);
        if (src == null) return null;
        BudgetVersion copy = new BudgetVersion();
        copy.setVersionName(newVersionName != null ? newVersionName : src.getVersionName() + "_副本");
        copy.setVersionNumber(src.getVersionNumber() + "_copy");
        copy.setBudgetId(src.getBudgetId());
        copy.setBudgetName(src.getBudgetName());
        copy.setVersionType(src.getVersionType());
        copy.setFiscalYear(src.getFiscalYear());
        copy.setBaseVersionId(versionId);
        copy.setVersionStatus("DRAFT");
        copy.setIsCurrent(false);
        copy.setIsPublished(false);
        copy.setIsActive(false);
        copy.setDelFlag(0);
        copy.setCreateTime(new Date());
        copy.setUpdateTime(new Date());
        save(copy);
        return copy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteVersions(List<String> versionIds) {
        if (versionIds == null || versionIds.isEmpty()) return false;
        BudgetVersion v = new BudgetVersion();
        v.setDelFlag(1);
        v.setUpdateTime(new Date());
        return update(v, new QueryWrapper<BudgetVersion>().in("VERSION_ID", versionIds));
    }

    @Override
    public Page<BudgetVersion> pageQuery(int pageNum, int pageSize, BudgetVersion condition) {
        Page<BudgetVersion> page = new Page<>(pageNum, pageSize);
        QueryWrapper<BudgetVersion> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (condition != null) {
            if (StringUtils.hasText(condition.getVersionName())) w.like("VERSION_NAME", condition.getVersionName());
            if (StringUtils.hasText(condition.getVersionStatus())) w.eq("VERSION_STATUS", condition.getVersionStatus());
            if (StringUtils.hasText(condition.getVersionType())) w.eq("VERSION_TYPE", condition.getVersionType());
            if (condition.getFiscalYear() != null) w.eq("FISCAL_YEAR", condition.getFiscalYear());
            if (StringUtils.hasText(condition.getCreatorName())) w.like("CREATOR_NAME", condition.getCreatorName());
        }
        w.orderByDesc("CREATE_TIME");
        return baseMapper.selectPage(page, w);
    }

    @Override
    public Map<String, Object> compareVersions(String version1, String version2) {
        Map<String, Object> result = new HashMap<>();
        BudgetVersion v1 = getById(version1);
        BudgetVersion v2 = getById(version2);
        result.put("version1", v1);
        result.put("version2", v2);
        result.put("differences", new ArrayList<>());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rollbackVersion(String versionId) {
        return setCurrentVersion(versionId);
    }

    @Override
    public List<BudgetVersion> listByBudgetId(String budgetId) {
        QueryWrapper<BudgetVersion> w = new QueryWrapper<>();
        w.eq("BUDGET_ID", budgetId).eq("DEL_FLAG", 0).orderByDesc("CREATE_TIME");
        return baseMapper.selectList(w);
    }

    @Override
    public BudgetVersion getVersionDetail(String versionId) {
        return getById(versionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishVersion(String versionId) {
        BudgetVersion v = new BudgetVersion();
        v.setVersionId(versionId);
        v.setIsPublished(true);
        v.setVersionStatus("ACTIVE");
        v.setUpdateTime(new Date());
        return updateById(v);
    }
}

