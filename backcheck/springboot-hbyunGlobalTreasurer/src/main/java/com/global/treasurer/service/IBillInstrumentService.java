package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillInstrumentDTO;
import com.global.treasurer.dto.BillInstrumentQueryDTO;
import com.global.treasurer.entity.TblBillInstrument;
import com.global.treasurer.vo.BillInstrumentVO;

import java.util.Map;

/**
 * 票据管理通用Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface IBillInstrumentService {

    /**
     * 分页查询票据列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<BillInstrumentVO> selectBillInstrumentList(BillInstrumentQueryDTO queryDTO);

    /**
     * 根据ID查询票据详情
     *
     * @param instrumentId 票据ID
     * @return 票据详情
     */
    BillInstrumentVO selectBillInstrumentById(Long instrumentId);

    /**
     * 新增票据
     *
     * @param dto 票据DTO
     * @return 票据实体
     */
    TblBillInstrument insertBillInstrument(BillInstrumentDTO dto);

    /**
     * 修改票据
     *
     * @param dto 票据DTO
     * @return 票据实体
     */
    TblBillInstrument updateBillInstrument(BillInstrumentDTO dto);

    /**
     * 批量删除票据
     *
     * @param instrumentIds 票据ID数组
     * @return 是否成功
     */
    boolean deleteBillInstrumentByIds(Long[] instrumentIds);

    /**
     * 票据背书
     *
     * @param instrumentId 票据ID
     * @param endorseData 背书数据
     * @return 是否成功
     */
    boolean endorseBill(Long instrumentId, Map<String, Object> endorseData);

    /**
     * 票据贴现
     *
     * @param instrumentId 票据ID
     * @param discountData 贴现数据
     * @return 是否成功
     */
    boolean discountBill(Long instrumentId, Map<String, Object> discountData);
}

