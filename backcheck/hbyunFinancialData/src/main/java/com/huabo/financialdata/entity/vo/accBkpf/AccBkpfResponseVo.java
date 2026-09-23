package com.huabo.financialdata.entity.vo.accBkpf;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 凭证库  分页查询   返回参数
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="凭证库 - 分页列表 - 返回参数")
public class AccBkpfResponseVo {

    @Schema(name = "科目编码")
    private String accid;

    @Schema(name = "科目名称")
    private String accName;

    @Schema(name = "凭证日期")
    private String pzDate;

    @Schema(name = "凭证号")
    private String pzh;

    @Schema(name = "抬头文本")
    private String lineText;

    @Schema(name = "凭证类型")
    private String pzType;

    @Schema(name = "附件数量")
    private String fj;

    @Schema(name = "期间，月份")
    private String aMonth;

    @Schema(name = "财务主管")
    private String cwzh;

    @Schema(name = "记账人")
    private String jzr;

    @Schema(name = "出纳人")
    private String cnr;

    @Schema(name = "审核人")
    private String shr;

    @Schema(name = "制单人")
    private String zdr;

    @Schema(name = "借方金额")
    private String md;

    @Schema(name = "贷方金额")
    private String mc;
    
    private Integer entryId;
    
    @Schema(name = "关联号，凭证表跟凭证明细 关联主键")
    private String glh;

}
