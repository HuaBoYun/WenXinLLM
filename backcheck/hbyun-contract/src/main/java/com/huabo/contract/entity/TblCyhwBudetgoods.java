package com.huabo.contract.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * @since 2022-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CYHW_BUDETGOODS")
@Schema(name="TblCyhwBudetgoods对象")
public class TblCyhwBudetgoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "GOODSID",type = IdType.INPUT)
    private BigDecimal goodsid;

    @TableField("GOODSNAME")
    private String goodsname;

    @TableField("GOODSNO")
    private String goodsno;

    @TableField("PARENTID")
    private BigDecimal parentid;

    @TableField(exist=false,fill=FieldFill.DEFAULT)
    private String realname;//真实名字
    
}
