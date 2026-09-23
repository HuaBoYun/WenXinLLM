package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.entity.TblFlowBussiness;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface TblFlowBussinessMapper extends BaseMapper<TblFlowBussiness> {
    @Select("SELECT * FROM TBL_FLOW_BUSSINESS WHERE FLOWID =  + flowid +  AND ROWNUM = 1 ORDER BY BUSSINESSID DESC")
    List<TblFlowBussiness> findBysql(BigDecimal flowid);

    @Select("SELECT * FROM TBL_FLOW_BUSSINESS WHERE FLOWID =  + flowid +  AND ROWNUM = 1 ORDER BY BUSSINESSID DESC")
    TblFlowBussiness findBy(BigDecimal flowid);

//    @Select("select * from TBL_FLOW_BUSSINESS where FLOWID in (#{flowid} ) and bussinessnumber =  + #{bussinessnumber}  and bussinessname like '% #{bussinessname} %'")
    @SelectProvider(type=TblFlowBussinessMapperSqlConfig.class,method="findByBussinessnumber")
    List<TblFlowBussiness> findByBussinessnumber(String flowid,TblFlowBussiness bussiness);

    @Delete("delete from TBL_FLOW_BUSSINESS where BUSSINESSID = #{bussinessid}")
    void deleteByBussinessid(BigDecimal bussinessid);

    @Select("SELECT * FROM TBL_FLOW_BUSSINESS WHERE FLOWID = #{flowid} AND ROWNUM = 1 ORDER BY BUSSINESSID DESC")
    List<TblFlowBussiness> findByflowid(BigDecimal flowid);
}
