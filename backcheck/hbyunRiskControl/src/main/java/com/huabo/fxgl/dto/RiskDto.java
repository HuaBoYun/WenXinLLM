package com.huabo.fxgl.dto;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.huabo.fxgl.entity.Controlmatrix;
import com.huabo.fxgl.entity.FlexibleFieldEntity;
import com.vip.vjtools.vjkit.base.annotation.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="风险创建保存数据")
public class RiskDto extends FlexibleFieldEntity {
    @NotNull
    @Schema(name="点击左侧三级风险类型 传入的风险类型主键，必填项")
    private String riskcatid;
    @Schema(name="风险ID, 新增忽略, 修改必传")
    private String riskid;
    @NotNull
    @Schema(name="风险编号,必填项")
    private String risknumber;
    @NotNull
    @Schema(name="风险名称,必填项")
    private String riskname;
    @Schema(name="风险描述")
    private String riskdes;
    @NotNull
    @Schema(name="责任部门ID,数字,必填项")
    private String belongsto;
    @Schema(name="审计程序")
    private String riskprogram;
    @Schema(name="外部规定")
    private String riskexternal;
    @Schema(name="公司规定")
    private String riskcompany;
    @Schema(name="合规红线")
    private String riskcompliance;
    @Schema(name="风险领域")
    private String riskcatname;
    @Schema(name="风险领域Id")
    private String riskcatidone;
    @Schema(name="风险级别")
    private String risklevel;
    @Schema(name = "下拉-制/修订 1-制定 2-修订")
    private Integer revisiontype;
    @Schema(name="关联-原风险ID")
    private Integer riskextid;
    @Schema(name="风险原因")
    private String riskcause;
    @Schema(name="合规义务")
    private String complianceobligation;
    @Schema(name="富文本框")
    private String content;
    @Schema(name="上传附件ids")
    private String attids;
    @Schema(name="风险分类ID")
    private String riskcatnametwo;
    @Schema(name = "密级主键")
    private BigDecimal secrectLevelId;
    @Schema(name = "知悉范围id")
    private String staffScopeIds;
    @Schema(name = "知悉范围名称")
    private String staffScopeNames;
    @Schema(name="公司责任领导")
    private String leadership;
    @Schema(name="配合单位或部门")
    private String cooperateOrg;
    @Schema(name="配合单位或部门NAME")
    private String cooperateOrgName;

    @Schema(name="四级风险")
    private String levelFourRisk;
    //风险应对
    @Schema(name="风险应对策略类型, 数字0-4")
    private String copingPlot;// 策略
    @Schema(name="风险期望值, 数字")
    private Integer riskHopeValue;
    @Schema(name="风险应对负责人ID, 数字")
    private String userId;
    @Schema(name="应对方案描述")
    private String yddes;
    @Schema(name="风险应对ID")
    private String copingId;
    @Schema(name="版本")
    private String version;


    @Schema(name="业务编号")
    private String flownumber;

    @NotNull
    @Schema(name="流程名称必填项")
    private String flowname;
    @Schema(name="流程id")
    private String flowid;
    //BUSINESS
    @Schema(name="业务ID,为空新增 不为空修改")
    private String bussinessid;
    @Schema(name="业务名称")
    private String bussinessname;
    @Schema(name="业务描述")
    private String bussinessdes;

    @Schema(name="所属公司")
    private String unitname;

    @Schema(name="月度评估最新上报月份")
    private String reportmonth;


    @Schema(name="关联管控措施")
    @TableField(exist = false)
    private Controlmatrix param;

    @Schema(name="关联风险模型库")
    private String stepid;

    @Schema(name = "风险模型库关联数据源")
    @TableField(exist = false)
    private BigDecimal bookid;

    @Schema(name = "风险模型库sql")
    @TableField(exist = false)
    private String sql;

    @Schema(name = "风险模型库模型名称")
    @TableField(exist = false)
    private String steptitle;

}
