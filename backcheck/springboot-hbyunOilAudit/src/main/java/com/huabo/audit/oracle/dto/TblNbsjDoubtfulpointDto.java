package com.huabo.audit.oracle.dto;

import java.util.Date;

import org.springframework.data.annotation.Transient;

import com.huabo.audit.oracle.entity.TblNbsjDoubtfulpointEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="疑点管理保存对象")
public class TblNbsjDoubtfulpointDto {
	
	private TblNbsjDoubtfulpointEntity tblNbsjDoubtfulpointEntity;
	
	@Schema(name = "ID")
	@Transient
	private Integer dpointid;
	
	@Schema(name = "疑点编号")
	@Transient
	private String dpnumber;
	
	@Schema(name = "疑点名称")
	@Transient
	private String dpname;
	
	@Schema(name = "编制人")
	@Transient
	private String editor;
	
	@Schema(name = "编制时间")
	@Transient
	private Date edittime;
	
	@Schema(name = "疑点描述")
	@Transient
	private String dpdescribe;
	
	@Schema(name = "备注")
	@Transient
	private String memo;
	
	@Schema(name = "测试结果")
	@Transient
	private String testresult;
	
	@Schema(name = "疑点状态")
	@Transient
	private String dpstatus;
	
}
