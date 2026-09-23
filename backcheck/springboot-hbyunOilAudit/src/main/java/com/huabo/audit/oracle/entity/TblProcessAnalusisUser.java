package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
 * @since 2021-10-26
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_PROCESS_ANALUSIS_USER")
@Schema(name="TblProcessAnalusisUser对象")
public class TblProcessAnalusisUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select ANALUSIS_USER_SEQUENCE.nextval from dual")
    @Schema(name = "主键Id 自增")
    @TableId("ID")
    private BigDecimal id;

      @Schema(name = "用户信息主键 TBL_STAFF(STAFFID)")
      @TableField("STAFFID")
    private String staffid;

      @Schema(name = "TBL_PROCESS_ANALYSIS(ANALID)")
      @TableField("ANALID")
    private String analid;

      @Schema(name = "审批时间")
      @TableField("SPDATE")
      @JSONField(format = "yyyy-MM-dd")
    private Date spdate;

      @Schema(name = "所属表单Id")
      @TableField("FROMID")
    private String fromid;


}
