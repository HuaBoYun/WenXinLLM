package com.huabo.system.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 系统业务单据下发通知表
 * </p>
 *
 * @author LHP
 * @since 2023-11-24
 */
@Data
@Schema(name="TblSystemDistributionType返回对象", description="系统业务单据下发通知类型返回对象")
public class TblSystemDistributionTypeVo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name="显示文本")
      private String textName;

    @Schema(name="隐藏值")
    private String textValue;

}
