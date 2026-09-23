package com.huabo.compliance.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.ElemGrade;
import com.huabo.compliance.entity.TblAssesselement;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * 要素 dao
 *
 * @author SongXiangYing
 */
public interface TblAssesselementMapper extends BaseMapper<TblAssesselement> {
/*
    @SelectProvider(type = TblAssesselementMapperSqlConfig.class, method = "findByPageInfo")
    @Results({
            @Result(column="ASSELEID",property="asseleid"),
            @Result(column="ELEMENTNAME",property="elementname"),
            @Result(column="STANDARDSCORE",property="standardscore"),
            @Result(column="MEMO",property="memo"),
            @Result(column="BUSINESSTYPE",property="businesstype"),
            @Result(column="BUSINESSATTRIBUTE",property="businessattribute"),
            @Result(column="AUDITPOINT",property="auditpoint"),
            @Result(column="ASSESSRULES",property="assessrules"),
            @Result(column="STATUS",property="status"),
            @Result(column="ELEMENTNUMBER",property="elementnumber"),
            @Result(column="TBLCOMANY",property="tblcomany"),
    })
    List<TblAssesselement> findByPageInfo(PageInfo<TblAssesselement> pageInfo);

    @SelectProvider(type = TblAssesselementMapperSqlConfig.class,method = "findByCount" )
    Integer findByCount( PageInfo<TblAssesselement> pageInfo);
*/
    @Select("select * from TBL_ASSESSELEMENT t  where t.ELEMENTNUMBER = #{elementNumber} and t.TBLCOMANY = #{orgid}")
    @Results({
            @Result(column="ASSELEID",property="asseleid"),
            @Result(column="ELEMENTNAME",property="elementname"),
            @Result(column="STANDARDSCORE",property="standardscore"),
            @Result(column="MEMO",property="memo"),
            @Result(column="BUSINESSTYPE",property="businesstype"),
            @Result(column="BUSINESSATTRIBUTE",property="businessattribute"),
            @Result(column="AUDITPOINT",property="auditpoint"),
            @Result(column="ASSESSRULES",property="assessrules"),
            @Result(column="STATUS",property="status"),
            @Result(column="ELEMENTNUMBER",property="elementnumber"),
            @Result(column="TBLCOMANY",property="tblcomany"),
    })
    List<TblAssesselement> getNumber(@Param("elementNumber") String elementNumber, @Param("orgid") String orgid);

    @Delete("delete from TBL_ASSESSELEMENT t where t.ASSELEID = #{deleteId}")
    void deleteId(@Param("deleteId") String deleteId);
    
    @Select("${sql}")
    List<TblAssesselement> getAssEssByIn(String sql);
    
    @Select("select * from TBL_ASSESSELEMENT t  where t.TBLCOMANY = #{orgid}")
    @Results({
            @Result(column="ASSELEID",property="asseleid"),
            @Result(column="ELEMENTNAME",property="elementname"),
            @Result(column="STANDARDSCORE",property="standardscore"),
            @Result(column="MEMO",property="memo"),
            @Result(column="BUSINESSTYPE",property="businesstype"),
            @Result(column="BUSINESSATTRIBUTE",property="businessattribute"),
            @Result(column="AUDITPOINT",property="auditpoint"),
            @Result(column="ASSESSRULES",property="assessrules"),
            @Result(column="STATUS",property="status"),
            @Result(column="ELEMENTNUMBER",property="elementnumber"),
            @Result(column="TBLCOMANY",property="tblcomany"),
    })
    List<TblAssesselement> getComany(@Param("orgid") String orgid);

    @Select("select * from TBL_ASSESSELEMENT t when t.ASSELEID = #{id}")
    @Results({
            @Result(column="ASSELEID",property="asseleid"),
            @Result(column="ELEMENTNAME",property="elementname"),
            @Result(column="STANDARDSCORE",property="standardscore"),
            @Result(column="MEMO",property="memo"),
            @Result(column="BUSINESSTYPE",property="businesstype"),
            @Result(column="BUSINESSATTRIBUTE",property="businessattribute"),
            @Result(column="AUDITPOINT",property="auditpoint"),
            @Result(column="ASSESSRULES",property="assessrules"),
            @Result(column="STATUS",property="status"),
            @Result(column="ELEMENTNUMBER",property="elementnumber"),
            @Result(column="TBLCOMANY",property="tblcomany"),
    })
    TblAssesselement get(@Param("id") BigDecimal id);


    @Select(" select ae.ELEMENTNAME,ac.STANDARDSCORE,asf.score,asf.reason,asf.assstaffid,ATT.ATTID,ATT.ATTNAME,ae.AUDITPOINT examination,ae.asseleid,ae.elementNumber  from tbl_AssessElement  ae left join TBL_ASSELE_CATEGORY ac on ae.ASSELEID = ac.ASSELEID   left join tbl_Assess_Mark  am on ae.ASSELEID = am.ASSELEID   left join TBL_ASSESS_STAFF asf on asf.assmarkid = am.assmarkid" +
            " LEFT JOIN TBL_ATTACHMENT att on ASF.ATTID=ATT.ATTID  where ac.ASSCATID = #{nodeId} and am.assid = #{assId} and am.assorgid =#{orgId} and asf.staffid= #{userId} and am.suitable = 1 and am.ASSELEID = ae.ASSELEID  order by ae.ELEMENTNAME")
    List<ElemGrade> findElementByassIdAndUserId(@Param("nodeId")BigDecimal nodeId,
                                               @Param("assId") BigDecimal assId,
                                               @Param("userId")BigDecimal userId,
                                               @Param("orgId")BigDecimal orgId);

    @Select("${sql}")
    <p extends IPage<TblAssesselement>> p getSqlPage(p page, @Param("sql") String sql);

    @Select("${sql}")
    List<ElemGrade> getQuery(@Param("sql") String sql);
    
}