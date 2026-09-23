package com.global.treasurer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 票据历史记录Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Mapper
public interface BillHistoryMapper {

    /**
     * 查询票据操作历史记录
     *
     * @param billId 票据ID
     * @return 历史记录列表
     */
    @Select("SELECT OPERATE_TIME as operateTime, OPERATE_TYPE as operateType, " +
            "OPERATOR_NAME as operatorName, REMARK as remark " +
            "FROM TBL_BILL_OPERATION_LOG WHERE BILL_ID = #{billId} ORDER BY OPERATE_TIME DESC")
    List<Map<String, Object>> selectBillHistory(@Param("billId") Long billId);

    /**
     * 查询票据登记历史记录
     *
     * @param billId 票据ID
     * @return 历史记录列表
     */
    @Select("SELECT CREATE_TIME as operateTime, '登记' as operateType, " +
            "CREATE_BY as operatorName, REMARK as remark " +
            "FROM TBL_BILL_REGISTRATION WHERE BILL_ID = #{billId}")
    List<Map<String, Object>> selectRegistrationHistory(@Param("billId") Long billId);

    /**
     * 查询票据背书历史记录
     *
     * @param billId 票据ID
     * @return 历史记录列表
     */
    @Select("SELECT ENDORSEMENT_DATE as operateTime, '背书' as operateType, " +
            "ENDORSER_NAME as operatorName, ENDORSEMENT_REASON as remark " +
            "FROM TBL_BILL_ENDORSEMENT WHERE BILL_ID = #{billId} ORDER BY ENDORSEMENT_DATE DESC")
    List<Map<String, Object>> selectEndorsementHistory(@Param("billId") Long billId);

    /**
     * 查询票据贴现历史记录
     *
     * @param billId 票据ID
     * @return 历史记录列表
     */
    @Select("SELECT DISCOUNT_DATE as operateTime, '贴现' as operateType, " +
            "APPLICANT_NAME as operatorName, DISCOUNT_REASON as remark " +
            "FROM TBL_BILL_DISCOUNT WHERE BILL_ID = #{billId} ORDER BY DISCOUNT_DATE DESC")
    List<Map<String, Object>> selectDiscountHistory(@Param("billId") Long billId);
}

