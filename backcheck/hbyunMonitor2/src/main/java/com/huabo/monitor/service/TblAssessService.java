package com.huabo.monitor.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.TblAssess;
import com.huabo.monitor.entity.TblAssessMark;
import com.huabo.monitor.entity.TblAssessMarkVo;
import com.huabo.monitor.entity.TblAssessPlan;
import com.huabo.monitor.entity.TblAssessPlanVo;
import com.huabo.monitor.entity.TblAssessStaffVo;
import com.huabo.monitor.entity.TblAssessVo;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.entity.TblStaff;
import com.huabo.monitor.entity.Tree;
import com.huabo.monitor.vo.param.fieldActivationVo;

public interface TblAssessService extends IService<TblAssess> {

    /**
     * 评价结果列表
     *
     * @return
     */
    //JsonBean findByPage(Integer pageNumber, Integer pageSize, String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates);

    IPage<TblAssessVo> initiatePjgl ( boolean isAudit,BigDecimal orgid,BigDecimal staffid,String realName,Integer pageNumber,String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates,Integer authorityType);

    PageInfo<TblAssessVo> initiatePjglNew ( boolean isAudit,BigDecimal orgid,BigDecimal staffid,String realName,Integer pageNumber,Integer pageSize,String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates,Integer authorityType,TblStaffUtil staff );

    /*
     * 
     * */
    PageInfo<TblAssess> initiatePjglPage ( boolean isAudit,BigDecimal orgid,BigDecimal staffid,String realName,Integer pageNumber,Integer pageSize,String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates,Integer authorityType,TblStaffUtil staff);

    
    /**
     *  评价结果列表
     */
    IPage<TblAssessVo> initiatePjjg ( Integer pageNumber,String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates,TblStaffUtil staff,Integer authorityType);

    PageInfo<TblAssessVo> initiatePjjgNew ( Integer pageNumber,String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates,TblStaffUtil staff,Integer authorityType,BigDecimal orgid);

    /**
     * 传入参数说明 tabName ---- 插入编号所在的表 列入 tbl_flow column --- 编号的列名 例如 tbl_flow 表中的
     * FLOWNUMBER orgCol --- 编号所在的组织的列名 列入 tbl_flow 表中 COMPANY orgid ----- 组织ID noId
     * ------ TBL_AUTONO_INFO的主键标识 列入 流程编号就是3 chChoiceCol -------
     * 插入编号所在的表添加额外条件的列名，例如风险分类里的 MODULETYPE choiceVal ---------
     * 插入编号所在的表添加额外条件的值，例如风险分类里的 MODULETYPE 的值FXSJK
     *
     *  项目编号生成
     */
    String findFlowNextId(String tblName, String column, String orgCol, BigDecimal orgid, Integer NoId,
                          String chChoiceCol, String choiceVal, String bjf) throws Exception;
    
 //PJ-年度-001
    String findAutoNextId(String tblName, String column, String orgCol, BigDecimal orgid, Integer NoId,
            String chChoiceCol, String choiceVal, String bjf) throws Exception;
    
    TblOrganization  queryOrganizationById( BigDecimal orgid);


    void queryAllPageBeanPid(IPage<Map<String,Object>> page,BigDecimal orgid,Integer orgtype,String realname);


    void findAll(IPage<Map<String,Object>> page ,String tblComany, String templeNumber ,String templename, String orgids,BigDecimal secrectLevelId,TblStaffUtil staff);


    Long selectTblassessNumber(String assessid, BigDecimal orgids)  throws Exception;


    // 评价立项-新建页面-保存
    JsonBean saveAssEss(TblStaffUtil staff, TblAssess tblAssess, String orgid,String ids);
    
    JsonBean saveAssPlan(TblStaffUtil staff, TblAssessPlan tblAssess, String orgid,String ids);
    
    // 评价模板  双百测算
    public JsonBean queryPjmb(BigDecimal  tmplId);
    // 评价立项-授权分页查询
    void getOrgByassId(IPage<Map<String,Object>> page,BigDecimal assid);
    // 评价立项 -授权列表-授权
    void findAssessMarkVoByPageBean(IPage<TblAssessMarkVo> page,BigDecimal assid, BigDecimal orgid);

    // 设置参评人权重列表
    public void getTblAssessStaffByMarkId(IPage<TblAssessStaffVo> page,BigDecimal assmarkid);

    // 得到TblAssess 参数 assmarkid

    TblAssess getTblAssessByassmarkid(BigDecimal assmarkid);

    // 保存参评人权重
    TblAssessMark  updateTblAssessStaff(BigDecimal assMarkId,String[] idArray,String[] varleArray);

    // 保存主评人
    JsonBean updateAssessMarkStaffid(String userid,String[] assMarkids );


    // 保存参评人
    JsonBean updateCanPingRen(String users,String assMarkids );

    // 项目立项-修改-查询所在部门ids
    public List<TblOrganization> getOrgByassId(BigDecimal assId);


    JsonBean updateAssEss(TblStaffUtil staff,String assid,String assessname,String start,String end,String staffid,BigDecimal templatekey,String orgid,String assteamname,String assteamlead,String assteammemberids,BigDecimal planid,String planname,BigDecimal secrectlevelid,String staffScopeNames,String staffscopeids,fieldActivationVo content,String attids );
    
    JsonBean updateAssPlan(TblStaffUtil staff,String start, String end,String attid,TblAssessPlan assess);
    
    /**
     *项目评分列表
     *
     * @return
     */
    JsonBean findByPage(Integer pageNumber, Integer pageSize, String assNumnber, String assName, String startDate, String endDate);

    JsonBean add(TblAssess tblAssess);

    JsonBean update(TblAssess tblAssess);

    JsonBean delete(BigDecimal assId);

    String deleteAssess(BigDecimal assid);

    Integer findTblAssessByTempid(BigDecimal assId) throws Exception;

    TblAssessVo queryTblAssessVoByAssidAndOrgid(BigDecimal assId,BigDecimal orgid);

    List<Map<String,Object>> getOrgTree(BigDecimal assorgid);
    
    JsonBean deleteCanPingRen(String assId);
    
    List<Tree> getNodeAllbm(BigDecimal nodeId);
    
    
    void insAssessTeamMember(String assteammemberid,BigDecimal assid);
    
    List<TblStaff> gettAssteamMembers(BigDecimal assId);
    
    void saveRepAtt(TblAttachment a, String assId);

    void delAssessAtt(BigDecimal attid, String assId);

    List<TblAttachment> getAssessAttListByAssid(BigDecimal reportid);
    
    //==
    IPage<TblAssessPlanVo> initiatePjjh ( boolean isAudit,BigDecimal orgid,BigDecimal staffid,String realName,Integer pageNumber,String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates,Integer status,Integer authorityType);

    PageInfo<TblAssessPlan> initiatePjjhPage ( boolean isAudit,BigDecimal orgid,BigDecimal staffid,String realName,Integer pageNumber,String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates,Integer status,Integer authorityType,Integer pageSize,TblStaffUtil staff,BigDecimal secrectLevelld)throws Exception;

    int  getMajorByAssid(BigDecimal assId,String staffid)throws Exception;
////////////////////
    // 评价立项-授权分页查询
    PageInfo<Map<String,Object>> getOrgByassIdNew(BigDecimal assid,Integer pageNumber,Integer pageSize) throws Exception;;

    PageInfo<TblAssessMarkVo> findAssessMarkIpage(Integer pagenumber,Integer pagesize,BigDecimal assid, BigDecimal orgid);

    
    //数据库兼容内控评价模块获取新编码
  	 String findFlowNextIdNk(String tblName, String column, String orgCol, BigDecimal orgid, Integer NoId, String chChoiceCol, String choiceVal, String bjf) throws Exception ;

  	TblAssessPlan selectOne(BigDecimal planid) throws Exception;
  	
}
