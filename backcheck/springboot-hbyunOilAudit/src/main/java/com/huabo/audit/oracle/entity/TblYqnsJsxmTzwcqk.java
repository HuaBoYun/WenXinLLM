package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 建设项目投资完成情况
 * @TableName TBL_YQNS_JSXM_TZWCQK
 */
@TableName(value ="TBL_YQNS_JSXM_TZWCQK")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsJsxmTzwcqk implements Serializable {

    @Bean
    public OracleKeyGenerator genKey() {
        return new OracleKeyGenerator();
    }
    /**
     * 建设项目投资完成情况主键
     */
    @Schema(name = "建设项目投资完成情况主键")
    @TableId(value = "JSXMTZWCQKID",type = IdType.INPUT)
    private Long jsxmtzwcqkid;

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
     * 合同编号
     */
    @Schema(name = "合同编号")
    @TableField(value = "HTBH")
    private String htbh;

    /**
     * 计划文号
     */
    @Schema(name = "计划文号")
    @TableField(value = "JHWH")
    private String jhwh;

    /**
     * 工程或费用名称
     */
    @Schema(name = "工程或费用名称")
    @TableField(value = "GCHFYMC")
    private String gchfymc;

    /**
     * 实施单位
     */
    @Schema(name = "实施单位")
    @TableField(value = "SSDW")
    private String ssdw;


    /**
     * 合同金额
     */
    @Schema(name = "合同金额")
    @TableField(value = "HTJE")
    private BigDecimal htje;

    /**
     * 投资节超（概算-实际完成）
     */
    @Schema(name = "投资节超（概算-实际完成）")
    @TableField(value = "TZJC")
    private String tzjc;



    /**
     * 批复概算投资
     */
    @Schema(name = "批复概算投资")
    @TableField(value = "PFGSTZJE")
    private BigDecimal pfgstzje;

    /**
     * 结算金额
     */
    @Schema(name = "结算金额")
    @TableField(value = "JSJE")
    private BigDecimal jsje;

    /**
     * 投资节超情况说明
     */
    @Schema(name = "投资节超情况说明")
    @TableField(value = "TZJCQKSM")
    private String tzjcqksm;

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

    @Schema(name = "主键集合")
    @TableField(exist = false)
    private List<String> ids;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attids;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    private List<TblAttachment> attachments;

    @Schema(name = "费用明细主键集合")
    @TableField(exist = false)
    private List<String> fymxIds;

    @Schema(name = "工程费用情况明细集合")
    @TableField(exist = false)
    private List<TblYqnsJsxmTzwcqkFymx> fymxGcfy;

    @Schema(name = "其他费用情况明细集合")
    @TableField(exist = false)
    private List<TblYqnsJsxmTzwcqkFymx> fymxQtfy;

    /**
     * 分类：三类|四类
     */
    @Schema(name = "分类：三类|四类")
    @TableField(value = "FL")
    private String fl;

    /**
     * 上报集合
     */
    @Schema(name = "上报集合")
    @TableField(value = "SBIDS")
    private String sbIds;
    
    @TableField(value="TBDW_ID")
    @Schema(name="填报单位")
    private String tbdwId;
    
    @Schema(name = "填报单位")
    @TableField(value="TBDW_NAME")
    private String tbdwName;
    
    
    @Schema(name = "下发人员id")
    @TableField(value="RWIDS")
    private String rwids;
    
    @Schema(name = "下发人员名称")
    @TableField(value="RWNAMES")
    private String rwnames;
    
    @Schema(name = "是否是同步数据 ，1-是 其余否")
    @TableField(value = "ISSYNC")
    private Integer isSync;
    
    @Schema(name = "项目状态:1已做审计项目，其他为未做")
    @TableField(value = "XMSTATUS")
    private Integer xmstatus;
    
    /**
     *项目类别
     */
    @Schema(name = "项目类别")
    @TableField(value = "XMZTTYPE")
    private String xmzttype;
    
    
    @Schema(name = "关联父级ID")
    @TableField(value = "PARENTID")
    private Long parentid;
    
    @Schema(name = "任务项目状态：0或其他 任务项目、1主项目")
    @TableField(value = "FZSTATUS")
    private Integer fzstatus;
    
    
    @Schema(name = "分配督导人员主键id")
    @TableField(value="FPDDRYID")
    private String fpddryid;
    
    @Schema(name = "下发人员名称")
    @TableField(value="FPDDRYNAME")
    private String fpddryname;

    
}