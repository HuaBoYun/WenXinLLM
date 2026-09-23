package com.huabo.fxgl.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Data
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
@TableName("TBL_RISK_CLAIM")
public class RiskClaim implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
	@Schema
    private Integer claimid;

	@Schema
    private String claimnumber;

	@Schema
    private String claimtype;

	@Schema
    private String claimname;

	@Schema
    private String claimdes;

	@Schema
    private String accidd;

	@Schema
    private String accnamed;

	@Schema
    private Integer pricenumber;

	  @Schema(name = "索赔日")
      @TableField("CLAIMDATE")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
      private Date claimdate;

	  @Schema(name = "入账日")
      @TableField("INDATE")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
      private Date indate;

}
