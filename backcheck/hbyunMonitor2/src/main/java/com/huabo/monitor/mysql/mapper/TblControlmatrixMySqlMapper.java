package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblControlmatrixMySql;
import com.huabo.monitor.mysql.entity.TblFlowMatrixMySql;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TblControlmatrixMySqlMapper extends BaseMapper<TblControlmatrixMySql> {

    @Select("select * from TBL_CONTROLMATRIX where CONMATID = #{tcmId}")
    List<TblControlmatrixMySql> findBysql(String tcmId);

    @Insert("insert into TBL_FLOW_MATRIX (FLOWID,CONMATID) values (#{flowid},#{conmatid})")
    void insertMatrixZJ(TblFlowMatrixMySql flowMatrixMySql);
}
