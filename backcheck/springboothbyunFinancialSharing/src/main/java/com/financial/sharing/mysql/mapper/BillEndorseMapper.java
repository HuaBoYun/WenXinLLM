package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.TblBillEndorse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 票据背书Mapper
 * @author system
 * @since 2025-01-05
 */
public interface BillEndorseMapper extends BaseMapper<TblBillEndorse> {

    /**
     * 查询票据背书记录
     * @param billId 票据ID
     * @return 背书记录列表
     */
    List<TblBillEndorse> selectByBillId(@Param("billId") String billId);
}

