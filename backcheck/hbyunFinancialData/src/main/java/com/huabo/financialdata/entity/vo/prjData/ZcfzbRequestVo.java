package com.huabo.financialdata.entity.vo.prjData;

import com.huabo.financialdata.entity.base.BaseDbSource;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 报表数据  资产负债表  分页查询  请求参数
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-12-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="报表数据  资产负债表  分页查询  请求参数")
public class ZcfzbRequestVo extends BaseDbSource {

    @Schema(name = "时间")
    private String date;

    @Schema(name = "账套年份")
    private Integer bookYear;

    @Schema(name = "账套名")
    private String book;

    @Schema(name = "所属模块, 智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx")
    private String mty;

}
