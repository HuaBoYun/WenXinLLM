package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblFlow;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblRiskFlow;
import com.huabo.system.mappersql.TblFlowMapperSqlConfig;
import com.huabo.system.oracle.vo.CopyVo;

@org.apache.ibatis.annotations.Mapper
public interface TblFlowMapper extends BaseMapper<TblFlow> {

    @Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
    TblFlow findByIdFlows(String faflowid);

    @Select("SELECT f.FLOWID,f.FLOWNUMBER,f.FLOWNAME,f.COMPANY,f.DEPARTINCHARGE,f.CREATETIME,f.FLOWSTATUS,f.FATHERFLOWID,f.DEPARTASSIST,f.status,f.fromid,f.SETTINGID,f.FIRINGSTATUS  FROM TBL_FLOW f WHERE f.FLOWID IN (t tSELECT t tmax(w.FLOWID) from TBL_FLOW w tWHERE t tw.FATHERFLOWID = 0 and w.InFlowDB is null and w.company=#{orgid}  " +
            " GROUP BY w.flownumber) and f.FLOWNAME like '%#{flowname }%' and f.FLOWNUMBER like '%#{flownumber}%' " +
            "and f.DEPARTINCHARGE = + de  and f.FIRINGSTATUS = + firingStatus +  AND FLOWID and orgid= #{orgid} NOT IN (SELECT FLOWID FROM TBL_SYSTEM_MODELFLOW WHERE MODELID IN (SELECT MODELID FROM TBL_SYSTEM_MODULE WHERE MODELORG = #{orgid}))")
    List<TblOrganization> listBySqlPage(PageInfo<TblOrganization> pageInfo,BigDecimal orgid);

    @Select("SELECT * from TBL_FLOW WHERE FLOWNUMBER = #{flowcode} and COMPANY = #{orgid}")
    List selectByTrim(String flowcode, String orgid);

    @Select("SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and COMPANY = #{ttribute.getOrgid()}")
    List selectByS(String sql);




    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID FROM TBL_FLOW f WHERE FLOWBYSYSTEM = '1' AND ( " +
            "FLOWID IN (select max(f.FLOWID) from (select f.* from TBL_FLOW f where 1=1 and DEPARTINCHARGE = #{orgid}  " +
            " start with  Fatherflowid = -1 connect by prior FLOWID=FATHERFLOWID ) f GROUP BY FLOWNUMBER)  " +
            "or FLOWID IN (select FATHERFLOWID from TBL_FLOW f where 1=1 and DEPARTINCHARGE = #{orgid}  " +
            " start with  FATHERFLOWID = -1 connect by prior FLOWID=FATHERFLOWID) " +
            ") AND COMPANY = (select FATHERORGID from TBL_ORGANIZATION where ORGID  = #{orgid}) OR FATHERORGID = -1 ")
    List<TblFlow> findBysqlFlow(String orgid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and f.FLOWID= #{fatherflowid}")
    TblFlow findByIdFlow(BigDecimal fatherflowid);


    @SelectProvider(method="selectTblFlowList",type=TblFlowMapperSqlConfig.class)
    IPage<TblFlow> selectTblFlowList(IPage<TblFlow> page, String orgid, String faflowid, String name, String code);

    @SelectProvider(method="findBysqAllversion",type=TblFlowMapperSqlConfig.class)
    IPage<TblFlow> findBysqAllversion(IPage<TblFlow> page, String orgid, String flowid);

    @Select("SELECT COUNT(*) FROM TBL_FLOW WHERE FATHERFLOWID = #{fatherflowid} AND FIRINGSTATUS = 1 AND FLOWID != #{flowid}")
    Integer findOtherFiringStatusByParentId(BigDecimal fatherflowid, BigDecimal flowid);

    @Update("UPDATE TBL_FLOW SET FIRINGSTATUS = #{firing}  WHERE FLOWID = #{flowid}")
    void excuteSql(BigDecimal flowid, BigDecimal firing);

    @Select("select * from TBL_RISK_FLOW where flowid = #{flowid}")
    List<TblRiskFlow> findBysql(String flowid);


    @Select("SELECT * FROM TBL_FLOW WHERE FLOWNUMBER = #{flownumber}")
    List<TblFlow> selectFlownumber(String flownumber);

    @Select("SELECT * from TBL_FLOW where FATHERFLOWID= #{flowid}")
    List<TblFlow> findByFlowid(String flowid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID FROM TBL_FLOW f where  FLOWBYSYSTEM='1' and COMPANY = #{orgid}  or  Fatherflowid=-1 ORDER BY f.FLOWID")
    List<TblFlow> findBysqlFlowByType(String orgid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID FROM TBL_FLOW f WHERE FLOWBYSYSTEM = '1' AND (" +
            "                    FLOWID IN (select max(f.FLOWID) from (select f.* from TBL_FLOW f where 1=1 and DEPARTINCHARGE = #{orgid}" +
            "                     start with  Fatherflowid = -1 connect by prior FLOWID=FATHERFLOWID ) f GROUP BY FLOWNUMBER)" +
            "                    or FLOWID IN (select FATHERFLOWID from TBL_FLOW f where 1=1 and DEPARTINCHARGE = #{orgid}" +
            "                     start with  Fatherflowid = -1 connect by prior FLOWID=FATHERFLOWID)  +" +
            "                    ) AND COMPANY = #{orgids} OR Fatherflowid = -1 ORDER BY f.POSITION,f.flowid")
    List<TblFlow> findByFlow(String orgid, String orgids);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID FROM TBL_FLOW f where  FLOWBYSYSTEM='1' and COMPANY = #{orgid}  or  Fatherflowid=-1 ORDER BY f.POSITION,f.flowid")
    List<TblFlow> findByFlows(String orgid);

    @Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
    TblFlow findById(BigDecimal flowid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.COMPANY FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and f.FLOWID= #{faflowid}")
    TblFlow findByFaflowid(String faflowid);

    @SelectProvider(method="findListByPageInfo",type=TblFlowMapperSqlConfig.class)
    IPage<TblFlow> findListByPageInfo(IPage<TblFlow> page, BigDecimal orgid,String faflowid, String flowname, String flownumber, String stutes, String desc, String belongsto, BigDecimal fatherFlowid, Integer firingStatus);

    @Update("UPDATE TBL_FLOW SET SETTINGID = #{settingid}  WHERE FLOWID = #{flowid}")
    void updateByFlowId(TblFlow flow);

    @Delete("DELETE FROM TBL_RISK_FLOW WHERE FLOWID = #{flowid}")
    void deleteTblRiskFlow(String flowid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and INFLOWDB =1   AND f.COMPANY= #{orgid}")
    List<TblFlow> findBysqlFlowh(String orgid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and f.FLOWID= #{fatherflowid}")
    TblFlow findByFatherFlowId(BigDecimal fatherflowid);

    @Insert("INSERT INTO TBL_RISK_FLOW(FLOWID,RISKID) VALUES(#{flowid},#{riskid} )")
    void insertRiskFlow(BigDecimal flowid, BigDecimal riskid);

    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and f.FLOWID= #{pid}")
    TblFlow findByIdPid(String pid);

    @SelectProvider(method="findListByPageInfoFlow",type=TblFlowMapperSqlConfig.class)
    IPage<TblFlow> findListByPageInfoFlow(IPage<TblFlow> page, String orgid, CopyVo vo);

    @Select("SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and COMPANY = #{orgid}")
    List<TblFlow> selectByOrgid(BigDecimal orgid);

    @Select("SELECT CONMATID FROM TBL_FLOW_MATRIX WHERE FLOWID = #{flowid} AND ROWNUM <= 1 ORDER BY CONMATID DESC")
    String findFlowMatrixByFlowid(BigDecimal flowid);

    @Select("SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and  VERSIONTYPE is NULL and COMPANY = #{orgid}")
    List<TblFlow> findByOrgid(BigDecimal orgid);

    @Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowId}")
    TblFlow findByflowId(BigDecimal flowId);


//    @Update("UPDATE TBL_FLOW SET FLOWNAME = #{flowname}  WHERE FLOWID = #{flowid}")
//    void updateByFlow(TblFlow flow);
}

