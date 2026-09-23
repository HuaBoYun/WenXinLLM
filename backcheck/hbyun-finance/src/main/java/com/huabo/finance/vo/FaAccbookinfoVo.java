package com.huabo.finance.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 账簿信息
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="FaAccbookinfo对象", description="账簿信息")
public class FaAccbookinfoVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "外币折算日期   	0=卡片建卡日期;1=业务发生日期;")
    private Integer convertDate;

      @Schema(name = "本币原值来源")
    private String localoriginvalue;

      @Schema(name = "账簿类别编码 ")
    private String accbooktypecode;

      @Schema(name = "账簿类别名称 ")
    private String accbooktypename;
      
      @Schema(name = "账簿名称 ")
    private String bookName;
}
