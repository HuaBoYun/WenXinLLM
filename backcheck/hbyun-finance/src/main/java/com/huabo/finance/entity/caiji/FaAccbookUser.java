package com.huabo.finance.entity.caiji;

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
 * 用户选择默认账簿
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("FA_ACCBOOK_USER")
@Schema(name="FaAccbookUser对象", description="用户选择默认账簿")
public class FaAccbookUser implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("FID")
      private String fid;

      @Schema(name = "账簿信息主键 ")
      @TableField("ACCBOOKID")
    private String accbookid;

      @Schema(name = "用户主键 ")
      @TableField("STAFFID")
    private String staffid;


}
