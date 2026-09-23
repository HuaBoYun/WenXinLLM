package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjMbMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectDataMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjSjjykMapper;
import com.huabo.audit.oracle.vo.DataProVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.TblNbsjProjectDataProService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;

@Service
public class TblNbsjProjectDataProServiceImpl  implements TblNbsjProjectDataProService {

	@Autowired
    private TblNbsjProjectDataMapper tblNbsjProjectDataMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
	private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
	private TblNbsjProjectService tblNbsjProjectService;
    
	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
	
	@Resource
	private TblNbsjMbMapper tblNbsjMbMapper;
	
	@Resource
	private TblNbsjSjjykMapper tblNbsjSjjykMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Resource
	private TblAttachmentService tblAttachmentService;
	
	@Override
	public void saveOrUpdate(TblProjectDataPreEntity dataPre) {
		// TODO Auto-generated method stub

	}

	@Override
	public void proDel(TblProjectDataPreEntity dataPre) {
		// TODO Auto-generated method stub

	}

	@Override
	public TblProjectDataPreEntity proDataById(BigDecimal id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addList(List<TblProjectDataPreEntity> tblProjectDataPres, String username, BigDecimal projectid,
			String projectname, String orgid) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer proDataById(String projectDataPreId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer selectProjectNumber(String projectDataPreId, String projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateTblProjectDataPre(TblProjectDataPreEntity dataPre) {
		// TODO Auto-generated method stub

	}
	
	
	
	//==
	@Override
	public JsonBean dataproPageList(String token, Integer pageNumber, Integer pageSize,DataProVo dataProVo,BigDecimal orgid,BigDecimal projectId) throws Exception {
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
//    	if(null == orgid) {
//    		orgid = loginStaff.getCurrentOrg().getOrgid().intValue();
//    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	/*PageInfo<TblNbsjProjectDataEntity> pageInfo = new PageInfo<TblNbsjProjectDataEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);*/
    	
    	//安全保密SQL
		String secrectSql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TNA.ORGID", "TNA.ORGID", 
				"TNA.CREATESTAFF", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
    	
		// 判断是否为项目组成员 是项目组成员可以获取该项目下的项目资料列表
		boolean ifTeam = false;
		List<TblNbsjProject> pro = tblNbsjProjectMapper.selectAuditItems(loginStaff.getStaffid());
		PageResult<TblNbsjProjectDataEntity> build = null;
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(pro.size()>0 && tnp!=null){
			if(projectId==null) {
				//==查询当前实施的项目！
//				TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
				if(tnp == null ) {
					return ResponseFormat.retParam(0,30003,resultMap);
				}
				projectId = tnp.getProjectId();
			}
			if(null == projectId) {
				return ResponseFormat.retParam(0,30003,resultMap);
			}
			if (pro != null) {
				ifTeam = true;
				dataProVo.setOrgid(orgid);
				dataProVo.setStaffid(loginStaff.getStaffid());
				dataProVo.setProjectId(projectId.toString());
				/*pageInfo.setTlist(
						this.tblNbsjProjectDataMapper.selectListByPageInfo(pageInfo, dataProVo, orgid, projectId,null));
				pageInfo.setTotalRecord(
						this.tblNbsjProjectDataMapper.selectCountByPageInfo(pageInfo, dataProVo, orgid, projectId,null));
				pageInfo.getTotalPage();*/
				//链表分页  xml 写法
				com.github.pagehelper.PageInfo<TblNbsjProjectDataEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
						.doSelectPageInfo(() -> tblNbsjProjectDataMapper.selectListByPageInfoXml(dataProVo,secrectSql));

				//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
				build = new PageResult<TblNbsjProjectDataEntity>().build(pageInfo);
			}
		}else{
//			dataProVo.setOrgid(orgid);
			dataProVo.setStaffid(loginStaff.getStaffid());
			//如果不是项目成员 查看是否为下发人员
			/*pageInfo.setTlist(
					this.tblNbsjProjectDataMapper.selectListByPageInfo(pageInfo, dataProVo, orgid, null,loginStaff.getStaffid()));
			pageInfo.setTotalRecord(
					this.tblNbsjProjectDataMapper.selectCountByPageInfo(pageInfo, dataProVo, orgid, null,loginStaff.getStaffid()));
			pageInfo.getTotalPage();*/
			//链表分页  xml 写法
			com.github.pagehelper.PageInfo<TblNbsjProjectDataEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
					.doSelectPageInfo(() -> tblNbsjProjectDataMapper.selectListByPageInfoXml(dataProVo,secrectSql));

			//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
			build = new PageResult<TblNbsjProjectDataEntity>().build(pageInfo);
		}
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", build);
    	resultMap.put("ifTeam",ifTeam);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dataproAdd(TblNbsjProjectDataEntity pd, String token,String attids,String mbids,String jykids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer count = this.tblNbsjProjectDataMapper.selectPlanCodeByOrgid(pd);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null);
		}
		
		//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,null);
		}
		BigDecimal projectId = tnp.getProjectId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30003,null);
		}
		pd.setProjectid(projectId);
		
		BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
    	
//		notice.setTblCreater(loginStaff);
		pd.setDataDate(new Date());
		pd.setOrgid(orgid+"");
		pd.setUsername(loginStaff.getRealname());
		pd.setCreatestaff(loginStaff.getStaffid());;
		
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(pd.getId() != null) {
			//修改；
			//this.tblNbsjProjectDataMapper.updateEntity(pd);
			this.tblNbsjProjectDataMapper.updateByPrimaryKeySelective(pd);
			//==附件，先删除 再重新添加
			this.tblAttachmentMapper.deleteAttmentRelationDataPj(pd.getId());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationDataPj(id, pd.getId());
				}
			}
			//模板库
			if (mbids != null && !"".equals(mbids)) {
				String[] ids = mbids.split(",");
				for (String mbid : ids) {
					tblNbsjMbMapper.insertMbdatapre(mbid,  pd.getId());
				}
			}
			//经验库 
			if (jykids != null && !"".equals(jykids)) {
				String[] ids = jykids.split(",");
				for (String jykid : ids) {
					tblNbsjSjjykMapper.insertjykdatapre(jykid, pd.getId());
				}
			}
		}else {
			//新增；
			//this.tblNbsjProjectDataMapper.insertEntity(pd);
			pd.setId(RandomUtil.uuBigDecimalId());
			this.tblNbsjProjectDataMapper.insertSelective(pd);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationDataPj(id, pd.getId());
				}
			}
			//模板库
			if (mbids != null && !"".equals(mbids)) {
				String[] ids = mbids.split(",");
				for (String mbid : ids) {
					tblNbsjMbMapper.insertMbdatapre(mbid,  pd.getId());
				}
			}
			//经验库 
			if (jykids != null && !"".equals(jykids)) {
				String[] ids = jykids.split(",");
				for (String jykid : ids) {
					tblNbsjSjjykMapper.insertjykdatapre(jykid, pd.getId());
				}
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("Doubtfulpoint",pd);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	@Override
	public JsonBean dataproAddFile(BigDecimal dataId,BigDecimal attid) throws Exception {
		TblNbsjProjectDataEntity pd = this.tblNbsjProjectDataMapper.selectById(dataId);
		if(pd.getId() != null&&attid!=null) {
			this.tblAttachmentMapper.insertAttmentRelationDataPj(attid.toString(), pd.getId());
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	@Override
	public JsonBean dataproDelete(BigDecimal dataId, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjProjectDataEntity plan = this.tblNbsjProjectDataMapper.selectById(dataId);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjProjectDataMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		tblNbsjMbMapper.deletetMbdatapre(dataId);
		tblNbsjSjjykMapper.deletetjykdatapre(dataId);
		this.tblNbsjProjectDataMapper.deleteById(dataId);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findDataProDetail(String token, BigDecimal dataId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblNbsjProjectDataEntity plan = this.tblNbsjProjectDataMapper.selectById(dataId);
		resultMap.put("Doubtfulpoint", plan);
		return ResponseFormat.retParam(1,200,resultMap);
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
        this.tblNbsjProjectDataMapper.deleteFileInfoByAttId(att.getAttid());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}

	@Override
	public JsonBean issueProject(String token, String staffId, String dataId,String projectid) throws Exception {
		// TODO Auto-generated method stub
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(StringUtils.isBlank(projectid)&&tnp != null) {
			projectid=tnp.getProjectId().toString();
		}
		String[] dataIds=dataId.split(",");
    	String[] roleids=staffId.split(",");
    	try {
    		TblNbsjProjectDataEntity dataPre=null;
    		for(String pid:dataIds){
    			dataPre = this.tblNbsjProjectDataMapper.selectById(new BigDecimal(pid));
    			for(String rid:roleids){
    				if(tblNbsjProjectDataMapper.checkIssue(new BigDecimal(pid),new BigDecimal(rid))!= null){
	    					if(pid!=null && pid.length()>0){
		    					dataPre.setFristuserid(rid);
		    					tblNbsjProjectDataMapper.updateEntity(dataPre);
    				     }
    				     tblNbsjProjectDataMapper.saveIssue(projectid,rid,pid);
    				}
    			}
    		}
		} catch (Exception e) {
			// TODO: handle exception
			   e.printStackTrace();
		}
    	return ResponseFormat.retParam(1,200,null);
	}
	
	@Override
	public JsonBean savexmfj(String token,BigDecimal dataId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjProjectDataEntity pd = this.tblNbsjProjectDataMapper.selectById(dataId);
		if(pd.getId() != null) {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByProjectid(pd.getProjectid());
			if(attList==null || attList.size()<=0) {
				return ResponseFormat.retParam(0,"项目无附件",null);
			}
			for (TblAttachment tblAttachment : attList) {
				TblAttachment tblAttachmentEntity =new TblAttachment(tblAttachment.getAttname(),tblAttachment.getAttpath(), tblAttachment.getAttsize(),tblAttachment.getMemo(),tblAttachment.getUploader(),
						tblAttachment.getIspythonflag(),tblAttachment.getFilename(),tblAttachment.getContentText(), tblAttachment.getStaffids(),tblAttachment.getAttachmentlevel(),tblAttachment.getIsEncrypted(),tblAttachment.getJmurl());
 	             tblAttachmentEntity.setAttid(RandomUtil.uuBigDecimalId());
 	            tblAttachmentEntity.setUploadtime(new Date());
	             tblAttachmentMapper.insert(tblAttachmentEntity);
				this.tblAttachmentMapper.insertAttmentRelationDataPj(tblAttachmentEntity.getAttid().toString(), pd.getId());
			}
			
		}
		return ResponseFormat.retParam(1,200,null);
	}

 

	@Override
	public JsonBean saveFj(String token,JSONArray arr) throws Exception {
		// TODO Auto-generated method stub
		try {
				for(Object obj:arr){
					Map<String, Object> map=(Map<String, Object>) obj;
					TblNbsjProjectDataEntity pd = this.tblNbsjProjectDataMapper.selectById(new BigDecimal(map.get("ID").toString()));
					this.tblAttachmentMapper.insertAttmentRelationDataPj(map.get("ATTID").toString(),pd.getId());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,null);

	}
}
