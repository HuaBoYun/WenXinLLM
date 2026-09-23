package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会计月份
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_ACCPERIODMONTH")
@Schema(name="BdAccperiodmonth对象", description="会计月份")
public class BdAccperiodmonth implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "会计月份主键")
      @TableField("PK_ACCPERIODMONTH")
    private String pkAccperiodmonth;

      @Schema(name = "所属会计期间")
      @TableField("PK_ACCPERIOD")
    private String pkAccperiod;

      @Schema(name = "会计期间方案")
      @TableField("PK_ACCPERIODSCHEME")
    private String pkAccperiodscheme;

      @Schema(name = "所属组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "会计月份")
      @TableField("ACCPERIODMTH")
    private String accperiodmth;

      @Schema(name = "开始日期")
      @TableField("BEGINDATE")
    private Date begindate;

      @Schema(name = "结束日期")
      @TableField("ENDDATE")
    private Date enddate;

      @Schema(name = "开始月")
      @TableField("BEGINMONTH")
    private String beginmonth;

      @Schema(name = "结束月")
      @TableField("ENDMONTH")
    private String endmonth;

      @Schema(name = "是否为调整 默认N")
      @TableField("ISADJ")
    private String isadj;

      @Schema(name = "年月组合信息")
      @TableField("YEARMTH")
    private String yearmth;

      @Schema(name = "数据来源  	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;


}
