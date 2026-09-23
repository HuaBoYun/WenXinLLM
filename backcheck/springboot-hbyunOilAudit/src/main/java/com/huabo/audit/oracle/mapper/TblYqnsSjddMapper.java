package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsSjdd;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblYqnsSjddMapper extends BaseMapper<TblYqnsSjdd> {
	
	
    @Delete("DELETE FROM TBL_YQNS_SJDDRW WHERE RWID = #{rwid}")
    void deleteoneById(BigDecimal rwid) throws Exception;

 

    @Select("select *  FROM TBL_YQNS_SJDDRW WHERE RWID = #{rwid}") 
    TblYqnsSjdd selectById(BigDecimal rwid) throws Exception;

 

    @Select("select *  FROM TBL_YQNS_SJDDRW WHERE TYPEID = #{typeid} and PROJECTID=${projectid}") 
    List<TblYqnsSjdd> selectByrwId(Long typeid,BigDecimal projectid) throws Exception;

}
