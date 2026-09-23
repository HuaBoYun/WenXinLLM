package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblBillEndorsement;
import com.global.treasurer.dto.BillEndorsementQueryDTO;
import com.global.treasurer.vo.BillEndorsementVO;
import com.global.treasurer.vo.CirculationRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 票据背书Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Mapper
public interface BillEndorsementMapper extends BaseMapper<TblBillEndorsement> {

    /**
     * 查询票据背书列表
     *
     * @param queryDTO 查询条件
     * @return 票据背书列表
     */
    List<BillEndorsementVO> selectBillEndorsementList(@Param("query") BillEndorsementQueryDTO queryDTO);

    /**
     * 根据ID查询票据背书详情
     *
     * @param endorsementId 背书ID
     * @return 票据背书详情
     */
    BillEndorsementVO selectBillEndorsementById(@Param("endorsementId") Long endorsementId);

    /**
     * 根据背书编号查询背书
     *
     * @param endorsementNumber 背书编号
     * @return 背书信息
     */
    TblBillEndorsement selectByEndorsementNumber(@Param("endorsementNumber") String endorsementNumber);

    /**
     * 批量删除票据背书
     *
     * @param endorsementIds 背书ID数组
     * @return 影响行数
     */
    int deleteBillEndorsementByIds(@Param("endorsementIds") Long[] endorsementIds);

    /**
     * 批量更新背书状态
     *
     * @param endorsementIds 背书ID列表
     * @param status 状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("endorsementIds") List<Long> endorsementIds, @Param("status") String status);

    /**
     * 根据票据ID查询流转记录（背书）
     *
     * @param billId 票据ID
     * @return 流转记录列表
     */
    List<CirculationRecordVO> selectCirculationByBillId(@Param("billId") Long billId);
}

