package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblLeaseReturn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 退租申请Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-02-09
 */
@Mapper
public interface LeaseReturnMapper extends BaseMapper<TblLeaseReturn> {

    /**
     * 根据租赁ID查询退租申请
     */
    TblLeaseReturn selectByLeaseId(@Param("leaseId") Long leaseId);

    /**
     * 根据退租ID查询退租详情
     */
    TblLeaseReturn selectByReturnId(@Param("returnId") Long returnId);

    /**
     * 更新退租状态
     */
    int updateReturnStatus(@Param("returnId") Long returnId, 
                          @Param("status") String status,
                          @Param("approvalComments") String approvalComments,
                          @Param("approvedBy") Long approvedBy);

    /**
     * 查询待审批的退租申请列表
     */
    List<TblLeaseReturn> selectPendingApprovalList();

    /**
     * 根据租赁ID删除退租申请
     */
    int deleteByLeaseId(@Param("leaseId") Long leaseId);
}

