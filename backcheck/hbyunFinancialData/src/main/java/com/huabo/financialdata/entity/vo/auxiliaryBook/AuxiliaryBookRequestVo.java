package com.huabo.financialdata.entity.vo.auxiliaryBook;

import com.huabo.financialdata.entity.base.BaseDbSource;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 辅助账  分页查询   请求参数
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="辅助账 - 分页列表 - 请求参数")
public class AuxiliaryBookRequestVo extends BaseDbSource {

    @Schema(name = "所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx ")
    private String mty;

    @Schema(name = "查询辅助账类型，辅助余额表，辅助信息表，辅助总账,ASSID--辅助编码,ASSTYPE--辅助类型,ASSNAME--辅助名称,ASSLEVEL--辅助级别,ASSSJBM--上级编码 QCDC--余额方向  QCMD==初期借方余额  QCMC--初期贷方余额  BQMD--借方发生额  BQMC--贷方发生额  QMMD--期末借方余额  QMMC--期末贷方余额，ACCNAME == 科目名称")
    private String type;

    @Schema(name = "查询条件 大于，小于，等于，不等于，包含，不包含")
    private String status;

    @Schema(name = "页面选中的td中查询值")
    private String values;
    @Schema(name = "查询条件 大于，小于，等于，不等于，包含，不包含")
    private String valueType;

    @Schema(name = "万能搜索条件")
    private String wnss;

    @Schema(name = "查询条件字符串")
    private String str;

    @Schema(name = "账套年份")
    private String bookYear;

    @Schema(name = "当前页数")
    private Integer pageNumber;

    @Schema(name = "每页数量")
    private Integer pageSize;

    @Schema(name = "无意义")
    private String choiceSearch;

    @Schema(name = "辅助类型")
    private String assType;

    private String assMd;

    @Schema(name = "辅助编码")
    private String assId;

    @Schema(name = "辅助名称")
    private String assName;

    @Schema(name = "辅助级别")
    private String assLevel;

    @Schema(name = "上级编码")
    private String assSjbm;

    @Schema(name = "左边所选择菜单， 1== 辅助信息表- 2 == 辅助余额表- 3 == 辅助总账")
    private String leftMenu;

    @Schema(name = "1=待摊费用 -- 2=银行账户--3=股东 4=客户-5=供应商-6=部门-7=金融机构 - 8=职员-9=专项费用-10=物料-11=投资单位")
    private String typeName;
}

