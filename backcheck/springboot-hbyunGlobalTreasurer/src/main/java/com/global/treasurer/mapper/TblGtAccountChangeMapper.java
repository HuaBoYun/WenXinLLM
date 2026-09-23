package com.global.treasurer.mapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtAccountChange;
import org.apache.ibatis.annotations.Param;

/**
 * 全球司库-账户变更Mapper接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
public interface TblGtAccountChangeMapper extends BaseMapper<TblGtAccountChange> {

    /**
     * 分页查询账户变更列表
     */
    IPage<TblGtAccountChange> selectPageList(
            Page<TblGtAccountChange> page,
            @Param("accountName") String accountName,
            @Param("changeType") String changeType,
            @Param("applicationStatus") String applicationStatus,
            @Param("orgId") BigDecimal orgId
    );

    /**
     * 按状态统计各机构的申请数量
     */
    List<Map<String, Object>> selectStatusCount(@Param("orgId") BigDecimal orgId);
}
