package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_NBSJ_PLANPROJECT")
@Schema(name="TblNbsjPlanprojectMySql对象")
public class TblNbsjPlanprojectMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("PLANPROJECTID")
    private BigDecimal planprojectid;

    @TableField("PROJECTNAME")
    private String projectname;

    @TableField("TARGETNAME")
    private String targetname;

    @TableField("FINISHTIME")
    private LocalDateTime finishtime;

    @TableField("ORGIDS")
    private String orgids;

    @TableField("ORGIDNAMES")
    private String orgidnames;

    @TableField("EXTERNALASSIG")
    private String externalassig;

    @TableField("PLANID")
    private BigDecimal planid;

    @TableField("BSJTYPE")
    private String bsjtype;

    @TableField("STARTTIME")
    private LocalDateTime starttime;

    @TableField("PROJECTMONEY")
    private String projectmoney;

    @TableField("AUDITTYPE")
    private String audittype;


}
