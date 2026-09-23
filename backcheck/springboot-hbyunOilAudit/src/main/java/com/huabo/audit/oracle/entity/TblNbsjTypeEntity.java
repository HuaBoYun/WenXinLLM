package com.huabo.audit.oracle.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_AUDITPLAN")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjTypeEntity {
	
	@TableId(value = "templeteid", type= IdType.AUTO)
    @Schema
    private Integer templeteId;

    @TableField(value = "templetecode")
    @Schema(name = "底稿编号")
    private String templeteCode;

    @TableField(value = "templetename")
    @Schema
    private String templeteName;

    @TableField(value = "templetetype")
    @Schema
    private String templeteType;

    @TableField(value = "templetedesc")
    @Schema
    private String templeteDesc;

    @TableField(value = "staffid")
    @Schema
    private Integer staffId;

    @TableField(value = "createdate")
    @Schema
    private Date createDate;

    @TableField(value = "updatedate")
    @Schema
    private Date updateDate;

    @TableField(value = "updatestaffid")
    @Schema
    private Integer updateStaffId;

    @TableField(value = "status")
    @Schema
    private Integer status;

    @TableField(value = "temptype")
    @Schema
    private Integer tempType;

    @TableField(value = "orgid")
    @Schema
    private Integer orgId;
}
