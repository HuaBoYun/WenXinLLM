package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.cybermonitor.util.IgnoreSwaggerParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 会议纪要
 */

@TableName("TBL_NBSJ_ENTERMEETING")
@Data
@Schema(name="进场纪要实体类")
@Accessors(chain = true)
public class TblNbsjEntermeetingEntity {
	
	public final static Integer NO_DEL=0;//未删除
	public final static Integer YE_DEL=1;//已删除
	
    @TableId(value = "enterid", type= IdType.AUTO)
    @Schema(name = "纪要id")
    private BigDecimal enterid;

    @TableField(value = "entercoed")
    @Schema(name = "进场纪要编号")
    private String entercoed;

    @TableField(value = "entername")
    @Schema(name = "进场纪要名称")
    private String entername;

    @TableField(value = "creatrtime")
    @Schema(name = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date creatrtime;

    @TableField(value = "status")
    @Schema(name="状态",hidden=true)
    private String status;

    @TableField(value = "content")
    @Schema(name = "内容")
    private String content;
    
    @TableField(value = "createstaffid")
    @Schema
    private String createstaffid;

    
    @TableField(exist = false)
    @Transient
    @Schema(name="附件信息",hidden=true)
    @IgnoreSwaggerParameter
    private List<TblAttachment> attachmentInfoList;
    
    @TableField(exist = false)
    @Transient
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private Set<TblAttachment> tblMetAtts;
    
    @TableField(exist = false)
    @Transient
    @Schema(name="项目",hidden=true)
    @IgnoreSwaggerParameter
    private TblNbsjProject project;
    
    @Transient
    @TableField(exist = false)
    @Schema(name="创建人",hidden=true)
    @IgnoreSwaggerParameter
    private Staff tblCreater;
    
    
}
