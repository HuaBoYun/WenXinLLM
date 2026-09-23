package com.global.treasurer.mapper;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblGtDirectConnectAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;

import java.util.List;
import java.util.Map;

/**
 * 全球司库-直联授权Mapper接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@Mapper
public interface TblGtDirectConnectAuthMapper extends BaseMapper<TblGtDirectConnectAuth> {

    /**
     * 分页查询直联授权列表
     *
     * @param accountNumber 账户号码
     * @param authType 授权类型
     * @param authStatus 授权状态
     * @return 直联授权列表
     */
    List<TblGtDirectConnectAuth> selectPageList(
            @Param("accountNumber") String accountNumber,
            @Param("authType") String authType,
            @Param("authStatus") String authStatus
    );

    /**
     * 按状态统计直联授权数量
     */
    List<Map<String, Object>> selectStatusCount(@Param("orgId") BigDecimal orgId);
}
