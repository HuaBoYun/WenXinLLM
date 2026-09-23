package com.huabo.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.LeaveAudit2LMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.service.LeaveAudit2LService;
import com.huabo.audit.util.AutoNo;
import com.huabo.audit.util.PageInfoUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author Rui
 * @ClassName LeaveAudit2LServiceImpl
 * @Description
 * @DATE 2023/9/14
 */
@Service
public class LeaveAudit2LServiceImpl implements LeaveAudit2LService {

    @Autowired
    private LeaveAudit2LMapper leaveAudit2LMapper;

    @Autowired
    private TblOrganizationMapper tblOrganizationMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String projectName, String auditOrg, Integer queryYear) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        LeaveAudit2LEntity leaveAudit2LEntity = new LeaveAudit2LEntity();
        if (StringUtil.isNotEmpty(projectName)) {
            leaveAudit2LEntity.setProjectName(projectName);
        }
        if (StringUtil.isNotEmpty(auditOrg)) {
            TblOrganization tblOrganization = new TblOrganization();
            tblOrganization.setOrgname(auditOrg);
            leaveAudit2LEntity.setAuditOrg(tblOrganization);
        }

        if(StringUtils.isNotBlank(user.getDeptIds())) {
        	leaveAudit2LEntity.setQueryDeptIds(user.getDeptIds());
        }
        leaveAudit2LEntity.setCurrentStaffId(user.getStaffid());
        
        if(queryYear != null) {
        	leaveAudit2LEntity.setQueryYear(queryYear);
        }
        
        Page<LeaveAudit2LEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> leaveAudit2LMapper.selectByEntity(leaveAudit2LEntity));
        PageInfo<LeaveAudit2LEntity> pageInfo = new PageInfoUtil<LeaveAudit2LEntity>().parsePageInfo(page);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());
        return ResponseFormat.retParam(1, 200, pageInfo);
    }
    
    @Override
	public JsonBean getListByDraftPlan(String token, Integer pageNumber, Integer pageSize, String projectName,
			String auditOrg, Integer queryYear, Integer sourceType, BigDecimal jhid) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        LeaveAudit2LEntity leaveAudit2LEntity = new LeaveAudit2LEntity();
        if (StringUtil.isNotEmpty(projectName)) { 
            leaveAudit2LEntity.setProjectName(projectName);
        }
        if (StringUtil.isNotEmpty(auditOrg)) {
            TblOrganization tblOrganization = new TblOrganization();
            tblOrganization.setOrgname(auditOrg);
            leaveAudit2LEntity.setAuditOrg(tblOrganization);
        }

        if(queryYear != null) {
        	leaveAudit2LEntity.setQueryYear(queryYear);
        }
        
//        leaveAudit2LEntity.setStatus(6);
        Page<LeaveAudit2LEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> leaveAudit2LMapper.selectListByDraftPlan(leaveAudit2LEntity,sourceType,jhid));
        PageInfo<LeaveAudit2LEntity> pageInfo = new PageInfoUtil<LeaveAudit2LEntity>().parsePageInfo(page);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());
        return ResponseFormat.retParam(1, 200, pageInfo);
	}
    
    @Override
	public List<LeaveAudit2LEntity> findExportList(String token, Integer pageNumber, Integer pageSize,
			String projectName, String auditOrg, Integer queryYear,String ids) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return null;
        }

        LeaveAudit2LEntity leaveAudit2LEntity = new LeaveAudit2LEntity();
        if (StringUtil.isNotEmpty(projectName)) {
            leaveAudit2LEntity.setProjectName(projectName);
        }
        if (StringUtil.isNotEmpty(auditOrg)) {
            TblOrganization tblOrganization = new TblOrganization();
            tblOrganization.setOrgname(auditOrg);
            leaveAudit2LEntity.setAuditOrg(tblOrganization);
        }

        if(StringUtils.isNotBlank(user.getDeptIds())) {
        	leaveAudit2LEntity.setQueryDeptIds(user.getDeptIds());
        }
        leaveAudit2LEntity.setCurrentStaffId(user.getStaffid());
        
        if(queryYear != null) {
        	leaveAudit2LEntity.setQueryYear(queryYear);
        }
        if(StringUtil.isNotEmpty(ids)) {
            leaveAudit2LEntity.setIds(ids);
        }
        
        List<LeaveAudit2LEntity> list = leaveAudit2LMapper.selectByEntity(leaveAudit2LEntity);
        return list;
	}

    @Override
    public JsonBean findById(String id) throws Exception {

        LeaveAudit2LEntity leaveAudit2LEntity = leaveAudit2LMapper.selectById(id);
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(leaveAudit2LEntity);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", leaveAudit2LEntity);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean updateEntity(LeaveAudit2LEntity leaveAudit2LEntity) throws Exception {
        leaveAudit2LMapper.updateEntity(leaveAudit2LEntity);
        return ResponseFormat.retParam(1,200,leaveAudit2LEntity);
    }

    @Override
    public JsonBean saveEntity(String token, LeaveAudit2LEntity leaveAudit2LEntity, int type) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            leaveAudit2LEntity.setCreateUser(tblStaff);
        }
        
        if((type == 1 && leaveAudit2LEntity.getLeaveNo() == null) || type == 0) {
        	Integer currentYear = LocalDate.now().getYear();
            BigDecimal maxNo = this.leaveAudit2LMapper.selectMaxAutoNo(currentYear);
            BigDecimal autoNo = AutoNo.getAutoNo(currentYear, maxNo);
            leaveAudit2LEntity.setLeaveNo(autoNo);
        }
        
        leaveAudit2LMapper.insertEntity(leaveAudit2LEntity);
        return ResponseFormat.retParam(1,200,leaveAudit2LEntity);
    }

    @Override
    public void deleteByIds(String ids) throws Exception {

        leaveAudit2LMapper.deleteEntity(ids);
    }

    @Override
    public void resolveSheet(XSSFSheet sheet, String token, Integer isCover) throws Exception {

        XSSFRow row = null;
        XSSFCell cell = null;
        LeaveAudit2LEntity preEntity = null;
        List<TblOrganization> tblOrganizations = tblOrganizationMapper.selectByExport();
        //"序号", "审计项目名称", "被审计单位","审计任职期间","委托书编号","委托时间"
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
                LeaveAudit2LEntity leaveAudit2LEntity = new LeaveAudit2LEntity();

                TblStaffUtil user = userProvider.get();
                if(user == null) {
                    TblStaff tblStaff = new TblStaff();
                    tblStaff.setStaffid(user.getStaffid());
                    leaveAudit2LEntity.setCreateUser(tblStaff);
                }
                
                cell = row.getCell(0);
                if(cell != null){
                    cell.setCellType(1);
                    leaveAudit2LEntity.setLeaveNo(StringUtils.isBlank(cell.getStringCellValue())?null:new BigDecimal(cell.getStringCellValue()));
                }

                cell = row.getCell(1);
                if (cell != null) {
                    cell.setCellType(1);
                    leaveAudit2LEntity.setProjectName(cell.getStringCellValue());
                }

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
                            leaveAudit2LEntity.setAuditOrgId(tblOrganization.getOrgid() + "");
                        }
                    }
                }

                cell = row.getCell(3);
                if (cell != null) {
                    cell.setCellType(1);
                    String timeRange = cell.getStringCellValue();
                    if (StringUtil.isNotEmpty(timeRange)) {
                        String[] timeArr = timeRange.split("~");
                        if (timeArr.length == 2) {
                            leaveAudit2LEntity.setAuditStartTime(DateUtil.parse(timeArr[0].trim(), "yyyy-MM-dd").toSqlDate());
                            leaveAudit2LEntity.setAuditEndTime(DateUtil.parse(timeArr[1].trim(), "yyyy-MM-dd").toSqlDate());
                        }

                    }
                }

                cell = row.getCell(4);
                if (cell != null) {
                    cell.setCellType(1);
                    leaveAudit2LEntity.setEntrustNo(cell.getStringCellValue());
                }

                cell = row.getCell(5);
                if (cell != null) {
                    cell.setCellType(1);
                    leaveAudit2LEntity.setEntrustTime(DateUtil.parse(cell.getStringCellValue().trim(), "yyyy-MM-dd").toSqlDate());
                }

                if(leaveAudit2LEntity.getLeaveNo() != null) {
                	preEntity = this.leaveAudit2LMapper.selectRequirementNoEntity(leaveAudit2LEntity.getLeaveNo());
                	 if(preEntity != null) {
                		 if(isCover == 0) {
                			 continue;
                		 }
                		 leaveAudit2LEntity.setId(preEntity.getId());
                         updateEntity(leaveAudit2LEntity);
                	 }else {
                		 saveEntity(token, leaveAudit2LEntity,1);
                	 }
                }else {
                	saveEntity(token, leaveAudit2LEntity,1);
                }
            }
        }
    }

    @Override
    public void distribute(String ids, String personIds) throws Exception {
        String[] idArr = ids.split(",");

        for (String id : idArr) {
            LeaveAudit2LEntity leaveAudit2LEntity = leaveAudit2LMapper.selectById(id);
            if (leaveAudit2LEntity != null) {
                leaveAudit2LEntity.setPersonIds(personIds);
                this.updateEntity(leaveAudit2LEntity);
            }
        }

    }


    /**
     * 通过ids查询 二级单位及成员单位离任审计
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    public   List<LeaveAudit2LEntity> findByIds(String ids)   {
        // 二级单位及成员单位离任审计 通过ids
        List<LeaveAudit2LEntity> beanList = leaveAudit2LMapper.findByIds(ids);
        return beanList;
    }

	@Override
	public JsonBean getAutoNo(String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
		
		//获取今年年份
        Integer currentYear = LocalDate.now().getYear();
        BigDecimal maxNo = this.leaveAudit2LMapper.selectMaxAutoNo(currentYear);
        BigDecimal autoNo = AutoNo.getAutoNo(currentYear, maxNo);
        return ResponseFormat.retParam(1,200,autoNo);
	}
}
