package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.InterimAuditDetailEntity;
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.InterimAuditDetailMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.service.InterimAuditDetailService;
import com.huabo.audit.util.PageInfoUtil;

import cn.hutool.core.date.DateUtil;

/**
 * @author Rui
 * @ClassName InterimAuditDetailServiceImpl
 * @Description
 * @DATE 2024/04/15
 */
@Service
public class InterimAuditDetailServiceImpl implements InterimAuditDetailService {

    @Autowired
    private InterimAuditDetailMapper interimAuditDetailMapper;

    @Autowired
    private TblStaffMapper tblStaffMapper;
    
    @Autowired
    private TblOrganizationMapper  tblOrganizationMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
	public List<InterimAuditDetailEntity> findByIds(String idStrs) throws Exception {
    	// 二级单位及成员单位离任审计 通过ids
        List<InterimAuditDetailEntity> beanList = interimAuditDetailMapper.findByIds(idStrs);
        return beanList;
	}

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String orgName, String teamLeaderId, String projectName,String createyear,BigDecimal tbid,String ids) throws Exception {
        TblStaffUtil user = userProvider.get();
            if(user == null) {
                return ResponseFormat.retParam(0,20006,null);
        }

        InterimAuditDetailEntity interimAuditDetailEntity = new InterimAuditDetailEntity();
        interimAuditDetailEntity.setCreateUserId(user.getStaffid().toString());

        if(StringUtil.isNotEmpty(orgName)){
            TblOrganization org = new TblOrganization();
            org.setOrgname(orgName);
            interimAuditDetailEntity.setOrg(org);
        }

        if(StringUtil.isNotEmpty(projectName)){
            interimAuditDetailEntity.setProjectName(projectName);
        }

        if(StringUtil.isNotEmpty(createyear)){
            interimAuditDetailEntity.setCreateyear(createyear);
        }

        if(StringUtils.isNotBlank(user.getDeptIds())){
            interimAuditDetailEntity.setQueryDeptIds(user.getDeptIds());
        }

        if(StringUtil.isNotEmpty(teamLeaderId)){
            interimAuditDetailEntity.setTeamLeaderId(teamLeaderId);
        }
        if(tbid!=null){
            interimAuditDetailEntity.setTbid(tbid);
        }
        if(StringUtil.isNotEmpty(ids)){
            interimAuditDetailEntity.setIds(ids);
        }
 

        Page<InterimAuditDetailEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> interimAuditDetailMapper.selectByEntity(interimAuditDetailEntity));
		PageInfo<InterimAuditDetailEntity> pageInfo = new PageInfoUtil<InterimAuditDetailEntity>().parsePageInfo(page);


        return ResponseFormat.retParam(1,200,pageInfo);
    }
    
	@Override
	public JsonBean getRzsjmxListDraftPlan(String token, Integer pageNumber, Integer pageSize, String orgName,
			String teamLeaderId, String projectName, String createyear, BigDecimal tbid, Integer sourceType, BigDecimal jhid) throws Exception {
		TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
	    }
	
	    InterimAuditDetailEntity interimAuditDetailEntity = new InterimAuditDetailEntity();
	    interimAuditDetailEntity.setCreateUserId(user.getStaffid().toString());
	
	    if(StringUtil.isNotEmpty(orgName)){
	        TblOrganization org = new TblOrganization();
	        org.setOrgname(orgName);
	        interimAuditDetailEntity.setOrg(org);
	    }
	
	    if(StringUtil.isNotEmpty(projectName)){
	        interimAuditDetailEntity.setProjectName(projectName);
	    }
	
	    if(StringUtil.isNotEmpty(createyear)){
	        interimAuditDetailEntity.setCreateyear(createyear);
	    }
	
	    if(StringUtils.isNotBlank(user.getDeptIds())){
	        interimAuditDetailEntity.setQueryDeptIds(user.getDeptIds());
	    }
	
	    if(StringUtil.isNotEmpty(teamLeaderId)){
	        interimAuditDetailEntity.setTeamLeaderId(teamLeaderId);
	    }
	    if(tbid!=null){
	        interimAuditDetailEntity.setTbid(tbid);
	    }
	    
	    interimAuditDetailEntity.setStatus(6);
	    interimAuditDetailEntity.setProjectType("是");
	    
	    Page<InterimAuditDetailEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> interimAuditDetailMapper.getListDraftPlan(interimAuditDetailEntity,sourceType,jhid));
		PageInfo<InterimAuditDetailEntity> pageInfo = new PageInfoUtil<InterimAuditDetailEntity>().parsePageInfo(page);
	    return ResponseFormat.retParam(1,200,pageInfo);
	}

    @Override
    public JsonBean findById(String id) throws Exception{

        InterimAuditDetailEntity interimAuditDetailEntity = interimAuditDetailMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", interimAuditDetailEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public JsonBean updateEntity(InterimAuditDetailEntity interimAuditDetailEntity) throws Exception{
    	if(interimAuditDetailEntity.getOrg()!=null) {
    		String newname=interimAuditDetailEntity.getOrg().getOrgname()+interimAuditDetailEntity.getLdzw()+interimAuditDetailEntity.getLdname()+"任中经济责任审计";
            interimAuditDetailEntity.setProjectName(newname);
    	}
        interimAuditDetailMapper.updateEntity(interimAuditDetailEntity);
        return ResponseFormat.retParam(1, "保存成功", interimAuditDetailEntity);
    }

    @Override
    public JsonBean saveEntity(String token, InterimAuditDetailEntity interimAuditDetailEntity) throws Exception{
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            interimAuditDetailEntity.setCreateUser(tblStaff);
        }
        if(interimAuditDetailEntity.getOrg()!=null) {
    		String newname=interimAuditDetailEntity.getOrg().getOrgname()+interimAuditDetailEntity.getLdzw()+interimAuditDetailEntity.getLdname()+"任中经济责任审计";
            interimAuditDetailEntity.setProjectName(newname);
    	}
        if(interimAuditDetailEntity.getOrgId()!=null) {
        	TblOrganization org = tblOrganizationMapper.findById(interimAuditDetailEntity.getOrgId());
        	String newname=org.getOrgname()+interimAuditDetailEntity.getLdzw()+interimAuditDetailEntity.getLdname()+"任中经济责任审计";
            interimAuditDetailEntity.setProjectName(newname);
        	
        }
        
        interimAuditDetailMapper.insertEntity(interimAuditDetailEntity);
        return ResponseFormat.retParam(1, "保存成功", interimAuditDetailEntity);
    }

    @Override
    public void deleteByIds(String ids) throws Exception{

        interimAuditDetailMapper.deleteEntity(ids);
    }

    public Date parseDate(String date){
        return DateUtil.parse(date.trim(),"yyyy-MM-dd").toSqlDate();
    }

    @Override
    public void distribute(String ids, String personIds) throws Exception {
        String[] idArr = ids.split(",");

        for (String id : idArr){
            InterimAuditDetailEntity interimAuditDetailEntity = interimAuditDetailMapper.selectById(id);
            if(interimAuditDetailEntity != null){
                interimAuditDetailEntity.setPersonIds(personIds);
                this.updateEntity(interimAuditDetailEntity);
            } 
        }

    }
    
    @Override
    public JsonBean resolveSheet(XSSFSheet sheet,String token) throws Exception {
        XSSFRow row = null;
        XSSFCell cell = null;
        List<InterimAuditDetailEntity> list=new ArrayList<InterimAuditDetailEntity>();
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
            row = sheet.getRow(i);
            if (row != null) {
            	InterimAuditDetailEntity interimAuditDetailEntity = new InterimAuditDetailEntity();
                TblStaffUtil user = userProvider.get();
                if(user != null) {
                    TblStaff tblStaff = new TblStaff();
                    tblStaff.setStaffid(user.getStaffid());
                    interimAuditDetailEntity.setCreateUser(tblStaff);
                }
//                interimAuditDetailEntity.setCreateTime(new java.util.Date());
                
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


                cell = row.getCell(0);
                if(cell != null){
                    cell.setCellType(1);
                    interimAuditDetailEntity.setSerialNumber(new BigDecimal(cell.getStringCellValue()));
                }
                cell = row.getCell(1);
                if(cell != null){
                    cell.setCellType(1);
                    
                    TblOrganization tblOrg = interimAuditDetailMapper.selectOrgByName(cell.getStringCellValue());
                    interimAuditDetailEntity.setOrgId(tblOrg.getOrgid());
                }
                
                cell = row.getCell(2);
                if(cell != null){
                    cell.setCellType(1);
                    interimAuditDetailEntity.setAuditInfo(cell.getStringCellValue());
                }
                cell = row.getCell(3);
                if(cell != null){
                    cell.setCellType(1);
						interimAuditDetailEntity.setAuditTime(cell.getStringCellValue());
                }
                cell = row.getCell(4);
                if(cell != null){
                    cell.setCellType(1);
                    try {
						interimAuditDetailEntity.setUnauditYear(cell.getStringCellValue());
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }

                cell = row.getCell(5);
                if(cell != null){
                    cell.setCellType(1);
                    try {
						interimAuditDetailEntity.setUnauditMonth(Integer.valueOf(cell.getStringCellValue()));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }

                cell = row.getCell(6);
                if(cell != null){
                    cell.setCellType(1);
                    interimAuditDetailEntity.setProjectName(cell.getStringCellValue());
                }

                cell = row.getCell(7);
                if(cell != null){
                    cell.setCellType(1);
                    interimAuditDetailEntity.setProjectType(cell.getStringCellValue());
                }

                cell = row.getCell(8);
                if(cell != null){
                    cell.setCellType(1);
                    interimAuditDetailEntity.setTextarea(cell.getStringCellValue());
                }
                
                cell = row.getCell(9);
                if(cell != null){
                    cell.setCellType(1);
						interimAuditDetailEntity.setWorkStartTime(cell.getStringCellValue());
                }
                
                cell = row.getCell(10);
                if(cell != null){
                    cell.setCellType(1);
						interimAuditDetailEntity.setWorkEndTime(cell.getStringCellValue());
                }
                
                cell = row.getCell(11);
                if(cell != null){
                    cell.setCellType(1);
						interimAuditDetailEntity.setDoAuditTime(cell.getStringCellValue());
                }
                
                cell = row.getCell(12);
                if(cell != null){
                    cell.setCellType(1);
                    interimAuditDetailEntity.setTeamLeaderId(cell.getStringCellValue());
                }
                
                cell = row.getCell(13);
                if(cell != null){
                    cell.setCellType(1);
                    interimAuditDetailEntity.setSubTeamLeaderId(cell.getStringCellValue());
                }
                
                cell = row.getCell(14);
                if(cell != null){
                    cell.setCellType(1);
                    interimAuditDetailEntity.setLeaderId(cell.getStringCellValue());
                }
                
                cell = row.getCell(15);
                if(cell != null){
                    cell.setCellType(1);
                    interimAuditDetailEntity.setChiefReviewerId(cell.getStringCellValue());
                }
                
                cell = row.getCell(16);
                if(cell != null){
                    cell.setCellType(1);
                    interimAuditDetailEntity.setDeputyReviewerId(cell.getStringCellValue());
                }
                
                cell = row.getCell(17);
                if(cell != null){
                    cell.setCellType(1);
                    interimAuditDetailEntity.setProjectlx(cell.getStringCellValue());
                }
                
                cell = row.getCell(18);
                if(cell != null){
                    cell.setCellType(1);
                    interimAuditDetailEntity.setRemarks(cell.getStringCellValue());
                }
                
                saveEntity(token, interimAuditDetailEntity);
                list.add(interimAuditDetailEntity);
            }
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", list);
        return ResponseFormat.retParam(1,200,resultMap);
    }

}
