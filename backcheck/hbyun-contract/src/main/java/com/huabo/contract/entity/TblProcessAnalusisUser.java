package com.huabo.contract.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
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

    @Schema(name = "主键Id 自增")
    @TableId(value = "ID" ,type = IdType.INPUT)
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
