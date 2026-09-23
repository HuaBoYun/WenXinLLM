package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblCubeBbmx;
import com.huabo.system.entity.TblCubeYS202201;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;


public interface TblCubeYS202201Mapper extends BaseMapper<TblCubeYS202201>{
    @Select("SELECT MAX(TCYID)+1 FROM TBL_CUBE_YS202201")
    BigDecimal selectMaxTCYID();

    @Select("SELECT COUNT(*) FROM TBL_CUBE_YS202201 WHERE TCYID = #{tcyId}")
    BigDecimal selectCountByTCYID(BigDecimal tcyId);

    @Insert({
            "INSERT INTO TBL_CUBE_YS202201 (TCYID, CODEBMMEATYPE, VALUE, CODEMEASURE, CODEENTITY, CODEVERSION, CODEMVTYPE)",
            "VALUES (#{tcyId}, #{codebmmeatype}, #{value}, #{codemeasure}, #{codeentity}, #{codeversion}, #{codemvtype})"
    })
    void insertTCY(TblCubeYS202201 tblCubeBbmx);

    @Update( "UPDATE TBL_CUBE_YS202201 SET "+
            "CODEBMMEATYPE = #{codebmmeatype},"+
            "VALUE = #{value},"+
            "CODEMEASURE = #{codemeasure},"+
            "CODEENTITY = #{codeentity},"+
            "CODEVERSION = #{codeversion},"+
            "CODEMVTYPE = #{codemvtype}"+
            "WHERE TCYID = #{tcyId}")
    void updateTCY(TblCubeYS202201 tblCubeBbmx);
}
