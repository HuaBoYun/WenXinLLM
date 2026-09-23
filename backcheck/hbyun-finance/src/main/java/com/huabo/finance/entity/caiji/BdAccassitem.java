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
 * 会计辅助核算项目
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_ACCASSITEM")
@Schema(name="BdAccassitem对象", description="会计辅助核算项目")
public class BdAccassitem implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
      @TableField("PK_ACCASSITEM")
    private String pkAccassitem;

      @Schema(name = "辅助核算项编码")
      @TableField("CODE")
    private String code;

      @Schema(name = "辅助核算项名称")
      @TableField("NAME")
    private String name;

      @Schema(name = "数据对象")
      @TableField("CLASSID")
    private String classid;

      @Schema(name = "输入长度 [1 , 200]")
      @TableField("INPUTLENGTH")
    private String inputlength;

      @Schema(name = "精度 [0 , 8]")
      @TableField("DIGITS")
    private String digits;

      @Schema(name = "所属组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "所属集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "数据来源 0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "参照名称")
      @TableField("REFNODENAME")
    private String refnodename;

      @Schema(name = "映射属性(接口单)")
      @TableField("TXBILLITEM")
    private String txbillitem;

      @Schema(name = "创建时间")
      @TableField("CREATIONTIME")
    private Date creationtime;

      @Schema(name = "创建人")
      @TableField("CREATOR")
    private String creator;

      @Schema(name = "最后修改时间")
      @TableField("MODIFIEDTIME")
    private Date modifiedtime;

      @Schema(name = "最后修改人")
      @TableField("MODIFIER")
    private String modifier;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;


}
