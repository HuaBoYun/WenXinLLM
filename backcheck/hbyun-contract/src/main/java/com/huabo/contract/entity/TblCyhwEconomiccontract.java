package com.huabo.contract.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-08
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_CYHW_ECONOMICCONTRACT")
@Schema(name="TblCyhwEconomiccontract对象")
public class TblCyhwEconomiccontract implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "CONTRACTID",type=IdType.INPUT)
      private BigDecimal contractid;

    @TableField("CONTRACTNAME")
    private String contractname;

    @TableField("FLOWID")
    private BigDecimal flowid;

    @TableField("CONTRACTNO")
    private String contractno;

    @TableField("CONTRACTDEPT")
    private BigDecimal contractdept;

    @TableField("CONTRACTLINK")
    private String contractlink;

    @TableField("CONTRACTMONEY")
    private BigDecimal contractmoney;

    @TableField("CONTRACTSTATUS")
    private BigDecimal contractstatus;

    @TableField("LINKDEPT")
    private String linkdept;

    @TableField("ORGID")
    private BigDecimal orgid;

    @TableField("CREATEUSER")
    private BigDecimal createuser;

    @TableField("CREATETIME")
    private LocalDateTime createtime;

    @TableField("CONTRACTTYPE")
    private String contracttype;

    @TableField("CONTRACTPEASON")
    private String contractpeason;


}
