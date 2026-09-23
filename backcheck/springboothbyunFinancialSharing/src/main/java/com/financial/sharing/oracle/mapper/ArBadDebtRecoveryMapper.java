package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.ArBadDebtRecoveryEntity;
import com.financial.sharing.vo.param.ArBadDebtRecoveryQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 坏账回收Mapper接口
 * @author system
 * @since 2026-01-04
 */
public interface ArBadDebtRecoveryMapper extends BaseMapper<ArBadDebtRecoveryEntity> {

    /**
     * 分页查询坏账回收
     */
    IPage<ArBadDebtRecoveryEntity> selectRecoveryPage(Page<ArBadDebtRecoveryEntity> page, 
                                                       @Param("param") ArBadDebtRecoveryQueryParam param);

    /**
     * 查询坏账回收列表（配合PageHelper使用）
     */
    List<ArBadDebtRecoveryEntity> selectRecoveryList(@Param("param") ArBadDebtRecoveryQueryParam param);

    /**
     * 根据原核销ID查询回收记录
     */
    List<ArBadDebtRecoveryEntity> selectByWriteOffId(@Param("writeOffId") String writeOffId);

    /**
     * 查询坏账回收统计
     */
    Map<String, Object> selectRecoveryStatistics(@Param("param") ArBadDebtRecoveryQueryParam param);

    /**
     * 生成回收单号
     */
    String generateRecoveryNo(@Param("prefix") String prefix, @Param("tenantId") Long tenantId);
}

