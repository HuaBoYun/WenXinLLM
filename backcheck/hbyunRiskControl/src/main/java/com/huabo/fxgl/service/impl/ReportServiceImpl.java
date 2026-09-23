package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.controller.ReportMsg;
import com.huabo.fxgl.mapper.OpenQueryMapperSqlConfig;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.mapper.ReportMapper;
import com.huabo.fxgl.mapper.RiskeventMapper;
import com.huabo.fxgl.service.IReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.GeneralEntity;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.TblTransferWorkUtils;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.*;
import com.huabo.fxgl.mapper.ReportMapper;
import com.huabo.fxgl.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.util.ProcessEnum;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import javax.annotation.Resource;

import static org.mockito.AdditionalMatchers.find;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-09
 */
@Slf4j
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements IReportService {

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IProcessSettingService processSettingService;

    @Autowired
    private INbsjProjectService nbsjProjectService;

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private IRepAttService repAttService;

    @Autowired
    private INbsjProjectService iNbsjProjectService;

    @Autowired
    private INbsjStaffselectService staffselectService;

    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Autowired
    private ReportMapper reportMapper;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 通过names time 查询LIST
     *
     * @param name
     * @param time
     * @return
     * @throws ParseException
     */
    @Override
    public List search(String name, String time) throws ParseException {
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.eq("reportname", name);
        }
        if (StringUtils.isNotBlank(time)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            queryWrapper.eq("reporttime", formatter.parse(time));
        }
        return list(queryWrapper);
    }

	@Value("${application.administrators:}")
	private String administrators;

    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description 对风险报告进行查询
    * @Date 2022/8/15
    * @param find
    * @param type
    * @param projectId
    * @param view
    * @param pageNumber
    * @param organization
    * @param staff
    * @return com.hbfk.util.JsonBean
    * @url:
    **/
    @Override
    public JsonBean reportList(Find find, String type, String projectId, String view, Integer pageNumber, Integer pageSize,String token) throws Exception {
        Map<String,Object> map=new HashMap<>();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
		Integer authorityType;
		if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())) {
			authorityType = 1;
		} else {
			authorityType = 0;
		}
        //TODO
        String identifier = "";
        if(StringUtils.isNotBlank(type)){
            if("nbsj".equals(type.trim())||"nbsj_gzt".equals(type.trim())){
             identifier=activityPluginsService.getoNState(ProcessEnum.SJ_SJBG.name());
              log.info("{}",identifier);
              NbsjProject nbsjProject= nbsjProjectService.getSelectProject(selectOrg.getOrgid());
              if (projectId!=null&&projectId.length()>0){
                  nbsjProject= nbsjProjectService.getById(projectId);
              }
                log.info("---------{}",identifier);
              if (nbsjProject!=null){
                  find.setId(nbsjProject.getProjectid());
              }
            }else if("nbsj_zdy".equals(type.trim())){
                NbsjProject nbsjProject= nbsjProjectService.getSelectProject(selectOrg.getOrgid());
                if (projectId!=null&&projectId.length()>0){
                    nbsjProject= nbsjProjectService.getById(projectId);
                }
                if (nbsjProject!=null){
                    find.setId(nbsjProject.getProjectid());
                }
            }
        }
        try {
        	BigDecimal orgid = selectOrg.getOrgid();
        	if (find.getOrgid() != null) {
        		orgid = new BigDecimal(find.getOrgid());
        	}
			//
        //String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(),authorityType == 0 , "ORGID", "LINKDEPTID", "REPORTERID", "SECRECTLEVELID", "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
        String sql = GeneralSQLConcatConfig.concatSecrectSqlEntity(new GeneralEntity(staffUtil.getCurrentOrg().getUseSecrect(),authorityType == 0 , "ORGID", "LINKDEPTID", "REPORTERID", "SECRECTLEVELID", "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds(),authorityType));
        PageInfo<Report> page = findAll(find, type, projectId, view, pageNumber, orgid, pageSize,staffUtil.getStaffid(),authorityType,sql);
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtils.isNotEmpty(page.getList())){
			page.getList().forEach(entity->{
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(entity,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,entity ); 
					item=null; // 处理并解除引用
					nameEntity=null; // 处理并解除引用
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			} );
		}
        map.put("pageNo",pageNumber);
        map.put("identifier",identifier);
        map.put("type",type);
        map.put("page",page);
        map.put("view",find.getView());
        map.put("projectId",projectId);
        
        
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return new JsonBean(1,"操作成功",map);
    }




    //TODO 修改ids传参
    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description
    * @Date 2022/8/9
    * @param ids 通过id集合批量删除
    * @return com.hbfk.util.JsonBean
    * @url:
    **/
    @Transactional
    @Override
    public JsonBean reportDelete(String[] ids) {
        // 首先删除中间表里的数据
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("REPORTID", ids);
        repAttService.remove(queryWrapper);

        if (this.removeByIds(Arrays.asList(ids))) {
          return  new JsonBean(1,"操作成功",null);
        }
        return new JsonBean(0,"删除失败",null);
    }







    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description 通过报告Id查询报告详情
    * @Date 2022/8/18
    * @param id
    * @param type
    * @return com.hbfk.util.JsonBean
    * @url:
    **/
    @Override
    public JsonBean reportDetail(String id, String type) {
       String identifier="";
        if (StringUtils.isNotEmpty(type)&&"nbsj".equals(type.trim())) {
            identifier=activityPluginsService.getoNState(ProcessEnum.SJ_SJBG.getValue());
        }
        Map<String,Object> map=new HashMap<>();
        if(id!=null){
            Report report = this.getById(id);
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
            if(report!=null){
        	//对灵活字段中的姓名名称及机构名称赋值
			fieldOrgStaffId item=new fieldOrgStaffId();
			BeanUtils.copyProperties(report,item); 
			fieldOrgStaffName nameEntity=ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity,report); 
            }
            List<Attachment> attachmentList= attachmentService.getByReportId(id);
            String mbtype = "";
            if (report.getReportfile() != null && !"".equals(report.getReportfile())) {
                mbtype = "can_update";
            }
            map.put("mbtype", mbtype);
            map.put("report",report);
            map.put("attachmentList",attachmentList);
        }
        map.put("type",type);
        map.put("identifier",identifier);
        return new JsonBean(1,"",map);
    }









//    /*
//    * @author zuoshun
//    * @version v1.0.1
//    * @Description 通过名字和日期判断是否报告记录已存在
//    * @Date 2022/8/18
//    * @param reportname
//    * @param startdate
//    * @return boolean
//    * @url:
//    **/
//    private boolean search(String reportname, String startdate) throws ParseException {
//        QueryWrapper<Report> queryWrapper=new QueryWrapper<>();
//        if (StringUtils.isNotBlank(reportname)){
//            queryWrapper.eq("REPORTNAME",reportname);
//        }
//        if (StringUtils.isNotBlank(startdate)){
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            queryWrapper.eq("REPORTTIME",formatter.parse(startdate));
//        }
//       return this.count(queryWrapper)>0;
//    }






    @Override
    public boolean add(Report report , String  attids) {
        //保存 Report 数据
    	reportMapper.insert(report);
        if (StringUtils.isNotBlank(attids)) {
            String[] ids = attids.split(",");
            for (int i = 0; i < ids.length; i++) {
                baseMapper.insertReportAtt(report.getReportid(), new BigDecimal(ids[i]));
            }
        }
        return true;
    }

    @Transactional
    @Override
    public boolean updateReport(Report report, String attids) {
        //更新 Report 数据
    	reportMapper.updateById(report);
        if (StringUtils.isNotBlank(attids)) {
            baseMapper.deleteReportAtt(report.getReportid());
            String[] ids = attids.split(",");
            for (int i = 0; i < ids.length; i++) {
                baseMapper.insertReportAtt(report.getReportid(), new BigDecimal(ids[i]));
            }
        }
        return true;
    }
    @Transactional
    @Override
    public boolean findbyId(String id) {
        Report report = getById(id);
        String mbtype = "";
        if (StringUtils.isNotBlank(report.getReportfile())) {
            mbtype = "can_update";
        }else{

        }
        return true;
    }
    /**
     * 总公司查看分公司上报的信息
     * @param find
     * @param type
     * @param projectId
     * @param view
     * @param pageNumber
     * @param pageSize
     * @param token
     * @return
     */
    @Override
    public JsonBean companyReportList(Find find, String type, String projectId, String view, Integer pageNumber, Integer pageSize, String token) throws Exception {
        Map<String,Object> map=new HashMap<>();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Integer authorityType;
        if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())) {
            authorityType = 1;
        } else {
            authorityType = 0;
        }
        String identifier = "";
        if(StringUtils.isNotBlank(type)){
            if("nbsj".equals(type.trim())||"nbsj_gzt".equals(type.trim())){
                identifier=activityPluginsService.getoNState(ProcessEnum.SJ_SJBG.name());
                log.info("{}",identifier);
                NbsjProject nbsjProject= nbsjProjectService.getSelectProject(selectOrg.getOrgid());
                if (projectId!=null&&projectId.length()>0){
                    nbsjProject= nbsjProjectService.getById(projectId);
                }
                log.info("---------{}",identifier);
                if (nbsjProject!=null){
                    find.setId(nbsjProject.getProjectid());
                }
            }else if("nbsj_zdy".equals(type.trim())){
                NbsjProject nbsjProject= nbsjProjectService.getSelectProject(selectOrg.getOrgid());
                if (projectId!=null&&projectId.length()>0){
                    nbsjProject= nbsjProjectService.getById(projectId);
                }
                if (nbsjProject!=null){
                    find.setId(nbsjProject.getProjectid());
                }
            }
        }
        BigDecimal orgid=null;
        if("fx_zdy".equals(type.trim())){
        	orgid=null; //风险报告台账是给总公司看的，需要查询子公司数据，所以这个地方不能指定orgid
        }else{
        	orgid=selectOrg.getOrgid();
        }
        if(StringUtils.isNotBlank(find.getCompanyname()) && find.getCompanyname().equals("公司本部")){
        	find.setCompanyname(staffUtil.getLinkOrg().getOrgname());
        }
        PageInfo<Report> page = findAll2(find, type, projectId, view, pageNumber,orgid , pageSize,staffUtil.getStaffid(),authorityType,staffUtil);
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtils.isNotEmpty(page.getList())){ 
			page.getList().forEach(entity->{
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(entity,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,entity ); 
					item=null; // 处理并解除引用
					nameEntity=null; // 处理并解除引用
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			} );
		}
        
        map.put("pageNo",pageNumber);
        map.put("identifier",identifier);
        map.put("type",type);
        map.put("page",page);
        map.put("view",find.getView());
        map.put("projectId",projectId);
        return new JsonBean(1,"操作成功",map);
    }

    private PageInfo<Report> findAll2(Find find, String type, String projectId, String view, Integer pageNumber, BigDecimal orgid, Integer pageSize, BigDecimal staffid, Integer authorityType, TblStaffUtil loginStaff) throws Exception {
    	//reportid,reportname,reporttime,reporttype,reportmode,reporter,reportdepartment,reporttempid,reportstatus,reportfile,memo,type,projectid,orgid,yjdes,fhstaffid,zqyjstaffid,reportcode,sendtime,fhstaffname,reporterid,reportdepartmentid,zqyjstaffname,reportlevel,status,TOREPORTDATE,reportsubstatus,SECRECTLEVELID,STAFFSCOPEIDS,STAFFSCOPENAMES,LINKDEPTID,CREATESTAFFID,CREATETIME
    	StringBuffer sqlSb = new StringBuffer("SELECT 	r.*,o.orgname as linkorgname FROM TBL_REPORT r");
    	sqlSb.append(" left join tbl_organization o on o.orgid=r.orgid ");
    	sqlSb.append(" WHERE r.STATUS = 6 AND r.REPORTSUBSTATUS = 1");
    	
        if (StringUtils.isNotBlank(type)){
        	sqlSb.append(" AND type = '").append(type).append("'");
        }
        if(orgid!=null){
        	sqlSb.append(" AND o.orgid = '").append(orgid).append("'");
        }
        //首页穿透查询公司数据
        if(StringUtils.isNotBlank(find.getCompanyname())){
        	sqlSb.append(" AND o.orgname like '%").append(find.getCompanyname()).append("%'");
        }
        if (StringUtils.isNotBlank(find.getName())) {
        	sqlSb.append(" AND REPORTNAME = '").append(find.getName()).append("'");
        }
        if (StringUtils.isNotBlank(find.getStartDate())) {
        	sqlSb.append(" AND REPORTTIME >= ").append(DataBaseSqlConfig.getDateStrFormat(find.getStartDate()));
        }
        if (StringUtils.isNotBlank(find.getEndDate())) {
        	sqlSb.append(" AND REPORTTIME <= ").append(DataBaseSqlConfig.getDateStrFormat(find.getEndDate()));
        }
    	 
        if(null!=find.getId()){
        	sqlSb.append(" AND projectId = '").append(find.getId()).append("'");
        }else {
            if (StringUtils.isNotEmpty(type) && type.length() > 2) { type = type.substring(0, 2);}
            if(type.equals("sj")){
                return new PageInfo();
            }
        }
        
        
        sqlSb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), 
        		false,
        		"r.ORGID", 
        		"LINKDEPTID", 
        		"CREATESTAFFID", 
        		"SECRECTLEVELID",
        		"STAFFSCOPEIDS", 
        		loginStaff.getStaffid(), 
        		loginStaff.getDeptIds(), 
        		loginStaff.getSecrectScopeIds()));
        
        sqlSb.append(" ORDER BY REPORTID DESC");
        
        String sql = sqlSb.toString();
        com.github.pagehelper.PageInfo<Report> pageInfo=pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> reportMapper.selectListByPageInfo(sql));
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtils.isNotEmpty(pageInfo.getList())){
			pageInfo.getList().forEach(entity->{
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(entity,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,entity ); 
					item=null; // 处理并解除引用
					nameEntity=null; // 处理并解除引用
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			} );
		}
        return pageInfo;
    }


    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description 进行分页查询
    * @Date 2022/8/9
    * @param find
    * @param type
    * @param projectId
    * @param view
    * @param pageNumber
    * @param orgid
    * @param pageSize
    * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.huabo.fxgl.entity.Report>
    * @url:
    **/
    private PageInfo<Report> findAll(Find find, String type, String projectId, String view, Integer pageNumber, BigDecimal orgid,Integer pageSize,BigDecimal staffid,Integer authorityType,String sql){
    	com.github.pagehelper.PageInfo<Report> pageInfo=null;
    	try {
    	QueryWrapper<Report> queryWrapper=new QueryWrapper<>();
        
        queryWrapper.select(Report.class, info ->!info.getColumn().equals("REPDESC"));//查询指定某字段以外的数据
        
        if (StringUtils.isNotBlank(type)){
            queryWrapper.eq("type",type);
        }
        if(orgid!=null){
            queryWrapper.eq("orgid",orgid);
        }
        if (StringUtils.isNotBlank(find.getName())) {
         queryWrapper.like("REPORTNAME",find.getName());
        }
        if (StringUtils.isNotBlank(find.getTitle())) {
            queryWrapper.like("REPORTTYPE",find.getTitle());
           }
        if (StringUtils.isNotBlank(find.getStartDate())) {
            queryWrapper.apply("REPORTTIME  >= to_date('"+find.getStartDate()+"','yyyy-MM-dd')");
        }
        if (StringUtils.isNotBlank(find.getEndDate())) {
            queryWrapper.apply( "REPORTTIME  <= to_date('"+find.getEndDate()+"','yyyy-MM-dd')");
        }
        
        if(find.getDeptid()!=null){
            queryWrapper.eq("LINKDEPTID",find.getDeptid());
        }

        if(null!=find.getId()){
            queryWrapper.eq("projectId",find.getId());
        }else {
            if (StringUtils.isNotEmpty(type) && type.length() > 2) { type = type.substring(0, 2);}
            if(type.equals("sj")){
                 return new PageInfo();
            }
        }
        queryWrapper.orderByDesc("REPORTID");
      pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> reportMapper.selectAllList(queryWrapper,sql));
//       FiexibleNameAssignment ment=new FiexibleNameAssignment();
//		if(CollectionUtils.isNotEmpty(pageInfo.getList())){
//			pageInfo.getList().forEach(entity->{
//				try {
//					//对灵活字段中的姓名名称及机构名称赋值
//					fieldOrgStaffId item=new fieldOrgStaffId();
//					BeanUtils.copyProperties(entity,item); 
//					fieldOrgStaffName nameEntity=ment.setOpenName(item);
//					BeanUtils.copyProperties(nameEntity,entity ); 
//					item=null; // 处理并解除引用
//					nameEntity=null; // 处理并解除引用
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//			} );
//		}
       } catch (Exception e) {
   		// TODO: handle exception
    	   e.printStackTrace();
   	}
         return pageInfo;
    }
    
    @Override
    public JsonBean reportToLeader(String token,BigDecimal risevid) throws Exception {
    	Report report = this.getById(risevid);
        if(!java.util.Objects.isNull(report.getStatus())&&report.getStatus()!=null&&report.getStatus().compareTo(new BigDecimal(6))!=0){ //判断是否已经审批完成  此处需要审批完成的状态值
        	return ResponseFormat.retParam(0, 5001, "未审批完成,不能进行上报！");
        }
        if(!Objects.isNull(report.getReportsubstatus())&&report.getReportsubstatus().equals("1")){ // 
        	return ResponseFormat.retParam(0, 5001, "请勿重复上报！");
        }
        report.setReportsubstatus("1"); //修改上报状态为已经上报
        report.setToreportdate(new Date());
        reportMapper.updateById(report);
        JsonBean jsonBean = new JsonBean(1, "success", "操作成功");
        return jsonBean;
 }




	@Override
	public Map<String, Object> getRiskReportTypeCountByCompany(String token, String company) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> result=new HashMap<String,Object>(); 
		final TblStaffUtil tblStaffUtil = userProvider.get();
		try {
			if(StringUtils.isBlank(company)&&tblStaffUtil.getLinkOrg()!=null){
				company=tblStaffUtil.getLinkOrg().getOrgid().toString();
			}
			List<Map<String, Object>> map=reportMapper.getRiskReportTypeCountByCompany(company);
			result.put("data", map);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

}
