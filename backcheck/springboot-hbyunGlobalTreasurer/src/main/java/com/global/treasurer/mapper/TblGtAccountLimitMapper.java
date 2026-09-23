package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblGtAccountLimit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 全球司库-账户限额Mapper接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@Mapper
public interface TblGtAccountLimitMapper extends BaseMapper<TblGtAccountLimit> {

    /**
     * 分页查询账户限额列表
     *
     * @param accountNumber 账户号码
     * @param limitType 限额类型
     * @param limitStatus 限额状态
     * @return 账户限额列表
     */
    List<TblGtAccountLimit> selectPageList(@Param("accountNumber") String accountNumber,
                                           @Param("limitType") String limitType,
                                           @Param("limitStatus") String limitStatus);
}
