package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblBiCkEcharts;
import com.huabo.system.entity.TblCubeBbmx;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;


public interface TblBigDataMapper extends BaseMapper<TblCubeBbmx>{
    @Select("SELECT MAX(TCBID)+1 FROM TBL_CUBE_BBMX")
    BigDecimal selectMaxTCBID();

    @Select("SELECT COUNT(*) FROM TBL_CUBE_BBMX WHERE PKACCP = #{pkaccp}")
    BigDecimal selectCountByPkAccp(BigDecimal pkaccp);

    @Insert("INSERT INTO TBL_CUBE_BBMX (TCBID, CODEBMMEATYPE, CODEENTITY, CODEVERSION, CODEMEASURE, CODEMVTYPE, CODEBMTRAIL, PKACCP, PKACCM, VALUE, FZHJ, ZCHJ) " +
            "VALUES (#{tcbId}, #{codebmmeatype}, #{codeentity}, #{codeversion}, #{codemeasure}, #{codemvtype}, #{codebmtrail}, #{pkaccp}, #{pkaccm}, #{value}, #{fzhj}, #{zchj})")
    void insertTCB(TblCubeBbmx tblCubeBbmx);

    @Update("UPDATE TBL_CUBE_BBMX " +
            "SET CODEBMMEATYPE = #{codebmmeatype}, " +
            "CODEENTITY = #{codeentity}, " +
            "CODEVERSION = #{codeversion}, " +
            "CODEMEASURE = #{codemeasure}, " +
            "CODEMVTYPE = #{codemvtype}, " +
            "CODEBMTRAIL = #{codebmtrail}, " +
            "PKACCP = #{pkaccp}, " +
            "PKACCM = #{pkaccm}, " +
            "VALUE = #{value}, " +
            "FZHJ = #{fzhj}, " +
            "ZCHJ = #{zchj} " +
            "WHERE PKACCP = #{pkaccp}")
    void updateTCB(TblCubeBbmx tblCubeBbmx);
}
