package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.mysql.entity.TblFlowBussinessMySql;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface TblFlowBussinessMySqlMapper extends BaseMapper<TblFlowBussinessMySql> {
    @Select("SELECT * FROM TBL_FLOW_BUSSINESS WHERE FLOWID =  + flowid + ORDER BY BUSSINESSID DESC LIMIT 1,1")
    List<TblFlowBussinessMySql> findBysql(BigDecimal flowid);

    @Select("SELECT * FROM TBL_FLOW_BUSSINESS WHERE FLOWID =  + flowid + ORDER BY BUSSINESSID DESC LIMIT 1,1")
    TblFlowBussinessMySql findBy(BigDecimal flowid);

    //    @Select("select * from TBL_FLOW_BUSSINESS where FLOWID in (#{flowid} ) and bussinessnumber =  + #{bussinessnumber}  and bussinessname like '% #{bussinessname} %'")
    @SelectProvider(type = TblFlowBussinessMapperSqlMySqlConfig.class, method = "findByBussinessnumber")
    List<TblFlowBussinessMySql> findByBussinessnumber(String flowid, TblFlowBussinessMySql bussiness);

    @Delete("delete from TBL_FLOW_BUSSINESS where BUSSINESSID = #{bussinessid}")
    void deleteByBussinessid(BigDecimal bussinessid);

    @Select("SELECT * FROM TBL_FLOW_BUSSINESS WHERE FLOWID = #{flowid}  ORDER BY BUSSINESSID DESC LIMIT 1,1")
    List<TblFlowBussinessMySql> findByflowid(BigDecimal flowid);
}
