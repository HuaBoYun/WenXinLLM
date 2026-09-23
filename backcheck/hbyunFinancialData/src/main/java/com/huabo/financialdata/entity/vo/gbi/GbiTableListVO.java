package com.huabo.financialdata.entity.vo.gbi;

import com.baomidou.mybatisplus.annotation.TableField;
import com.huabo.financialdata.entity.entity.GbiTableColumns;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * GBI上传的表信息
 * </p>
 *
 * @author 
 * @since 2024-04-21
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name="tableList", description="tableList")
public class GbiTableListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(name="tableId")
    private String tableId;

    @Schema(name="APPID")
    private String appId;

    @Schema(name="批次id")
    private String batchId;

    @Schema(name="批次编号")
    private String batchNo;

    @Schema(name="客户定义表名称")
    private String customTable;

    @Schema(name="描述")
    private String description;

    @Schema(name="真实库定义的表名称")
    private String tableName;

    @Schema(name="总行数")
    private Integer rowNum;

    @Schema(name="总列数")
    private Integer cellNum;

    @Schema(name="字段配置")
    private List<GbiTableColumns> settings;
}
