package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.common.ResultCode;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.exception.CommercialException;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjQuestionEntity;
import com.huabo.audit.oracle.entity.TblNbsjRefopmEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjQuestionMapper;
import com.huabo.audit.oracle.mapper.TblNbsjRefopmMapper;
import com.huabo.audit.oracle.vo.AuditQuestionVo;
import com.huabo.audit.oracle.vo.TblNbsjQuestionVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjQuestionService;
import com.huabo.audit.util.OrgInfoUtils;
import com.huabo.audit.util.PageInfo;

/**
* 描述:实现类
* @author: ziyao
* @date: 2022-04-13
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjQuestionServiceImpl extends ServiceImpl<TblNbsjQuestionMapper, TblNbsjQuestionEntity> implements TblNbsjQuestionService {
    @Autowired
    private TblNbsjQuestionMapper tblNbsjQuestionMapper;
    @Autowired
    private TblNbsjProjectService projectService;
    
    @Autowired
    private TblNbsjRefopmMapper tblNbsjRefopmMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;

	@Resource
    private UserProvider userProvider;
	
    @Override
    public com.github.pagehelper.PageInfo<TblNbsjQuestionEntity> auditQuestionList(AuditQuestionVo auditQuestion) throws Exception {
        //检查token
        TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			throw new CommercialException(ResultCode.BIZ_ERROR,"系统错误，请联系管理员！");
		}
        
        //通过token获取组织id(orgId)
        BigDecimal pid = loginStaff.getCurrentOrg().getOrgid();
        auditQuestion.setOrgId(pid);
        //分页插件
        PageHelper.startPage(auditQuestion.getPageNum(),auditQuestion.getPageSize());
        //审计问题列表查询
        List<TblNbsjQuestionEntity> auditQuestionList = tblNbsjQuestionMapper.getAuditQuestionList(auditQuestion);
        return new com.github.pagehelper.PageInfo<>(auditQuestionList);
    }

	@Override
	public TblNbsjQuestionEntity findNbsjQuestionBySheetId(String sheetid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> findQuestionObjects(String questions) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TblNbsjQuestionEntity> findQuestion(String factid) {
		// TODO Auto-generated method stub
		return null;
	}

	///////////////////////////////////////==
	@Override
	public JsonBean questionStorePageList(String token, Integer pageNumber, Integer pageSize,TblNbsjQuestionVo tblNbsjQuestionVo,Integer projectId) throws Exception {
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
    	
    	if(null == projectId) {
    		//==查询当前实施的项目！
    		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
    		if(tnp == null) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
    		projectId = tnp.getProjectId();
    		if(null == projectId) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
    	}
		TblNbsjProject project = tblNbsjProjectMapper.selectPJById(projectId);
		//tblNbsjQuestionMapper.updateBySheet();
		resultMap.put("project", project);
    	PageInfo<TblNbsjQuestionEntity> pageInfo = new PageInfo<TblNbsjQuestionEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjQuestionMapper.selectListByPageInfo(pageInfo,tblNbsjQuestionVo,projectId));
    	pageInfo.setTotalRecord(this.tblNbsjQuestionMapper.selectCountByPageInfo(pageInfo,tblNbsjQuestionVo,projectId));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public  List<TblNbsjSheetEntity> getQuestionStorePageList(TblStaffUtil loginStaff,TblNbsjQuestionVo tblNbsjQuestionVo,Integer projectid) throws Exception{
    	if(null == projectid) {
    		//==查询当前实施的项目！
    		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
    		if(tnp == null) {
    			return null;
    		}
    		projectid = tnp.getProjectId();
    	}
    	 List<TblNbsjSheetEntity> list =this.tblNbsjQuestionMapper.getNbsjQuestList(tblNbsjQuestionVo,projectid);
    	return list;
	}
	
	@Override
	public JsonBean questionStorePageAdd(TblNbsjQuestionEntity tnq, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer count = this.tblNbsjQuestionMapper.selectPlanCodeByOrgid(tnq);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null);
		}
    	
//		tnq..setAuditStaffId(loginStaff.getStaffid().intValue());
//		tnq.setCreateTime(new Date());
		tnq.setStatus(0);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(tnq.getSheetId() != null) {
			//修改；
			this.tblNbsjQuestionMapper.updateEntity(tnq);
		}else {
			//新增；
			this.tblNbsjQuestionMapper.insertEntity(tnq);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("Question",tnq);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean questionStorePageDelete(Integer questionid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjQuestionEntity plan = this.tblNbsjQuestionMapper.selectById(questionid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjQuestionMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblNbsjQuestionMapper.deleteById(questionid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean questionStorePageFQZG(Integer questionid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjQuestionEntity question = this.tblNbsjQuestionMapper.selectById(questionid);
		
		if(question == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
        if (question != null) {
        	TblNbsjRefopmEntity nbsjRefopm = null;
        	List<TblNbsjRefopmEntity> listNF = tblNbsjRefopmMapper.getByQuestid(questionid);
    		if(listNF!=null && listNF.size()>0){
    			nbsjRefopm = listNF.get(0);
    		}
        	
            if (nbsjRefopm == null || nbsjRefopm.getStatus() != 2) {
                if (question.getStatus() == 2) {
                    question.setRecStatus(TblNbsjQuestionEntity.FQYES);
                    
                    this.tblNbsjQuestionMapper.updateEntity(question);
                    return ResponseFormat.retParam(1,200,null);
                    
                } else {
                	return ResponseFormat.retParam(0,"该审计发现未进行事实确认",null);
//                    return JsonBean.error("该审计发现未进行事实确认");
                }
            } else {
            	return ResponseFormat.retParam(0,30001,null);
//                return JsonBean.error("该审计发现已整改完毕");
            }
        } else {
        	return ResponseFormat.retParam(0,"该审计发现未进行事实确认",null);
//            return JsonBean.error("发起整改失败");
        }
	}

	@Override
	public JsonBean confirmationQuestionStorePageList(String token, Integer pageNumber, Integer pageSize,
			TblNbsjQuestionVo tblNbsjQuestionVo, Integer projectId) throws Exception {
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
    	
    	if(null == projectId) {
    		//==查询当前实施的项目！
    		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
    		if(tnp == null) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
    		projectId = tnp.getProjectId();
    		if(null == projectId) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
    	}
    	
    	PageInfo<TblNbsjQuestionEntity> pageInfo = new PageInfo<TblNbsjQuestionEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjQuestionMapper.selectConfirmationListByPageInfo(pageInfo,tblNbsjQuestionVo,projectId));
    	pageInfo.setTotalRecord(this.tblNbsjQuestionMapper.selectConfirmationCountByPageInfo(pageInfo,tblNbsjQuestionVo,projectId));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean confirmationQuestionStoreLink(String token, Integer factid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Integer pageNumber = null;
		Integer pageSize= null;
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
//    	if(null == projectId) {
//    		//==查询当前实施的项目！
//    		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
//    		if(tnp == null) {
//    			return ResponseFormat.retParam(0,30003,resultMap);
//    		}
//    		projectId = tnp.getProjectId();
//    		if(null == projectId) {
//    			return ResponseFormat.retParam(0,30003,resultMap);
//    		}
//    	}
    	
    	PageInfo<TblNbsjQuestionEntity> pageInfo = new PageInfo<TblNbsjQuestionEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjQuestionMapper.selectConfirmationListByFact(pageInfo,factid));
    	pageInfo.setTotalRecord(0);
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
}
