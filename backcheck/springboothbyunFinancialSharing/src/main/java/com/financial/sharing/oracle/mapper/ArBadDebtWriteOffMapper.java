package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.ArBadDebtWriteOffEntity;
import com.financial.sharing.vo.param.ArBadDebtWriteOffQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 坏账核销Mapper接口
 * @author system
 * @since 2026-01-04
 */
public interface ArBadDebtWriteOffMapper extends BaseMapper<ArBadDebtWriteOffEntity> {

    /**
     * 分页查询坏账核销
     */
    IPage<ArBadDebtWriteOffEntity> selectBadDebtWriteOffPage(Page<ArBadDebtWriteOffEntity> page, 
                                                              @Param("param") ArBadDebtWriteOffQueryParam param);

    /**
     * 查询坏账核销列表（配合PageHelper使用）
     */
    List<ArBadDebtWriteOffEntity> selectBadDebtWriteOffList(@Param("param") ArBadDebtWriteOffQueryParam param);

    /**
     * 根据应收单ID查询坏账核销
     */
    ArBadDebtWriteOffEntity selectByReceivableId(@Param("receivableId") String receivableId);

    /**
     * 更新核销状态
     */
    int updateWriteOffStatus(@Param("writeOffId") String writeOffId, 
                             @Param("status") Integer status,
                             @Param("auditorId") String auditorId,
                             @Param("auditComments") String auditComments);

    /**
     * 查询坏账核销统计
     */
    Map<String, Object> selectBadDebtWriteOffStatistics(@Param("param") ArBadDebtWriteOffQueryParam param);

    /**
     * 生成核销单号
     */
    String generateWriteOffNo(@Param("prefix") String prefix, @Param("tenantId") Long tenantId);
}

