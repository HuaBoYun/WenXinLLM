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
 * 外币汇率
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_CURRINFO")
@Schema(name="BdCurrinfo对象", description="外币汇率")
public class BdCurrinfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "币种信息主键")
        @TableId("PK_CURRINFO")
      private String pkCurrinfo;

      @Schema(name = "源币种")
      @TableField("PK_CURRTYPE")
    private String pkCurrtype;

      @Schema(name = "汇率小数位数 0=0;1=1;2=2;3=3;4=4;5=5;6=6;7=7;8=8;")
      @TableField("RATEDIGIT")
    private String ratedigit;

      @Schema(name = " 折算模式0=源币种×汇率＝目的币种;1=源币种÷汇率＝目的币种;")
      @TableField("CONVMODE")
    private String convmode;

      @Schema(name = "所属外币汇率方案")
      @TableField("PK_EXRATESCHEME")
    private String pkExratescheme;

      @Schema(name = "目的币种")
      @TableField("OPPCURRTYPE")
    private String oppcurrtype;

      @Schema(name = " 最大折算误差")
      @TableField("MAXCONVERR")
    private String maxconverr;

      @Schema(name = "日汇率")
      @TableField("CURRRATE")
    private String currrate;

      @Schema(name = "期间汇率")
      @TableField("ADJUSTRATE")
    private String adjustrate;

      @Schema(name = "平均汇率")
      @TableField("AVGRATE")
    private String avgrate;

      @Schema(name = "分布式 	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "所属集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "所属组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "基点精度")
      @TableField("BPACCURACY")
    private String bpaccuracy;

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
