package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;
import com.huabo.audit.oracle.entity.ProjectProposalEvaluationEntity;
import com.huabo.audit.oracle.entity.ProjectSuggestionEntity;
import com.huabo.audit.oracle.entity.ServiceRequirementEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.ProjectProposalEvaluationMapper;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.service.ProjectProposalEvaluationService;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.PageInfoUtil;
/**
 * @author CJ
 * @ClassName ProjectProposalEvaluationServiceImpl
 * @Description
 * @DATE 2024/5/29
 */
@Service
public class ProjectProposalEvaluationServiceImpl implements ProjectProposalEvaluationService {

    @Autowired
    private ProjectProposalEvaluationMapper ProjectProposalEvaluationMapper;
    @Resource
 	private TblAttachmentMapper tblAttachmentMapper;
    @Autowired
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String id, String projectName, String projectType,String projectPurpose,String createyear,BigDecimal tbid,String ids) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

        ProjectProposalEvaluationEntity ProjectProposalEvaluationEntity = new ProjectProposalEvaluationEntity();
        
        ProjectProposalEvaluationEntity.setCreateUser(user.getStaffid());
        if(user.getDeptIds()!=null) {
        	 String[] split = user.getDeptIds().split(",");
             ProjectProposalEvaluationEntity.setDeptids(Arrays.asList(split));
        }
       
        if(StringUtil.isNotEmpty(id)){
            ProjectProposalEvaluationEntity.setId(new BigDecimal(id));
        }
		if(StringUtil.isNotEmpty(ids)){
			List<String> idsList = Arrays.stream(ids.split(","))
					.collect(Collectors.toList());
			ProjectProposalEvaluationEntity.setIds(idsList);
		}
        if(StringUtil.isNotEmpty(projectName)){
            ProjectProposalEvaluationEntity.setProjectName(projectName);
        }
        if(StringUtil.isNotEmpty(projectType)){
            ProjectProposalEvaluationEntity.setProjectType(projectType);
        }
        
        if(StringUtil.isNotEmpty(projectPurpose)){
        	ProjectProposalEvaluationEntity.setProjectPurpose(projectPurpose);
        }

        if(createyear!=null && createyear.length()>0) {
        	 
        	Date startdate = DateUtil.formatDate(createyear+"-01-01", "yyyy-MM-dd");
        	Date enddate = DateUtil.formatDate(createyear+"-12-31", "yyyy-MM-dd");
        	
        	ProjectProposalEvaluationEntity.setStartyear(startdate);
        	ProjectProposalEvaluationEntity.setEndyear(enddate);
        }
        ProjectProposalEvaluationEntity.setTbid(tbid);

        
        Page<ProjectProposalEvaluationEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> ProjectProposalEvaluationMapper.findList(ProjectProposalEvaluationEntity));
		PageInfo<ProjectProposalEvaluationEntity> pageInfo = new PageInfoUtil<ProjectProposalEvaluationEntity>().parsePageInfo(page);
        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(BigDecimal id) throws Exception{

        ProjectProposalEvaluationEntity projectProposalEvaluationEntity = ProjectProposalEvaluationMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", projectProposalEvaluationEntity);
		List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByProjectEvaluationAtt(id);
		resultMap.put("attList", attList);
        return ResponseFormat.retParam(1,200,resultMap);
    }

	@Override
	public JsonBean updateEntity(ProjectProposalEvaluationEntity projectProposalEvaluationEntity,String attids) throws Exception {
		
		ProjectProposalEvaluationMapper.updateByPrimaryKeySelective(projectProposalEvaluationEntity);
		if (attids != null && !"".equals(attids)) {
			this.tblAttachmentMapper.deleteAttachmentByProjectEvaluationAtt(projectProposalEvaluationEntity.getId());
			String[] ids = attids.split(",");
			for (String id : ids) {
				this.tblAttachmentMapper.insertAttachmentByProjectEvaluationAttId(new BigDecimal(id), projectProposalEvaluationEntity.getId());
			}
		}
		 return ResponseFormat.retParam(1, "保存成功", projectProposalEvaluationEntity);
	}

	@Override
	public JsonBean saveEntity(String token, ProjectProposalEvaluationEntity projectProposalEvaluationEntity,String attids)
			throws Exception {
	    TblStaffUtil user = userProvider.get();
        if(user != null) {
        	projectProposalEvaluationEntity.setCreateUser(user.getStaffid());
        	projectProposalEvaluationEntity.setId(null);
        	projectProposalEvaluationEntity.setCreateTime(new Date());
            ProjectProposalEvaluationMapper.insertSelective(projectProposalEvaluationEntity);
        	if (attids != null && !"".equals(attids)) {
    			String[] ids = attids.split(",");
    			for (String id : ids) {
    				this.tblAttachmentMapper.insertAttachmentByProjectEvaluationAttId(new BigDecimal(id), projectProposalEvaluationEntity.getId());
    			}
    		}
        }
        return ResponseFormat.retParam(1, "保存成功", projectProposalEvaluationEntity);
	}

	@Override
	public void deleteByIds(BigDecimal ids) throws Exception {
		// TODO Auto-generated method stub
		ProjectProposalEvaluationMapper.deleteByPrimaryKey(ids);
		this.tblAttachmentMapper.deleteAttachmentByProjectEvaluationAtt(ids);
	}
 
	
	  @Override
	    public JsonBean getLxZxsjList(String token, Integer pageNumber, Integer pageSize,ProjectProposalEvaluationEntity projectProposalEvaluationEntity) throws Exception {
	        TblStaffUtil user = userProvider.get();
	        if(user == null) {
	            return ResponseFormat.retParam(0,20006,null);
	        }
	        
	        projectProposalEvaluationEntity.setStatus(6);
	        
	        if(StringUtils.isNotBlank(projectProposalEvaluationEntity.getProjectName())) {
	        	projectProposalEvaluationEntity.setProjectName("%"+projectProposalEvaluationEntity.getProjectName()+"%");
	        }
	        
	        Page<ProjectProposalEvaluationEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> ProjectProposalEvaluationMapper.getLxZxsjList(projectProposalEvaluationEntity));
			PageInfo<ProjectProposalEvaluationEntity> pageInfo = new PageInfoUtil<ProjectProposalEvaluationEntity>().parsePageInfo(page);
	        return ResponseFormat.retParam(1,200,pageInfo);
	    }

	@Override
	public List<ProjectProposalEvaluationEntity> findByIds(List<String> ids) {
		 List<ProjectProposalEvaluationEntity> beanList = ProjectProposalEvaluationMapper.findByIds(ids);
	        return beanList;
	}

	
	  @Override
	    public JsonBean resolveSheet(XSSFSheet sheet,String token) throws Exception {
	        XSSFRow row = null;
	        XSSFCell cell = null;
	        TblStaffUtil user = userProvider.get();
            if(user != null) {
                TblStaff tblStaff = new TblStaff();
                tblStaff.setStaffid(user.getStaffid());
            }
            List<TblOrganization> tblOrganizations = tblOrganizationMapper.selectList(null);
            List<ProjectProposalEvaluationEntity> list=new ArrayList<ProjectProposalEvaluationEntity>();
	        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
	            row = sheet.getRow(i);
	            if (row != null) {
            		ProjectProposalEvaluationEntity entity=new ProjectProposalEvaluationEntity();
	            	for(int j=0;j<9;j++){
	            		
	            		cell = row.getCell(0);
	   	                if(cell != null){
	   	                  cell.setCellType(1); //排序
	   	                 entity.setSortNumber(Integer.valueOf(cell.getStringCellValue())); //业务类型
	   	                }
	            		
	            		
	            		 cell = row.getCell(1);
	   	                if(cell != null){
	   	                  cell.setCellType(1);
	   	                //业务类型"
	   	                 entity.setProjectType(cell.getStringCellValue()); //业务类型
	   	                }
	   	                //建议科室
	   	                cell = row.getCell(2);
	                     if (cell != null) {
	                     cell.setCellType(1);
	                     String cellValue = cell.getStringCellValue();
	                     if (StringUtil.isNotEmpty(cellValue)) {
	                         List<TblOrganization> collect = tblOrganizations.stream().filter(t -> {
	                             return cellValue.equals(t.getOrgname());
	                         }).collect(Collectors.toList());
	                         if (collect.size() > 0) {
	                             TblOrganization tblOrganization = collect.get(0);
	                             entity.setDepartmentId(tblOrganization.getOrgid());
	                             entity.setDepartmentName(cellValue);

	                         }
	                     }
	                 }
	   	                
	                     cell = row.getCell(3);
		   	                if(cell != null){
		   	                    cell.setCellType(1); 
		   	                 entity.setItemType(cell.getStringCellValue()); //项目类型
		   	                }
	   	                
	            	
	   	               cell = row.getCell(4);
	   	                if(cell != null){
	   	                    cell.setCellType(1); 
	   	                 entity.setProjectName(cell.getStringCellValue()); //审计项目名称
	   	                }
	   	              cell = row.getCell(5);
	   	                if(cell != null){
	   	                    cell.setCellType(1); 
	   	                 entity.setProjectPurpose(cell.getStringCellValue()); //立项理由及审计目的
	   	                }
	   	               cell = row.getCell(6);
	   	                if(cell != null){
	   	                    cell.setCellType(1); 
	   	                 entity.setConcernsContent(cell.getStringCellValue()); //重点关注内容
	   	                }
	            	
	   	             //单位范围
	   	             cell = row.getCell(7);
	                 if (cell != null) {
	                     cell.setCellType(1);
	                     String cellValue = cell.getStringCellValue();
	                     if (StringUtil.isNotEmpty(cellValue)) {
	                         List<TblOrganization> collect = tblOrganizations.stream().filter(t -> {
	                             return cellValue.equals(t.getOrgname());
	                         }).collect(Collectors.toList());
	                         if (collect.size() > 0) {
	                             TblOrganization tblOrganization = collect.get(0);
	                             entity.setUnitRangeId(tblOrganization.getOrgid());
	                             entity.setUnitRange(cellValue);

	                         }
	                     }
	                 }
	   	                
	   	                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	   	                
	   	                cell = row.getCell(8);
	   	                if(cell != null){
	   	                 Date timeRange = sdf.parse(cell.getStringCellValue());
	   	                if(timeRange!=null){
	   	                  entity.setTimeRangel(timeRange); //时间范围/起始
	   	                }
	   	                }
	   	                
	   	                cell = row.getCell(9);
	   	               if(cell != null){
	   	            	 Date timeRange = sdf.parse(cell.getStringCellValue());
		   	                if(timeRange!=null){
		   	                  entity.setTimeRangeR(timeRange); //时间范围/结束
		   	                }
	   	                }
	   	                
	            	}
	            	entity.setCreateUser(user.getStaffid());
	            	entity.setCreateTime(new Date());
	                saveEntity(token,entity,"");
	                list.add(entity);
	            }
	        }
	        Map<String,Object> resultMap = new HashMap<>();
	        resultMap.put("data", list);
	        return ResponseFormat.retParam(1,200,resultMap);
	    }

	@Override
	public void updateAuditScope(BigDecimal id, String auditScope) throws Exception {
		// TODO Auto-generated method stub
		ProjectProposalEvaluationEntity entity=ProjectProposalEvaluationMapper.selectById(id);
		entity.setAuditScope(auditScope);
		ProjectProposalEvaluationMapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public JsonBean getLxZxsjHzChooseList(String token, Integer pageNumber, Integer pageSize,
			ProjectProposalEvaluationEntity projectProposalEvaluationEntity) throws Exception {
		TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        
        Page<ProjectProposalEvaluationEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> ProjectProposalEvaluationMapper.selectLxZxsjHzChooseList(projectProposalEvaluationEntity));
		PageInfo<ProjectProposalEvaluationEntity> pageInfo = new PageInfoUtil<ProjectProposalEvaluationEntity>().parsePageInfo(page);
        return ResponseFormat.retParam(1,200,pageInfo);
	}

	@Override
	public JsonBean setLxZxsjHzShowList(String token, String idStrs) throws Exception {
		TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        
        this.ProjectProposalEvaluationMapper.updateShowListByIds(idStrs);
        
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean getLxZxsjHzDetailList(String token, Integer pageNumber, Integer pageSize,
			ProjectProposalEvaluationEntity projectProposalEvaluationEntity) throws Exception {
		TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        Page<ProjectProposalEvaluationEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> ProjectProposalEvaluationMapper.selectLxZxsjHzDetailList(projectProposalEvaluationEntity));
		PageInfo<ProjectProposalEvaluationEntity> pageInfo = new PageInfoUtil<ProjectProposalEvaluationEntity>().parsePageInfo(page);
        return ResponseFormat.retParam(1,200,pageInfo);
	}
}
