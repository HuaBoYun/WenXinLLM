package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblGuaranteeApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 担保申请Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface GuaranteeApplicationMapper extends BaseMapper<TblGuaranteeApplication> {

    List<TblGuaranteeApplication> selectApplicationList(Map<String, Object> params);

    TblGuaranteeApplication selectApplicationById(@Param("applicationId") Long applicationId);

    int updateApplicationStatus(@Param("applicationId") Long applicationId, @Param("status") String status);

    int batchDeleteByIds(@Param("applicationIds") List<Long> applicationIds);

    Map<String, Object> selectApplicationSummary(@Param("companyId") Long companyId);

    /**
     * 更新担保申请（自定义方法，避免与 MyBatis-Plus 的 updateById 冲突）
     */
    int updateApplication(TblGuaranteeApplication application);
}

