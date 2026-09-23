package com.financial.sharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.entity.TblProjectMember;
import java.util.List;

/**
 * 项目成员Service接口
 */
public interface TblProjectMemberService extends IService<TblProjectMember> {
    
    /**
     * 根据项目ID查询成员列表
     */
    List<TblProjectMember> getByProjectId(String projectId);
    
    /**
     * 根据用户ID查询成员列表
     */
    List<TblProjectMember> getByUserId(String userId);
    
    /**
     * 根据项目ID和用户ID查询成员
     */
    TblProjectMember getByProjectAndUser(String projectId, String userId);
    
    /**
     * 批量保存项目成员
     */
    boolean saveBatchMembers(List<TblProjectMember> members);
    
    /**
     * 删除项目的所有成员
     */
    boolean deleteByProjectId(String projectId);
}

