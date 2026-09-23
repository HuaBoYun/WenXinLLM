package com.huabo.fxgl.service.impl;

import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.user.UserProvider;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.GeneralEntity;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.*;
import com.huabo.fxgl.mapper.OpenQueryMapperSqlConfig;
import com.huabo.fxgl.mapper.RiskcategoryMapper;
import com.huabo.fxgl.mapper.RiskeventMapper;
import com.huabo.fxgl.service.*;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Slf4j
@Service
public class RiskeventServiceImpl extends ServiceImpl<RiskeventMapper, Riskevent> implements IRiskeventService {
    @Autowired
    private RiskeventMapper riskeventMapper;
    @Autowired
    private IRiskRiskeventService riskriskeventService;

    public List<Riskevent> findRiskeventByRiskId(String riskid){
        return riskeventMapper.selectRiskeventByRiskId(riskid);
    }

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private RiskcategoryServiceImpl riskCategoryService;

    @Autowired
    private RiskcategoryMapper riskcategoryMapper;
    
    @Resource
    private UserProvider userProvider;

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 获取风险类型树型的结构
     * @Date 2022/8/2
     * @param orgid
     * @param organization
     * @param treeName
     * @return com.hbfk.util.JsonBean
     * @url:
     **/
    @Override
    public JsonBean riskQueryLeft(String orgid, String token, String treeName) throws Exception {
        final TblStaffUtil tblStaffUtil = userProvider.get();
        final TblOrganizationUtil linkOrg = tblStaffUtil.getLinkOrg();
        final TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();//用户选择的组织
        if (StringUtils.isEmpty(orgid)) {
            orgid = currentOrg.getOrgid().toString();
        }
        final Organization organization1 = organizationService.getById(orgid);
        if (organization1!=null){
            orgid=organization1.getOrgid().toString();
            treeName=organization1.getOrgname();
        }
        final List<Riskcategory> tree = riskCategoryService.getRiskCateTreeByOrgId(orgid, "FXSJK", null);
        Map<String,Object> map=new HashMap<String,Object>(0);
        map.put("treeName",treeName);
        map.put("tree",tree);
        map.put("targetFrame","mainFramex");
        return ResponseFormat.retParam(1, 200, map);
    }

	@Value("${application.administrators:}")
	private String administrators;

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 风险事件库进行查询
     * @Date 2022/8/3
     * @param riskcatid
     * @param orgid
     * @param find
     * @param pageNumber
     * @param hbOrgEntity
     * @param ty
     * @return com.hbfk.util.JsonBean
     * @url:
     **/
    @Override
    public JsonBean disposalManage(String riskcatid, String orgid, Find find, Integer pageNumber, Integer pageSize, String token, String choiceSearch, String ty) throws Exception {
        final TblStaffUtil tblStaffUtil = userProvider.get();
        final TblOrganizationUtil linkOrg = tblStaffUtil.getLinkOrg();
        final TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();//用户选择的组织
        String organizationId = currentOrg.getOrgid().toString();
		Integer authorityType;
		if (JudgeRoleRight.judgeRoleRight(administrators, tblStaffUtil.getRoleNames())) {
			authorityType = 1;
		} else {
			authorityType = 0;
		}
        if (StringUtils.isNotBlank(ty) && ty.equals("hy")) {
            List<Organization> list = organizationService.list();
            if (list!=null&&list.size()>0){
                Organization org1 =list.get(0);
                organizationId=org1.getOrgid().toString();
            }
        }
        /*如果等于空，说明是没有具体查找*/
        if (StringUtils.isEmpty(riskcatid)) {
            Riskcategory cat = riskCategoryService.findQYFXByOrgid(organizationId,StringUtils.isNotBlank(ty) && ty.equals("hy")?"hyfx-shijian-k":"FXSJK");
            if (cat != null) {
                riskcatid = cat.getRiskcatid().toString();
            }
        }
        final Riskcategory riskcategory1 = new Riskcategory();
        riskcategory1.setRiskcatid(new BigDecimal(riskcatid));
        //获取所有子节点Id
    //    List<BigDecimal> childNode = riskCategoryService.findRiskcatidByChildNode(riskcategory1);
//      by 20240314  改为java递归处理（多数据融合）start
        List<Riskcategory> list = riskcategoryMapper.selectList(new QueryWrapper<>());
        String finalRiskcatid = riskcatid;
        List<BigDecimal> childNode = list.stream().filter(o -> new BigDecimal(finalRiskcatid).equals(o.getFatherriskcatid())).map(Riskcategory::getRiskcatid).collect(Collectors.toList());
        if (!childNode.isEmpty()){
            List<Riskcategory> subDepartments = new ArrayList<>();
            List<BigDecimal> bigDecimals = getSubDepartments(new BigDecimal(finalRiskcatid), list,subDepartments).stream().map(Riskcategory::getRiskcatid).collect(Collectors.toList());
            childNode.addAll(bigDecimals);
        }
//        end
        IPage<Riskevent> page=findRiskEventByRiskCatid(find, childNode,pageNumber,pageSize,tblStaffUtil.getStaffid(),authorityType);
        String riskcategoryName = null;
        Riskcategory riskcategory = riskCategoryService.getById(riskcatid);
        if (riskcategory!=null){
            riskcategoryName=riskCategoryService.findRiskcatByName(riskcategory);
        }
        Map<String,Object> map=new HashMap<>();
        map.put("choiceSearch",choiceSearch);
        map.put("riskcatName",riskcategoryName );
        map.put("find",find);
        map.put("page",page);
        map.put("riskcatid",riskcatid);
        JsonBean jsonBean = new JsonBean(1, "success", map);
//        jsonBean.setData(map);
        return jsonBean;
    }

    public List<Riskcategory> getSubDepartments(BigDecimal parentId, List<Riskcategory> allList,List<Riskcategory> subDepartments) {
        for (Riskcategory risk : allList) {
            if (risk.getFatherriskcatid().compareTo(parentId) == 0) {
                subDepartments.add(risk);
                
                List<Riskcategory> listSub = riskcategoryMapper.findSubRiskCateById(risk.getRiskcatid());
                
                // 递归查询子部门的子部门
                subDepartments.addAll(getSubDepartments2(risk.getRiskcatid(), listSub,subDepartments));
            }
        }
        return subDepartments;
    }
    
    public List<Riskcategory> getSubDepartments2(BigDecimal parentId, List<Riskcategory> allList,List<Riskcategory> subDepartments) {
        for (Riskcategory risk : allList) {
            if (risk.getFatherriskcatid().compareTo(parentId) == 0) {
                subDepartments.add(risk);
            }
        }
        return subDepartments;
    }

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private IRiskRiskeventService riskRiskeventService;

    @Autowired
    private IRiskService riskService;

    @Autowired
    private IFlowBussinessService flowBussinessService;

    @Autowired
    private IRiskeventOuterruleService riskeventOuterruleService;

    @Autowired
    private IRiskeventInnerruleService riskeventInnerruleService;

    @Autowired
    private IRiskeventMatrixService riskeventMatrixService;
    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 风险事件的详细信息
     * @Date 2022/8/10
     * @param eventid
     * @return com.hbfk.util.JsonBean
     * @url:
     **/
    @Override
    public JsonBean riskEventDetail(String eventid) {
        HashMap<String, Object> map = new HashMap<>();
        if(StringUtils.isNotBlank(eventid)){
//            Riskevent riskevent = this.getById(eventid); //风险事件查询
            QueryWrapper<Riskevent> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("riseveid",eventid);
            Riskevent riskevent = this.getOne(queryWrapper);
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
        	//对灵活字段中的姓名名称及机构名称赋值
			fieldOrgStaffId item=new fieldOrgStaffId();
			BeanUtils.copyProperties(riskevent,item); 
			fieldOrgStaffName nameEntity=ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity,riskevent ); 
            map.put("riskevent",riskevent);
            /*事件的相关属性*/
            List<Attachment> attachments= attachmentService.getByEventId(eventid);
            map.put("attachments",attachments);
            /*通过事件ID查询事件*/
            List<Risk> riskList= riskService.getByEventId(eventid);
            if (riskList!=null&&riskList.size()>0){/*使用riskId查询FlowBussiness*/
                List<BigDecimal> riskIdslist = riskList.stream().map(Risk::getRiskid).collect(Collectors.toList());
                List<FlowBussiness> flowBussinessList= flowBussinessService.getByRiskIds(riskIdslist);
                map.put("FlowBussiness",flowBussinessList);
            }
			if(CollectionUtils.isNotEmpty(riskList)){
				riskList.forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item1=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item1); 
						fieldOrgStaffName nameEntity1=ment.setOpenName(item1);
						BeanUtils.copyProperties(nameEntity1,entity ); 
						item1=null; // 处理并解除引用
						nameEntity1=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
            
            List<RiskeventMatrix> matrixList=riskeventMatrixService.getByEventId(eventid);
            List<RiskeventInnerrule> riskeventInnerrules=riskeventInnerruleService.getByEventId(eventid);
            List<RiskeventOuterrule> riskeventOuterrules=riskeventOuterruleService.getByEventId(eventid);
            map.put("riskList",riskList);
            map.put("RiskeventMatrix",matrixList);
            map.put("RiskeventInnerrule",riskeventInnerrules);
            map.put("RiskeventOuterrule",riskeventOuterrules);
        }
        return new JsonBean(1,"操作成功",map);
    }

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过查询条件find、所有分类子节点的id,进行风险事件的查询
     * @Date 2022/8/3
     * @param find
     * @param childNodeIds
     * @param pageNumber
     * @param pageSize
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.huabo.fxgl.entity.RiskRiskevent>
     * @url:
     **/
    private IPage<Riskevent> findRiskEventByRiskCatid(Find find, List<BigDecimal> childNodeIds, Integer pageNumber, int pageSize,BigDecimal staffid,Integer authorityType) {
        Page<Riskevent> page = new Page<>(pageNumber,pageSize);
        QueryWrapper<Riskevent> queryWrapper = new QueryWrapper<>();
        if (childNodeIds!=null && childNodeIds.size()>0){
            queryWrapper.in("RISKCATID",childNodeIds);
        }
        if (StringUtils.isNotEmpty(find.getCode())){
            queryWrapper.like("RISKEVENTCODE",find.getCode());
        }
        if (StringUtils.isNotEmpty(find.getName())){
            queryWrapper.like("RISKEVENTNAME",find.getName());
        }
        //TODO比较时间
        if (StringUtils.isNotEmpty(find.getStartDate())){
//            queryWrapper.apply(" OCCUREDDATE >= to_date('"+find.getStartDate()+"','yyyy-MM-dd')");
            queryWrapper.ge("OCCUREDDATE", find.getStartDate());
        }
        if (StringUtils.isNotEmpty(find.getEndDate())){
//            queryWrapper.apply(" OCCUREDDATE <= to_date('"+find.getEndDate()+"','yyyy-MM-dd')");
            queryWrapper.le("OCCUREDDATE", find.getEndDate());
        }
		if (Objects.equals(authorityType,0)){
			queryWrapper.eq("RISKFACTOR1", staffid);
		}

        queryWrapper.orderByDesc("RISEVEID");
       return this.page(page,queryWrapper);
    }



	@Override
	public Map<String, Object> riskevent_add(Riskevent risk, String attid) throws Exception {
		 Map<String,Object> result = new HashMap<String,Object>(0);
		if (risk != null && risk.getRiseveid() != null) {
			Riskevent findById = riskeventMapper.selectById(risk.getRiseveid());
			riskeventMapper.updateById(risk);
            riskeventMapper.deleteAttchMent(risk.getRiseveid().intValue());
		}else{
			risk.setCreatedate(new Date());
			riskeventMapper.insert(risk);
		}
		Attachment attachment=null;
		if(StringUtils.isNotEmpty(attid)){
			String[] ids=attid.split(",");
			for(String i:ids){
				attachment = attachmentService.getAttByAttid(i);
				if(attachment!=null){
					riskeventMapper.saveAttchMent(i,risk.getRiseveid());
				}
			}
		}
		result.put("riskid", risk.getRiseveid());
		result.put("code", 0);
		result.put("result", "操作成功!");
		return result;
	}

    /*@Override
    public JsonBean riskQueryLeft(String orgid, Organization organization, String treeName) {
        if (StringUtils.isEmpty(orgid)) {
            orgid = organization.getOrgid().toString();
        }
        final Organization organization1 = organizationService.getById(orgid);
        if (organization1!=null){
            orgid=organization1.getOrgid().toString();
            treeName=organization1.getOrgname();
        }
        final List<Riskcategory> tree = riskCategoryService.getRiskCateTreeByOrgId(orgid, "FXSJK", null);
        Map<String,Object> map=new HashMap<>();
        map.put("treeName",treeName);
        map.put("tree",tree);
        map.put("targetFrame","mainFramex");
        return new JsonBean(1,"操作成功",map);
    }*/
	
	@Override
	public JsonBean get_risksj_no(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        //拼接编号
        String yearStr = String.valueOf(DateUtil.thisYear());
        String riskeventcode = "风险事件-"+yearStr+"-";
        Integer maxno = this.riskeventMapper.get_risksj_no("'"+riskeventcode+"%'");
        if(null == maxno) {
        	maxno = 0;
        }
        maxno = maxno+1;
        riskeventcode = riskeventcode+maxno;
        
		return ResponseFormat.retParam(1, 200, riskeventcode);
	}
	

	@Override
	public JsonBean getMaxVersion(String token,BigDecimal id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //获取最大版本号
		Riskevent event = riskeventMapper.selectById(id);
		String riskeventcode=event.getRiskeventcode();
        Integer maxno = this.riskeventMapper.getMaxVersion("'"+riskeventcode+"%'");
        if(null == maxno) {
        	maxno = 0;
        }
        maxno = maxno+1;
		return ResponseFormat.retParam(1, 200, maxno);
	}
	 

	 @Override
	    public JsonBean disposalManageZh(String riskcatid, String orgid, Find find, Integer pageNumber, Integer pageSize, String token, String choiceSearch, String ty) throws Exception {
	        final TblStaffUtil tblStaffUtil = userProvider.get();
	        final TblOrganizationUtil linkOrg = tblStaffUtil.getLinkOrg();
	        final TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();//用户选择的组织
	        String organizationId = currentOrg.getOrgid().toString();
			Integer authorityType;
			Map<String,Object> map=new HashMap<>();
			try {
		
			if (JudgeRoleRight.judgeRoleRight(administrators, tblStaffUtil.getRoleNames())) {
				authorityType = 1;
			} else {
				authorityType = 0;
			}
	        if (StringUtils.isNotBlank(ty) && ty.equals("hy")) {
	            List<Organization> list = organizationService.list();
	            if (list!=null&&list.size()>0){
	                Organization org1 =list.get(0);
	                organizationId=org1.getOrgid().toString();
	            }
	        }
	        /*如果等于空，说明是没有具体查找*/
	        if (StringUtils.isEmpty(riskcatid)) {
	            Riskcategory cat = riskCategoryService.findQYFXByOrgid(organizationId,StringUtils.isNotBlank(ty) && ty.equals("hy")?"hyfx-shijian-k":"FXSJK");
	            if (cat != null) {
	                riskcatid = cat.getRiskcatid().toString();
	            }
	        }
	        final Riskcategory riskcategory1 = new Riskcategory();
	        riskcategory1.setRiskcatid(new BigDecimal(riskcatid));
	        //获取所有子节点Id
	    //    List<BigDecimal> childNode = riskCategoryService.findRiskcatidByChildNode(riskcategory1);
//	      by 20240314  改为java递归处理（多数据融合）start
	        List<Riskcategory> list = riskcategoryMapper.selectList(new QueryWrapper<Riskcategory>().eq("UNIT", currentOrg.getOrgid()));
	        String finalRiskcatid = riskcatid;
	        List<BigDecimal> childNode = list.stream().filter(o -> new BigDecimal(finalRiskcatid).equals(o.getFatherriskcatid())).map(Riskcategory::getRiskcatid).collect(Collectors.toList());
	        childNode.add(new BigDecimal(finalRiskcatid));
	        if (!childNode.isEmpty()){
	            List<Riskcategory> subDepartments = new ArrayList<>();
	            List<BigDecimal> bigDecimals = getSubDepartments(new BigDecimal(finalRiskcatid), list,subDepartments).stream().distinct().map(Riskcategory::getRiskcatid).collect(Collectors.toList());
	            childNode.addAll(bigDecimals);
	        }
	        List<BigDecimal> childNodes = childNode.stream().distinct().collect(Collectors.toList());
//	        end
	        //子公司看到自己公司的数据  总公司看到已经上报的数据
	        Riskevent event=new Riskevent();
	        event.setRiskeventcode(find.getCode());
	        event.setRiskeventname(find.getName());
	        event.setStartDate(find.getStartDate());
	        event.setEndDate(find.getEndDate());
	        event.setLosseventcategory(find.getType());
			if (Objects.equals(authorityType,0)){
		        event.setRiskfactor1(tblStaffUtil.getStaffid().toString());
			} 
			if (find.getOrgid() == null) {
				event.setUnit(organizationId);
			} else {
				event.setUnit(find.getOrgid().toString());
			}
			
	        PageInfo<Riskevent> page=null;
//	        if(linkOrg!=null&&linkOrg.getOrgid().toString().equals("1000")){ //总公司
//	        	page=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->riskeventMapper.getRiskeventPageInfoHead(event,childNode));
//	        }else{
	        //authorityType == 0
	       // String sql = GeneralSQLConcatConfig.concatSecrectSql(tblStaffUtil.getCurrentOrg().getUseSecrect(), authorityType == 0, "UNIT", "LINKDEPTID", "RISKFACTOR1", "SECRECTLEVELID", "STAFFSCOPEIDS", tblStaffUtil.getStaffid(), tblStaffUtil.getDeptIds(), tblStaffUtil.getSecrectScopeIds());
	        String sql = GeneralSQLConcatConfig.concatSecrectSqlEntity(new GeneralEntity(tblStaffUtil.getCurrentOrg().getUseSecrect(), authorityType == 0, "UNIT", "LINKDEPTID", "RISKFACTOR1", "SECRECTLEVELID", "STAFFSCOPEIDS", tblStaffUtil.getStaffid(), tblStaffUtil.getDeptIds(), tblStaffUtil.getSecrectScopeIds(),authorityType));

	        	page=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->riskeventMapper.getRiskeventPageInfo(event,childNodes,sql));
	        //}
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
	        String riskcategoryName = null;
	        Riskcategory riskcategory = riskCategoryService.getById(riskcatid);
	        if (riskcategory!=null){
	            riskcategoryName=riskCategoryService.findRiskcatByName(riskcategory);
	        }
	        map.put("riskcatName",riskcategoryName );
	        map.put("page",page);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("重大风险汇总异常");
			}
	        map.put("choiceSearch",choiceSearch);
	        map.put("find",find);
	        map.put("riskcatid",riskcatid);
	        JsonBean jsonBean = new JsonBean(1, "success", map);
//	        jsonBean.setData(map);
			
	        return jsonBean;
			
	 }
	 
	 @Override
    public  List<Riskevent> disposalManageZhExport(String ids,String riskcatid, String orgid, Find find, String token, String choiceSearch, String ty) throws Exception {
	 List<Riskevent> eventList=null;  
	 final TblStaffUtil tblStaffUtil = userProvider.get();
        final TblOrganizationUtil linkOrg = tblStaffUtil.getLinkOrg();
        final TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();//用户选择的组织
        String organizationId = currentOrg.getOrgid().toString();
		Integer authorityType;
		try {
		if (JudgeRoleRight.judgeRoleRight(administrators, tblStaffUtil.getRoleNames())) {
			authorityType = 1;
		} else {
			authorityType = 0;
		}
        if (StringUtils.isNotBlank(ty) && ty.equals("hy")) {
            List<Organization> list = organizationService.list();
            if (list!=null&&list.size()>0){
                Organization org1 =list.get(0);
                organizationId=org1.getOrgid().toString();
            }
        }
        /*如果等于空，说明是没有具体查找*/
        if (StringUtils.isEmpty(riskcatid)) {
            Riskcategory cat = riskCategoryService.findQYFXByOrgid(organizationId,StringUtils.isNotBlank(ty) && ty.equals("hy")?"hyfx-shijian-k":"FXSJK");
            if (cat != null) {
                riskcatid = cat.getRiskcatid().toString();
            }
        }
        final Riskcategory riskcategory1 = new Riskcategory();
        riskcategory1.setRiskcatid(new BigDecimal(riskcatid));
        List<Riskcategory> list = riskcategoryMapper.selectList(new QueryWrapper<Riskcategory>().eq("UNIT", currentOrg.getOrgid()));
        String finalRiskcatid = riskcatid;
        List<BigDecimal> childNode = list.stream().filter(o -> new BigDecimal(finalRiskcatid).equals(o.getFatherriskcatid())).map(Riskcategory::getRiskcatid).collect(Collectors.toList());
        childNode.add(new BigDecimal(finalRiskcatid));
        if (!childNode.isEmpty()){
            List<Riskcategory> subDepartments = new ArrayList<>();
            List<BigDecimal> bigDecimals = getSubDepartments(new BigDecimal(finalRiskcatid), list,subDepartments).stream().distinct().map(Riskcategory::getRiskcatid).collect(Collectors.toList());
            childNode.addAll(bigDecimals);
        }
        List<BigDecimal> childNodes = childNode.stream().distinct().collect(Collectors.toList());
//	        end
        //子公司看到自己公司的数据  总公司看到已经上报的数据
        Riskevent event=new Riskevent();
        event.setRiskeventcode(find.getCode());
        event.setRiskeventname(find.getName());
        event.setStartDate(find.getStartDate());
        event.setEndDate(find.getEndDate());
		if (Objects.equals(authorityType,0)){
	        event.setRiskfactor1(tblStaffUtil.getStaffid().toString());
		} 
		event.setUnit(organizationId);
		event.setIds(ids);
        PageInfo<Riskevent> page=null;
        String sql = GeneralSQLConcatConfig.concatSecrectSqlEntity(new GeneralEntity(tblStaffUtil.getCurrentOrg().getUseSecrect(), authorityType == 0, "UNIT", "LINKDEPTID", "RISKFACTOR1", "SECRECTLEVELID", "STAFFSCOPEIDS", tblStaffUtil.getStaffid(), tblStaffUtil.getDeptIds(), tblStaffUtil.getSecrectScopeIds(),authorityType));
        eventList=riskeventMapper.getRiskeventPageInfo(event,childNodes,sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("风险事件导出异常");
		}
        return eventList;
			
	 }
	 
	 
	 @Override
	    public JsonBean disposalManageZhMain(String riskcatid, String orgid, Find find, Integer pageNumber, Integer pageSize, String token, String ty, String choiceSearch,String losseventcategory) throws Exception {
	        final TblStaffUtil tblStaffUtil = userProvider.get();
	        final TblOrganizationUtil linkOrg = tblStaffUtil.getLinkOrg();
	        final TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();//用户选择的组织
	        String organizationId = currentOrg.getOrgid().toString();
			Integer authorityType;
			if (JudgeRoleRight.judgeRoleRight(administrators, tblStaffUtil.getRoleNames())) {
				authorityType = 1;
			} else {
				authorityType = 0;
			}
	        if (StringUtils.isNotBlank(ty) && ty.equals("hy")) {
	            List<Organization> list = organizationService.list();
	            if (list!=null&&list.size()>0){
	                Organization org1 =list.get(0);
	                organizationId=org1.getOrgid().toString();
	            }
	        }
	        /*如果等于空，说明是没有具体查找*/
	           List<BigDecimal> childNode=new ArrayList<BigDecimal>();
	        if(StringUtils.isNotBlank(riskcatid)){
	 	    if (StringUtils.isEmpty(riskcatid)) {
	            Riskcategory cat = riskCategoryService.findQYFXByOrgid(organizationId,StringUtils.isNotBlank(ty) && ty.equals("hy")?"hyfx-shijian-k":"FXSJK");
	            if (cat != null) {
	                riskcatid = cat.getRiskcatid().toString();
	            }
	        }
	        final Riskcategory riskcategory1 = new Riskcategory();
	        riskcategory1.setRiskcatid(new BigDecimal(riskcatid));
	        //获取所有子节点Id
	    //    List<BigDecimal> childNode = riskCategoryService.findRiskcatidByChildNode(riskcategory1);
//	      by 20240314  改为java递归处理（多数据融合）start
	        List<Riskcategory> list = riskcategoryMapper.selectList(new QueryWrapper<>());
	        String finalRiskcatid = riskcatid;
	          childNode = list.stream().filter(o -> new BigDecimal(finalRiskcatid).equals(o.getFatherriskcatid())).map(Riskcategory::getRiskcatid).collect(Collectors.toList());
	        childNode.add(new BigDecimal(finalRiskcatid));
	        if (!childNode.isEmpty()){
	            List<Riskcategory> subDepartments = new ArrayList<>();
	            List<BigDecimal> bigDecimals = getSubDepartments(new BigDecimal(finalRiskcatid), list,subDepartments).stream().map(Riskcategory::getRiskcatid).collect(Collectors.toList());
	            childNode.addAll(bigDecimals);
	        }
	        }
	        final  List<BigDecimal> child=childNode;
//	        end
	        //子公司看到自己公司的数据  总公司看到已经上报的数据
	        Riskevent event=new Riskevent();
	        event.setRiskeventcode(find.getCode());
	        event.setRiskeventname(find.getName());
	        event.setStartDate(find.getStartDate());
	        event.setEndDate(find.getEndDate());
	        event.setUnit(orgid);
	        
	        event.setLosseventcategory(losseventcategory);
			if (Objects.equals(authorityType,0)){
		        event.setRiskfactor1(tblStaffUtil.getStaffid().toString());
			}
			
		    String sql = GeneralSQLConcatConfig.concatSecrectSql(tblStaffUtil.getCurrentOrg().getUseSecrect(), false, "r.UNIT", "r.LINKDEPTID", "r.CREATESTAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", tblStaffUtil.getStaffid(), tblStaffUtil.getDeptIds(), tblStaffUtil.getSecrectScopeIds());
	        PageInfo<Riskevent> page=null;
	        if(StringUtils.isNotBlank(find.getCompanyname())){
	        	event.setUnitname(find.getCompanyname());
	        	page=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->riskeventMapper.getRiskeventPageInfoHead(event,child," and 1=1 "));
	        }else {
	        	 page=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->riskeventMapper.getRiskeventPageInfoHead(event,child,sql));
	        }
	        
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
	         String riskcategoryName = null;
	        Riskcategory riskcategory = riskCategoryService.getById(riskcatid);
	        if (riskcategory!=null){
	            riskcategoryName=riskCategoryService.findRiskcatByName(riskcategory);
	        }
	        Map<String,Object> map=new HashMap<>();
	        map.put("choiceSearch",choiceSearch);
	        map.put("riskcatName",riskcategoryName );
	        map.put("find",find);
	        map.put("page",page);
	        map.put("riskcatid",riskcatid);
	        map.put("orgid",orgid);
	        JsonBean jsonBean = new JsonBean(1, "success", map);
//	        jsonBean.setData(map);
	        return jsonBean;
	 }
	 
	 
	 @Override
	    public JsonBean getViewHistoricalVersions(String token,BigDecimal risevid) throws Exception {
	        final TblStaffUtil tblStaffUtil = userProvider.get();
	        final TblOrganizationUtil linkOrg = tblStaffUtil.getLinkOrg();
	        final TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();//用户选择的组织
	        String organizationId = currentOrg.getOrgid().toString();
			Integer authorityType;
			if (JudgeRoleRight.judgeRoleRight(administrators, tblStaffUtil.getRoleNames())) {
				authorityType = 1;
			} else {
				authorityType = 0;
			}
	        //子公司看到自己公司的数据  总公司看到已经上报的数据
	        Riskevent event = riskeventMapper.selectById(risevid);
	        Riskevent newEvent=new Riskevent();
            newEvent.setRiseveid(risevid);
            newEvent.setInitialfatherriseveid(event.getInitialfatherriseveid());
            newEvent.setRiskeventcode(event.getRiskeventcode());
			if (Objects.equals(authorityType,0)){
				newEvent.setRiskfactor1(tblStaffUtil.getStaffid().toString());
			}
	       List<Riskevent> page=riskeventMapper.getRiskeventHistory(newEvent);
//	        if(linkOrg!=null&&linkOrg.getOrgid().toString().equals("1000")){ //总公司
//	        	page=riskeventMapper.getRiskeventHistoryHead(newEvent);
//	        }else{
	        	//page=riskeventMapper.getRiskeventHistory(newEvent);
	       // }
	        Map<String,Object> map=new HashMap<>();
	        map.put("page",page);
	        JsonBean jsonBean = new JsonBean(1, "success", map);
	        return jsonBean;
	 }
	 
	 
	 @Override
	    public JsonBean getViewHistoricalVersionsMain(String token,BigDecimal risevid) throws Exception {
	        final TblStaffUtil tblStaffUtil = userProvider.get();
	        final TblOrganizationUtil linkOrg = tblStaffUtil.getLinkOrg();
	        final TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();//用户选择的组织
	        String organizationId = currentOrg.getOrgid().toString();
			Integer authorityType;
			if (JudgeRoleRight.judgeRoleRight(administrators, tblStaffUtil.getRoleNames())) {
				authorityType = 1;
			} else {
				authorityType = 0;
			}
	        //子公司看到自己公司的数据  总公司看到已经上报的数据
	        Riskevent event = riskeventMapper.selectById(risevid);
	        Riskevent newEvent=new Riskevent();
         newEvent.setRiseveid(risevid);
         newEvent.setInitialfatherriseveid(event.getInitialfatherriseveid());
         newEvent.setRiskeventcode(event.getRiskeventcode());
			if (Objects.equals(authorityType,0)){
				newEvent.setRiskfactor1(tblStaffUtil.getStaffid().toString());
			}
	       List<Riskevent> page=null;
	        	page=riskeventMapper.getRiskeventHistoryHead(newEvent);
	        	//else{
//	        	page=riskeventMapper.getRiskeventHistory(newEvent);
//	        }
	        Map<String,Object> map=new HashMap<>();
	        map.put("page",page);
	        JsonBean jsonBean = new JsonBean(1, "success", map);
	        return jsonBean;
	 }
	 

	 @Override
	    public JsonBean reportToLeader(String token,BigDecimal risevid) throws Exception {
	        Riskevent event = riskeventMapper.selectById(risevid);
	        if(event.getStatus()!=null&&event.getStatus().compareTo(new BigDecimal(6))!=0){ //判断是否已经审批完成  此处需要审批完成的状态值
	        	return ResponseFormat.retParam(0, 5001, "未审批完成,不能进行上报！");
	        }
	        if(!Objects.isNull(event.getReportStatus())&&event.getReportStatus().equals("1")){ //
	        	return ResponseFormat.retParam(0, 5001, "请勿重复上报！");
	        }
	        event.setReportStatus("1"); //修改上报状态为已经上报
	        event.setToreportdate(new Date());
	        riskeventMapper.updateById(event);
	        JsonBean jsonBean = new JsonBean(1, "success", "操作成功");
	        return jsonBean;
	 }

	@Override
	public JsonBean getRemindList(String token) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> map=new HashMap<String,Object>(0); 
		final TblStaffUtil tblStaffUtil = userProvider.get();
		  try {
			List<Riskevent> event= riskeventMapper.getRemindList(tblStaffUtil.getStaffid());
			 map.put("code",event.size());
			 map.put("list",event);
		} catch (Exception e) {
			// TODO: handle exception
		}
	        return  new JsonBean(1, "success", map);
	}

	//按单位（公司）统计风险事件数量
	@Override
	public   Map<String,Object>  getRiskeventCountByCompany(String token,String company) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object>  result=new HashMap<>();
		try {
			List<Map<String, Object>> list=riskeventMapper.getRiskeventCountByCompany();
			String[] xData=new String[list.size()];
			Long[] yData=new Long[list.size()];
			for(int i=0;i<list.size();i++){
				Map<String, Object> o=list.get(i);
				xData[i]=(String) o.get("NAME");
				yData[i]=(Long) o.get("NUM");
				
//				xData[i]=(String) o.get("NAME");
//				yData[i]=(Long) o.get("NUM");
			}
			result.put("yData", yData);
			result.put("xData", xData);
			result.put("code", 0);
			result.put("result", "操作成功!");
		} catch (Exception e) {
			// TODO: handle exception
		    e.printStackTrace();
		}
		return result;
	}

	@Override
	public Map<String, Object> getRiskLosseventcategory(String token, String company) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> result=new HashMap<String,Object>(); 
			final TblStaffUtil tblStaffUtil = userProvider.get();
		try {
			if(StringUtils.isBlank(company)&&tblStaffUtil.getLinkOrg()!=null){
				company=tblStaffUtil.getLinkOrg().getOrgid().toString();
			}
			List<Map<String, Object>> list=riskeventMapper.getRiskLosseventcategory(company);
			result.put("data", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Map<String, Object> getCompanyRiskEventList(String token, String year) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> map=new HashMap<>();
		
			List<Organization> orgList=riskeventMapper.getEventCompanyList();
		if(orgList!=null){
			List<JSONObject> obj=new ArrayList<JSONObject>();
		    Map<String, int[]> m=new HashMap<>();
		    int[] c1=new int[orgList.size()];
		    int[] c2=new int[orgList.size()];
		    String[] orgName=new String[orgList.size()];
	        for(int i=0;i<orgList.size();i++){
	        	Organization o=orgList.get(i);
	        	int count1=riskeventMapper.queryRisksNumberByType(o.getOrgid(),"1",year);
	        	int count2=riskeventMapper.queryRisksNumberByType(o.getOrgid(),"2",year);
	        	c1[i]=count1;
	        	c2[i]=count2;
	        	orgName[i]=o.getOrgname();
	        }
	        m.put("一般", c1);
	        m.put("重大", c2);
	        map.put("yAxis", m);
	        map.put("xAxis", orgName);
			}
		return map;
	}

}
