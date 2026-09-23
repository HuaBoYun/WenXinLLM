package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.*;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;


/**
 * @author：yhr
 * @date:2022-08-29 11:49
 * @description:
 */
public interface TblAssessMarkVoMapper  extends BaseMapper<TblAssessMarkVo> {
 

    @Select("select t.*,TS.REALNAME,ta2.ELEMENTNAME from TBL_ASSESS_MARK t\n" +
            "inner join TBL_ASSESSELEMENT TA2 on t.ASSELEID = TA2.ASSELEID\n" +
            "inner join TBL_STAFF TS on t.STAFFID = TS.STAFFID\n" +
            "where t.ASSID=#{assid} and t.ASSORGID=#{orgid}")
    @Results(id = "TblAssessMarkVo", value={
            @Result(property = "assmarkid",column = "assmarkid",id=true),

            @Result(property = "canpingrens",column = "assmarkid",many = @Many(select = "getStaffsBymarkid"))

    })
    <p extends IPage<TblAssessMarkVo>>  p  queryTblAssessMarkVoPage(p page, @Param("assid") BigDecimal assid, @Param("orgid") BigDecimal orgid);


    @Select("select * from TBL_ASSESS_MARK where assid=#{assId} and ASSORGID=#{orgId} and suitable=1")
    @ResultMap("TblAssessMarkVo")
    public List<TblAssessMarkVo> getAssessMarkByAssIdAndOrgId( @Param("assId") BigDecimal assId, @Param("orgId")  BigDecimal orgId);





    @Select("select t.*,ts.REALNAME,ts.USERNAME from TBL_ASSESS_STAFF t inner join TBL_STAFF ts on t.STAFFID=ts.STAFFID where t.ASSMARKID=#{assmarkid}" +
            "order by t.STAFFID")
    LinkedHashSet<TblAssessStaffVo> getStaffsBymarkid(@Param("assmarkid")BigDecimal assmarkid);


    @Select("select t.*,ts.REALNAME,ts.USERNAME from TBL_ASSESS_STAFF t inner join TBL_STAFF ts on t.STAFFID=ts.STAFFID where t.ASSMARKID=#{assmarkid}" +
            "order by t.STAFFID")
    <p extends IPage<TblAssessStaffVo>>  p  getStaffsBymarkidToList(p page,@Param("assmarkid") BigDecimal assmarkid);


    @Select("select * from TBL_ASSESS t where  t.assid =(\n" +
            "      select assid from TBL_ASSESS_MARK  where assmarkid=#{assmarkid}" +
            ") ")
    TblAssess getTblAssessByassmarkid(@Param("assmarkid") BigDecimal assmarkid);

    @Delete(" delete from TBL_ASSESS_MARK  where assid = #{assid} and assorgid = #{orgid}")
    int  deleteAssessMarkByAssIdAndOrgId(@Param("assid") BigDecimal assid,@Param("orgid")BigDecimal orgid);


     @Select("select * from TBL_ASSESS_MARK where assid=#{assid}")
     List<TblAssessMark> getAssessMarkByAssId(@Param("assid") BigDecimal assId) ;

     @Delete(" delete from TBL_ASSESS_MARK  where assid = #{assid}")
     int  deleteAssessMarkByAssId(@Param("assid") BigDecimal assid);


    /**
     *  计算总得分
     */

    @Select("select sum(score) sumscore\n" +
            "from (\n" +
            "         select sum(TAM.SCORE) / sum(t.standardscore) * catw score\n" +
            "         from (\n" +
            "                  SELECT E.asscatid,\n" +
            "                         to_char(E.CATWEIGHT * nvl2(E.FATHERASSCATID, CONNECT_BY_ROOT CATWEIGHT, 100) / 100,\n" +
            "                                 'fm9999999990.00')                                                     catw\n" +
            "                  FROM Tbl_Assesscategory e\n" +
            "                  CONNECT BY PRIOR E.asscatid = E.fatherasscatid\n" +
            "                  START WITH E.fatherasscatid is null\n" +
            "                         and asstemid = (select asstemid from TBL_ASSESS where ASSID = #{assid})\n" +
            "                  ORDER SIBLINGS BY E.asscatid\n" +
            "              ) lin\n" +
            "                  inner join TBL_ASSELE_CATEGORY t on lin.asscatid = t.asscatid\n" +
            "                  inner join TBL_ASSESSELEMENT ta on ta.ASSELEID = t.ASSELEID\n" +
            "                  inner join TBL_ASSESS_MARK TAM on ta.ASSELEID = TAM.ASSELEID\n" +
            "         where TAM.ASSID = #{assid}\n" +
            "           and tam.ASSORGID = #{orgid} and tam.SUITABLE=1\n" +
            "         group by lin.asscatid, lin.catw\n" +
            "     )\n")
    Double getSumScoreByAssidAndOrgId(@Param("assid") BigDecimal assid,@Param("orgid")BigDecimal orgid);

    
    @Select("select sum(score) sumscore\n" +
            "from (\n" +
            "         select sum(TAM.SCORE) / sum(t.standardscore) * catw score\n" +
            "         from (\n" +
            "                  SELECT E.asscatid,E.CATWEIGHT catw\n" +
            "                  FROM Tbl_Assesscategory E where asstemid = (select asstemid from TBL_ASSESS where ASSID = #{assid})\n" +
            "                  ORDER  BY E.asscatid\n" +
            "              ) lin\n" +
            "                  inner join TBL_ASSELE_CATEGORY t on lin.asscatid = t.asscatid\n" +
            "                  inner join TBL_ASSESSELEMENT ta on ta.ASSELEID = t.ASSELEID\n" +
            "                  inner join TBL_ASSESS_MARK TAM on ta.ASSELEID = TAM.ASSELEID\n" +
            "         where TAM.ASSID = #{assid}\n" +
            "           and tam.ASSORGID = #{orgid} and tam.SUITABLE=1\n" +
            "         group by lin.asscatid, lin.catw\n" +
            "     )\n")
    Double getSumScoreByAssidAndOrgIdNew(@Param("assid") BigDecimal assid,@Param("orgid")BigDecimal orgid);
    /*
       体系权重评分

     */


     @Select("select  lin.*, decode(isleaf,0,sum(score) over (partition by decode(isleaf,0,asscatid,FATHERASSCATID)),score)  sumscore  from (\n" +
             "select  lin.asscatid,isleaf,lin.FATHERASSCATID,lin.catname1,lin.catw||'%' catw,sum(TAM.SCORE) / sum(t.standardscore) * catw score  from\n" +
             "\n" +
             "    (\n" +
             "        SELECT\n" +
             "            E.asscatid,E.FATHERASSCATID, CONNECT_BY_ISLEAF AS isleaf,\n" +
             "            nvl2(E.FATHERASSCATID,CONNECT_BY_ROOT CATNAME||'-'||E.catname,E.catname) catname1, to_char(E.CATWEIGHT*nvl2(E.FATHERASSCATID,CONNECT_BY_ROOT CATWEIGHT,100)/100,'fm9999999990.00') catw\n" +
             "        FROM Tbl_Assesscategory e\n" +
             "        CONNECT BY PRIOR E.asscatid = E.fatherasscatid\n" +
             "        START WITH E.fatherasscatid is null and asstemid=(select asstemid from TBL_ASSESS where ASSID=#{assid})\n" +
             "        ORDER SIBLINGS BY  E.asscatid\n" +
             "    ) lin\n" +
             "        left join TBL_ASSELE_CATEGORY t on lin.asscatid=t.asscatid\n" +
             "        left join TBL_ASSESSELEMENT ta on ta.ASSELEID=t.ASSELEID\n" +
             "        left join TBL_ASSESS_MARK TAM on ta.ASSELEID = TAM.ASSELEID and TAM.ASSID=#{assid} and tam.ASSORGID=#{orgid}\n" +
             "\n" +
             "group by lin.asscatid,lin.catname1,lin.catw,lin.FATHERASSCATID,isleaf\n" +
             "    ) lin\n" +
             "order by   asscatid\n")
     List<Map<String,Object>> getPingFenTiXiQuanZhong(@Param("assid") BigDecimal assid,@Param("orgid")BigDecimal orgid);

     
     @Select("select  lin.asscatid,isleaf,lin.FATHERASSCATID,lin.catname1,lin.catw  catw,sum(TAM.SCORE) / sum(t.standardscore) * catw score,sum(TAM.SCORE) / sum(t.standardscore) * catw sumscore  from\n" +
             "\n" +
             "    (\n" +
             "        SELECT\n" +
             "            E.asscatid,E.FATHERASSCATID,1 AS isleaf,\n" +
             "       E.catname catname1,CATWEIGHT catw \n" +
             "        FROM Tbl_Assesscategory e\n" +
             "        where asstemid=(select asstemid from TBL_ASSESS where ASSID=#{assid})\n" +
             "        ORDER BY  E.asscatid\n" +
             "    ) lin\n" +
             "        left join TBL_ASSELE_CATEGORY t on lin.asscatid=t.asscatid\n" +
             "        left join TBL_ASSESSELEMENT ta on ta.ASSELEID=t.ASSELEID\n" +
             "        left join TBL_ASSESS_MARK TAM on ta.ASSELEID = TAM.ASSELEID and TAM.ASSID=#{assid} and tam.ASSORGID=#{orgid}\n" +
             "\n" +
             "group by lin.asscatid,lin.catname1,lin.catw,lin.FATHERASSCATID,isleaf ")
     List<Map<String,Object>> getPingFenTiXiQuanZhongNew(@Param("assid") BigDecimal assid,@Param("orgid")BigDecimal orgid);

     /*
       评价要素评分
      */

    @Select("select  lin.*,t.standardscore,ta.elementname,TAM.SCORE,TAM.ASSMARKID from\n" +
            "    (\n" +
            "        SELECT\n" +
            "            E.asscatid,\n" +
            "            nvl2(E.FATHERASSCATID,CONNECT_BY_ROOT CATNAME||'-'||E.catname,E.catname) catname1, to_char(E.CATWEIGHT*nvl2(E.FATHERASSCATID,CONNECT_BY_ROOT CATWEIGHT,100)/100,'fm9999999990.00') catw\n" +
            "        FROM Tbl_Assesscategory e\n" +
            "        CONNECT BY PRIOR E.asscatid = E.fatherasscatid\n" +
            "        START WITH E.fatherasscatid is null and asstemid=(select asstemid from TBL_ASSESS where ASSID=#{assid})\n" +
            "        ORDER SIBLINGS BY  E.asscatid\n" +
            "    ) lin\n" +
            "        inner join TBL_ASSELE_CATEGORY t on lin.asscatid=t.asscatid\n" +
            "        inner join TBL_ASSESSELEMENT ta on ta.ASSELEID=t.ASSELEID\n" +
            "        inner join TBL_ASSESS_MARK TAM on ta.ASSELEID = TAM.ASSELEID\n" +
            "        where TAM.ASSID=#{assid} and tam.ASSORGID=#{orgid} and tam.SUITABLE=1")
    List<Map<String,Object>> getPingFenYaoSu(@Param("assid") BigDecimal assid,@Param("orgid")BigDecimal orgid);


    @Select("select  lin.*,t.standardscore,ta.elementname,TAM.SCORE,TAM.ASSMARKID from\n" +
            "    (\n" +
            "        SELECT\n" +
            "            E.asscatid,E.catname catname1,CATWEIGHT catw FROM Tbl_Assesscategory e\n" +
            "       where asstemid=(select asstemid from TBL_ASSESS where ASSID=#{assid})\n" +
            "        ORDER   BY  E.asscatid\n" +
            "    ) lin\n" +
            "        inner join TBL_ASSELE_CATEGORY t on lin.asscatid=t.asscatid\n" +
            "        inner join TBL_ASSESSELEMENT ta on ta.ASSELEID=t.ASSELEID\n" +
            "        inner join TBL_ASSESS_MARK TAM on ta.ASSELEID = TAM.ASSELEID\n" +
            "        where TAM.ASSID=#{assid} and tam.ASSORGID=#{orgid} and tam.SUITABLE=1")
    List<Map<String,Object>> getPingFenYaoSuNew(@Param("assid") BigDecimal assid,@Param("orgid")BigDecimal orgid);

    /*
      评价详情
     */



    @Select("    select tass.elementname,sta.score,tass.asseleid,tac.standardscore,sta.REASON,TASS.AUDITPOINT,S.REALNAME,sta.ATTID\n" +
            "    from TBL_ASSESS_MARK am left join TBL_ASSESSELEMENT tass on tass.asseleid = am.asseleid\n" +
            "    left join Tbl_AssEle_Category tac on  tac.asseleid = tass.asseleid\n" +
            "    left join TBL_ASSESS_STAFF sta on am.ASSMARKID = sta.ASSMARKID\n" +
            "    LEFT JOIN TBL_STAFF S ON S.STAFFID = sta.STAFFID\n" +
            "    where am.ASSMARKID = #{assmarkid} and tac.asscatid = #{asscatid} and AM.SUITABLE = 1")
    List<Map<String,Object>> getPingJiaXiangQing(@Param("assmarkid") BigDecimal assmarkid,@Param("asscatid")BigDecimal asscatid);



    @Select("\n" +
            "select lin.*, ts.REALNAME,ts.USERNAME,T.ORGNAME from (\n" +
            " select *\n" +
            "  from TBL_ASSESS_MARK o\n" +
            "  where o.assmarkid in (select max(t.assmarkid)\n" +
            " from TBL_ASSESS_MARK t\n" +
            "    where t.ASSID = #{assId}\n" +
            "   group by assorgid)\n" +
            ") lin\n" +
            "   inner join TBL_ORGANIZATION T on lin.ASSORGID=T.ORGID\n" +
            "  inner join TBL_ASSESS TA on TA.ASSID=lin.ASSID"+
            "   inner join TBL_STAFF TS on TS.STAFFID=TA.LEADERID")

    <p extends IPage<TblAssessMarkVo>>  p  findMarkByOrgGroup(p page, @Param("assId") BigDecimal assId);






    @Select("    SELECT DISTINCT ASS.ASSESSID,ASS.ASSESSNAME,ST.REALNAME,(CASE when STA.STATUS =1 then 0 else STA.STATUS END) status from tbl_assess ass\n" +
            "    LEFT JOIN TBL_ASSESS_MARK mar on ASS.ASSID=MAR.ASSID\n" +
            "    LEFT JOIN TBL_ASSESS_STAFF sta on MAR.ASSMARKID=STA.ASSMARKID\n" +
            "    LEFT JOIN TBL_STAFF st on STA.STAFFID=ST.STAFFID  where  ASS.ASSID=#{assid}\n" +
            "    and STA.ORGID= #{orgid}  ORDER BY ASS.ASSESSID")
    <p extends IPage<Map<String,Object>>>  p  findByperson(p page, @Param("assid") BigDecimal assid,@Param("orgid")BigDecimal orgid);



    @Update("update TBL_ASSESS_MARK set state=#{state} where assid=#{assid}")
    int  updateStateByAssid(@Param("state") String state,@Param("assid")BigDecimal assid);
    
    
    
}
