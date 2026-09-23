package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsIssueListEntity;
import com.huabo.audit.oracle.entity.TblYqnsIssuesRecord;
import com.huabo.audit.oracle.entity.TblYqnsSjbgSjbgdg;
import com.huabo.audit.oracle.mapper.AuditIssueListMapper;
import com.huabo.audit.oracle.mapper.AuditProposeMapper;
import com.huabo.audit.oracle.mapper.EnginAuditProjectMapper;
import com.huabo.audit.oracle.mapper.FundAuditProjectMapper;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsIssuesRecordMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjbgSjbgdgMapper;
import com.huabo.audit.oracle.service.AuditIssueListService;
import com.huabo.audit.service.impl.ReservePropertyService;

/**
 * @ Author: dev@example.com
 * @ Date: 2023/9/11
 * @ TODO:
 **/
@Service
public class AuditIssueListServiceImpl implements AuditIssueListService {

	@Autowired
    AuditIssueListMapper auditIssueListMapper;

    // 存储文件中间表
    @Autowired
    TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private TblYqnsSjbgSjbgdgMapper tblYqnsSjbgSjbgdgMapper;
    
    @Resource
    private ImplementPlanMapper implementPlanMapper;
    
    @Resource
    private AuditProposeMapper auditProposeMapper;
    
    @Resource
    private TblYqnsIssuesRecordMapper tblYqnsIssuesRecordMapper;

    @Resource
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private EnginAuditProjectMapper enginAuditProjectMapper;
    
    @Resource
    private FundAuditProjectMapper fundAuditProjectMapper;
    
    @Resource
    private UserProvider userProvider;


    @Override
    public JsonBean saveOrUpdate(TblYqnsIssueListEntity param) {
        JsonBean jsonBean = null;
        
        if(param.getSjbgdgid() != null) {
        	TblYqnsSjbgSjbgdg bean = this.tblYqnsSjbgSjbgdgMapper.selectById(param.getSjbgdgid());
        	param.setProjectId(bean.getProjectId());
        	if(bean != null && bean.getProjectId() != null) {
        		String projectName = implementPlanMapper.selectProjectNameById(bean.getProjectId());
                param.setProjectName(projectName);
                
                //获取审理科人员
                TblYqnsEnginAuditProjectEntity enginProject = this.enginAuditProjectMapper.selectBySjbg(bean.getProjectId());
                
                if(enginProject != null) {
                	param.setRectPerson(new BigDecimal(enginProject.getFpslkryid()));
                	param.setRectPerName(enginProject.getFpslkryname());
                }else {
                	TblYqnsFundAuditProjectEntity fundProject = this.fundAuditProjectMapper.selectBySjbg(bean.getProjectId());
                	if(fundProject != null) {
                		param.setRectPerson(new BigDecimal(fundProject.getFpslkryid()));
                    	param.setRectPerName(fundProject.getFpslkryname());
                	}
                }
        	}
        }
        
        TblYqnsIssuesRecord record = null;
        
        if (param.getId() == null) {
            // 代表新增数据
            param.setId(RandomUtil.uuBigDecimalId());
            param.setStatus(0);
            Integer flag = auditIssueListMapper.insert(param);
            
            record = new TblYqnsIssuesRecord();
            record.setCreateTime(new Date());
            record.setIssuesId(param.getId());
            record.setVersion(1);
            record.setRecordId(RandomUtil.uuBigDecimalId());
            record.setRectPerson(param.getRectPerson());
            record.setRectPersonName(param.getRectPerName());
            this.tblYqnsIssuesRecordMapper.insert(record);
        } else {
            // 代表修改数据
            Integer flag = auditIssueListMapper.updateById(param);
            record = tblYqnsIssuesRecordMapper.selectMaxVersionByIssues(param.getId());
            record.setRectPerson(param.getRectPerson());
            record.setRectPersonName(param.getRectPerName());
            this.tblYqnsIssuesRecordMapper.updateById(record);
        }
        String fileIds = param.getFileIds();
        if(StringUtils.isNotBlank(fileIds)) {
        	 for (String s : fileIds.split(",")) {
                 tblAttachmentMapper.insertAttachmentByIssueId(s,param.getId());
             }
        }
        jsonBean = ResponseFormat.retParam(1,200, param);
        return jsonBean;
    }

    @Override
    public JsonBean getIssueById(BigDecimal issueId) {
        JsonBean jsonBean = null;
        TblYqnsIssueListEntity tblYqnsIssueListEntity = auditIssueListMapper.selectById(issueId);
        if (tblYqnsIssueListEntity != null) {
            //构建预留字段返回
            reservePropertyService.buildReserveProperty(tblYqnsIssueListEntity);

            HashMap<String, Object> result = new HashMap<>();
            List<TblAttachment> tblAttachmentList = tblAttachmentMapper.selectAttListByIssueId(issueId);
            result.put("baseData", tblYqnsIssueListEntity);
            result.put("fileData", tblAttachmentList);
            jsonBean = ResponseFormat.retParam(1, "查询成功", result);
        } else {
            jsonBean = ResponseFormat.retParam(0, "暂无此数据", null);
        }
        return jsonBean;
    }

    @Override
    public JsonBean getAllIssueInfo(Integer pageNumber,Integer pageSize,TblYqnsIssueListEntity param) {

		try {
			TblStaffUtil user = userProvider.get();
			if(user == null) {
	              return ResponseFormat.retParam(0,20006,null);
	        }
			
			HashMap<String, Object> result = new HashMap<>();

	        com.huabo.audit.util.PageInfo<TblYqnsIssueListEntity> info = new com.huabo.audit.util.PageInfo<TblYqnsIssueListEntity>();
	        PageInfo<TblYqnsIssueListEntity> pageInfo;
	        QueryWrapper<TblYqnsIssueListEntity> queryWrapper = new QueryWrapper<>();
	        if (StringUtils.isNotBlank(param.getIssueNumber())) {
	        	 queryWrapper.like("ISSUENUMBER", param.getIssueNumber());
	        }
	        if(StringUtils.isNotBlank(param.getRectClass())) {
	        	queryWrapper.eq("RECTCLASS", param.getIssueNumber());
	        }
	        if (StringUtils.isNotBlank(param.getProjectName())) {
	       	 queryWrapper.like("PROJECTNAME", param.getProjectName());
	       }
	        
	        if (StringUtils.isNotBlank(param.getUnitName())) {
	          	 queryWrapper.like("UNITNAME", param.getUnitName());
	        }
	        
	        if(user.getRoleNames()!=null && !user.getRoleNames().contains("审理")) {
	        	queryWrapper.eq("OPERATORID", user.getStaffid());
	        }
	        
	        pageInfo = PageMethod.startPage(pageNumber, pageSize, " ADDTIME desc ").doSelectPageInfo(() -> auditIssueListMapper.selectList(queryWrapper));

	        //构建预留字段返回
	        reservePropertyService.buildReserveProperty(pageInfo.getList());

	        // 构建返回值条件
	        info.setCurrentPage(pageInfo.getPageNum());
	        info.setPageSize(pageInfo.getPageSize());
	        info.setTotalRecord((int) pageInfo.getTotal());
	        info.setTlist(pageInfo.getList());
	        result.put("pageInfo", info);
	        return ResponseFormat.retParam(1, "查询成功", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
          
		 return ResponseFormat.retParam(0, "查失败询", null);
        

       
    }

    @Override
    public JsonBean deleteAttachFile(BigDecimal issueId, String fileIds) {
        String[] split = fileIds.split(",");
        for (String s : split) {
        	tblAttachmentMapper.deleteAttachmentByIssueIdAndAttId(s, issueId);
        }
       
        return ResponseFormat.retParam(1,"删除成功",null);
    }

    @Override
    public JsonBean delete(BigDecimal id) {
        Integer flag = auditIssueListMapper.deleteById(id);
        this.tblYqnsIssuesRecordMapper.deleteByIssuesId(id);
        return ResponseFormat.retParam(1,200,null);
    }

	@Override
	public JsonBean getPreAddInfo(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		
        LocalDateTime now = LocalDateTime.now();
        Period period = Period.ofMonths(-1);
        LocalDateTime lastMonth = now.minus(period);
        
        resultMap.put("timeLimit", lastMonth);
		resultMap.put("operatorId", loginStaff.getStaffid());
		resultMap.put("operator", loginStaff.getRealname());
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean excelUtils(HttpServletResponse response, String token, TblYqnsIssueListEntity param, List<String> idList)
			throws Exception {
		// 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        QueryWrapper<TblYqnsIssueListEntity> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(param.getIssueNumber())) {
        	 queryWrapper.like("ISSUENUMBER", param.getIssueNumber());
        }
        if(StringUtils.isNotBlank(param.getRectClass())) {
        	queryWrapper.eq("RECTCLASS", param.getIssueNumber());
        }
        if (CollectionUtils.isNotEmpty(idList)) {
            queryWrapper.in("ID", idList);
        }
        
        queryWrapper.orderByDesc("ID");
        
        List<TblYqnsIssueListEntity> list = auditIssueListMapper.selectList(queryWrapper);
        

        String[] titles = {"序号","审计报告定稿标题", "在报告中对应的编号","定性（定性词典）", "问题所属单位","问题涉及企业的管理层级",
        		"底稿名称","问题金额","审减金额","资产损失（万元）","风险程度","事实表述","问题定性",
        		"定性法规依据","处理意见或整改建议","问题发生年度","整改时限", "整改督促牵头部门或单位", 
        		"整改分类","创建人","创建时间"};
        Object[] obj = null;
        List<Object[]> objs = new ArrayList<Object[]>(0);
        Integer no = 1;
        for (TblYqnsIssueListEntity entity : list) {
            obj = new Object[titles.length];
            obj[0] = no;
            obj[1] = entity.getSjbgdgTitle();
            obj[2] = entity.getIssueNumber();
            obj[3] = entity.getQualitative(); 
            obj[4] = entity.getUnitName(); 
            obj[5] = entity.getWtsjglcj();
            obj[6] = entity.getDraft();
            obj[7] = entity.getMoney();
            obj[8] = entity.getReviewMoney();
            obj[9] = entity.getAssetLoss();
            obj[10] = entity.getRiskLevel();
            obj[11] = entity.getIssueDetail();
            obj[12] = entity.getProblemQualitative();
            obj[13] = entity.getQualitativeRule();
            obj[14] = entity.getCorrectPropose();
            obj[15] = entity.getProblemYear(); 
            obj[16] = entity.getTimeLimit()==null?"":entity.getTimeLimit(); 
            obj[17] = entity.getUrgeDepartment();
            obj[18] = entity.getRectClass();
            obj[19] = entity.getOperator(); 
            obj[20] = entity.getAddTime()==null?"":DateUtil.parseDate(entity.getAddTime(), DateUtil.DATE_SMALL_STR);
            objs.add(obj);
            no++;
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("问题清单".getBytes(), "UTF-8") + ".xlsx");
        ImportOrExportExcelUtil.exportExcel(titles, objs, response.getOutputStream(), null);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
	}

	@Override
	public JsonBean getAuditOrgList(String token, BigDecimal sjbgdgid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //通过审计报告获取被审计单位
        String auditOrgId = this.tblYqnsSjbgSjbgdgMapper.selectAuditOrgIdStrs(sjbgdgid);
        
        List<TblOrganization> orgList = this.tblYqnsSjbgSjbgdgMapper.selectAuditOrgListByIds(auditOrgId);
        
		return ResponseFormat.retParam(1, 200, orgList);
	}

	@Override
	public JsonBean assignment(String token, BigDecimal issueId, BigDecimal rectPerson, String rectPersonName)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblYqnsIssuesRecord record = null;
		TblYqnsIssueListEntity issues = null;
		issues = this.auditIssueListMapper.selectById(issueId);
		record = tblYqnsIssuesRecordMapper.selectMaxVersionByIssues(issues.getId());
		issues.setRectPerson(rectPerson);
		issues.setRectPerName(rectPersonName);
			//修改整改人
			record.setRectPerson(rectPerson);
			record.setRectPersonName(rectPersonName);
			this.tblYqnsIssuesRecordMapper.updateById(record);
		this.auditIssueListMapper.updateById(issues);
		return ResponseFormat.retParam(1, 200, null);
	}
}
