package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2022-05-07
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_ORG_RIGHT")
@Schema(name="TblOrgRight对象", description="")
public class TblOrgRight implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ORGID")
    private BigDecimal orgid;

    @TableField("RIGHTID")
    private BigDecimal rightid;


}
