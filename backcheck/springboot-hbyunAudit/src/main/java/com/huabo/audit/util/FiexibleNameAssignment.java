package com.huabo.audit.util;

import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huabo.audit.service.TblOrganizaService;
import com.huabo.audit.service.TblStaffService;
import com.huabo.audit.service.impl.TblOrganizaServiceImpl;
import com.huabo.audit.service.impl.TblStaffServiceImpl;
import com.huabo.audit.vo.param.fieldOrgStaffId;
import com.huabo.audit.vo.param.fieldOrgStaffName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="灵活字段名称类赋值通用接口")
@Component
public class FiexibleNameAssignment {
	 
 
	
	public  fieldOrgStaffName setOpenName(fieldOrgStaffId idEntity){
		fieldOrgStaffName entity=new fieldOrgStaffName();
		try {
			 TblStaffService staffService =new TblStaffServiceImpl();
			 TblOrganizaService tblOrganizaService =new TblOrganizaServiceImpl();

			if(Objects.nonNull(idEntity.getStaffid1())){
			    entity.setStaffidname1(staffService.selectNameByids(idEntity.getStaffid1())) ;
			}
			if(Objects.nonNull(idEntity.getStaffid2())){
		 	    entity.setStaffidname2(staffService.selectNameByids(idEntity.getStaffid2()));
			}
			if(Objects.nonNull(idEntity.getStaffid3())){
			        entity.setStaffidname3(staffService.selectNameByids(idEntity.getStaffid3()));
			}
			if(Objects.nonNull(idEntity.getStaffid4())){
			        entity.setStaffidname4(staffService.selectNameByids(idEntity.getStaffid4()));
			}
			if(Objects.nonNull(idEntity.getStaffid5())){
			        entity.setStaffidname5(staffService.selectNameByids(idEntity.getStaffid5()));
			}
			if(StringUtils.isNotBlank(idEntity.getStaffids1())){
			        entity.setStaffidsname1(staffService.selectNamesByids(idEntity.getStaffids1()));
			}
			if(StringUtils.isNotBlank(idEntity.getStaffids2())){
			        entity.setStaffidsname2(staffService.selectNamesByids(idEntity.getStaffids2()));
			}
			if(StringUtils.isNotBlank(idEntity.getStaffids3())){
			        entity.setStaffidsname3(staffService.selectNamesByids(idEntity.getStaffids3()));
			}
			if(StringUtils.isNotBlank(idEntity.getStaffids4())){
			        entity.setStaffidsname4(staffService.selectNamesByids(idEntity.getStaffids4()));
			}
			if(StringUtils.isNotBlank(idEntity.getStaffids5())){
			        entity.setStaffidsname5(staffService.selectNamesByids(idEntity.getStaffids5()));
			}
			if(Objects.nonNull(idEntity.getOrgid1())){
			        entity.setOrgidname1(tblOrganizaService.selectNameByids(idEntity.getOrgid1())); 
			}
			if(Objects.nonNull(idEntity.getOrgid2())){
			        entity.setOrgidname2(tblOrganizaService.selectNameByids(idEntity.getOrgid2())); 
			}
			if(Objects.nonNull(idEntity.getOrgid3())){
			        entity.setOrgidname3(tblOrganizaService.selectNameByids(idEntity.getOrgid3())); 
			}
			if(Objects.nonNull(idEntity.getOrgid4())){
			        entity.setOrgidname4(tblOrganizaService.selectNameByids(idEntity.getOrgid4())); 
			}
			if(Objects.nonNull(idEntity.getOrgid5())){
			        entity.setOrgidname5(tblOrganizaService.selectNameByids(idEntity.getOrgid5())); 
			}
			if(StringUtils.isNotBlank(idEntity.getOrgids1())){
			        entity.setOrgidsname1(tblOrganizaService.selectNamesByids(idEntity.getOrgids1())); 
			}
			if(StringUtils.isNotBlank(idEntity.getOrgids2())){
			        entity.setOrgidsname2(tblOrganizaService.selectNamesByids(idEntity.getOrgids2())); 
			}
			if(StringUtils.isNotBlank(idEntity.getOrgids3())){
			        entity.setOrgidsname3(tblOrganizaService.selectNamesByids(idEntity.getOrgids3())); 
			}
			if(StringUtils.isNotBlank(idEntity.getOrgids4())){
			        entity.setOrgidsname4(tblOrganizaService.selectNamesByids(idEntity.getOrgids4())); 
			}
			if(StringUtils.isNotBlank(idEntity.getOrgids5())){
			        entity.setOrgidsname5(tblOrganizaService.selectNamesByids(idEntity.getOrgids5())); 
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}

}
