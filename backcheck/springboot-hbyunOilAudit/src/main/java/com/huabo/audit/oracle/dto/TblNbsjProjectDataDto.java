package com.huabo.audit.oracle.dto;

import java.util.Date;

import org.springframework.data.annotation.Transient;

import com.huabo.audit.oracle.entity.TblNbsjProjectDataEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="项目资料准备保存对象")
public class TblNbsjProjectDataDto {
	
	private TblNbsjProjectDataEntity tblNbsjProjectDataEntity;
	
	@Schema(name = "ID")
	@Transient
	private Integer dataId;
	
	@Schema(name = "所属项目")
	@Transient
	private Integer projectId;
	
	@Schema(name = "资料名称")
	@Transient
	private String dataName;
	
	@Schema
	@Transient
	private String dataCapacity;
	
	@Schema
	@Transient
	private Date dataDate;
	
	
}
