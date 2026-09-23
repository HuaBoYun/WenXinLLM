package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
 * @since 2022-05-24
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_NBSJ_EXPERIENCE")
@Schema(name="TblNbsjExperience对象")
public class TblNbsjExperience implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("TEMPLETEID")
      private BigDecimal templeteid;

    @TableField("TEMPLETECODE")
    private String templetecode;

    @TableField("TEMPLETENAME")
    private String templetename;

    @TableField("TEMPLETETYPE")
    private String templetetype;

    @TableField("TEMPLETEDESC")
    private String templetedesc;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("CREATEDATE")
    private LocalDateTime createdate;

    @TableField("UPDATEDATE")
    private LocalDateTime updatedate;

    @TableField("UPDATESTAFFID")
    private BigDecimal updatestaffid;

    @TableField("STATUS")
    private BigDecimal status;

    @TableField("TEMPTYPE")
    private BigDecimal temptype;

    @TableField("ORGID")
    private BigDecimal orgid;


}
