package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillRegistrationDTO;
import com.global.treasurer.dto.BillRegistrationQueryDTO;
import com.global.treasurer.entity.TblBillRegistration;
import com.global.treasurer.vo.BillRegistrationVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 票据登记Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
public interface IBillRegistrationService extends IService<TblBillRegistration> {

    /**
     * 查询票据登记列表(分页)
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<BillRegistrationVO> selectBillRegistrationList(BillRegistrationQueryDTO queryDTO);

    /**
     * 根据ID查询票据登记详情
     *
     * @param billId 票据ID
     * @return 票据登记详情
     */
    BillRegistrationVO selectBillRegistrationById(Long billId);

    /**
     * 新增票据登记
     *
     * @param dto 票据登记信息
     * @return 新增的票据对象
     */
    TblBillRegistration insertBillRegistration(BillRegistrationDTO dto);

    /**
     * 修改票据登记
     *
     * @param dto 票据登记信息
     * @return 修改后的票据对象
     */
    TblBillRegistration updateBillRegistration(BillRegistrationDTO dto);

    /**
     * 批量删除票据登记
     *
     * @param billIds 票据ID数组
     * @return 删除结果
     */
    boolean deleteBillRegistrationByIds(Long[] billIds);

    /**
     * 批量导入票据
     *
     * @param billList 票据列表
     * @return 导入结果
     */
    int batchImportBills(java.util.List<BillRegistrationDTO> billList);

    /**
     * 作废票据
     *
     * @param billId 票据ID
     * @param reason 作废原因
     * @return 作废结果
     */
    boolean cancelBill(Long billId, String reason);

    // 临时添加的方法声明,用于解决编译错误
    default void exportBillRegistration(BillRegistrationQueryDTO queryDTO, HttpServletResponse response) {}
    default java.util.Map<String, Object> getBillRegistrationStatistics(BillRegistrationQueryDTO queryDTO) { return null; }
}

