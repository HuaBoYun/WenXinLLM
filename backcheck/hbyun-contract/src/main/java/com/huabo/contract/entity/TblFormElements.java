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
 * @since 2022-03-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_FORM_ELEMENTS")
@Schema(name="TblFormElements对象")
public class TblFormElements implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "ELEID" ,type = IdType.INPUT)
      private BigDecimal eleid;

    @TableField("FORMID")
    private BigDecimal formid;

    @TableField("ELELABEL")
    private String elelabel;

    @TableField("ELECODE")
    private String elecode;

    @TableField("ISREQUIRE")
    private BigDecimal isrequire;

    @TableField("ELEORDER")
    private BigDecimal eleorder;

    @TableField("ELETYPE")
    private String eletype;

    @TableField("SHOWLIST")
    private BigDecimal showlist;

    @TableField("ELENAME")
    private String elename;

    @TableField("SHOWORDER")
    private BigDecimal showorder;

    @TableField("DEFAULTVALUE")
    private String defaultvalue;


}
