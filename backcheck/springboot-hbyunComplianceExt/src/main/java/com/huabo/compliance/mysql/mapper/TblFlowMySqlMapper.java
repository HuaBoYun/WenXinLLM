package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;

import com.huabo.compliance.mysql.entity.TblFlowMySql;
import com.huabo.compliance.mysql.entity.TblOrganizationMySql;
import com.huabo.compliance.mysql.entity.TblRiskFlowMySql;
import com.huabo.compliance.vo.CopyVo;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface TblFlowMySqlMapper extends BaseMapper<TblFlowMySql> {

    @Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
    TblFlowMySql findByIdFlows(String faflowid);

    @Select("SELECT f.FLOWID,f.FLOWNUMBER,f.FLOWNAME,f.COMPANY,f.DEPARTINCHARGE,f.CREATETIME,f.FLOWSTATUS,f.FATHERFLOWID,f.DEPARTASSIST,f.status,f.fromid,f.SETTINGID,f.FIRINGSTATUS  FROM TBL_FLOW f WHERE f.FLOWID IN (t tSELECT t tmax(w.FLOWID) from TBL_FLOW w tWHERE t tw.FATHERFLOWID = 0 and w.InFlowDB is null and w.company=#{orgid}  " +
            " GROUP BY w.flownumber) and f.FLOWNAME like '%#{flowname }%' and f.FLOWNUMBER like '%#{flownumber}%' " +
            "and f.DEPARTINCHARGE = + de  and f.FIRINGSTATUS = + firingStatus +  AND FLOWID and orgid= #{orgid} NOT IN (SELECT FLOWID FROM TBL_SYSTEM_MODELFLOW WHERE MODELID IN (SELECT MODELID FROM TBL_SYSTEM_MODULE WHERE MODELORG = #{orgid}))")
    List<TblOrganizationMySql> listBySqlPage(PageInfo<TblOrganizationMySql> pageInfo, BigDecimal orgid);

    @Select("SELECT * from TBL_FLOW WHERE FLOWNUMBER=' + flowcode + ' and COMPANY= + orgid")
    List selectByTrim(String flowcode, String orgid);

    @Select("SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and COMPANY = #{ttribute.getOrgid()}")
    List selectByS(String sql);


    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID FROM TBL_FLOW f WHERE FLOWBYSYSTEM = '1' AND ( " +
            "FLOWID IN (select max(f.FLOWID) from (select f.* from TBL_FLOW f where 1=1 and DEPARTINCHARGE = #{orgid}  " +
            " and getFatherflowidList(Fatherflowid = -1)) f GROUP BY FLOWNUMBER)  " +
            "or FLOWID IN (select FATHERFLOWID from TBL_FLOW f where 1=1 and DEPARTINCHARGE = #{orgid}  " +
            " and getFatherflowidList(FATHERFLOWID = -1)) " +
            ") AND COMPANY = (select FATHERORGID from TBL_ORGANIZATION where ORGID  = #{orgid}) OR FATHERORGID = -1 ")
    List<TblFlowMySql> findBysqlFlow(String orgid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and f.FLOWID= #{fatherflowid}")
    TblFlowMySql findByIdFlow(BigDecimal fatherflowid);


    @SelectProvider(method = "selectTblFlowList", type = TblFlowMapperSqlMySqlConifg.class)
    List<TblFlowMySql> selectTblFlowList(PageInfo<TblFlowMySql> pageInfo, String orgid, String faflowid, String name, String code);

    @SelectProvider(method = "selectTblFlowCount", type = TblFlowMapperSqlMySqlConifg.class)
    Integer selectTblFlowCount(PageInfo<TblFlowMySql> pageInfo, String orgid, String faflowid, String name, String code);

    @SelectProvider(method = "findBysqAllversion", type = TblFlowMapperSqlMySqlConifg.class)
    List<TblFlowMySql> findBysqAllversion(PageInfo<TblFlowMySql> pageInfo, String orgid, String flowid);

    @SelectProvider(method = "findBysqAllversionCount", type = TblFlowMapperSqlMySqlConifg.class)
    Integer findBysqAllversionCount(PageInfo<TblFlowMySql> pageInfo, String orgid, String flowid);


    @Select("SELECT COUNT(*) FROM TBL_FLOW WHERE FATHERFLOWID = #{fatherflowid} AND FIRINGSTATUS = 1 AND FLOWID != #{flowid}")
    Integer findOtherFiringStatusByParentId(BigDecimal fatherflowid, BigDecimal flowid);

    @Update("UPDATE TBL_FLOW SET FIRINGSTATUS = #{firing}  WHERE FLOWID = #{flowid}")
    void excuteSql(Integer flowid, Integer firing);

    @Select("select * from TBL_RISK_FLOW where flowid = #{flowid}")
    List<TblRiskFlowMySql> findBysql(String flowid);


    @Select("SELECT * FROM TBL_FLOW WHERE FLOWNUMBER = #{flownumber}")
    List<TblFlowMySql> selectFlownumber(String flownumber);

    @Select("SELECT * from TBL_FLOW where FATHERFLOWID= #{flowid}")
    List<TblFlowMySql> findByFlowid(String flowid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID FROM TBL_FLOW f where  FLOWBYSYSTEM='1' and COMPANY = #{orgid}  or  Fatherflowid=-1 ORDER BY f.FLOWID")
    List<TblFlowMySql> findBysqlFlowByType(String orgid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID FROM TBL_FLOW f WHERE FLOWBYSYSTEM = '1' AND (" +
            "                    FLOWID IN (select max(f.FLOWID) from (select f.* from TBL_FLOW f where 1=1 and DEPARTINCHARGE = #{orgid}" +
            "                     and getFatherflowidList(Fatherflowid = -1) ) f GROUP BY FLOWNUMBER)" +
            "                    or FLOWID IN (select FATHERFLOWID from TBL_FLOW f where 1=1 and DEPARTINCHARGE = #{orgid}" +
            "                     and getFatherflowidList(Fatherflowid = -1) ))  +" +
            "                    ) AND COMPANY = #{orgids} OR Fatherflowid = -1 ORDER BY f.POSITION,f.flowid")
    List<TblFlowMySql> findByFlow(String orgid, String orgids);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID FROM TBL_FLOW f where  FLOWBYSYSTEM='1' and COMPANY = #{orgid}  or  Fatherflowid=-1 ORDER BY f.POSITION,f.flowid")
    List<TblFlowMySql> findByFlows(String orgid);

    @Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
    TblFlowMySql findById(BigDecimal flowid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.COMPANY FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and f.FLOWID= #{faflowid}")
    TblFlowMySql findByFaflowid(String faflowid);

    @SelectProvider(method = "findListByPageInfo", type = TblFlowMapperSqlMySqlConifg.class)
    List<TblFlowMySql> findListByPageInfo(PageInfo<TblFlowMySql> pageInfo, BigDecimal orgid, String faflowid, String flowname, String flownumber, String stutes, String desc, String belongsto, BigDecimal fatherFlowid, Integer firingStatus);

    @SelectProvider(method = "findCountByPageInfo", type = TblFlowMapperSqlMySqlConifg.class)
    Integer findCountByPageInfo(BigDecimal orgid, String faflowid, String flowname, String flownumber, String stutes, String desc, String belongsto, BigDecimal fatherFlowid, Integer firingStatus);

    @Update("UPDATE TBL_FLOW SET SETTINGID = #{settingid}  WHERE FLOWID = #{flowid}")
    void updateByFlowId(TblFlowMySql flow);

    @Delete("DELETE FROM TBL_RISK_FLOW WHERE FLOWID = #{flowid}")
    void deleteTblRiskFlow(String flowid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and INFLOWDB =1   AND f.COMPANY= #{orgid}")
    List<TblFlowMySql> findBysqlFlowh(String orgid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and f.FLOWID= #{fatherflowid}")
    TblFlowMySql findByFatherFlowId(BigDecimal fatherflowid);

    @Insert("INSERT INTO TBL_RISK_FLOW(FLOWID,RISKID) VALUES(#{flowid},#{riskid} )")
    void insertRiskFlow(BigDecimal flowid, BigDecimal riskid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and f.FLOWID= #{pid}")
    TblFlowMySql findByIdPid(String pid);

    @SelectProvider(method = "findListByPageInfoFlow", type = TblFlowMapperSqlMySqlConifg.class)
    List<TblFlowMySql> findListByPageInfoFlow(PageInfo<TblFlowMySql> pageInfo, String orgid, CopyVo vo);

    @SelectProvider(method = "findCountByPageInfoFlow", type = TblFlowMapperSqlMySqlConifg.class)
    Integer findCountByPageInfoFlow(String orgid, CopyVo vo);

    @Select("SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and COMPANY = #{orgid}")
    List<TblFlowMySql> selectByOrgid(BigDecimal orgid);

    @Select("SELECT CONMATID FROM TBL_FLOW_MATRIX WHERE FLOWID = #{flowid} ORDER BY CONMATID DESC LIMIT 1,1")
    String findFlowMatrixByFlowid(BigDecimal flowid);

    @Select("SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and  VERSIONTYPE is NULL and COMPANY = #{orgid}")
    List<TblFlowMySql> findByOrgid(BigDecimal orgid);

    @Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowId}")
    TblFlowMySql findByflowId(Integer flowId);


//    @Update("UPDATE TBL_FLOW SET FLOWNAME = #{flowname}  WHERE FLOWID = #{flowid}")
//    void updateByFlow(TblFlow flow);
}

