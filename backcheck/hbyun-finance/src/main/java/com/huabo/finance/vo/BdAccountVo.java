package com.huabo.finance.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.finance.vo.BaseVo;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会计科目基本信息
 * </p>
 *
 * @author L
 * @since 2025-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="BdAccount对象", description="会计科目基本信息")
public class BdAccountVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "科目编码")
    private String code;

      @Schema(name = "科目名称")
    private String name;

      @Schema(name = "上级科目")
    private String pid;

      @Schema(name = "内部码")
    private String innercode;


      @Schema(name = "科目类型")
    private String pkAcctype;

      @Schema(name = "现金分类  	0=其它;1=现金科目;2=银行科目;3=现金等价物;")
    private Integer cashtype;

      @Schema(name = "助记码")
    private String remcode;

      @Schema(name = "科目方向 0=借方;1=贷方;")
    private Integer balanorient;

      @Schema(name = "默认币种")
    private String currency;
      
      @Schema(name = "创建组织")
    private String pkorg;
      
      @Schema(name = "创建集团")
    private String pkgroup;
      
      @Schema(name = "上级父科目主键")
    private String fpkaccount;
      
      @Schema(name = "最小科目级次")
    private Integer minaccLev;
    
      @Schema(name = "最大科目级次")
    private Integer maxaccLev;
      
      
}
