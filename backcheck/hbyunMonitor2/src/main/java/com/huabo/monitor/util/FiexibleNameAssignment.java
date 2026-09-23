package com.huabo.monitor.util;

import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import cn.hutool.extra.spring.SpringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="灵活字段名称类赋值通用接口")
@Component
public class FiexibleNameAssignment {
	/*
	 * @param t 关联主表的联名称
	 * 
	 * */
//	public static String setRealnameSql(String t){
//		StringBuffer buffer=new StringBuffer();
//		buffer.append(",staffid1.realname as staffidname1,staffid2.realname as staffidname2,staffid3.realname as staffidname3,staffid4.realname as staffidname4,staffid5.realname as staffidname5");
//		buffer.append(",(SELECT WM_CONCAT(realname) FROM TBL_STAFF  staffids1 WHERE ','||").append(t).append(".staffids1||',' LIKE '%,'||staffids1.staffid||',%') AS staffidsname1");
//		buffer.append(",(SELECT WM_CONCAT(realname) FROM TBL_STAFF  staffids2 WHERE ','||").append(t).append(".staffids2||',' LIKE '%,'||staffids2.staffid||',%') AS staffidsname2");
//		buffer.append(",(SELECT WM_CONCAT(realname) FROM TBL_STAFF  staffids3 WHERE ','||").append(t).append(".staffids3||',' LIKE '%,'||staffids3.staffid||',%') AS staffidsname3");
//		buffer.append(",(SELECT WM_CONCAT(realname) FROM TBL_STAFF  staffids4 WHERE ','||").append(t).append(".staffids4||',' LIKE '%,'||staffids4.staffid||',%') AS staffidsname4");
//		buffer.append(",(SELECT WM_CONCAT(realname) FROM TBL_STAFF  staffids5 WHERE ','||").append(t).append(".staffids5||',' LIKE '%,'||staffids5.staffid||',%') AS staffidsname5");
//		buffer.append(",orgid1.orgname as orgidname1,orgid2.orgname as orgidname2,orgid3.orgname as orgidname3,orgid4.orgname as orgidname4,orgid5.orgname as orgidname5");
//		buffer.append(",(SELECT WM_CONCAT(orgname) FROM TBL_ORGANIZATION  orgids1  WHERE ','||").append(t).append(".orgids1||',' LIKE '%,'||orgids1.orgid||',%') AS orgidsname1");
//		buffer.append(",(SELECT WM_CONCAT(orgname) FROM TBL_ORGANIZATION  orgids2  WHERE ','||").append(t).append(".orgids2||',' LIKE '%,'||orgids2.orgid||',%') AS orgidsname2");
//		buffer.append(",(SELECT WM_CONCAT(orgname) FROM TBL_ORGANIZATION  orgids3  WHERE ','||").append(t).append(".orgids3||',' LIKE '%,'||orgids3.orgid||',%') AS orgidsname3");
//		buffer.append(",(SELECT WM_CONCAT(orgname) FROM TBL_ORGANIZATION  orgids4  WHERE ','||").append(t).append(".orgids4||',' LIKE '%,'||orgids4.orgid||',%') AS orgidsname4");
//		buffer.append(",(SELECT WM_CONCAT(orgname) FROM TBL_ORGANIZATION  orgids5  WHERE ','||").append(t).append(".orgids5||',' LIKE '%,'||orgids5.orgid||',%') AS orgidsname5");
//		return buffer.toString();
//	}
	
	/*
	 * @param t 关联主表的联名称
	 * 
	 * */
//	public static String setLeftSql(String t){
//		StringBuffer LeftBuffer=new StringBuffer();
//		LeftBuffer.append(" left join tbl_staff staffid1 on staffid1.STAFFID=").append(t).append(".STAFFID1 ");
//		LeftBuffer.append(" left join tbl_staff staffid2 on staffid2.STAFFID=").append(t).append(".STAFFID2 ");
//		LeftBuffer.append(" left join tbl_staff staffid3 on staffid3.STAFFID=").append(t).append(".STAFFID3 ");
//		LeftBuffer.append(" left join tbl_staff staffid4 on staffid4.STAFFID=").append(t).append(".STAFFID4 ");
//		LeftBuffer.append(" left join tbl_staff staffid5 on staffid5.STAFFID=").append(t).append(".STAFFID5 ");
//		LeftBuffer.append(" left join TBL_ORGANIZATION orgid1 on orgid1.orgid=").append(t).append(".orgid1 ");
//		LeftBuffer.append(" left join TBL_ORGANIZATION orgid2 on orgid2.orgid=").append(t).append(".orgid2 ");
//		LeftBuffer.append(" left join TBL_ORGANIZATION orgid3 on orgid3.orgid=").append(t).append(".orgid3 ");
//		LeftBuffer.append(" left join TBL_ORGANIZATION orgid4 on orgid4.orgid=").append(t).append(".orgid4 ");
//		LeftBuffer.append(" left join TBL_ORGANIZATION orgid5 on orgid5.orgid=").append(t).append(".orgid5 ");
//		return LeftBuffer.toString();
//	}
 
	
	public  fieldOrgStaffName setOpenName(fieldOrgStaffId idEntity){
		fieldOrgStaffName entity=new fieldOrgStaffName();
		try {
			 ITblStaffService staffService =SpringUtil.getBean(ITblStaffService.class);
			 TblOrganizaService tblOrganizaService =SpringUtil.getBean(TblOrganizaService.class);

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
