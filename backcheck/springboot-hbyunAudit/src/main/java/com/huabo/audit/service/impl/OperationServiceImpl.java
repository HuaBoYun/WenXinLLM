package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.util.PageResult;

import io.lettuce.core.dynamic.annotation.Param;

import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.Pamas;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ExclUtils;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ReflectUtils;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjBugCriterion;
import com.huabo.audit.oracle.entity.TblNbsjDoubtfulpointEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjBugCriterionMapper;
import com.huabo.audit.oracle.mapper.TblNbsjDoubtfulpointMapper;
import com.huabo.audit.oracle.mapper.TblNbsjSheetMapper;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.service.NbsjExperienceService;
import com.huabo.audit.service.OperationService;
import com.huabo.audit.service.TblNbsjProjectService;

@Service
public class OperationServiceImpl implements OperationService {

	@Resource
	private TblNbsjProjectService tblNbsjProjectService;
	
	@Resource
	public TblNbsjDoubtfulpointMapper tblNbsjDoubtfulpointMapper;
	
	@Resource
	public TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	public TblNbsjSheetMapper tblNbsjSheetMapper;
	
	@Resource
	public TblNbsjBugCriterionMapper tblNbsjBugCriterionMapper;
	
	@Resource
    private UserProvider userProvider;
	
	
	@Override
	public JsonBean dealSendDigao(String token, String modelTye, Pamas pamas) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblNbsjProject project = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(project == null) {
			return ResponseFormat.retParam(0,10001,null);
		}
		resultMap.put("project", project);
		String[] res = null;
		
		if(modelTye!=null && modelTye.equals("nbsj_yigl")) {
			//独立出来
			String[] heads = new String[]{"疑点编号","疑点名称","疑点描述","测试结果","编制人"};
			String selectIds = pamas.getSelectIds();
			List<TblNbsjDoubtfulpointEntity> list = this.tblNbsjDoubtfulpointMapper.exclTblDoubtfulpointByids(selectIds);
			List<Object[]> strArray = list.stream().map(t -> new Object[]{t.getDpnumber(),t.getDpname(), t.getDpdescribe(), t.getTestresult(), t.getEditor()}).collect(Collectors.toList());
			String[] result = ExclUtils.generateExclByArray("疑点管理", heads, strArray);
			res = new String[]{result[0],result[1],"疑点管理.xls"};
		}
		
		TblAttachment attachment = new TblAttachment();
		attachment.setAttname(res[2]);
		attachment.setUploader(loginStaff.getUsername());
		attachment.setUploadtime(new Date());
		attachment.setAttpath(res[0]);
		attachment.setAttsize(Double.parseDouble(res[1]+"")/1024);
		Map<String, String> map = ReflectUtils.getObjectValue(pamas);
		boolean isOne = true;
		String backUrl = pamas.getUrl();
		for (Entry<String, String> entry : map.entrySet()) {  
			if(isOne){
				backUrl+="?"+entry.getKey()+"="+entry.getValue();
				isOne = false;
			}else{
				backUrl+="&"+entry.getKey()+"="+entry.getValue();
			}
		}
		attachment.setAttid(RandomUtil.uuBigDecimalId());
		this.tblAttachmentMapper.insertEntity(attachment);
		resultMap.put("attachment", attachment);
		resultMap.put("modelTye", modelTye);
		resultMap.put("pamas", pamas);
		resultMap.put("backUrl", URLEncoder.encode(backUrl,"UTF-8"));
		return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean dealSendDigaoListFile(String token, String modelTye, Pamas pamas, Integer pageNumber,
			Integer pageSize, TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	Map<String, String> map = ReflectUtils.getObjectValue(pamas);
		boolean isOne = true;
		String backUrl = pamas.getUrl();
		for (Entry<String, String> entry : map.entrySet()) {  
			if(isOne){
				backUrl+="?"+entry.getKey()+"="+entry.getValue();
				isOne = false;
			}else{
				backUrl+="&"+entry.getKey()+"="+entry.getValue();
			}
		}
		resultMap.put("backUrl", backUrl);
		PageResult<TblNbsjSheetEntity> build = null;
	/*	PageInfo<TblNbsjSheetEntity> pageInfo = new PageInfo<TblNbsjSheetEntity>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setCondition(sheet);*/
		if(modelTye!=null && modelTye.equals("nbsj_yigl")){
			TblNbsjProject project = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
			tBlNbsjSheetVo.setProjectid(project.getProjectId());
			//链表分页  xml 写法
			
			com.github.pagehelper.PageInfo<TblNbsjSheetEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
					.doSelectPageInfo(() -> tblNbsjSheetMapper.selectListByPageInfoxml(tBlNbsjSheetVo,loginStaff));
			
			//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
			 build = new PageResult<TblNbsjSheetEntity>().build(pageInfo);
			/*pageInfo.setTlist(this.tblNbsjSheetMapper.findByProjectIdAndPmUserId(pageInfo,project.getProjectId()));
			pageInfo.setTotalRecord(this.tblNbsjSheetMapper.findByProjectIdAndPmUserIdCount(pageInfo,project.getProjectId()));*/
			 
		}else{
			 //审计之外发送至底稿附件返回LISt
			/* pageBean = tblWorksheetService.findAllTblWorksheet(user.getStaffid().toString(),num, pageBean.getPageSize(),modelType,find);
			 mv.addObject("pageBean", pageBean);*/
		}
		resultMap.put("pageInfo", build);
		resultMap.put("pamas", pamas);
		resultMap.put("modelTye", modelTye);
		return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean saveSendDigaoListFile(String token, String modelTye, Pamas pamas, BigDecimal workId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		try {
			String[] res = null;
			if(modelTye!=null && modelTye.equals("nbsj_yigl")){
				//独立出来
				String[] heads = new String[]{"疑点编号","疑点名称","疑点描述","测试结果","编制人"};
				String selectIds = pamas.getSelectIds();
				List<TblNbsjDoubtfulpointEntity> list = this.tblNbsjDoubtfulpointMapper.exclTblDoubtfulpointByids(selectIds);
				List<Object[]> strArray = list.stream().map(t -> new Object[]{t.getDpnumber(),t.getDpname(), t.getDpdescribe(), t.getTestresult(), t.getEditor()}).collect(Collectors.toList());
				String[] result = ExclUtils.generateExclByArray("疑点管理", heads, strArray);
				res = new String[]{result[0],result[1],"疑点管理.xls"};
				
				TblAttachment attachment = new TblAttachment();
				attachment.setAttname(res[2]);
				attachment.setUploader(loginStaff.getUsername());
				attachment.setUploadtime(new Date());
				attachment.setAttpath(res[0]);
				attachment.setAttsize(Double.parseDouble(res[1]+"")/1024);
				attachment.setAttid(RandomUtil.uuBigDecimalId());
				this.tblAttachmentMapper.insertEntity(attachment);
				this.tblAttachmentMapper.insertAttmentRelationSheet(attachment.getAttid().toString(),workId);
			}else{
				/*System.out.println(url);
			
				TblWorksheet worksheet = this.tblWorksheetService.findByid(workId);
				if(null!=worksheet){
					String[] res = operationInvoke.invoke(modelType,pamas);
					TblAttachment attachment = new TblAttachment();
					attachment.setAttname(res[2]);
					TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
					attachment.setUploader(user.getUsername());
					attachment.setUploadtime(new Date());
					attachment.setAttpath(res[0]);
					attachment.setAttsize(Double.parseDouble(res[1]+"")/1024);
					this.attachmentService.add(attachment);
				
					worksheet.getTblAttachments().add(attachment);
				
					if(modelTye!=null && modelTye.equals("nk_pjpf")){
						List<ElemGrade> lists = tblAssessElementService.findElementByassIdAndUserId(pamas.getNodeId(), pamas.getAssid(), user.getStaffid(), pamas.getOrgId(),pamas.getSelectIds());
						if(lists!=null && lists.size()>0){
							String attids="";
							for (ElemGrade elemGrade : lists) {
								if(elemGrade.getAttid()!=null ){
									attids+=elemGrade.getAttid()+",";
								}
							}
							if(StringUtils.isNotBlank(attids)){
								List<TblAttachment> atts = attachmentService.findByIds(attids.substring(0, attids.length() - 1));
								if(atts!=null && atts.size()>0){
									for (TblAttachment tblAttachment : atts) {
										worksheet.getTblAttachments().add(tblAttachment);
									}
								}
							}
						}
					}
					if(modelTye!=null && modelTye.equals("nk_csrw")){
						List<TblTesttask> list = tblTesttaskService.findByIdAll(pamas.getSelectIds());
						String attids="";
						if(list!=null && list.size()>0){
							for (TblTesttask tblTesttask : list) {
								if(tblTesttask.getAttid()!=null ){
									attids+=tblTesttask.getAttid()+",";
								}
							}
							if(StringUtils.isNotBlank(attids)){
								List<TblAttachment> atts = attachmentService.findByIds(attids.substring(0, attids.length() - 1));
								if(atts!=null && atts.size()>0){
									for (TblAttachment tblAttachment : atts) {
										worksheet.getTblAttachments().add(tblAttachment);
									}
								}
							}
						}
					}
					this.tblWorksheetService.modify(worksheet);
				}*/
			}
		} catch (Exception e) {
			throw e;
		}
		return ResponseFormat.retParam(1,200,null);
	}


	@Override
	public JsonBean dealSendDoubtful(String token, String modelTye, Pamas pamas) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		String[] res = null;
		
		String[] heads = new String[]{"疑点编号","疑点名称","疑点描述","测试结果","编制人"};
		String selectIds = pamas.getSelectIds();
		List<TblNbsjDoubtfulpointEntity> list = this.tblNbsjDoubtfulpointMapper.exclTblDoubtfulpointByids(selectIds);
		List<Object[]> strArray = list.stream().map(t -> new Object[]{t.getDpnumber(),t.getDpname(), t.getDpdescribe(), t.getTestresult(), t.getEditor()}).collect(Collectors.toList());
		String[] result = ExclUtils.generateExclByArray("疑点管理", heads, strArray);
		res = new String[]{result[0],result[1],"疑点管理.xls"};
		
		TblAttachment attachment = new TblAttachment();
		attachment.setAttname(res[2]);
		attachment.setUploader(loginStaff.getUsername());
		attachment.setUploadtime(new Date());
		attachment.setAttpath(res[0]);
		attachment.setAttsize(Double.parseDouble(res[1]+"")/1024);
		attachment.setAttid(RandomUtil.uuBigDecimalId());
		this.tblAttachmentMapper.insertEntity(attachment);
		Map<String, String> map = ReflectUtils.getObjectValue(pamas);
		boolean isOne = true;
		String backUrl = pamas.getUrl();
		for (Entry<String, String> entry : map.entrySet()) {  
			if(isOne){
				backUrl+="?"+entry.getKey()+"="+entry.getValue();
				isOne = false;
			}else{
				backUrl+="&"+entry.getKey()+"="+entry.getValue();
			}
		}  
		
		resultMap.put("attachment", attachment);
		resultMap.put("modelTye", modelTye);
		resultMap.put("pamas", pamas);
		resultMap.put("backUrl", URLEncoder.encode(backUrl,"UTF-8"));
		resultMap.put("loginStaff", loginStaff);
		return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean dealSendDefect(String token, String modelTye, Pamas pamas) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		String[] res = null;
		
		String[] heads = new String[]{"疑点编号","疑点名称","疑点描述","测试结果","编制人"};
		String selectIds = pamas.getSelectIds();
		List<TblNbsjDoubtfulpointEntity> list = this.tblNbsjDoubtfulpointMapper.exclTblDoubtfulpointByids(selectIds);
		List<Object[]> strArray = list.stream().map(t -> new Object[]{t.getDpnumber(),t.getDpname(), t.getDpdescribe(), t.getTestresult(), t.getEditor()}).collect(Collectors.toList());
		String[] result = ExclUtils.generateExclByArray("疑点管理", heads, strArray);
		res = new String[]{result[0],result[1],"疑点管理.xls"};
		
		TblAttachment attachment = new TblAttachment();
		attachment.setAttname(res[2]);
		attachment.setUploader(loginStaff.getUsername());
		attachment.setUploadtime(new Date());
		attachment.setAttpath(res[0]);
		attachment.setAttsize(Double.parseDouble(res[1]+"")/1024);
		attachment.setAttid(RandomUtil.uuBigDecimalId());
		this.tblAttachmentMapper.insertEntity(attachment);
		List<TblNbsjBugCriterion> criList = this.tblNbsjBugCriterionMapper.findListByOrgid(loginStaff.getCurrentOrg().getOrgid());
		
		Map<String, String> map = ReflectUtils.getObjectValue(pamas);
		boolean isOne = true;
		String backUrl = pamas.getUrl();
		for (Entry<String, String> entry : map.entrySet()) {  
			if(isOne){
				backUrl+="?"+entry.getKey()+"="+entry.getValue();
				isOne = false;
			}else{
				backUrl+="&"+entry.getKey()+"="+entry.getValue();
			}
		}  
		resultMap.put("attachment", attachment);
		resultMap.put("modelTye", modelTye);
		resultMap.put("pamas", pamas);
		resultMap.put("backUrl", URLEncoder.encode(backUrl,"UTF-8"));
		resultMap.put("criList", criList);
		return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean dealSendEvidence(String token, String modelTye, Pamas pamas) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblNbsjProject project = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(project == null) {
			return ResponseFormat.retParam(0,10001,null);
		}
		resultMap.put("project", project);
		String[] res = null;
		
		if(modelTye!=null && modelTye.equals("nbsj_yigl")) {
			//独立出来
			String[] heads = new String[]{"疑点编号","疑点名称","疑点描述","测试结果","编制人"};
			String selectIds = pamas.getSelectIds();
			List<TblNbsjDoubtfulpointEntity> list = this.tblNbsjDoubtfulpointMapper.exclTblDoubtfulpointByids(selectIds);
			List<Object[]> strArray = list.stream().map(t -> new Object[]{t.getDpnumber(),t.getDpname(), t.getDpdescribe(), t.getTestresult(), t.getEditor()}).collect(Collectors.toList());
			String[] result = ExclUtils.generateExclByArray("疑点管理", heads, strArray);
			res = new String[]{result[0],result[1],"疑点管理.xls"};
		}
		
		TblAttachment attachment = new TblAttachment();
		attachment.setAttname(res[2]);
		attachment.setUploader(loginStaff.getUsername());
		attachment.setUploadtime(new Date());
		attachment.setAttpath(res[0]);
		attachment.setAttsize(Double.parseDouble(res[1]+"")/1024);
		Map<String, String> map = ReflectUtils.getObjectValue(pamas);
		boolean isOne = true;
		String backUrl = pamas.getUrl();
		for (Entry<String, String> entry : map.entrySet()) {  
			if(isOne){
				backUrl+="?"+entry.getKey()+"="+entry.getValue();
				isOne = false;
			}else{
				backUrl+="&"+entry.getKey()+"="+entry.getValue();
			}
		}
		attachment.setAttid(RandomUtil.uuBigDecimalId());
		this.tblAttachmentMapper.insertEntity(attachment);
		resultMap.put("attachment", attachment);
		resultMap.put("modelTye", modelTye);
		resultMap.put("pamas", pamas);
		resultMap.put("backUrl", URLEncoder.encode(backUrl,"UTF-8"));
		return ResponseFormat.retParam(1,200,resultMap);
	}

	
}
