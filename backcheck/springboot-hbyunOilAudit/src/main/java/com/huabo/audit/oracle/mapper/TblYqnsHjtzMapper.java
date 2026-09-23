package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsHjtz;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

public interface TblYqnsHjtzMapper extends BaseMapper<TblYqnsHjtz> {
	
    @Delete("DELETE FROM TBL_YQNS_HJTZ WHERE RYID = #{ryid}")
    void deleteoneById(BigDecimal ryid) throws Exception;

    @Select("select *  FROM TBL_YQNS_HJTZ WHERE RYID = #{ryid}") 
    TblYqnsHjtz selectById(BigDecimal ryid) throws Exception;

    @Insert("INSERT INTO TBL_YQNS_HJTZ_ATT(RYID,ATTID) VALUES (#{ryid},#{aid})")
    void insertAttInfoAtt( BigDecimal ryid, String aid) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_HJTZ_ATT where  ATTID=#{aid}")
    void deleteAttInfoAtt( String aid) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_HJTZ_ATT where RYID=#{ryid}")
    void deleteAttInfoAttByxm(BigDecimal ryid) throws Exception;

    @Update("UPDATE TBL_YQNS_HJTZ SET XFRYNAMES = #{xfrynames},XFRYIDS=#{xfryids} WHERE RYID = #{ryid}")
    void xfry(String xfryids, String xfrynames,BigDecimal ryid) throws Exception;

}
