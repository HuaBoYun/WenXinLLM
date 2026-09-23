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
 * 外币汇率
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_EXRATESCHEME")
@Schema(name="BdExratescheme对象", description="外币汇率")
public class BdExratescheme implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "外币汇率方案主键")
      @TableField("PK_EXRATESCHEME")
    private String pkExratescheme;

      @Schema(name = "外币汇率方案编码")
      @TableField("CODE")
    private String code;

      @Schema(name = " 	外币汇率方案名称")
      @TableField("NAME")
    private String name;

      @Schema(name = "外币汇率方案描述")
      @TableField("DESCIPTION")
    private String desciption;

      @Schema(name = "分布式 	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "所属集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "所属组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "时区")
      @TableField("PK_TIMEZONE")
    private String pkTimezone;

      @Schema(name = "默认")
      @TableField("DEFAULTFLAG")
    private String defaultflag;

      @Schema(name = "发布行")
      @TableField("PK_BANKDOC")
    private String pkBankdoc;

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


}
