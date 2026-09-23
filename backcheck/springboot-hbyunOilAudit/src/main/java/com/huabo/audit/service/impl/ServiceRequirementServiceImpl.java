package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.entity.ServiceRequirementEntity;
import com.huabo.audit.oracle.mapper.ServiceRequirementMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.service.ServiceRequirementService;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.AutoNo;
import com.huabo.audit.util.PageInfoUtil;
/**
 * @author Rui
 * @ClassName ServiceSuggestionServiceImpl
 * @Description
 * @DATE 2023/9/6
 */
@Service
public class ServiceRequirementServiceImpl implements ServiceRequirementService {

    @Autowired
    private ServiceRequirementMapper serviceRequirementMapper;

    @Autowired
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String id, String auditItem, Integer queryYear,String projectType) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

        ServiceRequirementEntity serviceRequirementEntity = new ServiceRequirementEntity();
        if(StringUtil.isNotEmpty(id)){
//            serviceRequirementEntity.setId(new BigDecimal(id)); 
        	serviceRequirementEntity.setRequirementNo(new BigDecimal(id));
        }
        if(StringUtil.isNotEmpty(auditItem)){
            serviceRequirementEntity.setAuditItem(auditItem);
        }
        if(StringUtil.isNotEmpty(projectType)){
            serviceRequirementEntity.setProjectType(projectType);
        }

        if(StringUtils.isNotBlank(user.getDeptIds())) {
        	serviceRequirementEntity.setQueryDeptIds(user.getDeptIds());
        }
        serviceRequirementEntity.setCurrentStaffId(user.getStaffid());
        
        if(queryYear != null) {
        	serviceRequirementEntity.setQueryYear(queryYear);
        }

        Page<ServiceRequirementEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> serviceRequirementMapper.selectByEntity(serviceRequirementEntity));
		PageInfo<ServiceRequirementEntity> pageInfo = new PageInfoUtil<ServiceRequirementEntity>().parsePageInfo(page);
        return ResponseFormat.retParam(1,200,pageInfo);
    }
    
    @Override
    public List<ServiceRequirementEntity> findExprotList(String token, Integer pageNumber, Integer pageSize, String id, String auditItem, Integer queryYear,String ids) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return null;
        }

        ServiceRequirementEntity serviceRequirementEntity = new ServiceRequirementEntity();
        if(StringUtil.isNotEmpty(id)){
            serviceRequirementEntity.setId(new BigDecimal(id));
        }
        if(StringUtil.isNotEmpty(auditItem)){
            serviceRequirementEntity.setAuditItem(auditItem);
        }

        if(StringUtils.isNotBlank(user.getDeptIds())) {
        	serviceRequirementEntity.setQueryDeptIds(user.getDeptIds());
        }
        serviceRequirementEntity.setCurrentStaffId(user.getStaffid());
        
        if(queryYear != null) {
        	serviceRequirementEntity.setQueryYear(queryYear);
        }
        if(StringUtils.isNotBlank(ids)) {
            serviceRequirementEntity.setIds(ids);
        }

        List<ServiceRequirementEntity> list = serviceRequirementMapper.selectByEntity(serviceRequirementEntity);
        return list;
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        ServiceRequirementEntity serviceRequirementEntity = serviceRequirementMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", serviceRequirementEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public void updateEntity(ServiceRequirementEntity serviceRequirementEntity) throws Exception{
        serviceRequirementMapper.updateEntity(serviceRequirementEntity);
    }

    @Override
    public void saveEntity(String token, ServiceRequirementEntity serviceRequirementEntity, int type) throws Exception{
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            serviceRequirementEntity.setCreateUser(tblStaff);
        }
        if((type == 1 && serviceRequirementEntity.getRequirementNo() == null) || type == 0) {
        	Integer currentYear = LocalDate.now().getYear();
            BigDecimal maxNo = this.serviceRequirementMapper.selectMaxAutoNo(currentYear);
            BigDecimal autoNo = AutoNo.getAutoNo(currentYear, maxNo);
            serviceRequirementEntity.setRequirementNo(autoNo);
            serviceRequirementMapper.insertEntity(serviceRequirementEntity);
        }else {
        	 serviceRequirementMapper.insertEntity(serviceRequirementEntity);
        }
    }

    @Override
    public void deleteByIds(String ids) throws Exception{

        serviceRequirementMapper.deleteEntity(ids);
    }

    @Override
    public void resolveSheet(XSSFSheet sheet, String token, Integer isCover) throws Exception {

        XSSFRow row = null;
        XSSFCell cell = null;

        List<TblOrganization> tblOrganizations = tblOrganizationMapper.selectByExport();
        ServiceRequirementEntity preEntity = null;
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
            row = sheet.getRow(i);
            if (row != null) {
                ServiceRequirementEntity serviceRequirementEntity = new ServiceRequirementEntity();

                TblStaffUtil user = userProvider.get();
                if(user != null) {
                    TblStaff tblStaff = new TblStaff();
                    tblStaff.setStaffid(user.getStaffid());
                    serviceRequirementEntity.setCreateUser(tblStaff);
                }

                cell = row.getCell(0);
                if(cell != null){
                    cell.setCellType(1);
                    serviceRequirementEntity.setRequirementNo(StringUtils.isBlank(cell.getStringCellValue())?null:new BigDecimal(cell.getStringCellValue()));
                }
                
                
                cell = row.getCell(1);
                if(cell != null){
                    cell.setCellType(1);
                    String cellValue = cell.getStringCellValue();
                    if(StringUtil.isNotEmpty(cellValue)){
                        List<TblOrganization> collect = tblOrganizations.stream().filter(t -> {
                            return cellValue.equals(t.getOrgname());
                        }).collect(Collectors.toList());
                        if(collect.size() > 0){
                            TblOrganization tblOrganization = collect.get(0);
                            serviceRequirementEntity.setOrganization(tblOrganization);
                            serviceRequirementEntity.setOrganizationId(tblOrganization.getOrgid());
                        }
                    }
                }
                
                
                cell = row.getCell(2);
                if(cell != null){
                    cell.setCellType(1);
                    serviceRequirementEntity.setAuditItem(cell.getStringCellValue());
                }

                cell = row.getCell(3);
                if(cell != null){
                    cell.setCellType(1);
                    serviceRequirementEntity.setAuditPurpose(cell.getStringCellValue());
                }

                cell = row.getCell(4);
                if(cell != null){
                    cell.setCellType(1);
                    serviceRequirementEntity.setConcernsContent(cell.getStringCellValue());
                }

                

                cell = row.getCell(5);
                if(cell != null){
                    cell.setCellType(1);
                    serviceRequirementEntity.setUnitRange(cell.getStringCellValue());
                }

                cell = row.getCell(6);
                if(cell != null){
                    cell.setCellType(1);
                    serviceRequirementEntity.setTimeRange(cell.getStringCellValue());
                }

                cell = row.getCell(7);
                if(cell != null){
                    cell.setCellType(1);
                    serviceRequirementEntity.setProjectType(cell.getStringCellValue());
                }

                cell = row.getCell(8);
                if(cell != null){
                    cell.setCellType(1);
                    serviceRequirementEntity.setRemark(cell.getStringCellValue());
                }

                if(serviceRequirementEntity.getRequirementNo() != null) {
                	preEntity = this.serviceRequirementMapper.selectRequirementNoEntity(serviceRequirementEntity.getRequirementNo());
                	 if(preEntity != null) {
                		 if(isCover == 0) {
                			 continue;
                		 }
                		 serviceRequirementEntity.setId(preEntity.getId());
                         updateEntity(serviceRequirementEntity);
                	 }else {
                		 saveEntity(token, serviceRequirementEntity,1);
                	 }
                }else {
                	saveEntity(token, serviceRequirementEntity,1);
                }
                
            }
        }
    }

    @Override
    public void distribute(String ids, String personIds) throws Exception {
        String[] idArr = ids.split(",");

        for (String id : idArr){
            ServiceRequirementEntity serviceRequirementEntity = serviceRequirementMapper.selectById(id);
            if(serviceRequirementEntity != null){
                serviceRequirementEntity.setPersonIds(personIds);
                this.updateEntity(serviceRequirementEntity);
            }
        }

    }

	@Override
	public JsonBean getAutoNo(String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
		
		//获取今年年份
        Integer currentYear = LocalDate.now().getYear();
        BigDecimal maxNo = this.serviceRequirementMapper.selectMaxAutoNo(currentYear);
        BigDecimal autoNo = AutoNo.getAutoNo(currentYear, maxNo);
        return ResponseFormat.retParam(1,200,autoNo);
	}
}
