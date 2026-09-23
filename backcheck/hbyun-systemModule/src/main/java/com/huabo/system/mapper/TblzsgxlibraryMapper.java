package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.Tblzsgxlibrary;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblzsgxlibraryMapper extends BaseMapper<Tblzsgxlibrary> {

    @Select("SELECT * FROM TBL_ZSGX_LIBRARY WHERE LIBRARYID = #{libraryid}")
    Tblzsgxlibrary selectId(BigDecimal id);

    @SelectProvider(method="selectType",type=TblzsgxlibraryMapperSqlConfig.class)
    IPage<Tblzsgxlibrary> selectType(IPage<Tblzsgxlibrary> page,Tblzsgxlibrary library, String type,BigDecimal orgid);

    @Delete("DELETE FROM TBL_ZSGX_LIBRARY WHERE LIBRARYID = #{libraryid}")
    void deleteId(BigDecimal libraryid);
 
    @Insert("INSERT INTO TBL_ZSGX_LIBRARY_ATT(LIBRARYID,ATTID) VALUES (#{libraryid},#{attid})")
	void insertAttInfoAtt(BigDecimal libraryid, String attid) throws Exception;

    @Delete("DELETE FROM TBL_ZSGX_LIBRARY_ATT WHERE LIBRARYID = #{libraryid}")
    void deleteattId(BigDecimal libraryid);
    
    //根据效力位阶分组统计数量
    @Select("SELECT count(*) hzcount,XLLEVEL FROM TBL_ZSGX_LIBRARY where CREATEORGANID=#{orgid} and LRTYPE=#{lrtype}  GROUP BY XLLEVEL ")
    List<Tblzsgxlibrary> selectbywj(BigDecimal orgid,String lrtype);
    
    //根据专题分类分组统计数量
    @Select("SELECT count(*) hzcount,TOPLICCLASS FROM TBL_ZSGX_LIBRARY where CREATEORGANID=#{orgid} and LRTYPE=#{lrtype}  GROUP BY TOPLICCLASS ")
    List<Tblzsgxlibrary> selectbyzt(BigDecimal orgid,String lrtype);
    
    //根据制定机关分组统计数量
    @Select("SELECT count(*) hzcount,ZDORGAN FROM TBL_ZSGX_LIBRARY where CREATEORGANID=#{orgid} and LRTYPE=#{lrtype}  GROUP BY ZDORGAN ")
    List<Tblzsgxlibrary> selectbyjg(BigDecimal orgid,String lrtype);
    
    //根据时效性分组统计数量
    @Select("SELECT count(*) hzcount,TIMELINESS FROM TBL_ZSGX_LIBRARY where CREATEORGANID=#{orgid} and LRTYPE=#{lrtype} GROUP BY TIMELINESS ")
    List<Tblzsgxlibrary> selectbysxx(BigDecimal orgid,String lrtype);
    
    //根据法规类别/文件夹名称分组统计数量
    @Select("SELECT count(*) hzcount,FGCATEGORY FROM TBL_ZSGX_LIBRARY where CREATEORGANID=#{orgid} and LRTYPE=#{lrtype}  GROUP BY FGCATEGORY ")
    List<Tblzsgxlibrary> selectbyfglb(BigDecimal orgid,String lrtype);
    
    //根据公布年份分组统计数量
    @Select("SELECT count(*) hzcount,GBYEAR FROM TBL_ZSGX_LIBRARY where CREATEORGANID=#{orgid} and LRTYPE=#{lrtype} GROUP BY GBYEAR ")
    List<Tblzsgxlibrary> selectbygbnf(BigDecimal orgid,String lrtype);

}
