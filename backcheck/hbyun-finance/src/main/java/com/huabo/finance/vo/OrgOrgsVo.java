package com.huabo.finance.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="OrgOrgs对象", description="财务组织信息")
public class OrgOrgsVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = 1L;

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

}
