package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.service.*;
import com.huabo.audit.util.PageResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.vo.TblNbsjBugVo;
import com.huabo.audit.util.FiexibleNameAssignment;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;
import com.huabo.audit.vo.param.fieldOrgStaffId;
import com.huabo.audit.vo.param.fieldOrgStaffName;

@Service
public class TblNbsjBugServiceImpl implements TblNbsjBugService {

	@Autowired
	private TblNbsjBugMapper tblNbsjBugMapper;

	@Autowired
	private ActivityPluginsService activityPluginsService;
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	private TblNbsjProjectService tblNbsjProjectService;

	@Resource
	private TblNbsjInnerruleMapper tblNbsjInnerruleMapper;

	@Resource
	private TblNbsjOuterruleMapper tblNbsjOuterruleMapper;
	
	
	@Resource
	private TblStaffService staffService;
	
	
	@Resource
	private TblOrganizaService tblOrganizaService;

	@Resource
	private TblNbsjAuditplanService tblNbsjAuditplanService;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public void add(TblNbsjBugEntity tblBug) {
		// TODO Auto-generated method stub

	}

	@Override
	public List findAll(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblNbsjBugEntity> findBySql(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveInnerId(String bugId, String innerId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TblNbsjBugEntity> findByAll(String orgid, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOuterId(String outerId, String innerId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public TblNbsjBugEntity findById(BigDecimal bigDecimal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findByneed(String orgid, String orgtype) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getInnerRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getOuterRules() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void update(TblNbsjBugEntity tblBug) {
		// TODO Auto-generated method stub

	}

	@Override
	public List findByneed(String number, String fstart, String fend, String uname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List search(String plancode, String searchbegintime, String searchendtime, String plantype, String state,
					   String planname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAllReform() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> qxglExport(String orgid, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblNbsjBugEntity findByCode(String code, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblNbsjBugEntity findByCode(String code, String type, String orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblNbsjBugEntity> findByCriterionId(BigDecimal criterionId) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * 缺陷管理-附件列表
	 *
	 * @param token
	 * @param bugId
	 * @return
	 */
	@Override
	public JsonBean defectFileList(String token, BigDecimal bugId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblNbsjBugMapper.defectFileList(bugId);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	//==
	@Override
	public JsonBean bugPageList(String token, Integer pageNumber, Integer pageSize, TblNbsjBugVo tblNbsjBugVo,BigDecimal orgid)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		if (pageNumber == null) {
			pageNumber = 1;
		}
		if (pageSize == null) {
			pageSize = 15;
		}
		
		tblNbsjBugVo.setUseSecrect(loginStaff.getCurrentOrg().getUseSecrect());
		tblNbsjBugVo.setSecrectStaff(loginStaff.getStaffid());
		tblNbsjBugVo.setSecrectScopeIds(loginStaff.getSecrectScopeIds());

		BigDecimal companyid = loginStaff.getCurrentOrg().getOrgid();
		tblNbsjBugVo.setCompanyid(companyid);
		tblNbsjBugVo.setOrgid(orgid);
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		//链表分页  xml 写法
		
		String sql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), true, "TNA.bugdepartment", "TNA.linkdeptid","TNA.CREATESTAFFID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
		
		com.github.pagehelper.PageInfo<TblNbsjBugEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjBugMapper.selectListByPageInfoXml(tblNbsjBugVo,sql));
		FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtil.isNotEmpty(pageInfo.getList())){
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
		
		
		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		PageResult<TblNbsjBugEntity> build = new PageResult<TblNbsjBugEntity>().build(pageInfo);
		/*PageInfo<TblNbsjBugEntity> pageInfo = new PageInfo<TblNbsjBugEntity>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjBugMapper.selectListByPageInfo(pageInfo, tblNbsjBugVo,orgid,companyid));
		pageInfo.setTotalRecord(this.tblNbsjBugMapper.selectCountByPageInfo(pageInfo, tblNbsjBugVo,orgid,companyid));
		pageInfo.getTotalPage();*/
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean dgbugAdd(TblNbsjBugEntity bug, String token, String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
		if(null==bug.getBugdepartment() || "".equals(bug.getBugdepartment())) {
			BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
			bug.setBugdepartment(orgid+"");
			bug.setLinkdeptid(loginStaff.getLinkDetp().getOrgid());
		}

		Integer count = this.tblNbsjBugMapper.selectPlanCodeByOrgid(bug);
		if (count > 0) {
			return ResponseFormat.retParam(0, 202, null);
		}
		
//	  	if(null == bug.getProjectId() ){
//    		//==查询当前实施的项目！
//    		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
//    		if(tnp == null) {
//    			return ResponseFormat.retParam(0,30003,null);
//    		}
//    		BigDecimal projectId = tnp.getProjectId();
//    		if(null == projectId) {
//    			return ResponseFormat.retParam(0,30003,null);
//    		}
//    		bug.setProjectId(projectId);
//    	}
    	
		
//		bug.setCreatestaffid(loginStaff.getStaffid()+"");
//		bug.setDiscovertime(new Date());
		bug.setBugreformstatus("0");
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		BigDecimal bugcriid = bug.getBugcriid();

		if (bug.getBugid() != null) {
			//修改；
//			this.tblNbsjBugMapper.updateByPrimaryKey(bug);
		    TblNbsjBug bugEntity =new TblNbsjBug();
			BeanUtils.copyProperties(bug,bugEntity);
			this.tblNbsjBugMapper.updateById(bugEntity);

			BigDecimal bugid = bug.getBugid();
			this.tblNbsjBugMapper.deleteCriLink(bugid, bugcriid);
			this.tblNbsjBugMapper.deleteCriLinkall(bugid);
			
			//==附件，先删除 再重新添加
			this.tblNbsjBugMapper.deleteAttmentRelationBUG(bug.getBugid());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblNbsjBugMapper.insertAttmentRelationBUG(id, bug.getBugid());
				}
			}
		} else {
			//新增；
			bug.setBugreformstatus("0");
			bug.setBugid(RandomUtil.uuBigDecimalId());
			bug.setCreatestaffid(loginStaff.getStaffid());
			bug.setCreatetime(new Date());
			bug.setUnit(loginStaff.getLinkOrg().getOrgid());
			  TblNbsjBug bugEntity =new TblNbsjBug();
				BeanUtils.copyProperties(bug,bugEntity);
			this.tblNbsjBugMapper.insert(bugEntity);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					this.tblNbsjBugMapper.insertAttmentRelationBUG(id, bug.getBugid());
				}
			}
		}
		
		//缺陷级别
		BigDecimal bugid = bug.getBugid();
		this.tblNbsjBugMapper.insertCriLink(bugid,bugcriid);
		
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("WorkReport", bug);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	
	@Override
	public JsonBean bugAdd(TblNbsjBugEntity bug, String token, String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if(null==bug.getBugdepartment() || "".equals(bug.getBugdepartment())) {
			BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
			bug.setBugdepartment(orgid+"");
		}

		Integer count = this.tblNbsjBugMapper.selectPlanCodeByOrgid(bug);
		if (count > 0) {
			return ResponseFormat.retParam(0, 202, null);
		}
		
//		bug.setCreatestaffid(loginStaff.getStaffid()+"");
//		bug.setDiscovertime(new Date());
		bug.setBugreformstatus("0");
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		BigDecimal bugcriid = bug.getBugcriid();

		if (bug.getBugid() != null) {
			//修改；
			this.tblNbsjBugMapper.updateEntity(bug);

			BigDecimal bugid = bug.getBugid();
			this.tblNbsjBugMapper.deleteCriLink(bugid, bugcriid);
			
			//==附件，先删除 再重新添加
			this.tblNbsjBugMapper.deleteAttmentRelationBUG(bug.getBugid());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblNbsjBugMapper.insertAttmentRelationBUG(id, bug.getBugid());
				}
			}
		} else {
			//新增；
			this.tblNbsjBugMapper.insertEntity(bug);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					this.tblNbsjBugMapper.insertAttmentRelationBUG(id, bug.getBugid());
				}
			}
		}
		
		//缺陷级别
		BigDecimal bugid = bug.getBugid();
		this.tblNbsjBugMapper.insertCriLink(bugid,bugcriid);
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("WorkReport", bug);
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	@Override
	public JsonBean bugDelete(BigDecimal bugid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
//		TblNbsjBugEntity plan = this.tblNbsjBugMapper.selectById(bugid);

		if (bugid == null) {
			return ResponseFormat.retParam(0, 50001, null);
		}

//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjBugMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblNbsjBugMapper.deleteById(bugid);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean findBugDetail(String token, BigDecimal bugid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);

		TblNbsjBugEntity plan = this.tblNbsjBugMapper.selectById(bugid);
		FiexibleNameAssignment ment=new FiexibleNameAssignment();
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(plan,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,plan ); 
					item=null; // 处理并解除引用
					nameEntity=null; // 处理并解除引用
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		resultMap.put("bug", plan);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	/**
	 * 缺陷管理-导出
	 *
	 * @param token
	 * @param orgId
	 * @return
	 */
	@Override
	public JsonBean defect_file_export(String token, BigDecimal orgId,HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			orgId = loginStaff.getCurrentOrg().getOrgid();
		}
		response.setContentType("application/binary;charset=UTF-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			response.setHeader("Content-disposition", "attachment; filename=" + new String("缺陷管理".getBytes(), "iso-8859-1") + ".xlsx");// 组装附件名称和格式
			ServletOutputStream outputStream = response.getOutputStream();
//			List<Object[]> objList1 = new ArrayList<Object[]>();
			List<Object[]> objList = tblNbsjBugMapper.defect_file_export(orgId);
//			for (int i = 0; i < objList.size(); i++) {
//				Object[] o = objList.get(i);
//				Object[] tempob = new Object[9];
//				tempob[0] = o.getProjectName();
//				tempob[1] = o.getSheetCode();
//				tempob[2] = o.getSheetName();
//				tempob[3] = o.getOrgname();
//				tempob[4] = o.getBusinessAffiliation();
//				tempob[5] = o.getApprover();
//				tempob[6] = o.getTargetName();
//				tempob[7] = o.getRiskLevel();
//				tempob[8] = o.getTargetName();
//				tempob[9] = o.getBusinessType();
//
//				objList1.add(tempob);
//			}
			String[] titles = {"缺陷编号", "缺陷级别", "发现日期", "发现人", "是否财务相关", "缺陷性质","组织", "缺陷部门", "是否需要整改", "业务描述", "缺陷描述"};
			ImportOrExportExcelUtil.exportExcel(titles, objList, outputStream, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1, 200, "ImportOrExportExcelUtil.exportExcel(titles, objList1, outputStream, null);");
	}

	@Override
	public JsonBean innerCommonQxwtList(String token, Integer pageNumber, Integer pageSize, BigDecimal bugid)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		if (pageNumber == null) {
			pageNumber = 1;
		}
		if (pageSize == null) {
			pageSize = 15;
		}

		BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);

		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblNbsjInnerrule> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjInnerruleMapper.selectInnerCommonListByPageInfoXml(bugid,orgid));
		PageResult<TblNbsjInnerrule> build = new PageResult<TblNbsjInnerrule>().build(pageInfo);
		/*PageInfo<TblNbsjInnerrule> pageInfo = new PageInfo<TblNbsjInnerrule>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjBugMapper.selectInnerCommonListByPageInfo(pageInfo, bugid,orgid));
		pageInfo.setTotalRecord(this.tblNbsjBugMapper.selectInnerCommonCountByPageInfo(pageInfo, bugid,orgid));
		pageInfo.getTotalPage();*/
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean outerCommonQxwtList(String token, Integer pageNumber, Integer pageSize, BigDecimal bugid)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		if (pageNumber == null) {
			pageNumber = 1;
		}
		if (pageSize == null) {
			pageSize = 15;
		}
		
		BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblNbsjOuterruleEntity> pageInfo = PageMethod.startPage(pageNumber,pageSize)
				.doSelectPageInfo(() -> tblNbsjOuterruleMapper.selectOuterCommonListByPageInfoXml(bugid,orgid));
		PageResult<TblNbsjOuterruleEntity> build = new PageResult<TblNbsjOuterruleEntity>().build(pageInfo);
		/*PageInfo<TblNbsjOuterruleEntity> pageInfo = new PageInfo<TblNbsjOuterruleEntity>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjBugMapper.selectOuterCommonListByPageInfo(pageInfo, bugid,orgid));
		pageInfo.setTotalRecord(this.tblNbsjBugMapper.selectOuterCommonCountByPageInfo(pageInfo, bugid,orgid));
		pageInfo.getTotalPage();*/
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean bugInnrulidsAdd(String token, BigDecimal bugid, String innrulids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if (innrulids != null && !"".equals(innrulids)) {
			String[] innrulidss = innrulids.split(",");
			for (String innrulid : innrulidss) {
				Integer cnt = this.tblNbsjBugMapper.findCountByBugInner(innrulid, bugid);
				if(cnt==0) {
					this.tblNbsjBugMapper.insertBugInnrulids(innrulid, bugid);
				}
			}
		}

		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean bugOutrulidsAdd(String token, BigDecimal bugid, String outrulids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if (outrulids != null && !"".equals(outrulids)) {
			String[] outrulidss = outrulids.split(",");
			for (String outrulid : outrulidss) {
				Integer cnt = this.tblNbsjBugMapper.findCountByBugOuter(outrulid, bugid);
				if(cnt==0) {
					this.tblNbsjBugMapper.insertBugOutrulids(outrulid, bugid);
				}
			}
		}

		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean innerCommonLinkList(String token, Integer pageNumber, Integer pageSize, BigDecimal bugid)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		if (pageNumber == null) {
			pageNumber = 1;
		}
		if (pageSize == null) {
			pageSize = 15;
		}

		BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblNbsjInnerrule> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjInnerruleMapper.selectInnerCommonListByLinkXml(bugid,orgid));
		PageResult<TblNbsjInnerrule> build = new PageResult<TblNbsjInnerrule>().build(pageInfo);

		/*PageInfo<TblNbsjInnerrule> pageInfo = new PageInfo<TblNbsjInnerrule>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjBugMapper.selectInnerCommonListByLink(pageInfo, bugid,orgid));
		pageInfo.setTotalRecord(0);
		pageInfo.getTotalPage();*/
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean outerCommonLinkList(String token, Integer pageNumber, Integer pageSize, BigDecimal bugid)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		if (pageNumber == null) {
			pageNumber = 1;
		}
		if (pageSize == null) {
			pageSize = 15;
		}

		BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblNbsjOuterruleEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjOuterruleMapper.selectOuterCommonListByLinkXml(bugid,orgid));
		PageResult<TblNbsjOuterruleEntity> build = new PageResult<TblNbsjOuterruleEntity>().build(pageInfo);

		/*PageInfo<TblNbsjOuterruleEntity> pageInfo = new PageInfo<TblNbsjOuterruleEntity>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjBugMapper.selectOuterCommonListByLink(pageInfo, bugid,orgid));
		pageInfo.setTotalRecord(0);
		pageInfo.getTotalPage();*/
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean defectQxwtAdd(String token, BigDecimal bugid, String bugids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		if (bugids != null && !"".equals(bugids)) {
			String[] bugidss = bugids.split(",");
			for (String child_bugid : bugidss) {
				Integer cnt = this.tblNbsjBugMapper.findCountByChildBugid(child_bugid, bugid);
				if(cnt==0) {
					this.tblNbsjBugMapper.updateBugFatherBugId(child_bugid, bugid);
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean defectLinkList(String token, Integer pageNumber, Integer pageSize, BigDecimal bugid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		if (pageNumber == null) {
			pageNumber = 1;
		}
		if (pageSize == null) {
			pageSize = 15;
		}
		
//		Integer orgid = loginStaff.getCurrentOrg().getOrgid().intValue();
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);

		/*PageInfo<TblNbsjBugEntity> pageInfo = new PageInfo<TblNbsjBugEntity>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjBugMapper.selectDefectLinkList(pageInfo, bugid));
		pageInfo.setTotalRecord(0);
		pageInfo.getTotalPage();*/
		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblNbsjBugEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjBugMapper.selectDefectLinkListXml(bugid));
		PageResult<TblNbsjBugEntity> build = new PageResult<TblNbsjBugEntity>().build(pageInfo);
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean bugInnrulidsDelete(String token, BigDecimal bugid, String innrulids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if (innrulids != null && !"".equals(innrulids)) {
			String[] innrulidss = innrulids.split(",");
			for (String innrulid : innrulidss) {
				this.tblNbsjBugMapper.deleteInnerLinkById(bugid, innrulids);
			}
		}

		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean bugOutrulidsDelete(String token, BigDecimal bugid, String outrulids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if (outrulids != null && !"".equals(outrulids)) {
			String[] outrulidss = outrulids.split(",");
			for (String outrulid : outrulidss) {
				this.tblNbsjBugMapper.deleteOuterLinkById(bugid, outrulid);
			}
		}

		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean bugFatherDelete(String token, BigDecimal bugid, String bugids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if (bugids != null && !"".equals(bugids)) {
			String[] bugidss = bugids.split(",");
			for (String child_bugid : bugidss) {
				this.tblNbsjBugMapper.updateBugFatherNullBygId(child_bugid);
			}
		}

		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean findBugCriterion(String token,String bugtype) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
		
		List<TblNbsjBugCriterion> list = this.tblNbsjBugMapper.findBugCriterion(orgid,bugtype);

		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("list", list);
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	
	@Override
	public R removeAttInfoByAttId(String token, String attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return R.fail("用户已失效！");
		}
        return this.deleteRealtionAttInfo(attId);
	}

	private R deleteRealtionAttInfo(String attId) throws Exception {
		boolean b = false;
        TblAttachment att = this.tblAttachmentMapper.selectEntityById(new BigDecimal(attId));
        this.tblNbsjBugMapper.deleteFileInfoByAttId(att.getAttid());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}
	
	
	
	@Override
	public List<TblNbsjBugEntity>  exportList(String token,  TblNbsjBugVo tblNbsjBugVo,BigDecimal orgid)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();

		
		tblNbsjBugVo.setUseSecrect(loginStaff.getCurrentOrg().getUseSecrect());
		tblNbsjBugVo.setSecrectStaff(loginStaff.getStaffid());
		tblNbsjBugVo.setSecrectScopeIds(loginStaff.getSecrectScopeIds());

		BigDecimal companyid = loginStaff.getCurrentOrg().getOrgid();
		tblNbsjBugVo.setCompanyid(companyid);
		tblNbsjBugVo.setOrgid(orgid);
		//链表分页  xml 写法
		String sql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), true, "TNA.bugdepartment", "TNA.linkdeptid","TNA.CREATESTAFFID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
		List<TblNbsjBugEntity> list = tblNbsjBugMapper.selectListByPageInfoXml(tblNbsjBugVo,sql);
		return list;
	}

	@Override
	public void resolveSheet(Sheet sheet, String token) throws Exception {
//		XSSFRow row = null;
		Row row = null;
//		XSSFCell cell = null;
		Cell cell = null;
		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
			row = sheet.getRow(i);
			if (row != null) {
				TblNbsjBugEntity tblNbsjBugEntity = new TblNbsjBugEntity();

				TblStaffUtil user = userProvider.get();
				TblStaff tblStaff = new TblStaff();
				if(user != null) {
					tblStaff.setStaffid(user.getStaffid());
					//创建人ID
					tblNbsjBugEntity.setCreatestaffid(tblStaff.getStaffid());
//					//审计单位/创建人单位ID
					tblStaff.setOrgid(user.getCurrentOrg().getOrgid());
//					tblNbsjAuditplan.setAuditorgid(tblStaff.getOrgid());

				}
				//计划编码  autoCode
				JsonBean autoCodeByJhgl = tblNbsjAuditplanService.getAutoCodeByQxgl(token);
				Map<String, Object> data = (Map<String, Object>) autoCodeByJhgl.getData();
				String autoCode = (String) data.get("autoCode");
				tblNbsjBugEntity.setBugnumber(autoCode);


				//创建日期
				tblNbsjBugEntity.setCreatetime(new Date());


				//defectsname缺陷名称
				cell = row.getCell(1);
				if(StringUtils.isBlank(cell.getStringCellValue())){
					continue;
				}
				if(cell != null){
					cell.setCellType(1);
					tblNbsjBugEntity.setDefectsname(cell.getStringCellValue());
				}

				//缺陷描述及依据
				cell = row.getCell(2);
				if(cell != null){
					cell.setCellType(1);
					tblNbsjBugEntity.setBugdescripte(cell.getStringCellValue());
				}
				//发生时间
				cell = row.getCell(3);
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 定义日期格式
					Date date = dateFormat.parse(cell.getStringCellValue()); // 将字符串解析为日期
					tblNbsjBugEntity.setDiscovertime(date);
				}

				//涉及金额
				cell = row.getCell(4);
				if(cell != null){
					cell.setCellType(1);
					String samount = cell.getStringCellValue();
					if(samount !=null && StringUtils.isNotBlank(samount)){
						BigDecimal value = new BigDecimal(samount);
						tblNbsjBugEntity.setAmount(value);
					}
				}

				//原因分析
				cell = row.getCell(5);
				if(cell != null){
					cell.setCellType(1);
					tblNbsjBugEntity.setCauseanalysis(cell.getStringCellValue());
				}
				//缺陷类别
				cell = row.getCell(6);
				if(cell != null){
					cell.setCellType(1);
					tblNbsjBugEntity.setDefectcategory(cell.getStringCellValue());
				}



				//bugcrilevel 缺陷等级
				cell = row.getCell(7);
				if(cell != null){
					cell.setCellType(1);
					tblNbsjBugEntity.setBugcrilevel(cell.getStringCellValue());
				}
				// 缺陷种类
				cell = row.getCell(8);
				if(cell != null){
					cell.setCellType(1);
					tblNbsjBugEntity.setDefecttype(cell.getStringCellValue());
				}
				//分别获取当前 隶属组织 当前的bugcrilevel 以及 何种缺陷  缺陷等级ID
				String cellValue = tblNbsjBugMapper.selectBugcriid(tblNbsjBugEntity.getBugcrilevel(),tblNbsjBugEntity.getDefecttype(),tblStaff.getOrgid());
				if(cellValue != null){
					BigDecimal bigDecimal = new BigDecimal(cellValue);
					tblNbsjBugEntity.setBugcriid(bigDecimal);
				}

				//是否涉诉
				cell = row.getCell(9);
				if(cell != null){
					cell.setCellType(1);
					tblNbsjBugEntity.setLitigation(cell.getStringCellValue());
				}
				//是否境外
				cell = row.getCell(10);
				if(cell != null){
					cell.setCellType(1);
					tblNbsjBugEntity.setOverseas(cell.getStringCellValue());
				}
				dgbugAdd(tblNbsjBugEntity, token,null);
			}
		}
	}


}
