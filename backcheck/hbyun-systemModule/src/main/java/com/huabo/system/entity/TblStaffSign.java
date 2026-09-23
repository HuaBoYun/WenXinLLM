package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 电子签名存储表
 * </p>
 *
 * @author lhp
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STAFF_SIGN")
@Schema(name="TblStaffSign对象", description="电子签名存储表")
public class TblStaffSign implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name="电子签名主键")
        @TableId(value="SIGNID",type = IdType.INPUT)
      private String signid;

      @Schema(name="电子签名所属人员主键")
      @TableField("SIGNSTAFF")
    private BigDecimal signstaff;

      @Schema(name="电子签名所属人员姓名")
      @TableField("SIGNSTAFFNAME")
    private String signstaffname;

      @Schema(name="电子签名二进制数据")
      @TableField("SIGNATURE")
    private String signature;

      @Schema(name="创建时间")
      @TableField("CREATEDATE")
    private Date createdate;

      @Schema(name="创建人主键")
      @TableField("CREATESTAFF")
    private BigDecimal createstaff;

      @Schema(name="创建人姓名")
      @TableField("CREATESTAFFNAME")
    private String createstaffname;

      @Schema(name="修改时间")
      @TableField("UPDATEDATE")
    private Date updatedate;

      @Schema(name="修改人主键")
      @TableField("UPDATESTAFF")
    private BigDecimal updatestaff;

      @Schema(name="修改人姓名")
      @TableField("UPDATESTAFFNAME")
    private String updatestaffname;


}
