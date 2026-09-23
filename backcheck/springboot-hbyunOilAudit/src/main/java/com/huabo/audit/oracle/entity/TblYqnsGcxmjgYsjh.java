package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工程项目竣工验收计划
 *
 * @TableName TBL_YQNS_GCXMJG_YSJH
 */
@Data
@TableName(value = "TBL_YQNS_GCXMJG_YSJH")
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsGcxmjgYsjh extends BaseReservedProperty implements Serializable {
    @Bean
    public OracleKeyGenerator genKey() {
        return new OracleKeyGenerator();
    }

    /**
     * 工程项目竣工验收计划表主键
     */
    @Schema(name = "工程项目竣工验收计划表主键")
    @TableId(value = "GCXMJGYSJHID", type = IdType.INPUT)
    private Long gcxmjgysjhid;
    
    @Schema(name = "序号")
    @TableField(value = "GCXMJGYSJHNO")
    private BigDecimal gcxmjgysjhNo;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    /**
     * 项目名称
     */
    @Schema(name = "项目名称")
    @TableField(value = "XMMC")
    private String xmmc;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField(value = "CJR")
    private String cjr;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField(value = "CJSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cjsj;

    /**
     * 扩展字段1
     */
    @Schema(name = "扩展字段1")
    @TableField(value = "EXT1")
    private String ext1;

    /**
     * 扩展字段2
     */
    @Schema(name = "扩展字段2")
    @TableField(value = "EXT2")
    private String ext2;

    /**
     * 扩展字段
     */
    @Schema(name = "扩展字段")
    @TableField(value = "EXT3")
    private String ext3;

    /**
     * 组织
     */
    @Schema(name = "组织")
    @TableField(value = "ORGID")
    private Long orgid;

    /**
     * 更新人
     */
    @Schema(name = "更新人")
    @TableField(value = "GXR")
    private String gxr;

    /**
     * 更新时间
     */
    @Schema(name = "更新时间")
    @TableField(value = "GXSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date gxsj;

    /**
     * 审批状态
     */
    @Schema(name = "审批状态")
    @TableField(value = "SPZT")
    private Long spzt;

    /**
     * 建设单位
     */
    @Schema(name = "建设单位")
    @TableField(value = "JSDW")
    private String jsdw;

    /**
     * 项目类别
     */
    @Schema(name = "项目类别")
    @TableField(value = "XMLB")
    private String xmlb;

    /**
     * 项目总投资金额（万元）
     */
    @Schema(name = "项目总投资金额（万元）")
    @TableField(value = "XMZTZJE")
    private BigDecimal xmztzje;

    
    @Schema(name = "项目数量")
    @TableField(value = "ITEMCOUNT")
    private BigDecimal itemCount;
    
    /**
     * 项目投产时间
     */
    @Schema(name = "项目投产时间")
    @TableField(value = "XMTCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date xmtcsj;

    /**
     * 生产考核完成时间
     */
    @Schema(name = "生产考核完成时间")
    @TableField(value = "SCKHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sckhwcsj;

    /**
     * 消防设施验收-计划完成时间
     */
    @Schema(name = "消防设施验收-计划完成时间")
    @TableField(value = "XFSSYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date xfssysjhwcsj;

    /**
     * 安全设施验收-计划完成时间
     */
    @Schema(name = "安全设施验收-计划完成时间")
    @TableField(value = "AQSSYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date aqssysjhwcsj;

    /**
     * 水土保持设施验收-计划完成时间
     */
    @Schema(name = "水土保持设施验收-计划完成时间")
    @TableField(value = "STBCSSYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date stbcssysjhwcsj;

    /**
     * 节能验收-计划完成时间
     */
    @Schema(name = "节能验收-计划完成时间")
    @TableField(value = "JNYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date jnysjhwcsj;

    /**
     * 雷电防护装置验收-计划完成时间
     */
    @Schema(name = "雷电防护装置验收-计划完成时间")
    @TableField(value = "LDFHZZYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ldfhzzysjhwcsj;

    /**
     * 竣工结算验收-上报审计时间
     */
    @Schema(name = "竣工结算验收-上报审计时间")
    @TableField(value = "JGJSYSSBSJSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date jgjsyssbsjsj;

    /**
     * 初步验收-计划完成时间
     */
    @Schema(name = "初步验收-计划完成时间")
    @TableField(value = "CBYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cbysjhwcsj;

    /**
     * 环境保护验收-计划完成时间
     */
    @Schema(name = "环境保护验收-计划完成时间")
    @TableField(value = "HJBHYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date hjbhysjhwcsj;

    /**
     * 职业病防护设施验收-计划完成时间
     */
    @Schema(name = "职业病防护设施验收-计划完成时间")
    @TableField(value = "ZYBFHSSYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date zybfhssysjhwcsj;

    /**
     * 土地利用验收-计划完成时间
     */
    @Schema(name = "土地利用验收-计划完成时间")
    @TableField(value = "TDLYYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date tdlyysjhwcsj;

    /**
     * 安全防范系统验收-计划完成时间
     */
    @Schema(name = "安全防范系统验收-计划完成时间")
    @TableField(value = "AQFFXTYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date aqffxtysjhwcsj;

    /**
     * 档案验收-计划完成时间
     */
    @Schema(name = "档案验收-计划完成时间")
    @TableField(value = "DAYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date daysjhwcsj;

    /**
     * 项目结算验收-计划完成时间
     */
    @Schema(name = "项目结算验收-计划完成时间")
    @TableField(value = "XMJSYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date xmjsysjhwcsj;

    /**
     * 竣工验收-计划完成时间
     */
    @Schema(name = "竣工验收-计划完成时间")
    @TableField(value = "JGYSJHWCSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date jgysjhwcsj;

    /**
     * 备注
     */
    @Schema(name = "备注")
    @TableField(value = "BZ")
    private String bz;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Schema(name = "开始时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;

    @Schema(name = "结束时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;

    @TableField(value = "STATUS")
    @Schema(name="状态")
    private Integer status;

    @Schema(name = "主键集合")
    @TableField(exist = false)
    private List<String> ids;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    private List<TblAttachment> attachments;
    
    @Schema(name = "查询年度")
    @TableField(exist = false)
    private Integer queryYear;
    
    @TableField(exist = false)
    @Schema(name="用户查看数据权限部门")
    private String queryDeptIds;
    
    @TableField(exist = false)
    @Schema(name="当前查询人")
    private BigDecimal currentStaffId;
    
}