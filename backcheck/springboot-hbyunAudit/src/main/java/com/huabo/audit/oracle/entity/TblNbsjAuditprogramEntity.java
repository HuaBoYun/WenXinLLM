package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述: 
 * author: ziyao
 * date: 2022-04-20
 */
@TableName("TBL_NBSJ_AUDITPROGRAM")
@Data
@Schema(name="审计指引-实体类")
@Accessors(chain = true)
public class TblNbsjAuditprogramEntity {

    @TableId(value = "programid", type= IdType.INPUT)
    @Schema(name = "主键")
    private BigDecimal programId;

    @TableField(value = "targetid")
    @Schema(name = "模板目录id", required = true)
    private BigDecimal targetId;

    @TableField(value = "businesstype")
    @Schema(name = "业务单元", required = true)
    private String businessType;

    @TableField(value = "risksource")
    @Schema(name = "风险归属")
    private String riskSource;

    @TableField(value = "suditprocess")
    @Schema(name = "审计程序", required = true)
    private String suditProcess;

    @TableField(value = "riskpoint")
    @Schema(name = "风险描述", required = true)
    private String riskPoint;

    @TableField(value = "tempid")
    @Schema(name="模板id",hidden=true)
    private BigDecimal tempId;

    @TableField(value = "updatetime")
    @Schema(name="修改时间",hidden=true)
    private Date updateTime;

    @TableField(value = "createtime")
    @Schema(name="创建时间",hidden=true)
    private Date createTime;

    @TableField(value = "biodata")
    @Schema(name = "所需资料")
    private String bioData;

    @TableField(value = "status")
    @Schema(hidden=true)
    private Integer status;

    @TableField(value = "control")
    @Schema(name = "控制措施", required = true)
    private String control;

    @TableField(value = "programcode")
    @Schema(hidden=true)
    private String programCode;

    @TableField(value = "isuseprogram")
    @Schema(hidden=true)
    private Integer isUseProgram;

    @Schema(name="员工名称",hidden=true)
    private String staffName;
}
