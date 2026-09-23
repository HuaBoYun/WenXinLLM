package com.huabo.compliance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
public interface  TblAssessTargetMapper extends BaseMapper<TblAssessTarget> {




    @Delete("  delete  from TBL_ASSESS_TARGET  where assid = #{assid} and orgid = #{orgid}")
    int  deleteAssessTargetByAssIdAndOrgId(@Param("assid") BigDecimal assid, @Param("orgid")BigDecimal orgid);

    @Delete("  delete  from TBL_ASSESS_TARGET  where assid = #{assid}")
    int  deleteAssessTargetByAssId(@Param("assid") BigDecimal assid);


    @Select("select TAT.*,T.ORGNAME,TS.STAFFID,TS.USERNAME,TS.REALNAME,TA.ASSESSNAME,Ta.ASSESSID from TBL_ASSESS_TARGET TAT inner join TBL_ORGANIZATION T on TAT.ORGID = T.ORGID\n" +
            "inner join TBL_ASSESS TA on TA.ASSID=TAT.ASSID inner join TBL_STAFF TS on TS.STAFFID=TA.LEADERID\n" +
            "where TAT.assid=#{assId}")
    <p extends IPage<TblAssessTargetVo>>  p  getOrgList(p page, @Param("assId") BigDecimal assId);

    /**
     *   评价结果--点击结果分页查询
     */

   @Select("\n" +
           "select TAT.*, lin.state , Ta.ASSESSDATE,ts.STAFFID, ts.REALNAME,ts.USERNAME,T.ORGNAME from (\n" +
           "                  select *\n" +
           "                  from TBL_ASSESS_MARK o\n" +
           "                  where o.assmarkid in (select max(t.assmarkid)\n" +
           "                                        from TBL_ASSESS_MARK t\n" +
           "                                        where t.ASSID = #{assId} and t.suitable = 1\n" +
           "                                        group by assorgid)\n" +
           "                    and o.staffid = #{staffid}\n" +
           "              ) lin  inner join TBL_ASSESS_TARGET TAT on lin.ASSESSTARGETID=TAT.ASSESSTARGETID\n" +
           "                     inner join TBL_ORGANIZATION T on TAT.ORGID = T.ORGID\n" +
           "                     inner join TBL_ASSESS TA on TA.ASSID=TAT.ASSID inner join\n" +
           "                     TBL_STAFF TS on TS.STAFFID=TA.LEADERID\n")

          <p extends IPage<TblAssessTargetVo>>  p  findMarkByOrgGroupZuPing(p page, @Param("assId") BigDecimal assId,@Param("staffid") BigDecimal staffid);
    /**
     *
     * @param assesstargetid
     * @return
     */
    @Select("select * from TBL_ASSESS_TARGET where assesstargetid=#{assesstargetid}")
    @Results(id = "getMyOneTargetVo", value={
            @Result(property = "assesstargetid",column = "assesstargetid",id=true),
            @Result(property = "tblTargetBugs",column =   "assesstargetid",many = @Many(select = "getTargetBugsByAMORGID")),
            @Result(property = "tblTargetRisks",column =  "assesstargetid",many = @Many(select = "getTargetRisksByAMORGID")),
            @Result(property = "tblTargetSheets",column = "assesstargetid",many = @Many(select = "getTargetSheetsByAMORGID"))

    })
    TblAssessTargetVo  getMyOneTargetVo(@Param("assesstargetid") BigDecimal assesstargetid);

     //查询缺陷
    @Select("select * from TBL_BUG where BUGID in (select BUGID from TBL_AMORG_PROBLEM where AMORGID=#{assesstargetid})")
    Set<TblBug> getTargetBugsByAMORGID(@Param("assesstargetid") BigDecimal assesstargetid);
    //查询风险发现
    @Select("select t.*,ts.REALNAME from  TBL_NBKZ_RISK t  left join TBL_STAFF TS on t.CREATEORID = TS.STAFFID   where RISKID in (select riskid  from TBL_AMORG_PROBLEM where AMORGID=#{assesstargetid})")
    Set<TblNbkzRisk> getTargetRisksByAMORGID(@Param("assesstargetid") BigDecimal assesstargetid);
    // 查询底稿
    @Select("select * from TBL_WORKSHEET where WORKSHEETID in (select SHEETID  from TBL_AMORG_PROBLEM where AMORGID=#{assesstargetid})")
    Set<TblWorksheet> getTargetSheetsByAMORGID(@Param("assesstargetid") BigDecimal assesstargetid);


    @Select("select * from TBL_ASSESS_TARGET where assid=#{assid} and orgid=#{orgId}")
     List<TblAssessTarget> getTarget(@Param("assid") BigDecimal assId, @Param("orgId")BigDecimal orgId) ;

    
    @Select("select * from TBL_ASSESS_TARGET where assid=#{id} ")
    TblAssessTarget selectTblAssessTarget(@Param("id")BigDecimal id) ;

   
    
    
}
