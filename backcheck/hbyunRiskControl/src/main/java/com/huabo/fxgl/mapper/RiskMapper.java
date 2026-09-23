package com.huabo.fxgl.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.vo.RISKTOP10;
import org.apache.ibatis.annotations.*;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.Outerrule;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.RiskOuterrule;
import com.huabo.fxgl.entity.Riskcategory;
import com.huabo.fxgl.entity.TblRiskBusinessDic;
import com.huabo.fxgl.entity.TblRiskProcessDic;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Repository
public interface RiskMapper extends BaseMapper<Risk> {
    // 自定义SQL的分页, SQL语句见XML文件
    /*IPage<Risk> selectPage1(IPage page, @Param("ew") Wrapper<T> queryWrapper);*/
	
	@Select("select * from tbl_risk where riskstatus=2")
	List<Risk> selectOpenRisk();
	
    List<Outerrule> findOuterRuleByRiskId(String riskid, @Param("ew") Wrapper<T> queryWrapper);

    IPage<Risk> findAll(@Param("orgid") BigDecimal orgid, IPage page, @Param("ew") Wrapper<T> queryWrapper);

    IPage<Innerrule> findInnerRuleByRiskId(String riskid,IPage page, @Param("ew") Wrapper<T> queryWrapper);

    IPage<Innerrule> selectInnerRulePage(BigDecimal riskid, IPage page, @Param("ew") Wrapper<T> queryWrapper);

    IPage<Outerrule> findOuterRuleByRiskidPageBean(BigDecimal riskid, IPage page, @Param("ew") Wrapper<T> queryWrapper);


    @Select("select * from tbl_riskcategory t where RISKCATNAME in('企业风险','业务风险','专项风险') ")
    List<Riskcategory> findQYFXByOrgid(QueryWrapper queryWrapper);

    @Select( "SELECT count(*) FROM TBL_RISK WHERE RISKNUMBER =#{risknumber} AND UNIT= #{orgid}")
    Integer checkRiskNumber(@Param("risknumber") String risknumber,@Param("orgid")String orgid);

    @Select("select count(*) from TBL_RISK_INNERRULE where RISKID =#{param1} and INNRULID = #{param2}")
    Integer isexist(String getRiskid,String getInnrulid);

    @Select("select count(*) from TBL_RISK_OUTERRULE where RISKID = #{riskid} and OUTRULID = #{outrulid}")
    Integer isexist2(RiskOuterrule riskOuterRule);

    //自定义分页，，sql语句见xml文件
    IPage<Risk> selectPage2(IPage page, @Param("ew") Wrapper<T> queryWrapper);

    /*@Select("SELECT unit FROM Tbl_Risk WHERE riskid= #{param1}")
    Risk selectUnitByRiskId(String oldRiskid);

    @Select("SELECT CONMATID FROM TBL_RISK_CONTROLMATRIX " +
            "WHERE RISKID = #{riskid} " +
            "AND ROWNUM <= 1 ORDER BY CONMATID DESC")
    String selectControlMatrixId(BigDecimal riskid) throws Exception;*/

    Risk geTblRiskBySave(@Param("ew") Wrapper<T> queryWrapper);
    //查询最大riskid在tbl_risk中用于geTblRiskBySave
    /*@Select("select max(riskid) from tbl_risk")
    BigDecimal max1();
    @Select("select * from TBL_RISK where riskid =#{riskid}")
    List findById(String riskid);*/
    // 自定义SQL的分页, SQL语句见XML文件
    IPage<Risk> selectPage1(IPage page, @Param("ew") Wrapper<T> queryWrapper);

    List<Risk> selectPage1(@Param("ew") Wrapper<T> queryWrapper,@Param("sql")String sql);
    
    Integer selectPage1Count(@Param("ew") Wrapper<T> queryWrapper);
    
    // 自定义SQL的分页, SQL语句见XML文件
    List<Risk> selectPage2(@Param("ew") Wrapper<T> queryWrapper, @Param("ids") String ids, @Param("closestatus") BigDecimal closestatus,@Param("sql")String sql);

    
    List<Risk> selectPage3(@Param("ew") Wrapper<T> queryWrapper, @Param("ids") String ids, @Param("sql") String sql);

    @Select("SELECT unit FROM TBL_RISK WHERE riskid= #{oldRiskid}")
    Risk selectUnitByRiskId(String oldRiskid);

    @Select("SELECT CONMATID FROM TBL_RISK_CONTROLMATRIX " +
            "WHERE RISKID = #{riskid} " +
            "AND ROWNUM <= 1 ORDER BY CONMATID DESC")
    String selectControlMatrixId(BigDecimal riskid) throws Exception;
    @Select("SELECT CONMATID FROM TBL_RISK_CONTROLMATRIX " +
            "WHERE RISKID = #{riskid} " +
            " ORDER BY CONMATID DESC")
    List<String> selectControlMatrixId2(BigDecimal riskid) throws Exception;

    List<Risk> geTblRiskBySave(BigDecimal riskid, @Param("ew") Wrapper<T> queryWrapper);

    //查询最大riskid在tbl_risk中用于geTblRiskBySave
    @Select("select max(riskid) from tbl_risk")
    BigDecimal max1();
    //详情请看xml文件
    List<Risk>  findList(@Param("ew") Wrapper<T> queryWrapper);

    @Select("select * from TBL_RISK where riskid =#{riskid}")
    List findById(String riskid);

    @Select("SELECT * FROM TBL_RISK where RISKNUMBER=#{risknumber} and RISKCATID=#{riskcatid}")
    List<Risk> findTblRiskByRiskNumber(@Param("risknumber") String risknumber, @Param("riskcatid") String riskcatid);

    @Select("SELECT * FROM TBL_RISK WHERE RISKID IN (SELECT RISKID FROM TBL_RISK_FLOW WHERE FLOWID = #{flowid}) ORDER BY RISKID DESC")
    List<Risk> findTblRiskByFlowId(@Param("flowid") String flowid);

    //1.删除自定义表单相关信息
    @Delete("DELETE TBL_FORM_CONTROELE WHERE ELEID IN (SELECT ELEID FROM TBL_FORM_ELEMENTS WHERE FORMID IN (SELECT FORMID FROM TBL_FORM_INFO WHERE FLOWID IN (${riskIds})))")
    int deleteTBLFORMCONTROELE(@Param("riskIds") String riskIds);

    @Delete("DELETE TBL_FORM_ELEMENTS WHERE FORMID IN (SELECT FORMID FROM TBL_FORM_INFO WHERE FLOWID IN (${riskIds}))")
    int deleteTBLFORMELEMENTS(@Param("riskIds") String riskIds);

    @Delete("DELETE TBL_FORM_VALUE WHERE FORMID IN (SELECT FORMID FROM TBL_FORM_INFO WHERE FLOWID IN (${riskIds}))")
    int deleteTBLFORMVALUE(@Param("riskIds") String riskIds);

    @Delete("DELETE TBL_FORM_INFO WHERE FLOWID IN (${riskIds})")
    int deleteTBLFORMINFO(@Param("riskIds") String riskIds);

    //2.删除控制措施;
    @Select("SELECT WM_CONCAT(CONMATID) FROM TBL_RISK_CONTROLMATRIX WHERE RISKID IN (${riskIds})")
    String selectByRiskId1(@Param("riskIds") String riskIds);

    @Delete("DELETE TBL_RISK_CONTROLMATRIX WHERE RISKID IN (${riskIds})")
    int deleteTBLRISKCONTROLMATRIX(@Param("riskIds") String riskIds);

    @Delete("DELETE TBL_CONTROLMATRIX WHERE CONMATID IN (${resultId1})")
    int deleteTBLCONTROLMATRIX(@Param("resultId1") String resultId1);

    //3.删除风险事件
    @Delete("DELETE TBL_RISK_RISKEVENT WHERE RISKID IN (${riskIds})")
    int deleteTBLRISKRISKEVENT(@Param("riskIds") String riskIds);

    //4.删除内规
    @Delete("DELETE TBL_RISK_INNERRULE WHERE RISKID IN(${riskIds})")
    int deleteTBLRISKINNERRULE(@Param("riskIds") String riskIds);

    //删除外规关系
    @Delete("DELETE TBL_RISK_OUTERRULE WHERE RISKID IN (${riskIds})")
    int deleteTBLRISKOUTERRULE(@Param("riskIds") String riskIds);

    //3.删除流程相关信息
    @Select("SELECT WM_CONCAT(FLOWID) FROM TBL_RISK_FLOW WHERE RISKID IN (${riskIds})")
    String selectByRiskId2(@Param("riskIds") String riskIds);

    @Delete("DELETE TBL_FLOWDES WHERE FLOWID IN  (${resultId2})")
    int deleteTBLFLOWDES(@Param("resultId2") String resultId2);

    @Delete("DELETE TBL_RISK_FLOW WHERE RISKID IN (${riskIds})")
    int deleteTBLRISKFLOW(@Param("riskIds") String riskIds);

    @Delete("DELETE TBL_FLOW_BUSSINESS WHERE FLOWID IN  (${resultId2})")
    int deleteTBLFLOWBUSSINESS(@Param("resultId2") String resultId2);

    @Delete("DELETE TBL_FLOW WHERE FLOWID IN  (${resultId2})")
    int deleteTBLFLOW(@Param("resultId2") String resultId2);

    @Delete("DELETE TBL_RISK WHERE RISKID IN (${riskIds})")
    int deleteTBLRISK(@Param("riskIds") String riskIds);
    
    
    @Delete("DELETE from tbl_risk_att WHERE riskid= #{riskid} ")
    int deleteRiskAtt(@Param("riskid") BigDecimal riskid);
    
    @Insert("insert into tbl_risk_att(ATTID, riskid) values (#{attid}, #{riskid})")
    int insertRiskAtt(@Param("riskid") BigDecimal riskid, @Param("attid") BigDecimal attid);

    @Select("WITH node_cte(riskid, riskextid) AS (select node.riskid, node.riskextid "
			+ "from TBL_RISK node where node.riskid = #{riskextid} "
			+ "union all "
			+ "select n.riskid, n.riskextid "
			+ "from TBL_RISK n, node_cte c "
			+ "where n.riskid = c.riskextid) SELECT * FROM node_cte")
	List<Risk> recursionRiskExtId(@Param("riskextid")BigDecimal riskextid);

    @Select("select * from TBL_RISK where riskid in (${riskIds}) order by VERSION desc")
    Page<Risk> findRecursionRiskExtId(@Param("page") Page<Risk> page,@Param("riskIds") String riskIds);
    
    @Select("SELECT * FROM TBL_RISK_PROCESS_DIC WHERE 1=1 order by SERNO ASC ")
	List<TblRiskProcessDic> get_risk_process() throws Exception;
    
    @Select("SELECT * FROM TBL_RISK_BUSINESS_DIC WHERE processname=#{processname} order by SERNO ASC ")
	List<TblRiskBusinessDic> get_risk_business(String processname) throws Exception;
    
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(RISKNUMBER,INSTR(RISKNUMBER,'-',-1)+1)))  "
			+ " FROM TBL_RISK "
			+ " WHERE RISKNUMBER LIKE ${riskno} ")
	Integer get_risk_no(String riskno) throws Exception;
    
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(controlnumber,INSTR(controlnumber,'-C',-1)+2)))  "
			+ " FROM TBL_CONTROLMATRIX "
			+ " WHERE controlnumber LIKE ${riskcontrolno} ")
	Integer get_riskcontrol_no(String riskcontrolno) throws Exception;
    
    
    Integer getGroupRiskTrendChart(@Param("orgid")BigDecimal orgid,@Param("year")String year,@Param("type")String type);
    
    @Select("select year(riskcreatedt) from tbl_risk group by year(riskcreatedt)")
    List<Integer> getRiskYearList();

	List<Risk> selectRisk(@Param("queryParam")Risk queryParam,@Param("authorityType")Integer authorityType,@Param("idList")List<BigDecimal> idList,@Param("sql")String sql);

	
	List<Risk> getTjfxpgjgList(@Param("queryParam")Risk queryParam,@Param("authorityType")Integer authorityType,@Param("idList")List<BigDecimal> idList,@Param("sql")String sql);

	List<Risk> getRiskPointTaskList(@Param("queryParam")Risk queryParam,@Param("authorityType")Integer authorityType,@Param("idList")List<BigDecimal> idList,@Param("sql")String sql);

    @Select("select  LEVELID  from TBL_SECRECT_LEVEL where LEVELTYPE=2 and level >=(select level from TBL_SECRECT_LEVEL where   levelid=#{id})")
    List<String> getSecrectLevel(@Param("id")String id);
    
    
    @Select("select count(1) from tbl_risk where  riskname=#{name} and RISKDES=#{des} and unit=#{unit}")
    int getCountByRiskname(@Param("name")String name,@Param("des")String des,@Param("unit")BigDecimal unit);


    List<Map<String, Object>> riskCatnameRisks(@Param("company")BigDecimal company);
    
   Integer getZs(@Param("company")BigDecimal company);

   Integer getYsp(@Param("company")BigDecimal company);

   Integer getWsp(@Param("company")BigDecimal company);

   List<Map<String, Object>> getCountByOrg(@Param("year")String year);

   @Select("select o.orgname as name,count(1) as number from tbl_report r  left join tbl_organization o on o.orgid=r.orgid where type='fx_zdy'  and r.STATUS = 6 AND r.REPORTSUBSTATUS = 1 group by o.orgname")
   List<Map<String, Object>> reportByOrg(@Param("year")String year);

   @Select("select  max(TO_NUMBER(version))   from tbl_risk where (riskid=#{id} or riskextid=#{id})")
   BigDecimal getMaxVersion(@Param("id")Integer id);


    @Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNUMBER = #{orgid} LIMIT 1")
    String getNumberById(String orgid);

    @Select("SELECT STAFFID FROM TBL_STAFF WHERE USERNAME = #{username} LIMIT 1")
    BigDecimal getUserById(String username);

    @Select("SELECT REALNAME FROM TBL_STAFF WHERE USERNAME = #{username} LIMIT 1")
    String getUserIdByRealName(String username);


    @Select("SELECT RISKID FROM TBL_RISK WHERE TOP10TYPE = 'aqyh' AND TOP10ID = #{TOP10ID} LIMIT 1 ")
    String getRiskIdByTOPID(String TOP10ID);

    @Select("SELECT RISKID FROM TBL_RISK WHERE TOP10TYPE = 'scyx' AND TOP10ID = #{TOP10ID} LIMIT 1 ")
    String getScRiskIdByTOPID(String TOP10ID);

    @Select("SELECT ORGID FROM TBL_STAFF WHERE USERNAME = #{username} LIMIT 1")
    BigDecimal getOrgidByuser(String username);


    @Select("SELECT RISKCATID FROM TBL_RISKCATEGORY WHERE RISKCATNAME = '安全环保风险' AND UNIT = #{orgid} LIMIT 1")
    String getOneFxId(String orgid);
    @Select("SELECT RISKCATID FROM TBL_RISKCATEGORY WHERE RISKCATNAME = '放射源失控风险' AND UNIT = #{orgid} LIMIT 1")
    String getTwoFxId(String orgid);
    @Select("SELECT RISKCATID FROM TBL_RISKCATEGORY WHERE RISKCATNAME = '安全生产风险' AND UNIT = #{orgid} LIMIT 1")
    String getscTwoFxId(String orgid);/*安全生产风险*/

    /**
     * 统计指定公司在指定月份创建的风险数量
     * @param orgid 公司ID
     * @param month 月份，格式：yyyy-MM
     * @return 风险数量
     */
    @Select("SELECT COUNT(1) FROM TBL_RISK WHERE UNIT = #{orgid} AND TO_CHAR(RISKCREATEDT, 'YYYY-MM') = #{month}")
    Integer countRiskByMonth(@Param("orgid") String orgid, @Param("month") String month);


    @Update("UPDATE TBL_RISK SET RISKORDER =  #{riskorder} WHERE RISKID = #{riskId}")
    Integer updateOrderById(@Param("riskId") BigDecimal riskId, @Param("riskorder") BigDecimal riskorder);

    List<RISKTOP10> selectRiskTop();

}