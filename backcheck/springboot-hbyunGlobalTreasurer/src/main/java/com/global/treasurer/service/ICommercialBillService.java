package com.global.treasurer.service;

import com.global.treasurer.dto.CommercialBillDTO;
import com.global.treasurer.dto.CommercialBillQueryDTO;
import com.global.treasurer.entity.TblCommercialBill;
import com.global.treasurer.vo.CommercialBillVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * 商业汇票Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
public interface ICommercialBillService {

    /**
     * 查询商业汇票列表
     */
    PageInfo<CommercialBillVO> selectCommercialBillList(CommercialBillQueryDTO queryDTO);

    /**
     * 查询商业汇票详情
     */
    CommercialBillVO selectCommercialBillById(Long billId);

    /**
     * 新增商业汇票
     */
    TblCommercialBill insertCommercialBill(CommercialBillDTO dto);

    /**
     * 修改商业汇票
     */
    TblCommercialBill updateCommercialBill(CommercialBillDTO dto);

    /**
     * 批量删除商业汇票
     */
    boolean deleteCommercialBillByIds(Long[] billIds);

    /**
     * 开立汇票
     */
    boolean issueBill(Map<String, Object> issueData);

    /**
     * 承兑汇票
     */
    boolean acceptBill(Long billId, Map<String, Object> acceptData);

    /**
     * 背书汇票
     */
    boolean endorseBill(Long billId, Map<String, Object> endorseData);

    /**
     * 贴现汇票
     */
    boolean discountBill(Long billId, Map<String, Object> discountData);

    /**
     * 到期处理
     */
    boolean matureBill(Long billId);
}
