package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.Bug;
import com.huabo.fxgl.mapper.BugMapper;
import com.huabo.fxgl.service.IBugService;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.*;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.Outerrule;
import com.huabo.fxgl.mapper.BugMapper;
import com.huabo.fxgl.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LiYe
 * @author LiYe
 * @since 2022-08-12
 * import javax.annotation.Resource;
 * import java.util.HashMap;
 * import java.util.LinkedList;
 * import java.util.List;
 * import java.util.Map;
 * import java.util.stream.Collectors;
 * <p>
 * /**
 * <p>
 * 服务实现类
 * </p>
 * @since 2022-08-05
 */
@Slf4j
@Service
public class BugServiceImpl extends ServiceImpl<BugMapper, Bug> implements IBugService {
    @Autowired
    private BugMapper bugMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public Bug findByCode(String code, String type, String orgid) {
        List<Bug> list = bugMapper.findByCode(code, type, new BigDecimal(orgid));
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public IPage<Bug> getBugList(Bug bug, String startdate, String enddate, IPage page, String orgid, String orgtype, String buglevel, String type) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotEmpty(type) && type.length() > 2) {
            type = type.substring(0, 2);
        }
        log.info("-----------------------------type: " + type);
        queryWrapper.like("BU.BUGBYSYSTEM", type);
        if (bug != null) {
            if (bug.getBugnumber() != null) {
                queryWrapper.like("BUGNUMBER", bug.getBugnumber());
            }
            if (bug.getBugsource() != null) {
                queryWrapper.like("BUGSOURCE", bug.getBugsource());
            }
            if (bug.getBugreformstatus() != null) {
                queryWrapper.like("BUGREFORMSTATUS", bug.getBugreformstatus());
            }
            if (bug.getDiscoverperson() != null) {
                queryWrapper.like("DISCOVERPERSON", bug.getDiscoverperson());
            }
        }
        if (startdate != null && !"".equals(startdate)) {
            queryWrapper.ge("DISCOVERTIME", LocalDate.parse(startdate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (enddate != null && !"".equals(enddate)) {
            queryWrapper.le("DISCOVERTIME", LocalDate.parse(enddate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (buglevel != null && buglevel.trim() != "") {
            queryWrapper.eq("CIR.BUGCRIID", buglevel);
        }

        queryWrapper.orderByDesc("bu.BUGID");

        if (orgtype != null && orgtype.equals("0") && !"".equals(orgtype)) {
            return bugMapper.selectPage1(orgid, page, queryWrapper);
        } else {
            return bugMapper.selectPage2(orgid, page, queryWrapper);
        }
//        return  bugMapper.selectPage1(page,queryWrapper);
    }


/*    public IPage findALL(Bug bug, String startdate, String enddate, Integer startIndex, Integer pageSize, String orgid, String orgtype, String buglevel, String type) {
        QueryWrapper queryWrapper = new QueryWrapper();

        if(StringUtils.isNotEmpty(type) && type.length()>2){type = type.substring(0,2);}
        String sql="";
        String sqlCount="";
        if (orgtype!=null && orgtype.equals("0") && !"".equals(orgtype)) {
            bugMapper.findALL1(orgid,type);
        }else{
            bugMapper.findALL2(orgid,type);
            }
        if (bug!=null) {
            if (bug.getBugnumber()!=null) {
                queryWrapper.like("BUGNUMBER",bug.getBugnumber());
            }
            if (bug.getBugsource()!=null) {
                queryWrapper.like("BUGSOURCE",bug.getBugsource());
            }
            if (bug.getBugreformstatus()!=null) {
                queryWrapper.like("BUGREFORMSTATUS",bug.getBugreformstatus());
            }
            if (bug.getDiscoverperson()!=null) {
                queryWrapper.like("DISCOVERPERSON",bug.getDiscoverperson());
            }
        }

            if (startdate!=null && !"".equals(startdate)) {
                queryWrapper.ge("DISCOVERTIME", LocalDate.parse(startdate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));


            }
            if (enddate!=null&&!"".equals(enddate)) {
                queryWrapper.le("DISCOVERTIME", LocalDate.parse(enddate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            }

        if(buglevel!=null && buglevel.trim()!=""){
            sql+=" and CIR.BUGCRIID ="+buglevel;
            sqlCount+=" and CIR.BUGCRIID ="+buglevel;
        }
        sql+="  ORDER BY bu.BUGID desc ";
        queryWrapper.orderByDesc();



        return tblBugDao.findByPageBean(sql, sqlCount, startIndex, pageSize);
    }*/


    @Autowired
    private IOrganizationService organizationService;


    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description 缺陷全查
    * @Date 2022/8/15
    * @param orgid
    * @param orgtype
    * @param pageNumber
    * @param searchbegintime
    * @param searchendtime
    * @param plancode
    * @param plantype
    * @param state
    * @param buglevelquery
    * @param type
    * @param hbOrgEntity
    * @param staff
    * @return com.hbfk.util.JsonBean
    * @url:
    **/
    @Override
    public JsonBean defectList(String orgid, String orgtype, Integer pageNumber, Integer pageSize, String searchbegintime, String searchendtime, String plancode,
                               String plantype, String state, String buglevelquery, String type, String token) throws Exception {
        TblStaffUtil tblStaffUtil = userProvider.get();
        TblOrganizationUtil linkOrg = tblStaffUtil.getLinkOrg();
        TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();


        if (orgid == null || orgid.equals("")) {
            orgid = currentOrg.getOrgid().toString();
            orgtype = currentOrg.getOrgtype().toString();
        }
        log.info("{}===={}", currentOrg.getOrgid(),linkOrg.getOrgid());

        Organization organization = organizationService.getById(orgid);
         boolean isSelect=organizationService.isAuditByOrgId(linkOrg.getOrgid().toString());
         JsonBean jsonBean=null;
        if (isSelect) {
            if (linkOrg.getOrgid().toString().equals(orgid) && orgtype != null && !orgtype.equals("")) {
                orgid = currentOrg.getOrgid().toString();
                orgtype = currentOrg.getOrgtype().toString();
            }
            if (orgtype == null || orgtype.equals("")) {
                orgtype = organization.getOrgtype() == null ? "0" : organization.getOrgtype().toString();
            }
            jsonBean= findAll(plancode,plantype,searchbegintime, searchendtime, pageNumber,pageSize, orgid,
                    orgtype, buglevelquery, type,state);
        } else {
            if (linkOrg.getOrgid().toString().equals(orgid)) {
                orgid =linkOrg.getOrgid().toString();
                orgtype = linkOrg.getOrgtype().toString();
                jsonBean = findAll(plancode,plantype,searchbegintime, searchendtime, pageNumber,pageSize, orgid,
                        orgtype, buglevelquery, type,state);
            } else {
                jsonBean=new JsonBean();
            }
        }

        return jsonBean;
    }





    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description 封装全查条件
    * @Date 2022/8/6
    * @param null
    * @return
    * @url:
    **/

    private JsonBean findAll(String plancode, String plantype, String searchbegintime, String searchendtime,
                             Integer pageNumber, int pageSize, String orgid, String orgtype, String buglevelquery, String type, String state) {
        if(StringUtils.isNotEmpty(type) && type.length()>2){
            type = type.substring(0,2);
        }
        QueryWrapper<Bug> queryWrapper=new QueryWrapper<>();
        if (orgtype != null && orgtype.equals("0")) {
            queryWrapper.eq("BUGDEPARTMENT",orgid).like("BU.BUGBYSYSTEM",type);
        }else{
            String sql="(BUGDEPARTMENT in (select ORGID from TBL_ORGANIZATION where 1=1  and ORGTYPE=0 start with  fatherorgid="+orgid+"connect by prior fatherorgid=ORGID"+") or BUGDEPARTMENT="+orgid+")";
            queryWrapper.apply(sql);
            if (type!=null){
                queryWrapper.like("BU.BUGBYSYSTEM",type);
            }
        }
        if (plancode!=null) {
            queryWrapper.like("BUGNUMBER",plancode);
        }
        if (plantype!=null) {
            queryWrapper.like("BUGSOURCE",plantype);
        }
        if (state != null && state.length() > 0) {
            queryWrapper.like("BUGREFORMSTATUS",plancode);
        }
        if (searchbegintime!=null && !"".equals(searchbegintime)) {
            queryWrapper.apply("DISCOVERTIME >= TO_DATE('"+searchbegintime+"', 'yyyy-MM-dd') ");
        }
        if (searchendtime!=null&&!"".equals(searchendtime)) {
            queryWrapper.apply(" DISCOVERTIME <= TO_DATE('"+searchendtime+"', 'yyyy-MM-dd') ");
        }
        if(buglevelquery!=null && !buglevelquery.trim().equals("")){
            queryWrapper.eq("CIR.BUGCRIID",buglevelquery);
        }
        IPage<Bug> page=new Page<>(pageNumber,pageSize);
        final IPage<Bug> iPage = bugMapper.findAll(page, queryWrapper);
        return new JsonBean(1,"操作成功",iPage.getRecords());
    }



    @Autowired
    private IBugAttService bugAttService;
    @Autowired
    private IBugCriterionService iBugCriterionService;
    @Autowired
    private IBugInnerruleService iBugInnerruleService;
    @Autowired
    private IBugOuterruleService iBugOuterruleService;

    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description 删除缺陷，先删除中间表的的数据
    * @Date 2022/8/6
    * @param selectProjectid
    * @param wt
    * @param type
    * @param wtorgid
    * @param orgid
    * @param choiceSearch
    * @return void
    * @url:
    **/
    @Override
    public JsonBean defectDelete(String selectProjectid, String wt, String type, String wtorgid, String orgid, String choiceSearch) {
        if (selectProjectid==null){
            return new JsonBean(0,"删除失败",null);
        }
        bugAttService.deleteByBudId(selectProjectid);
        iBugInnerruleService.deleteByBudId(selectProjectid);
        iBugOuterruleService.deleteByBudId(selectProjectid);
        return this.removeById(selectProjectid)?new JsonBean(1,"删除成功",null):new JsonBean(0,"删除失败",null);
    }


    @Autowired
    private IBugInnerruleService bugInnerruleService;

    @Autowired
    private IBugOuterruleService bugOuterruleService;

   @Autowired
   private IBugCriterionService bugCriterionService;

   @Autowired
   private IAttachmentService iAttachmentService;

   @Autowired
   ICriterionService criterionService;

   @Autowired
   private IBugAttService iBugAttService;
    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 获取该缺陷的详情
     * @Date 2022/8/8
     * @param id
     * @param organization
     * @return com.hbfk.util.JsonBean
     * @url:
     **/
    @Override
    public JsonBean defectDetail(String id, String token) throws Exception {
        if(id==null){
           return new JsonBean();
        }
         TblStaffUtil tblStaffUtil = userProvider.get();
         TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();
         Bug bug = this.getById(id);
         Organization organization1 = organizationService.getById(bug.getBugdepartment());
        List<Innerrule> innerrules= bugInnerruleService.getInnerRulesByBugId(id);
        List<Outerrule> outerrules=bugOuterruleService.getOuterRulesByBugId(id);
        List<Attachment> attachments=iBugAttService.getBugAttachmentByBugId(id);
        List<Bug>children =bugMapper.getChildrenById(id);
        List<Criterion> criterionList=bugCriterionService.getCriterionByBugId(id);
        Map<String,Object> map=new HashMap<>();
        if(currentOrg!=null){
            List<Criterion> list=criterionService.getByOrgId(currentOrg.getOrgid());
            map.put("list",list);
        }
        map.put("children",children);
        map.put("attachments",attachments);
        map.put("innerRules",innerrules);
        map.put("outerRules",outerrules);
        map.put("criterionList",criterionList);
        map.put("tblBug",bug);
        return new JsonBean(1,"操作成功",map);
    }

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 获取子节点
     * @Date 2022/8/8
     * @param Id
     * @return java.util.List<com.huabo.fxgl.entity.Bug>
     * @url:
     **/
    private List<Bug> getChildrenById(String Id) {
        final List<Bug> list = this.list();
        if (list == null || list.size() <= 0) {
            return new LinkedList<>();
        }
        return list.stream().filter(item -> {
            return item.getFatherbugid() != null && item.getFatherbugid().equals(Id);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Object[]> qxglExport(String orgid, String type) {
        List<HashMap> bugs = bugMapper.qxglExport(orgid, type);
        //String[] titles = { "缺陷编号", "缺陷级别", "发现日期", "发现人", "是否财务相关", "缺陷性质", "公司名称", "缺陷部门", "是否需要整改", "业务描述",
        //					"缺陷描述" }; 125127 n
        List<Object[]> list = new ArrayList<>();

        for (Map map : bugs) {
            Object[] objects = new Object[11];
            objects[0] = map.get("BUGNUMBER");
            objects[1] = map.get("BUGCRILEVEL");
            objects[2] = map.get("DISCOVERTIME");
            objects[3] = map.get("DISCOVERPERSON");
            objects[4] = map.get("BUGSOURCE");
            objects[5] = map.get("BUGPROPERTY");
            objects[6] = map.get("ORGS");
            objects[7] = map.get("ORGNAME");
            objects[8] = map.get("NEEDREFORM");
            objects[9] = map.get("BUSINESSDESCRIPTION");
            objects[10] = map.get("bugdescripte");
            list.add(objects);
        }
        return list;
    }


}
