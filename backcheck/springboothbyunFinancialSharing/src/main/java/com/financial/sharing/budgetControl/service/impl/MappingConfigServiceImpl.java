package com.financial.sharing.budgetControl.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.budgetControl.dto.MappingConfigQueryParam;
import com.financial.sharing.budgetControl.entity.TblMappingConfig;
import com.financial.sharing.budgetControl.mapper.MappingConfigMapper;
import com.financial.sharing.budgetControl.service.MappingConfigService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 映射配置Service实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class MappingConfigServiceImpl extends ServiceImpl<MappingConfigMapper, TblMappingConfig> 
        implements MappingConfigService {

    @Override
    public MyJsonBean queryPage(MappingConfigQueryParam param) {
        try {
            LambdaQueryWrapper<TblMappingConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblMappingConfig::getOrgId, param.getOrgId());
            
            if (StringUtils.isNotBlank(param.getMappingCode())) {
                wrapper.like(TblMappingConfig::getMappingCode, param.getMappingCode());
            }
            if (StringUtils.isNotBlank(param.getMappingName())) {
                wrapper.like(TblMappingConfig::getMappingName, param.getMappingName());
            }
            if (StringUtils.isNotBlank(param.getSourceSystem())) {
                wrapper.eq(TblMappingConfig::getSourceSystem, param.getSourceSystem());
            }
            if (StringUtils.isNotBlank(param.getMappingType())) {
                wrapper.eq(TblMappingConfig::getMappingType, param.getMappingType());
            }
            if (StringUtils.isNotBlank(param.getIsEnabled())) {
                wrapper.eq(TblMappingConfig::getIsEnabled, param.getIsEnabled());
            }
            
            wrapper.orderByAsc(TblMappingConfig::getSortNo);
            wrapper.orderByDesc(TblMappingConfig::getCreateTime);

            Page<TblMappingConfig> page = new Page<>(param.getPageNumber(), param.getPageSize());
            IPage<TblMappingConfig> result = this.page(page, wrapper);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询映射配置失败", e);
            return MyJsonBean.errorData("查询映射配置失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryById(String mappingId) {
        try {
            TblMappingConfig config = this.getById(mappingId);
            if (config == null) {
                return MyJsonBean.errorData("映射配置不存在");
            }
            return MyJsonBean.successData(config);
        } catch (Exception e) {
            log.error("查询映射配置失败", e);
            return MyJsonBean.errorData("查询映射配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean add(TblMappingConfig config) {
        try {
            // 检查映射编码是否重复
            LambdaQueryWrapper<TblMappingConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblMappingConfig::getMappingCode, config.getMappingCode());
            wrapper.eq(TblMappingConfig::getOrgId, config.getOrgId());
            long count = this.count(wrapper);
            if (count > 0) {
                return MyJsonBean.errorData("映射编码已存在");
            }

            // 设置创建信息
            String userId = UserUtils.getUserId();
            config.setCreateUser(userId);
            config.setCreateTime(new Date());
            config.setUpdateUser(userId);
            config.setUpdateTime(new Date());

            this.save(config);
            return MyJsonBean.successData("新增成功");
        } catch (Exception e) {
            log.error("新增映射配置失败", e);
            return MyJsonBean.errorData("新增映射配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean modify(TblMappingConfig config) {
        try {
            TblMappingConfig existConfig = this.getById(config.getMappingId());
            if (existConfig == null) {
                return MyJsonBean.errorData("映射配置不存在");
            }

            String userId = UserUtils.getUserId();
            config.setUpdateUser(userId);
            config.setUpdateTime(new Date());

            this.updateById(config);
            return MyJsonBean.successData("修改成功");
        } catch (Exception e) {
            log.error("修改映射配置失败", e);
            return MyJsonBean.errorData("修改映射配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean remove(String mappingId) {
        try {
            TblMappingConfig config = this.getById(mappingId);
            if (config == null) {
                return MyJsonBean.errorData("映射配置不存在");
            }

            this.removeById(mappingId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除映射配置失败", e);
            return MyJsonBean.errorData("删除映射配置失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryBySourceSystem(String sourceSystem, String orgId) {
        try {
            LambdaQueryWrapper<TblMappingConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblMappingConfig::getSourceSystem, sourceSystem);
            wrapper.eq(TblMappingConfig::getOrgId, orgId);
            wrapper.eq(TblMappingConfig::getIsEnabled, "Y");
            wrapper.orderByAsc(TblMappingConfig::getSortNo);
            
            List<TblMappingConfig> configs = this.list(wrapper);
            return MyJsonBean.successData(configs);
        } catch (Exception e) {
            log.error("查询映射配置失败", e);
            return MyJsonBean.errorData("查询映射配置失败：" + e.getMessage());
        }
    }
}

