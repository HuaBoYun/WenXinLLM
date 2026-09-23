package com.huabo.system.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblControlmatrix;
import com.huabo.system.entity.TblFlowMatrix;


import java.util.List;

@Mapper
public interface TblControlmatrixMapper extends BaseMapper<TblControlmatrix> {

    @Select("select * from TBL_CONTROLMATRIX where CONMATID = #{tcmId}")
    List<TblControlmatrix> findBysql(String tcmId);

    @Insert("insert into TBL_FLOW_MATRIX (FLOWID,CONMATID) values (#{flowid},#{conmatid})")
    void insertMatrixZJ(TblFlowMatrix flowMatrix);
}
