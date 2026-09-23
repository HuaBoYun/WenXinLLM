package com.huabo.financialdata.service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户账簿授权管理
 *
 * @author lee
 * @version 1.0.0
 **/
public interface IManageUserBookService {

    /**
     * 查询用户已关联授权的账套ID集合
     *
     * @param staffId 用户ID
     * @return 返回用户已授权的账套ID集合
     */
    List<String> getBookIdListByStaffId(BigDecimal staffId);


    /**
     * 更新用户和其授权的账套选择中状态为NULL（null-->未选择）
     *
     * @param staffId    用户ID
     * @param status     待更新的状态：null-->未选中 0-->选中
     */
    void updateStatusByStaffIdAndBookIdList(BigDecimal staffId, Integer status);

    /**
     * 根据staffId和账套ID列表更新账套状态
     * @param staffId
     * @param bookIdList
     * @param status
     */
    void updateStatusByStaffIdAndBookIdListone(BigDecimal staffId, String bookIdList, Integer status);


}
