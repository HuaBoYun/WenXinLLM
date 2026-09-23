package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillIdentificationDTO;
import com.global.treasurer.dto.BillIdentificationQueryDTO;
import com.global.treasurer.entity.TblBillIdentification;
import com.global.treasurer.vo.BillIdentificationVO;

import java.util.List;

/**
 * 票据标识Service接口
 *
 * @author 华博云开发团队
 * @since 2026-02-02
 */
public interface IBillIdentificationService {

    /**
     * 分页查询票据标识列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<BillIdentificationVO> selectBillIdentificationList(BillIdentificationQueryDTO queryDTO);

    /**
     * 根据ID查询票据标识详情
     *
     * @param identificationId 标识ID
     * @return 标识详情
     */
    BillIdentificationVO selectBillIdentificationById(Long identificationId);

    /**
     * 新增票据标识
     *
     * @param dto 标识信息
     * @return 新增的标识
     */
    TblBillIdentification insertBillIdentification(BillIdentificationDTO dto);

    /**
     * 修改票据标识
     *
     * @param dto 标识信息
     * @return 修改后的标识
     */
    TblBillIdentification updateBillIdentification(BillIdentificationDTO dto);

    /**
     * 批量删除票据标识
     *
     * @param identificationIds 标识ID数组
     * @return 是否成功
     */
    boolean deleteBillIdentificationByIds(Long[] identificationIds);

    /**
     * 根据类型查询标识列表
     *
     * @param identificationType 标识类型
     * @return 标识列表
     */
    List<BillIdentificationVO> selectByType(String identificationType);

    /**
     * 获取标识树形结构
     *
     * @param queryDTO 查询条件
     * @return 树形结构列表
     */
    List<BillIdentificationVO> selectIdentificationTree(BillIdentificationQueryDTO queryDTO);

    /**
     * 批量更新启用状态
     *
     * @param identificationIds 标识ID数组
     * @param isEnabled 启用状态
     * @return 是否成功
     */
    boolean batchUpdateEnabled(Long[] identificationIds, Integer isEnabled);

    /**
     * 检查标识编码是否存在
     *
     * @param identificationCode 标识编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkCodeExists(String identificationCode, Long excludeId);
}

