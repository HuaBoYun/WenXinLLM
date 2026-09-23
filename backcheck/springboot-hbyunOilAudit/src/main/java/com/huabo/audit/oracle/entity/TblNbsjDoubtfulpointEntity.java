package com.huabo.audit.oracle.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_DOUBTFULPOINT")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjDoubtfulpointEntity {

//	private Set tblAttachments = new HashSet(0);
	
	@TableId(value = "dpointid", type = IdType.AUTO)
    @Schema
    private Integer dpointid;

    @TableField(value = "dpnumber")
    @Schema
    private String dpnumber;
    
    @TableField(value = "editor")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private String editor;
    
    @TableField(value = "edittime")
    @Schema(hidden=true)
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date edittime;
    
	@TableField(value = "dpdescribe")
    @Schema
    private String dpdescribe;

	@TableField(value = "memo")
    @Schema
    private String memo;
	
	@TableField(value = "dpname")
    @Schema
    private String dpname;
	
	@TableField(value = "testresult")
    @Schema
    private String testresult;
	
	@TableField(value = "dpstatus")
    @Schema
    private String dpstatus;
	
	@TableField(value = "dpbysystem")
    @Schema
    private String dpbysystem;
	
	@TableField(value = "orgid")
    @Schema(hidden=true)
	@IgnoreSwaggerParameter
    private Integer orgid;
	
	
	@TableField(value = "projectid")
    @Schema
    private Integer projectid;
	
	
}
