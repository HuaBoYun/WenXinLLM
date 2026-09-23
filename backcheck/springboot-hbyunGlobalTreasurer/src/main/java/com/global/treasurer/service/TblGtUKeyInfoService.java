package com.global.treasurer.service;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblGtUKeyInfo;

/**
 * 全球司库-U盾信息Service接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
public interface TblGtUKeyInfoService extends IService<TblGtUKeyInfo> {

    /**
     * 分页查询U盾信息列表
     *
     * @param page 分页对象
     * @param orgId 组织ID
     * @param holderName 账户名称
     * @param ukeyNo U盾编号
     * @param ukeyStatus U盾状态
     * @return 分页结果
     */
    IPage<TblGtUKeyInfo> getPageList(Page<TblGtUKeyInfo> page,
                                     BigDecimal orgId,
                                     Long ukeyId,
                                     String holderName,
                                     String ukeyNo,
                                     String ukeyStatus);

    /**
     * 新增U盾信息
     *
     * @param entity U盾信息实体
     * @return 是否成功
     */
    boolean saveUKeyInfo(TblGtUKeyInfo entity);

    /**
     * 更新U盾信息
     *
     * @param entity U盾信息实体
     * @return 是否成功
     */
    boolean updateUKeyInfo(TblGtUKeyInfo entity);

    /**
     * 删除U盾信息
     *
     * @param ukeyId U盾ID
     * @return 是否成功
     */
    boolean deleteUKeyInfo(BigDecimal ukeyId);

    /**
     * 锁定U盾
     *
     * @param ukeyId U盾ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean lockUKey(BigDecimal ukeyId, BigDecimal operatorId);

    /**
     * 解锁U盾
     *
     * @param ukeyId U盾ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean unlockUKey(BigDecimal ukeyId, BigDecimal operatorId);
}
