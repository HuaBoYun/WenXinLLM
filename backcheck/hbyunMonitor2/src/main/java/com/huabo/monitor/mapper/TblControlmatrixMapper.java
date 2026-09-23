package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.entity.TblControlmatrix;
import com.huabo.monitor.entity.TblFlowMatrix;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TblControlmatrixMapper extends BaseMapper<TblControlmatrix> {

    @Select("select * from TBL_CONTROLMATRIX where CONMATID = #{tcmId}")
    List<TblControlmatrix> findBysql(String tcmId);

    @Insert("insert into TBL_FLOW_MATRIX (FLOWID,CONMATID) values (#{flowid},#{conmatid})")
    void insertMatrixZJ(TblFlowMatrix flowMatrix);
}
