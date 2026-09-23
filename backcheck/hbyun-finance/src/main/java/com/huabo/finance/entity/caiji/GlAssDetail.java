package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 辅助凭证明细
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("GL_ASS_DETAIL")
@Schema(name="GlAssDetail对象", description="辅助凭证明细")
public class GlAssDetail implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("FID")
      private String fid;

      @Schema(name = "凭证明细主键")
      @TableField("PK_DETAIL")
    private String pkDetail;

      @Schema(name = "凭证主键")
      @TableField("PK_VOUCHER")
    private String pkVoucher;

      @Schema(name = "辅助信息主键")
      @TableField("PK_ASSACCID")
    private String pkAssaccid;

      @Schema(name = "分录序号")
      @TableField("FSEQ")
    private String fseq;

      @Schema(name = "借方金额")
      @TableField("DEBITAMOUNT")
    private String debitamount;

      @Schema(name = "贷方金额")
      @TableField("CREDITAMOUNT")
    private String creditamount;

      @Schema(name = "公司信息")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "集团信息")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "年份")
      @TableField("YEARV")
    private String yearv;

      @Schema(name = "月份")
      @TableField("MONTH")
    private String month;

      @Schema(name = "摘要")
      @TableField("DESCRIPTION")
    private String description;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;

      @Schema(name = "数据来源 -2系统同步 ")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;


}
