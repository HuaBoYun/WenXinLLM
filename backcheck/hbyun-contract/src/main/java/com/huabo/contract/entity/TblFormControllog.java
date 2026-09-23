package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-03-30
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_FORM_CONTROLLOG")
@Schema(name="TblFormControllog对象")
public class TblFormControllog implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "RULELOGID",type = IdType.INPUT)
      private BigDecimal rulelogid;

    @TableField("CREATEDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdate;

    @TableField("OPERATION")
    private String operation;

    @TableField("RETURNRESULT")
    private String returnresult;

    @TableField("INPARAM")
    private String inparam;

    @TableField("RULENO")
    private String ruleno;

    @TableField("EXECUTESTAFF")//执行人
    private String executestaff;

    @TableField("RETURNDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date returndate;


}
