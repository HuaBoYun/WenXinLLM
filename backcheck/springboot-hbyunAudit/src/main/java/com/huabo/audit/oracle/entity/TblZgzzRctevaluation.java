package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 整改评价信息表
 * </p>
 *
 * @author LHP
 * @since 2023-11-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_ZGZZ_RCTEVALUATION")
@Schema(name="TblZgzzRctevaluation对象", description="整改评价信息表")
@Table(name = "TBL_ZGZZ_RCTEVALUATION")
public class TblZgzzRctevaluation implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
      @TableField("EVALID")
      @Id
      @Column(name="EVALID")
    private BigDecimal evalId;

      @Schema(name = "创建人主键")
      @TableField("CREATESTAFF")
      @Column(name="CREATESTAFF")
    private BigDecimal createStaff;

      @Schema(name = "创建时间")
      @TableField("CREATETIME")
      @Column(name="CREATETIME")
      @JSONField(format = "yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

      @Schema(name = "修改人主键")
      @TableField("UPDATESTAFF")
      @Column(name="UPDATESTAFF")
    private BigDecimal updateStaff;

      @Schema(name = "修改时间")
      @TableField("UPDATETIME")
      @Column(name="UPDATETIME")
      @JSONField(format = "yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

      @Schema(name = "所属部门主键")
      @TableField("LINKDEPT")
      @Column(name="LINKDEPT")
    private BigDecimal linkDept;

      @Schema(name = "所属公司主键")
      @TableField("LINKORG")
      @Column(name="LINKORG")
    private BigDecimal linkOrg;

      @Schema(name = "审批状态 0-未评价 1-审批中 2-已退回 3-已撤销 6-已完成")
      @TableField("STATUS")
      @Column(name="STATUS")
    private Integer status;

      @Schema(name = "检查过程")
      @TableField("INSPECTIONPROCESS")
      @Column(name="INSPECTIONPROCESS")
    private String inspectionProcess;

      @Schema(name = "评价人")
      @TableField("EVALUATOR")
      @Column(name="EVALUATOR")
    private BigDecimal evaluator;

      @Schema(name = "评价时间")
      @TableField("EVALUATIME")
      @Column(name="EVALUATIME")
      @JSONField(format = "yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date evaluaTime;

      @Schema(name = "整改结果状态 1-未整改，2-已整改未到位、3-已整改到位、4-关闭")
      @TableField("RESULTSTATUS")
      @Column(name="RESULTSTATUS")
    private Integer resultStatus;

      @Schema(name = "整改落实信息主键")
      @TableField("IMPLID")
      @Column(name="IMPLID")
    private String implId;
      
      @Schema(name = "整改截止时间")
      @TableField("RECTENDTIME")
      @Column(name="RECTENDTIME")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date rectendTime;

}
