package com.huabo.contract.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableName("TBL_YW_FROM")
@Schema(name="TblYwFrom对象")
public class TblYwFrom implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "FROMID" , type = IdType.INPUT)
      private BigDecimal fromid;

    @TableField("FROMNUMBER")
    private String fromnumber;

    @TableField("FROMNAME")
    private String fromname;

    @TableField("MEO")
    private String meo;

    @TableField("ORGID")
    private BigDecimal orgid;


}
