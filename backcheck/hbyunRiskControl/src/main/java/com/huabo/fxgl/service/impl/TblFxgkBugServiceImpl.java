package com.huabo.fxgl.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.TblAttachment;
import com.huabo.fxgl.entity.TblNbsjBugCriterion;
import com.huabo.fxgl.entity.TblNbsjBugEntity;
import com.huabo.fxgl.entity.TblNbsjInnerrule;
import com.huabo.fxgl.entity.TblNbsjOuterruleEntity;
import com.huabo.fxgl.mapper.TblAttachmentMapper;
import com.huabo.fxgl.mapper.TblFxgkBugMapper;
import com.huabo.fxgl.mapper.TblNbsjBugMapper;
import com.huabo.fxgl.service.ActivityPluginsService;
import com.huabo.fxgl.service.TblFxgkBugService;
import com.huabo.fxgl.service.TblNbsjBugService;
import com.huabo.fxgl.util.ProcessEnum;
import com.huabo.fxgl.vo.TblFxgkBugVo;

@Service
public class TblFxgkBugServiceImpl implements TblFxgkBugService {

	@Autowired
	private TblNbsjBugMapper tblNbsjBugMapper;

	@Autowired
	private ActivityPluginsService activityPluginsService;
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;


	@Resource
	private TblFxgkBugMapper tblFxgkBugMapper;
	
	@Resource
    private UserProvider userProvider;
	
	
	/**
	 * 缺陷管理-附件列表
	 *
	 * @param token
	 * @param bugId
	 * @return
	 */
	@Override
	public JsonBean defectFileList(String token, Integer bugId) throws Exception {
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
	public JsonBean bugPageList(String token, Integer pageNumber, Integer pageSize, TblFxgkBugVo tblNbsjBugVo,Integer orgid)
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
		
//		if(null == orgid) {
//			orgid = loginStaff.getCurrentOrg().getOrgid().intValue();
//		}
		BigDecimal companyid = loginStaff.getCurrentOrg().getOrgid();
		Map<String, Object> resultMap = new HashMap<String, Object>(0);

//		PageInfo<TblNbsjBugEntity> pageInfo = new PageInfo<TblNbsjBugEntity>();
//		pageInfo.setPageSize(pageSize);
//		pageInfo.setCurrentPage(pageNumber);
//		pageInfo.setTlist(this.tblFxgkBugMapper.selectListByPageInfo(pageInfo, tblNbsjBugVo,orgid,companyid));
//		pageInfo.setTotalRecord(this.tblNbsjBugMapper.selectCountByPageInfo(pageInfo, tblNbsjBugVo,orgid,companyid));
//		pageInfo.getTotalPage();
		PageInfo<TblNbsjBugEntity> pageInfo2 = new PageInfo<TblNbsjBugEntity>();
		com.github.pagehelper.PageInfo<TblNbsjBugEntity> info = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> {
			try {
				this.tblFxgkBugMapper.selectListByPageInfo2(tblNbsjBugVo, orgid, companyid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		pageInfo2.setPageSize(info.getPageSize());
		pageInfo2.setCurrentPage(info.getPageNum());
		pageInfo2.setTlist(info.getList());
		pageInfo2.setTotalRecord((int)info.getTotal());
		pageInfo2.getTotalPage();
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
//		resultMap.put("pageInfo", pageInfo);
		resultMap.put("pageInfo", pageInfo2);
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
		
		Integer bugcriid = bug.getBugcriid();

		if (bug.getBugid() != null) {
			//修改；
			this.tblNbsjBugMapper.updateEntity(bug);

			BigDecimal bugid = bug.getBugid();
			this.tblNbsjBugMapper.deleteCriLink(bugid, bugcriid);
			
			//==附件，先删除 再重新添加
			this.tblNbsjBugMapper.deleteAttmentRelationBUG(bug.getBugid().intValue());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblNbsjBugMapper.insertAttmentRelationBUG(id, bug.getBugid().intValue());
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
					this.tblNbsjBugMapper.insertAttmentRelationBUG(id, bug.getBugid().intValue());
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
	public JsonBean bugDelete(Integer bugid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		TblNbsjBugEntity plan = this.tblNbsjBugMapper.selectById(bugid);

		if (plan == null) {
			return ResponseFormat.retParam(0, 50001, null);
		}

		this.tblNbsjBugMapper.deleteByFatherId(bugid);
		this.tblNbsjBugMapper.deleteById(bugid);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean findBugDetail(String token, Integer bugid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);

		TblNbsjBugEntity plan = this.tblNbsjBugMapper.selectById(bugid);
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
	public JsonBean defect_file_export(String token, Integer orgId,HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			orgId = loginStaff.getCurrentOrg().getOrderid();
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
	public JsonBean innerCommonQxwtList(String token, Integer pageNumber, Integer pageSize, Integer bugid)
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

		PageInfo<TblNbsjInnerrule> pageInfo = new PageInfo<TblNbsjInnerrule>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjBugMapper.selectInnerCommonListByPageInfo(pageInfo, bugid,orgid));
		pageInfo.setTotalRecord(this.tblNbsjBugMapper.selectInnerCommonCountByPageInfo(pageInfo, bugid,orgid));
		pageInfo.getTotalPage();
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean outerCommonQxwtList(String token, Integer pageNumber, Integer pageSize, Integer bugid)
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

		PageInfo<TblNbsjOuterruleEntity> pageInfo = new PageInfo<TblNbsjOuterruleEntity>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjBugMapper.selectOuterCommonListByPageInfo(pageInfo, bugid,orgid));
		pageInfo.setTotalRecord(this.tblNbsjBugMapper.selectOuterCommonCountByPageInfo(pageInfo, bugid,orgid));
		pageInfo.getTotalPage();
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean bugInnrulidsAdd(String token, Integer bugid, String innrulids) throws Exception {
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
	public JsonBean bugOutrulidsAdd(String token, Integer bugid, String outrulids) throws Exception {
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
	public JsonBean innerCommonLinkList(String token, Integer pageNumber, Integer pageSize, Integer bugid)
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

		PageInfo<TblNbsjInnerrule> pageInfo = new PageInfo<TblNbsjInnerrule>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjBugMapper.selectInnerCommonListByLink(pageInfo, bugid,orgid));
		pageInfo.setTotalRecord(0);
		pageInfo.getTotalPage();
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean outerCommonLinkList(String token, Integer pageNumber, Integer pageSize, Integer bugid)
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

		PageInfo<TblNbsjOuterruleEntity> pageInfo = new PageInfo<TblNbsjOuterruleEntity>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjBugMapper.selectOuterCommonListByLink(pageInfo, bugid,orgid));
		pageInfo.setTotalRecord(0);
		pageInfo.getTotalPage();
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean defectQxwtAdd(String token, Integer bugid, String bugids) throws Exception {
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
	public JsonBean defectLinkList(String token, Integer pageNumber, Integer pageSize, Integer bugid) throws Exception {
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

		PageInfo<TblNbsjBugEntity> pageInfo = new PageInfo<TblNbsjBugEntity>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjBugMapper.selectDefectLinkList(pageInfo, bugid));
		pageInfo.setTotalRecord(0);
		pageInfo.getTotalPage();
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean bugInnrulidsDelete(String token, Integer bugid, String innrulids) throws Exception {
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
	public JsonBean bugOutrulidsDelete(String token, Integer bugid, String outrulids) throws Exception {
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
	public JsonBean bugFatherDelete(String token, Integer bugid, String bugids) throws Exception {
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
	public JsonBean findBugCriterion(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
		
		List<TblNbsjBugCriterion> list = this.tblNbsjBugMapper.findBugCriterion(orgid);
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("list", list);
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	
	@Override
	public void removeAttInfoByAttId(String token, String attId) throws Exception {
//		TblStaffUtil loginStaff = userProvider.get();
//		if(loginStaff == null) {
//			return R.fail("用户已失效！");
//		}
        this.deleteRealtionAttInfo(attId);
	}

	private void deleteRealtionAttInfo(String attId) throws Exception {
		boolean b = false;
        TblAttachment att = this.tblAttachmentMapper.selectEntityById(attId);
        this.tblNbsjBugMapper.deleteFileInfoByAttId(att.getAttid().intValue());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
	}

}
