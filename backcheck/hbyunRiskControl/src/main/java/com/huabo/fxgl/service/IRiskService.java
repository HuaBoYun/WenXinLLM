package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.*;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
public interface IRiskService extends IService<Risk> {

    PageInfo<Risk> getRiskList(String parentId, Risk risk,Integer authorityType,Integer pageNo,Integer pageSize, TblStaffUtil staffUtil) throws Exception ;

    public Integer checkRiskNumber(String risknumber,String orgid);

    IPage<Innerrule> getInnerRuleList(IPage page, BigDecimal riskid, Innerrule innerrule);

    Page<Risk> findRiskByHistoricalVersion(Page<Risk> page,BigDecimal riskid);

    IPage<Outerrule> findOuterRuleByRiskidPageBean(IPage page, BigDecimal riskid, Outerrule outerrule);

    Integer findRiskByRisknumberAndOrgid(String risknumber,String orgid);

    IPage<Innerrule> findInnerRuleByRiskId(String riskid,Innerrule innerrule,IPage page);

    Integer isexist(RiskInnerrule riskInnerRule);

    /**
     * 查找当前公司下 以及 外部爬取的所有外归
     * @param orgid
     * @param page
     * @param tblOuterrule
     * @return
     */
   IPage findAll(BigDecimal orgid, Page page, Outerrule tblOuterrule);

   /**
    * 根据Tbl_risk riskid 查找所有选中的外归
    * @param riskid
    * @param outerrule
    * @return
    */
   List<Outerrule> findOuterRuleByRiskId(String riskid,Outerrule outerrule);

    Integer isexist2(RiskOuterrule riskOuterRule);

    void saveNewRisk(Risk risk, TblOrganizationUtil attribute, String oldRiskid, String isflow, Staff user, Flow f, Controlmatrix controlmatrix, FlowBussiness riskBussiness,String attids,RiskCoping cop)throws Exception;
    
    String selectControlMatrixId(BigDecimal riskid) throws Exception;
//    Risk geTblRiskBySave(BigDecimal riskid);
    
    Risk findById(String riskid);

    List<Risk> getByEventId(String eventid);

    PageInfo<Risk> getRiskList2(String parentId, Risk risk,Integer authorityType,Integer pageNo,Integer pageSize,String flowname,String busname,TblStaffUtil staffUtil,Integer iscurrentversion)throws Exception;
    
    PageInfo<Risk> getTesttaskList2(String parentId, Risk risk,Integer authorityType,Integer pageNo,Integer pageSize,String flowname,String busname,TblStaffUtil staffUtil)throws Exception;


   /* void saveNewRisk(Risk risk, Organization attribute, String oldRiskid,
                     String isflow, Staff user, String flownumber,
                     String flowname, Controlmatrix controlmatrix,
                     FlowBussiness riskBussiness) throws Exception;*/

    void delRiskInfoAll(String riskIds);
    List<Object[]> findexport(List<BigDecimal> riskcatid, String riskid);

    List<Object[]> exportFindRiskByPGRisks(Risk risk,Integer authorityType,List<BigDecimal> riskcatids,TblStaffUtil staffUtil) throws Exception;

    List<Object[]> exportFindRiskByPGRisksGroup(Risk risk,Integer authorityType,TblStaffUtil staffUtil) throws Exception;

    
    PageInfo<Risk> findRiskByPGRisks(Risk risk,Integer authorityType,Integer pageNo,Integer pageSize,List<BigDecimal> riskcatids,TblStaffUtil staffUtil) throws Exception;

    //集团风险数据库
    PageInfo<Risk> findRiskByPGRisksGroup(Risk risk,Integer authorityType,Integer pageNo,Integer pageSize,TblStaffUtil staffUtil) throws Exception;

    
    PageInfo<Risk> getTjfxpgjgList(Risk risk,Integer authorityType,Integer pageNo,Integer pageSize,TblStaffUtil staffUtil) throws Exception;

    
    Risk findTblRiskByFlowId(String flowid);

    String getMaxLevelById(BigDecimal riskid);

    Integer queryNumberRisksYiBan(BigDecimal orgid,String yiban);

    Integer queryNumberRisksByType(BigDecimal orgid,String year,String type);

    Integer queryNumberRisksZhongDa(BigDecimal orgid, String zhongda);
    
    JsonBean get_risk_process(String token) throws Exception;
    
    JsonBean get_risk_business(String token,String processname) throws Exception;
    
    JsonBean get_risk_no(String token,String businessno) throws Exception;
    
    
    JsonBean get_riskNo(String token) throws Exception;

    JsonBean get_riskcontrol_no(String token,String riskno) throws Exception;
    
    Map<String, Object> getGroupRiskTrendChart(String token,String year)throws Exception;
    
    Map<String, Object> getAnnualRiskGroup(String token)throws Exception;
    
    PageInfo<Risk> getRiskList3(Risk risk,Integer pageNo,Integer pageSize,String token)throws Exception;

    List<Object[]> exportRiskList3(Risk risk,String token,String ids)throws Exception;

    Map<String, Object> importRiskInfo (String token,MultipartFile file)throws Exception;
    
    Map<String, Object> riskCatnameRisks(String token,BigDecimal company)throws Exception;
    Map<String, Object> riskNumbers(String token, BigDecimal company)throws Exception;
    
    Map<String, Object> getCountByOrg(String year)throws Exception;
    Map<String, Object> reportByOrg(String year)throws Exception;

    BigDecimal getMaxVersion(Integer id)throws Exception;

    /**
     * 获取近12个月风险趋势统计
     * @param orgid 公司ID
     * @return 包含月份和数量的Map
     * @throws Exception
     */
    Map<String, Object> getRiskTrendLast12Months(String orgid) throws Exception;

    PageInfo<Risk> getRiskPointTaskList(Risk risk,Integer authorityType,Integer pageNo,Integer pageSize,TblStaffUtil staffUtil) throws Exception;


    void updateRiskOrder(BigDecimal riskid, BigDecimal riskorder);

    JsonBean getRiskTopList() throws Exception;

}
