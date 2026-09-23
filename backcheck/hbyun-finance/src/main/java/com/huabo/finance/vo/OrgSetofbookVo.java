package com.huabo.finance.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
public class OrgSetofbookVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "编码")
    private String code;

      @Schema(name = "名称")
    private String name;

      @Schema(name = "简称")
    private String shortname;

      @Schema(name = "助记码")
    private String mnecode;

      @Schema(name = "分布式  	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
    private Integer dataoriginflag;

}
