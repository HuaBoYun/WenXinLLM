package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_AUDITSTEPSQL")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjAuditStepSQLEntity {

//	private static final long serialVersionUID = 1L;

	@TableId(value = "sqlid", type= IdType.AUTO)
    @Schema
    private BigDecimal sqlId;
	
	@TableId(value = "sqlcontent")
    @Schema
    private String sqlContent;
	
	@TableField(value = "createtime")
    @Schema
    private Date createTime;

    @TableField(value = "updatetime")
    @Schema
    private Date updateTime;
    
    @TableField(value = "auditstep")
    @Schema
    private TblNbsjAuditStepEntity auditStep;
	
}
