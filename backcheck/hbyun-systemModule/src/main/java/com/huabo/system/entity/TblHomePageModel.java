package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
    @TableName("TBL_HOME_PAGE_MODEL")
@Schema(name="TblHomePageModel对象", description="")
public class TblHomePageModel implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value="ID",type = IdType.INPUT)
      private BigDecimal id;

    @TableField("NAME")
    private String name;

    @TableField("URL")
    private String url;

    @TableField("TYPE")
    private String type;

    @TableField("RIGHTID")
    private BigDecimal rightid;


}
