package com.financial.sharing.enterpriseReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.enterpriseReport.dto.FormGroupQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblFormGroup;
import com.financial.sharing.enterpriseReport.mapper.FormGroupMapper;
import com.financial.sharing.enterpriseReport.service.FormGroupService;
import com.hbfk.util.user.UserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 表单组Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class FormGroupServiceImpl extends ServiceImpl<FormGroupMapper, TblFormGroup> 
        implements FormGroupService {

    @Override
    public List<TblFormGroup> getList(FormGroupQueryParam param) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        return baseMapper.selectFormGroupList(
            tenantId,
            param.getDirectoryId(),
            param.getGroupName(),
            param.getStatus()
        );
    }

    @Override
    public TblFormGroup getDetail(String groupId) {
        TblFormGroup formGroup = this.getById(groupId);
        if (formGroup != null) {
            // 可以在这里补充关联信息
            String tenantId = UserUtils.getUser().getOrgid().toString();
            List<TblFormGroup> list = baseMapper.selectFormGroupList(
                tenantId, null, null, null
            );
            for (TblFormGroup group : list) {
                if (group.getGroupId().equals(groupId)) {
                    formGroup.setDirectoryName(group.getDirectoryName());
                    break;
                }
            }
        }
        return formGroup;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveFormGroup(TblFormGroup formGroup) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();
        
        formGroup.setTenantId(tenantId);
        
        // 检查表单组编码是否重复
        int count = baseMapper.checkGroupCodeExists(
            formGroup.getGroupCode(),
            tenantId,
            formGroup.getGroupId()
        );
        if (count > 0) {
            throw new RuntimeException("表单组编码已存在");
        }
        
        if (StringUtils.isEmpty(formGroup.getGroupId())) {
            // 新增
            formGroup.setCreateUser(userId);
            formGroup.setCreateTime(now);
            formGroup.setUpdateUser(userId);
            formGroup.setUpdateTime(now);
            
            // 设置默认排序号
            if (formGroup.getSortNo() == null) {
                formGroup.setSortNo(0);
            }
            
            // 设置默认状态
            if (StringUtils.isEmpty(formGroup.getStatus())) {
                formGroup.setStatus("ACTIVE");
            }
            
            return this.save(formGroup);
        } else {
            // 修改
            formGroup.setUpdateUser(userId);
            formGroup.setUpdateTime(now);
            
            return this.updateById(formGroup);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFormGroup(String groupId) {
        // TODO: 检查是否有表单模板关联
        // TODO: 检查是否有报表任务关联
        
        return this.removeById(groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSort(String groupId, Integer sortNo) {
        TblFormGroup formGroup = this.getById(groupId);
        if (formGroup == null) {
            throw new RuntimeException("表单组不存在");
        }
        
        formGroup.setSortNo(sortNo);
        formGroup.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        formGroup.setUpdateTime(new Date());
        
        return this.updateById(formGroup);
    }

    @Override
    public List<TblFormGroup> getListByDirectoryId(String directoryId) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        QueryWrapper<TblFormGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("TENANT_ID", tenantId);
        wrapper.eq("DIRECTORY_ID", directoryId);
        wrapper.eq("STATUS", "ACTIVE");
        wrapper.orderByAsc("SORT_NO", "CREATE_TIME");
        
        return this.list(wrapper);
    }
}

