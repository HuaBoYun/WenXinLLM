package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.cybermonitor.util.IgnoreSwaggerParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@TableName("TBL_NBSJ_LEAVEMEETING")
@Data
@Schema(name="离场纪要实体类")
@Accessors(chain = true)
public class TblNbsjLeavemeetingEntity {
	
	public final static Integer NO_DEL=0;//未删除
	public final static Integer YE_DEL=1;//已删除

	@TableId(value = "leaveid", type= IdType.AUTO)
    @Schema
    private BigDecimal leaveid;

    @TableField(value = "leavecoed")
    @Schema(name = "离场纪要编号")
    private String leavecoed;
    
    @TableField(value = "leavename")
    @Schema(name = "离场纪要名称")
    private String leavename;
    
    @TableField(value = "creatrtime")
    @Schema(name = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date creatrtime;
    
    @TableField(value = "content")
    @Schema(name = "富文本框")
    private String content;
    
	@TableField(value = "tblCreater")
    @Schema(hidden=true)
	@IgnoreSwaggerParameter
    private Staff tblCreater;
	
	@TableField(value = "project")
    @Schema(hidden=true)
	@IgnoreSwaggerParameter
    private TblNbsjProject project;
	
	@TableField(value = "status")
    @Schema(name="状态",hidden=true)
	@IgnoreSwaggerParameter
    private Integer status;
	
	@TableField(value = "tblLeaveAtts")
    @Schema(hidden=true)
	@IgnoreSwaggerParameter
    private Set<TblAttachment> tblLeaveAtts;
	
	@TableField(value = "createstaffid")
    @Schema(hidden=true)
	@IgnoreSwaggerParameter
    private String createstaffid;
}
