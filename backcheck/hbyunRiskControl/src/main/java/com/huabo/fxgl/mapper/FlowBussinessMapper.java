package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.FlowBussiness;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-04
 */
@Repository
public interface FlowBussinessMapper extends BaseMapper<FlowBussiness> {
//    by 20240314 多数据兼容修改
//    @Select("SELECT * FROM TBL_FLOW_BUSSINESS WHERE FLOWID = #{flowid} AND ROWNUM = 1 ORDER BY BUSSINESSID DESC")
    @Select("SELECT * FROM TBL_FLOW_BUSSINESS WHERE FLOWID = #{flowid} ORDER BY BUSSINESSID DESC")
    List<FlowBussiness> findUniqueByFlowId(BigDecimal flowid) throws Exception;

    @Select("select MAX(BUSSINESSID) from TBL_FLOW_BUSSINESS")
    Long selectMaxBussinessId();
}
