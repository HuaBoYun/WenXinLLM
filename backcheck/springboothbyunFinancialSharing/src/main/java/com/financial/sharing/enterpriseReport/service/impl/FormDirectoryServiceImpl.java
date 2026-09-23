package com.financial.sharing.enterpriseReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.enterpriseReport.dto.FormDirectoryQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblFormDirectory;
import com.financial.sharing.enterpriseReport.mapper.FormDirectoryMapper;
import com.financial.sharing.enterpriseReport.service.FormDirectoryService;
import com.hbfk.util.user.UserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表单目录Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class FormDirectoryServiceImpl extends ServiceImpl<FormDirectoryMapper, TblFormDirectory> 
        implements FormDirectoryService {

    @Override
    public List<TblFormDirectory> getDirectoryTree(FormDirectoryQueryParam param) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        // 查询所有目录
        QueryWrapper<TblFormDirectory> wrapper = new QueryWrapper<>();
        wrapper.eq("TENANT_ID", tenantId);
        if (!StringUtils.isEmpty(param.getStatus())) {
            wrapper.eq("STATUS", param.getStatus());
        }
        if (!StringUtils.isEmpty(param.getDirectoryName())) {
            wrapper.like("DIRECTORY_NAME", param.getDirectoryName());
        }
        wrapper.orderByAsc("SORT_NO", "CREATE_TIME");
        
        List<TblFormDirectory> allDirectories = this.list(wrapper);
        
        // 构建树形结构
        return buildTree(allDirectories, null);
    }

    @Override
    public List<TblFormDirectory> getList(FormDirectoryQueryParam param) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        QueryWrapper<TblFormDirectory> wrapper = new QueryWrapper<>();
        wrapper.eq("TENANT_ID", tenantId);
        
        if (!StringUtils.isEmpty(param.getDirectoryCode())) {
            wrapper.eq("DIRECTORY_CODE", param.getDirectoryCode());
        }
        if (!StringUtils.isEmpty(param.getDirectoryName())) {
            wrapper.like("DIRECTORY_NAME", param.getDirectoryName());
        }
        if (!StringUtils.isEmpty(param.getParentDirectoryId())) {
            wrapper.eq("PARENT_DIRECTORY_ID", param.getParentDirectoryId());
        }
        if (!StringUtils.isEmpty(param.getStatus())) {
            wrapper.eq("STATUS", param.getStatus());
        }
        
        wrapper.orderByAsc("SORT_NO", "CREATE_TIME");
        
        return this.list(wrapper);
    }

    @Override
    public TblFormDirectory getDetail(String directoryId) {
        return this.getById(directoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveDirectory(TblFormDirectory directory) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();
        
        directory.setTenantId(tenantId);
        
        if (StringUtils.isEmpty(directory.getDirectoryId())) {
            // 新增
            directory.setCreateUser(userId);
            directory.setCreateTime(now);
            directory.setUpdateUser(userId);
            directory.setUpdateTime(now);
            
            // 计算层级
            if (StringUtils.isEmpty(directory.getParentDirectoryId())) {
                directory.setDirectoryLevel(1);
            } else {
                TblFormDirectory parent = this.getById(directory.getParentDirectoryId());
                if (parent != null) {
                    directory.setDirectoryLevel(parent.getDirectoryLevel() + 1);
                } else {
                    directory.setDirectoryLevel(1);
                }
            }
            
            // 设置默认排序号
            if (directory.getSortNo() == null) {
                directory.setSortNo(0);
            }
            
            // 设置默认状态
            if (StringUtils.isEmpty(directory.getStatus())) {
                directory.setStatus("ACTIVE");
            }
            
            return this.save(directory);
        } else {
            // 修改
            directory.setUpdateUser(userId);
            directory.setUpdateTime(now);
            
            // 如果父目录改变,需要重新计算层级
            TblFormDirectory oldDirectory = this.getById(directory.getDirectoryId());
            if (oldDirectory != null) {
                String oldParentId = oldDirectory.getParentDirectoryId();
                String newParentId = directory.getParentDirectoryId();
                
                if (!StringUtils.isEmpty(newParentId) && !newParentId.equals(oldParentId)) {
                    TblFormDirectory parent = this.getById(newParentId);
                    if (parent != null) {
                        directory.setDirectoryLevel(parent.getDirectoryLevel() + 1);
                    }
                }
            }
            
            return this.updateById(directory);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDirectory(String directoryId) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        // 检查是否有子目录
        int childCount = baseMapper.countChildren(directoryId, tenantId);
        if (childCount > 0) {
            throw new RuntimeException("该目录下有子目录,不能删除");
        }
        
        // TODO: 检查是否有表单组关联
        
        return this.removeById(directoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveDirectory(String directoryId, String targetParentId) {
        TblFormDirectory directory = this.getById(directoryId);
        if (directory == null) {
            throw new RuntimeException("目录不存在");
        }

        // 不能移动到自己的子目录下
        if (isChildDirectory(directoryId, targetParentId)) {
            throw new RuntimeException("不能移动到自己的子目录下");
        }

        directory.setParentDirectoryId(targetParentId);

        // 重新计算层级
        if (StringUtils.isEmpty(targetParentId)) {
            directory.setDirectoryLevel(1);
        } else {
            TblFormDirectory parent = this.getById(targetParentId);
            if (parent != null) {
                directory.setDirectoryLevel(parent.getDirectoryLevel() + 1);
            }
        }

        directory.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        directory.setUpdateTime(new Date());

        return this.updateById(directory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSort(String directoryId, Integer sortNo) {
        TblFormDirectory directory = this.getById(directoryId);
        if (directory == null) {
            throw new RuntimeException("目录不存在");
        }

        directory.setSortNo(sortNo);
        directory.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        directory.setUpdateTime(new Date());

        return this.updateById(directory);
    }

    /**
     * 构建树形结构
     *
     * @param allDirectories 所有目录
     * @param parentId 父目录ID
     * @return 树形结构
     */
    private List<TblFormDirectory> buildTree(List<TblFormDirectory> allDirectories, String parentId) {
        List<TblFormDirectory> result = new ArrayList<>();

        for (TblFormDirectory directory : allDirectories) {
            String pid = directory.getParentDirectoryId();

            // 判断是否为当前父节点的子节点
            boolean isChild = (parentId == null && StringUtils.isEmpty(pid)) ||
                            (parentId != null && parentId.equals(pid));

            if (isChild) {
                // 递归查找子节点
                List<TblFormDirectory> children = buildTree(allDirectories, directory.getDirectoryId());
                directory.setChildren(children);
                result.add(directory);
            }
        }

        return result;
    }

    /**
     * 判断是否为子目录
     *
     * @param parentId 父目录ID
     * @param childId 子目录ID
     * @return 是否为子目录
     */
    private boolean isChildDirectory(String parentId, String childId) {
        if (StringUtils.isEmpty(childId)) {
            return false;
        }

        if (parentId.equals(childId)) {
            return true;
        }

        TblFormDirectory child = this.getById(childId);
        if (child == null || StringUtils.isEmpty(child.getParentDirectoryId())) {
            return false;
        }

        return isChildDirectory(parentId, child.getParentDirectoryId());
    }
}
