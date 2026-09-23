package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.entity.TblProjectMember;
import com.financial.sharing.mapper.TblProjectMemberMapper;
import com.financial.sharing.service.TblProjectMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 项目成员Service实现
 */
@Slf4j
@Service
public class TblProjectMemberServiceImpl extends ServiceImpl<TblProjectMemberMapper, TblProjectMember> 
        implements TblProjectMemberService {

    @Override
    public List<TblProjectMember> getByProjectId(String projectId) {
        return this.baseMapper.selectByProjectId(projectId);
    }

    @Override
    public List<TblProjectMember> getByUserId(String userId) {
        return this.baseMapper.selectByUserId(userId);
    }

    @Override
    public TblProjectMember getByProjectAndUser(String projectId, String userId) {
        return this.baseMapper.selectByProjectAndUser(projectId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchMembers(List<TblProjectMember> members) {
        try {
            return this.saveBatch(members);
        } catch (Exception e) {
            log.error("批量保存项目成员失败", e);
            throw new RuntimeException("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByProjectId(String projectId) {
        try {
            return this.baseMapper.deleteByProjectId(projectId) > 0;
        } catch (Exception e) {
            log.error("删除项目成员失败", e);
            throw new RuntimeException("删除失败: " + e.getMessage());
        }
    }
}

