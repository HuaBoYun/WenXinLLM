package com.financial.sharing.groupControl.service;

import com.financial.sharing.groupControl.dto.DimensionMemberQueryParam;
import com.financial.sharing.groupControl.entity.TblDimensionMember;

import java.util.List;

/**
 * 维度成员服务接口
 * 
 * @author system
 * @since 2026-01-30
 */
public interface DimensionMemberService {

    /**
     * 查询成员树形结构
     * 
     * @param param 查询参数
     * @return 树形结构列表
     */
    List<TblDimensionMember> getTree(DimensionMemberQueryParam param);

    /**
     * 查询成员详情
     * 
     * @param memberId 成员ID
     * @return 成员详情
     */
    TblDimensionMember getDetail(String memberId);

    /**
     * 保存成员（新增或修改）
     * 
     * @param member 成员信息
     * @return 保存结果
     */
    boolean save(TblDimensionMember member);

    /**
     * 删除成员（级联删除子节点）
     * 
     * @param memberId 成员ID
     * @return 删除结果
     */
    boolean delete(String memberId);

    /**
     * 批量删除成员
     * 
     * @param memberIds 成员ID列表
     * @return 删除结果
     */
    boolean batchDelete(List<String> memberIds);

    /**
     * 移动成员位置
     * 
     * @param memberId 成员ID
     * @param targetParentId 目标父成员ID
     * @param sortNo 排序号
     * @return 移动结果
     */
    boolean move(String memberId, String targetParentId, Integer sortNo);

    /**
     * 检查成员编码是否存在
     * 
     * @param dimensionId 维度ID
     * @param memberCode 成员编码
     * @param memberId 成员ID（修改时传入，新增时为null）
     * @return 是否存在
     */
    boolean checkCodeExists(String dimensionId, String memberCode, String memberId);

    /**
     * 更新成员状态
     * 
     * @param memberId 成员ID
     * @param status 状态
     * @return 更新结果
     */
    boolean updateStatus(String memberId, String status);
}

