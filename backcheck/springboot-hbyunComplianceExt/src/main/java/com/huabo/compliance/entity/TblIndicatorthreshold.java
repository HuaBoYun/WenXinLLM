package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_INDICATORTHRESHOLD")
@Schema(name="TblIndicatorthreshold对象")
public class TblIndicatorthreshold implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("THRESHOLDID")
      private BigDecimal thresholdid;

    @TableField("SEQUENCENUMBER")
    private String sequencenumber;

    @TableField("TOLERANCE")
    private String tolerance;

    @TableField("THRESHOLDNAME")
    private String thresholdname;

    @TableField("REGIONVALUE")
    private String regionvalue;

    @TableField("PREWARNINGMETHOD")
    private String prewarningmethod;

    @TableField("MEMO")
    private String memo;

    @TableField("INDICATORID")
    private Integer indicatorid;

    @TableField("TOLERANCELOWER")
    private String tolerancelower;

    @TableField("TOLERANCEUPPER")
    private String toleranceupper;


}
