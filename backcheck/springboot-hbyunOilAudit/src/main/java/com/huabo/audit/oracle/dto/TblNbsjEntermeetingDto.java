package com.huabo.audit.oracle.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Transient;

import com.baomidou.mybatisplus.annotation.TableField;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjEntermeetingEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblStaff;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="进场纪要保存对象")
public class TblNbsjEntermeetingDto {
	
	private TblNbsjEntermeetingEntity tblNbsjEntermeetingEntity;
	
	@Schema(name = "ID")
	@Transient
	private String enterid;
	
	@Schema(name = "进场纪要编号")
	@Transient
	private String entercoed;
	
	@Schema(name = "进场纪要名称")
	@Transient
	private String entername;
	
	@Schema(name = "内容")
	@Transient
	private String content;
	
	@Schema
	@Transient
	private Date creatrtime;
	
	@Schema
	@Transient
	private String status;
	
	@Schema
	@Transient
	private String createstaffid;
	
	//==

}
