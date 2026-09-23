package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFlowBussiness;
import com.huabo.system.mappersql.TblFlowBussinessMapperSqlConfig;

@org.apache.ibatis.annotations.Mapper
public interface TblFlowBussinessMapper extends BaseMapper<TblFlowBussiness> {
    @Select("SELECT * FROM TBL_FLOW_BUSSINESS WHERE FLOWID =  #{flowid}  ORDER BY BUSSINESSID DESC")
    List<TblFlowBussiness> findBysql(BigDecimal flowid);

    @Select("SELECT * FROM TBL_FLOW_BUSSINESS WHERE FLOWID = #{flowid} ORDER BY BUSSINESSID DESC")
    TblFlowBussiness findBy(BigDecimal flowid);

    @SelectProvider(type=TblFlowBussinessMapperSqlConfig.class,method="findByBussinessnumber")
    List<TblFlowBussiness> findByBussinessnumber(String flowid,TblFlowBussiness bussiness);

    @Delete("delete from TBL_FLOW_BUSSINESS where BUSSINESSID = #{bussinessid}")
    void deleteByBussinessid(BigDecimal bussinessid);

    @Select("SELECT * FROM TBL_FLOW_BUSSINESS WHERE FLOWID = #{flowid} ORDER BY BUSSINESSID DESC")
    List<TblFlowBussiness> findByflowid(BigDecimal flowid);
}
