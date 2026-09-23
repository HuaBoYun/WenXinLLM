package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会计期间档案
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_ACCPERIOD")
@Schema(name="BdAccperiod对象", description="会计期间档案")
public class BdAccperiod implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "会计年度主键")
        @TableId("PK_ACCPERIOD")
      private String pkAccperiod;

      @Schema(name = "所属组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "所属集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "会计年度")
      @TableField("PERIODYEAR")
    private String periodyear;

      @Schema(name = "终止日期")
      @TableField("BEGINDATE")
    private Date begindate;

    @TableField("ENDDATE")
    private Date enddate;

      @Schema(name = "会计期间个数")
      @TableField("PERIODNUM")
    private String periodnum;

      @Schema(name = "期间个数小于12")
      @TableField("ISLESSTWELVE")
    private String islesstwelve;

      @Schema(name = "会计季度个数")
      @TableField("QUARTERNUM")
    private String quarternum;

      @Schema(name = "会计半年个数")
      @TableField("HALFYEARNUM")
    private String halfyearnum;

      @Schema(name = "是否启用调整")
      @TableField("ISADJUSTABLE")
    private String isadjustable;

      @Schema(name = "期间方案主键")
      @TableField("PK_ACCPERIODSCHEME")
    private String pkAccperiodscheme;

      @Schema(name = "会计月份")
      @TableField("ACCPERIODMONTH")
    private String accperiodmonth;

      @Schema(name = "会计季度")
      @TableField("ACCPERIODQUART")
    private String accperiodquart;

      @Schema(name = "会计半年")
      @TableField("ACCHALFYEAR")
    private String acchalfyear;

      @Schema(name = "是否是当前新增数据 默认N")
      @TableField("ISNEWDATAFORCURR")
    private String isnewdataforcurr;

      @Schema(name = "分布式字段 0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "创建人")
      @TableField("CREATOR")
    private String creator;

      @Schema(name = "最后修改人")
      @TableField("MODIFIER")
    private String modifier;

      @Schema(name = "创建时间")
      @TableField("CREATIONTIME")
    private Date creationtime;

      @Schema(name = "最后修改时间")
      @TableField("MODIFIEDTIME")
    private Date modifiedtime;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;


}
