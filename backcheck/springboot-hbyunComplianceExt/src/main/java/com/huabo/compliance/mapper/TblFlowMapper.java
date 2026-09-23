package com.huabo.compliance.mapper;

import com.huabo.compliance.entity.TblFlow;
import com.huabo.compliance.entity.TblRiskFlow;
import com.huabo.compliance.vo.CopyVo;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-09-05
 */
public interface TblFlowMapper extends BaseMapper<TblFlow> {

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
    TblFlow findById(BigDecimal flowid);
	
	@Select("SELECT * from TBL_FLOW WHERE FLOWNUMBER=' + flowcode + ' and COMPANY= + orgid")
    List selectByTrim(String flowcode, String orgid);
	
	@Select("SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and COMPANY = #{ttribute.getOrgid()}")
	List selectByS(String sql);
	
	@Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID FROM TBL_FLOW f WHERE FLOWBYSYSTEM = '1' AND (" +
            "                    FLOWID IN (select max(f.FLOWID) from (select f.* from TBL_FLOW f where 1=1 and DEPARTINCHARGE = #{orgid}" +
            "                     start with  Fatherflowid = -1 connect by prior FLOWID=FATHERFLOWID ) f GROUP BY FLOWNUMBER)" +
            "                    or FLOWID IN (select FATHERFLOWID from TBL_FLOW f where 1=1 and DEPARTINCHARGE = #{orgid}" +
            "                     start with  Fatherflowid = -1 connect by prior FLOWID=FATHERFLOWID)  +" +
            "                    ) AND COMPANY = #{orgids} OR Fatherflowid = -1 ORDER BY f.POSITION,f.flowid")
    List<TblFlow> findByFlow(String orgid, String orgids);
	
	@Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID FROM TBL_FLOW f where  FLOWBYSYSTEM='1' and COMPANY = #{orgid}  or  Fatherflowid=-1 ORDER BY f.POSITION,f.flowid")
    List<TblFlow> findByFlows(String orgid);
	
	@Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and f.FLOWID= #{fatherflowid}")
    TblFlow findByIdFlow(BigDecimal fatherflowid);
	
	@Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID FROM TBL_FLOW f WHERE FLOWBYSYSTEM = '1' AND ( " +
            "FLOWID IN (select max(f.FLOWID) from (select f.* from TBL_FLOW f where 1=1 and DEPARTINCHARGE = #{orgid}  " +
            " start with  Fatherflowid = -1 connect by prior FLOWID=FATHERFLOWID ) f GROUP BY FLOWNUMBER)  " +
            "or FLOWID IN (select FATHERFLOWID from TBL_FLOW f where 1=1 and DEPARTINCHARGE = #{orgid}  " +
            " start with  FATHERFLOWID = -1 connect by prior FLOWID=FATHERFLOWID) " +
            ") AND COMPANY = (select FATHERORGID from TBL_ORGANIZATION where ORGID  = #{orgid}) OR FATHERORGID = -1 ")
    List<TblFlow> findBysqlFlow(String orgid);

	@Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID FROM TBL_FLOW f where  FLOWBYSYSTEM='1' and COMPANY = #{orgid}  or  Fatherflowid=-1 ORDER BY f.FLOWID")
    List<TblFlow> findBysqlFlowByType(String orgid);
	
	@SelectProvider(method="selectTblFlowList",type= TblFlowMapperSqlConifg.class)
    List<TblFlow> selectTblFlowList(PageInfo<TblFlow> pageInfo, String orgid, String faflowid, String name, String code);

    @SelectProvider(method="selectTblFlowCount",type=TblFlowMapperSqlConifg.class)
    Integer selectTblFlowCount(PageInfo<TblFlow> pageInfo, String orgid, String faflowid, String name, String code);
	
    @SelectProvider(method="findBysqAllversion",type=TblFlowMapperSqlConifg.class)
    List<TblFlow> findBysqAllversion(PageInfo<TblFlow> pageInfo, String orgid, String flowid);

    @SelectProvider(method="findBysqAllversionCount",type=TblFlowMapperSqlConifg.class)
    Integer findBysqAllversionCount(PageInfo<TblFlow> pageInfo, String orgid, String flowid);
    
    @Select("SELECT COUNT(*) FROM TBL_FLOW WHERE FATHERFLOWID = #{fatherflowid} AND FIRINGSTATUS = 1 AND FLOWID != #{flowid}")
    Integer findOtherFiringStatusByParentId(BigDecimal fatherflowid, BigDecimal flowid);
    
    @Update("UPDATE TBL_FLOW SET FIRINGSTATUS = #{firing}  WHERE FLOWID = #{flowid}")
    void excuteSql(Integer flowid, Integer firing);
    
    @Select("select * from TBL_RISK_FLOW where flowid = #{flowid}")
    List<TblRiskFlow> findBysql(String flowid);
    
    @Delete("DELETE FROM TBL_RISK_FLOW WHERE FLOWID = #{flowid}")
    void deleteTblRiskFlow(String flowid);
    
    @Select("SELECT * FROM TBL_FLOW WHERE FLOWNUMBER = #{flownumber}")
    List<TblFlow> selectFlownumber(String flownumber);
    
    @Select("SELECT * from TBL_FLOW where FATHERFLOWID= #{flowid}")
    List<TblFlow> findByFlowid(String flowid);
    
    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.COMPANY FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and f.FLOWID= #{faflowid}")
    TblFlow findByFaflowid(String faflowid);
    
    @SelectProvider(method="findListByPageInfo",type=TblFlowMapperSqlConifg.class)
    List<TblFlow> findListByPageInfo(PageInfo<TblFlow> pageInfo, BigDecimal orgid,String faflowid, String flowname, String flownumber, String stutes, String desc, String belongsto, BigDecimal fatherFlowid, Integer firingStatus);

    @SelectProvider(method="findCountByPageInfo",type=TblFlowMapperSqlConifg.class)
    Integer findCountByPageInfo(BigDecimal orgid,String faflowid, String flowname, String flownumber, String stutes, String desc, String belongsto, BigDecimal fatherFlowid, Integer firingStatus);

    @Update("UPDATE TBL_FLOW SET SETTINGID = #{settingid}  WHERE FLOWID = #{flowid}")
    void updateByFlowId(TblFlow flow);
    
    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and INFLOWDB =1   AND f.COMPANY= #{orgid}")
    List<TblFlow> findBysqlFlowh(String orgid);
    
    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and f.FLOWID= #{fatherflowid}")
    TblFlow findByFatherFlowId(BigDecimal fatherflowid);
    
    @Insert("INSERT INTO TBL_RISK_FLOW(FLOWID,RISKID) VALUES(#{flowid},#{riskid} )")
    void insertRiskFlow(BigDecimal flowid, BigDecimal riskid);
    
    @Select("SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and f.FLOWID= #{pid}")
    TblFlow findByIdPid(String pid);
    
    @SelectProvider(method="findListByPageInfoFlow",type=TblFlowMapperSqlConifg.class)
    List<TblFlow> findListByPageInfoFlow(PageInfo<TblFlow> pageInfo, String orgid, CopyVo vo);

    @SelectProvider(method="findCountByPageInfoFlow",type=TblFlowMapperSqlConifg.class)
    Integer findCountByPageInfoFlow(String orgid, CopyVo vo);
    
    @Select("SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and COMPANY = #{orgid}")
    List<TblFlow> selectByOrgid(BigDecimal orgid);
    
    @Select("SELECT CONMATID FROM TBL_FLOW_MATRIX WHERE FLOWID = #{flowid} AND ROWNUM <= 1 ORDER BY CONMATID DESC")
    String findFlowMatrixByFlowid(BigDecimal flowid);
    
    @Select("SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and  VERSIONTYPE is NULL and COMPANY = #{orgid}")
    List<TblFlow> findByOrgid(BigDecimal orgid);
    
    
    
    
}
