package com.huabo.finance.vr;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.finance.vo.BaseVo;

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
@Schema(name="OrgOrgs对象", description="财务组织信息表")
public class OrgOrgVr extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
      private String pkOrg;

      @Schema(name = "编码")
    private String code;

      @Schema(name = "名称")
    private String name;

      @Schema(name = "内部编码")
    private String innercode;

      @Schema(name = "简称")
    private String shortname;

      @Schema(name = "助记码")
    private String mnecode;

      @Schema(name = "上级业务单元")
    private String pkFatherorg;
      
      @Schema(name = "所属采集方案")
    private String fplanid;
      
      @Schema(name = "所属账套")
    private String finaccount;

      @Schema(name = "启用状态 1=未启用;2=已启用;3=已停用;")
    private Integer enablestate;

      @Schema(name = "分布式  	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;-2=财务同步")
    private Integer dataoriginflag;

      @Schema(name = "对照系统组织")
    private BigDecimal comparisonorgid;

      @Schema(name = "创建人")
    private BigDecimal creator;

      @Schema(name = "修改人")
    private BigDecimal modifier;

      @Schema(name = "创建时间")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;

      @Schema(name = "修改时间")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

      
      @Schema(name = "公司子集")
      private List<OrgOrgVr> childrenList ;

}
