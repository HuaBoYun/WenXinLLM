package com.huabo.finance.vr;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 账簿类型
 * </p>
 *
 * @author L
 * @since 2025-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="OrgSetofbook对象", description="账簿类型")
public class OrgSetofbookVr implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "账簿类型主键")
      private String pkSetofbook;

      @Schema(name = "编码")
    private String code;

      @Schema(name = "名称")
    private String name;

      @Schema(name = "简称")
    private String shortname;

      @Schema(name = "助记码")
    private String mnecode;

      @Schema(name = "所属集团")
    private String pkGroup;
      
      @Schema(name = "所属集团名称")
    private String pkGroupName;

      @Schema(name = "科目体系")
    private String pkAccsystem;
      
      @Schema(name = "科目体系名称")
      private String pkAccsystemName;

      @Schema(name = "本位币")
    private String pkStandardcurr;
      
      @Schema(name = "本位币名称")
      private String standardcurrName;
      
      @Schema(name = "会计期间方案")
    private String pkAccperiodscheme;
      
      @Schema(name = "会计期间方案名称")
      private String accperiodschemeName;
      
      @Schema(name = "分布式  	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
    private Integer dataoriginflag;

      @Schema(name = "财务核算账簿")
    private String isaccountbook;

      @Schema(name = "责任核算账簿")
    private String isliabilitybook;

      @Schema(name = "核算要素体系")
    private String pkCheckelemsystem;
      
      @Schema(name = "核算要素体系名称")
      private String checkelemsystemName;

      @Schema(name = "所属组织")
    private String pkOrg;
      
      @Schema(name = "所属组织名称")
      private String pkOrgName;

      @Schema(name = "核算目的")
    private String checkaim;

      @Schema(name = "外币汇率方案")
    private String pkExratescheme;
      
      @Schema(name = "外币汇率方案名称")
      private String exrateschemeName;

      @Schema(name = "创建人")
    private String creator;
      
      @Schema(name = "创建人姓名")
      private String creatorName;

      @Schema(name = "创建时间")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;
      
      @Schema(name = "最后修改人")
    private String modifier;

      @Schema(name = "最后修改人姓名")
    private String modifierName;

      @Schema(name = "最后修改时间")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;


}
