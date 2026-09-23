package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblProjectMember;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 项目成员Mapper
 */
public interface TblProjectMemberMapper extends BaseMapper<TblProjectMember> {
    
    /**
     * 根据项目ID查询成员列表
     */
    List<TblProjectMember> selectByProjectId(@Param("projectId") String projectId);
    
    /**
     * 根据用户ID查询成员列表
     */
    List<TblProjectMember> selectByUserId(@Param("userId") String userId);
    
    /**
     * 根据项目ID和用户ID查询成员
     */
    TblProjectMember selectByProjectAndUser(@Param("projectId") String projectId, @Param("userId") String userId);
    
    /**
     * 删除项目的所有成员
     */
    int deleteByProjectId(@Param("projectId") String projectId);
}

