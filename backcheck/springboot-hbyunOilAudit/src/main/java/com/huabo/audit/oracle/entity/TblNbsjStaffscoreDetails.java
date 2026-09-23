package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_NBSJ_STAFFSCORE_DETAILS")
@Schema(name="TBL_NBSJ_STAFFSCORE_DETAILS对象", description="审计-人员评分明细表")
public class TblNbsjStaffscoreDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "staffScore_details_id", type= IdType.AUTO)
    @Schema(name = "人员评分明细表主键")
    private BigDecimal staffScore_details_id;

    @TableField(value = "staffScore_id")
    @Schema(name = "人员评分表主键")
    private BigDecimal staffScore_id;

    @TableField(value = "Project")
    @Schema(name = "项目")
    private String project;

    @TableField(value = "Consideration")
    @Schema(name = "考虑因素")
    private String consideration;

    @TableField(value = "Grading")
    @Schema(name = "评分标准")
    private String grading;

    @TableField(value = "AuditTeamLeaderScore")
    @Schema(name = "审计组长评分")
    private String auditTeamLeaderScore;

    @TableField(value = "Remark")
    @Schema(name = "备注")
    private String remark;

   
}
