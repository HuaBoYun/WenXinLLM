package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 财务组织信息表
 * </p>
 *
 * @author L
 * @since 2025-03-14
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("ORG_ORGS")
@Schema(name="OrgOrgs对象", description="财务组织信息表")
public class OrgOrgs implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("PK_ORG")
      private String pkOrg;

      @Schema(name = "编码")
      @TableField("CODE")
    private String code;

      @Schema(name = "名称")
      @TableField("NAME")
    private String name;

      @Schema(name = "内部编码")
      @TableField("INNERCODE")
    private String innercode;

      @Schema(name = "简称")
      @TableField("SHORTNAME")
    private String shortname;

      @Schema(name = "助记码")
      @TableField("MNECODE")
    private String mnecode;

      @Schema(name = "上级业务单元")
      @TableField("PK_FATHERORG")
    private String pkFatherorg;
      
      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;
      
      @Schema(name = "所属账套")
      @TableField("FINACCOUNT")
    private String finaccount;

      @Schema(name = "启用状态 1=未启用;2=已启用;3=已停用;")
      @TableField("ENABLESTATE")
    private Integer enablestate;

      @Schema(name = "分布式  	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;-2=财务同步")
      @TableField("DATAORIGINFLAG")
    private Integer dataoriginflag;

      @Schema(name = "对照系统组织")
      @TableField("COMPARISONORGID")
    private BigDecimal comparisonorgid;

      @Schema(name = "创建人")
      @TableField("CREATOR")
    private BigDecimal creator;

      @Schema(name = "修改人")
      @TableField("MODIFIER")
    private BigDecimal modifier;

      @Schema(name = "创建时间")
      @TableField("CREATIONTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;

      @Schema(name = "修改时间")
      @TableField("MODIFIEDTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;


}
