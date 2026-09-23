package com.huabo.finance.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 辅助账余额
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("GL_ASS_BALANE")
@Schema(name="GlAssBalane对象", description="辅助账余额")
public class GlAssBalaneVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "会计年度")
    private String year;

      @Schema(name = "会计期间")
    private String period;

      @Schema(name = "所属公司")
    private String pkOrg;
      

      @Schema(name = "辅助名称")
    private String assname;

      @Schema(name = "描述")
    private String assdes;

      @Schema(name = "辅助核算项主键")
    private String pkAccassitem;

      @Schema(name = "描述")
    private String assdd;
      
}
