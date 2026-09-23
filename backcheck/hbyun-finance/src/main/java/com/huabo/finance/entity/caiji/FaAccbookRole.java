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
 * 账簿角色授权
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("FA_ACCBOOK_ROLE")
@Schema(name="FaAccbookRole对象", description="账簿角色授权")
public class FaAccbookRole implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("FID")
      private String fid;

      @Schema(name = "账簿信息主键 ")
      @TableField("ACCBOOKID")
    private String accbookid;

      @Schema(name = "角色主键 ")
      @TableField("ROLEID")
    private String roleid;


}
