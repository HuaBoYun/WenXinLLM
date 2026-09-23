package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.ProjectSuggestionEntity;
import com.huabo.audit.oracle.entity.ServiceRequirementEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.ProjectSuggestionMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.service.ProjectSuggestionService;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.PageInfoUtil;
/**
 * @author Rui
 * @ClassName ProjectSuggestionServiceImpl
 * @Description
 * @DATE 2023/9/6
 */
@Service
public class ProjectSuggestionServiceImpl implements ProjectSuggestionService {

    @Autowired
    private ProjectSuggestionMapper projectSuggestionMapper;
    
    @Autowired
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String id, String projectName, String projectType, String createYear, String ids,Integer status) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

        ProjectSuggestionEntity projectSuggestionEntity = new ProjectSuggestionEntity();
        if(StringUtil.isNotEmpty(id)){
            projectSuggestionEntity.setId(new BigDecimal(id));
        }
        if(StringUtil.isNotEmpty(projectName)){
            projectSuggestionEntity.setProjectName(projectName);
        }
        if(StringUtil.isNotEmpty(projectType)){
            projectSuggestionEntity.setProjectType(projectType);
        }
        if(StringUtils.isNotBlank(user.getDeptIds())){
        	projectSuggestionEntity.setQueryDeptIds(user.getDeptIds());
        }
        if(status != null) {
        	projectSuggestionEntity.setStatus(status);
        }
        if(StringUtils.isNotBlank(ids)){
            projectSuggestionEntity.setIds(ids);
        }



        
        TblStaff loginuser = new TblStaff();
        loginuser.setStaffid(user.getStaffid());
        loginuser.setOrgid(user.getLinkDetp().getOrgid());
        projectSuggestionEntity.setCreateUser(loginuser);

        Page<ProjectSuggestionEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> projectSuggestionMapper.selectByEntity(projectSuggestionEntity,createYear));
		PageInfo<ProjectSuggestionEntity> pageInfo = new PageInfoUtil<ProjectSuggestionEntity>().parsePageInfo(page);

        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        ProjectSuggestionEntity projectSuggestionEntity = projectSuggestionMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", projectSuggestionEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public void updateEntity(ProjectSuggestionEntity projectSuggestionEntity) throws Exception{
        projectSuggestionMapper.updateEntity(projectSuggestionEntity);
    }

    @Override
    public JsonBean saveEntity(String token, ProjectSuggestionEntity projectSuggestionEntity) throws Exception{
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff(); 
            tblStaff.setStaffid(user.getStaffid());
            projectSuggestionEntity.setCreateUser(tblStaff);
        }
        projectSuggestionMapper.insertEntity(projectSuggestionEntity);
        return ResponseFormat.retParam(1,200,projectSuggestionEntity);
    }

    @Override
    public void deleteByIds(String ids) throws Exception{

        projectSuggestionMapper.deleteEntity(ids);
    }

    @Override
    public void resolveSheet(XSSFSheet sheet,String token) throws Exception {
        XSSFRow row = null;
        XSSFCell cell = null;
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
            row = sheet.getRow(i);
            if (row != null) {
                ProjectSuggestionEntity projectSuggestionEntity = new ProjectSuggestionEntity();

                TblStaffUtil user = userProvider.get();
                if(user != null) {
                    TblStaff tblStaff = new TblStaff();
                    tblStaff.setStaffid(user.getStaffid());
                    projectSuggestionEntity.setCreateUser(tblStaff);
                }

                
                cell = row.getCell(1);
                if(cell != null){
                    cell.setCellType(1);
                    List<TblOrganization> byname = tblOrganizationMapper.findByname(cell.getStringCellValue());
                	if(byname!=null && byname.size()>0) {
                		projectSuggestionEntity.setTborgid(byname.get(0).getOrgid());
                	}
                    
                    projectSuggestionEntity.setTborgname(cell.getStringCellValue());
                }
                
                
                cell = row.getCell(2);
                if(cell != null){
                    cell.setCellType(1);
                    projectSuggestionEntity.setProjectName(cell.getStringCellValue());
                }

                
                cell = row.getCell(3);
                if(cell != null){
                    cell.setCellType(1);
                    projectSuggestionEntity.setProjectPurpose(cell.getStringCellValue());
                }

                
              
                cell = row.getCell(4);
                if(cell != null){
                    cell.setCellType(1);
                    projectSuggestionEntity.setConcernsContent(cell.getStringCellValue());
                }
              
               
                cell = row.getCell(5);
                if(cell != null){
                    cell.setCellType(1);
                    projectSuggestionEntity.setUnitRange(cell.getStringCellValue());
                }

                cell = row.getCell(6);
                if(cell != null){
                    cell.setCellType(1);
                    projectSuggestionEntity.setTimeRange(cell.getStringCellValue());
                }

                cell = row.getCell(7);
                if(cell != null){
                    cell.setCellType(1);
                    projectSuggestionEntity.setProjectType(cell.getStringCellValue());
                }
                
                cell = row.getCell(8);
                if(cell != null){
                    cell.setCellType(1);
                    projectSuggestionEntity.setRemark(cell.getStringCellValue());
                }

                saveEntity(token, projectSuggestionEntity);
            }
        }
    }

    @Override
    public void distribute(String ids, String personIds) throws Exception {
        String[] idArr = ids.split(",");

        for (String id : idArr){
            ProjectSuggestionEntity projectSuggestionEntity = projectSuggestionMapper.selectById(id);
            if(projectSuggestionEntity != null){
                projectSuggestionEntity.setPersonIds(personIds);
                this.updateEntity(projectSuggestionEntity);
            }
        }

    }
}
