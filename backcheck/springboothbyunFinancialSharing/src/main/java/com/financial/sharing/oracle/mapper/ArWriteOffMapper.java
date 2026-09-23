package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.ArWriteOffEntity;
import com.financial.sharing.vo.param.ArWriteOffQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 核销记录Mapper接口
 * @author system
 * @since 2026-01-04
 */
public interface ArWriteOffMapper extends BaseMapper<ArWriteOffEntity> {

    /**
     * 分页查询核销记录
     */
    IPage<ArWriteOffEntity> selectWriteOffPage(Page<ArWriteOffEntity> page, @Param("param") ArWriteOffQueryParam param);

    /**
     * 查询核销记录列表（配合PageHelper使用）
     */
    List<ArWriteOffEntity> selectWriteOffList(@Param("param") ArWriteOffQueryParam param);

    /**
     * 根据收款单ID查询核销记录
     */
    List<ArWriteOffEntity> selectByReceiptId(@Param("receiptId") String receiptId);

    /**
     * 根据应收单ID查询核销记录
     */
    List<ArWriteOffEntity> selectByReceivableId(@Param("receivableId") String receivableId);

    /**
     * 批量插入核销记录
     */
    int batchInsert(@Param("writeOffs") List<ArWriteOffEntity> writeOffs);

    /**
     * 反核销（删除核销记录）
     */
    int reverseWriteOff(@Param("receiptId") String receiptId);

    /**
     * 查询核销统计
     */
    Map<String, Object> selectWriteOffStatistics(@Param("param") ArWriteOffQueryParam param);
}

