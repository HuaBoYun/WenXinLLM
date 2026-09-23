package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.CommercialBillQueryDTO;
import com.global.treasurer.entity.TblCommercialBill;
import com.global.treasurer.vo.CommercialBillVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商业汇票Mapper接口
 */
@Mapper
public interface CommercialBillMapper extends BaseMapper<TblCommercialBill> {

    /**
     * 查询商业汇票列表
     */
    List<CommercialBillVO> selectCommercialBillList(@Param("query") CommercialBillQueryDTO queryDTO);

    /**
     * 根据ID查询商业汇票详情
     */
    CommercialBillVO selectCommercialBillById(Long billId);

    /**
     * 根据汇票号码查询
     */
    TblCommercialBill selectByBillNo(String billNo);

    /**
     * 批量删除商业汇票(逻辑删除)
     */
    int deleteCommercialBillByIds(@Param("billIds") Long[] billIds);

    /**
     * 更新汇票状态
     */
    int updateBillStatus(@Param("billId") Long billId, @Param("billStatus") String billStatus);

    /**
     * 统计汇票数量
     */
    Integer countByStatus(String billStatus);
}
