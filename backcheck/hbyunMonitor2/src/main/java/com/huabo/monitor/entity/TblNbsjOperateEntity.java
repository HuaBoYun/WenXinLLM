package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@TableName("TBL_NBSJ_OPERATE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjOperateEntity {
	
	public static final Integer FINISH = 1;
	public static final Integer UNFINISH = 0;
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")  
	@TableId(value = "OPERATEID", type= IdType.AUTO)
    @Schema
    private Integer operateid;

    @TableField(value = "finish")
    @Schema
    private Integer finish;
    
    @TableField(value = "finishTime")
    @Schema
    private Date finishtime;
    
    @TableField(value = "AUTHID")
    @Schema
    private Integer authId;
    
    @TableField(value = "SHETTID")
    @Schema
    private Integer sheetId;
    
    @Schema
    @Transient
    private TblNbsjAuthorizationEntity authorization;
    
    @Schema
    @Transient
    private TblNbsjSheetEntity nbsjSheet;
    
    @TableField(value = "businessType")
    @Schema(hidden=true)
    @Transient
    private String businessType;
    
    @TableField(value = "riskAttrbution")
    @Schema(hidden=true)
    @Transient
    private String riskAttrbution;
    
    @TableField(value = "suditProcess")
    @Schema(hidden=true)
    private String suditProcess;
   //@IgnoreSwaggerParameter
    
    @TableField(value = "programid")
    @Schema(hidden=true)
    @Transient
    private String programid;
    
    @TableField(value = "renyuan")
    @Schema(hidden=true)
    @Transient
    private String renyuan;
    
    @Schema(hidden=true)
    @Transient
    private String control;
    
    @Schema(hidden=true)
    @Transient
    private String riskPoint;
    
    @Schema(hidden=true)
    @Transient
    private String riskSource;
    
    
}
