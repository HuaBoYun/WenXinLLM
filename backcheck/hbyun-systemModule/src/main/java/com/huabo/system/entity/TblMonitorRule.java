package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_MONITOR_RULE")
@Schema(name="TblMonitorRule对象", description="")
public class TblMonitorRule implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value="RULEID",type = IdType.INPUT)
      private BigDecimal ruleid;

    @TableField("RULENAME")
    private String rulename;

    @TableField("RULEDESCRIPTION")
    private String ruledescription;

    @TableField("RULECODE")
    private String rulecode;

    @TableField("RULEPRIORITY")
    private String rulepriority;

    @TableField("RISKLEVEL")
    private String risklevel;

    @TableField("SATUS")
    private String satus;

    @TableField("REGEXP")
    private String regexp;

    @TableField("RULESQL")
    private String rulesql;

    @TableField("MEMO")
    private String memo;

    @TableField("INRULEDB")
    private String inruledb;

    @TableField("TIPS")
    private String tips;

    @TableField("ORGID")
    private BigDecimal orgid;

    @TableField("CONNECTIONSTRINGS")
    private String connectionstrings;

    @TableField("RUNSTATUS")
    private BigDecimal runstatus;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("REPORTSQL")
    private String reportsql;



//TblMonitorSolution

    @Transient
    private BigDecimal solutionid;
    @Transient
    private String solutioncode;
    @Transient
    private String solutionname;
    @Transient
    private String solutionstatus;
    @Transient
    private Date createdate;
    @Transient
    private BigDecimal type;
    @Transient
    private String exefrequncy;
}
