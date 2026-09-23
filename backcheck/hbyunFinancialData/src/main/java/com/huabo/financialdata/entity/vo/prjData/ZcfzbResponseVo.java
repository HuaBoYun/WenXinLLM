package com.huabo.financialdata.entity.vo.prjData;

import com.huabo.financialdata.entity.entity.AccBook;
import com.huabo.financialdata.entity.entity.AccPeriod;
import com.huabo.financialdata.entity.entity.AccReportYbal;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 报表数据  资产负债表  分页查询  返回参数
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-12-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="报表数据  资产负债表  分页查询  返回参数")
public class ZcfzbResponseVo {

    private List<AccReportYbal> zcfzb_list;

    private List<AccPeriod> accPeriodList;

    @Schema(name = "所属模块, 智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx")
    private String mty;

    private String strMonth;

    private AccBook accBook;
}
