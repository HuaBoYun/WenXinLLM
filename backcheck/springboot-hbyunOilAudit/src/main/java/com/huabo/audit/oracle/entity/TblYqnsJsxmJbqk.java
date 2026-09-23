package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 建设项目基本情况表
 *
 * @TableName TBL_YQNS_JSXM_JBQK
 */
@Data
@TableName(value = "TBL_YQNS_JSXM_JBQK")
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsJsxmJbqk implements Serializable {

    @Bean
    public OracleKeyGenerator genKey() {
        return new OracleKeyGenerator();
    }
    /**
     * 建设项目基本情况主键
     */
    @Schema(name = "建设项目基本情况主键")
    @TableId(value = "JSXMJBQKID", type = IdType.INPUT)
    private Long jsxmjbqkid;

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
     * 验收项目名称
     */
    @Schema(name = "验收项目名称")
    @TableField(value = "YSXMMC")
    private String ysxmmc;



    /**
     * 计划投资金额（万元）
     */
    @Schema(name = "计划投资金额（万元）")
    @TableField(value = "JHTZJE")
    private BigDecimal jhtzje;

    /**
     * 承包方式（EPC或施工承包）
     */
    @Schema(name = "承包方式（EPC或施工承包）")
    @TableField(value = "CBFS")
    private String cbfs;

    /**
     *	 开工时间
     */
    @Schema(name = "开工时间")
    @TableField(value = "KJGSJ")
    private String kjgsj;

    
    
    /**
     * 	竣工时间
     */
    @Schema(name = "竣工时间")
    @TableField(value = "JGSJ")
    private String jgsj;
    
    /**
     * 项目投产时间
     */
    @Schema(name = "项目投产时间")
    @TableField(value = "XMTCSJ")
    private String xmtcsj;

    /**
     * 试运行合格时间
     */
    @Schema(name = "试运行合格时间")
    @TableField(value = "SYXHGSJ")
    private String syxhgsj;

    /**
     * 资料归档时间
     */
    @Schema(name = "资料归档时间")
    @TableField(value = "ZLGDSJ")
    private String zlgdsj;

    /**
     * 完成工程结算时间（二审）
     */
    @Schema(name = "完成工程结算时间（二审）")
    @TableField(value = "WCGCJSSJ")
    private String wcgcjssj;

    /**
     * 拟竣工验收时间
     */
    @Schema(name = "拟竣工验收时间")
    @TableField(value = "NJGYSSJ")
    private String njgyssj;

    /**
     * 其他专项验收情况
     */
    @Schema(name = "其他专项验收情况")
    @TableField(value = "QTZXYSQK")
    private String qtzxysqk;

    /**
     * 是否完成财务决算(是或否)
     */
    @Schema(name = "是否完成财务决算(是或否)")
    @TableField(value = "SFWCCWJS")
    private String sfwccwjs;

    /**
     * 项目负责人
     */
    @Schema(name = "项目负责人")
    @TableField(value = "XMFZR")
    private String xmfzr;

    /**
     * 工程费用结算金额
     */
    @Schema(name = "工程费用结算金额")
    @TableField(value = "GCFYJSJE")
    private BigDecimal gcfyjsje;


    /**
     * 联系电话
     */
    @Schema(name = "联系电话")
    @TableField(value = "LXDH")
    private String lxdh;

    /**
     * 备注
     */
    @Schema(name = "备注")
    @TableField(value = "BZ")
    private String bz;

    /**
     * 分类：三类|四类
     */
    @Schema(name = "分类：三类|四类")
    @TableField(value = "FL")
    private String fl;

    /**
     * 竣工计划
     */
    @Schema(name = "竣工计划")
    @TableField(value = "JGJH")
    private String jgjh;

    /**
     * 上报集合
     */
    @Schema(name = "上报集合")
    @TableField(value = "SBIDS")
    private String sbIds;

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
    private List<String> attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    private List<TblAttachment> attachments;
    
    
    
    @Schema(name = "计划文号附件id")
    @TableField(value = "ATTID")
    private BigDecimal attid;
    
    
    @Schema(name = "计划文号附件名称")
    @TableField(value = "ATTNAME")
    private String attname;
    
    
    
    @Schema(name = "填报单位id")
    @TableField(value = "TBORGID")
    private BigDecimal tborgid;
    
    
    @Schema(name = "填报单位名称")
    @TableField(value = "TBORGNAME")
    private String tborgname;
    
    
    @Schema(name = "创建年度")
    @TableField(exist = false)
    private String createyear;
    
    @Schema(name = "审批状态")
    @TableField(value = "STATUS")
    private Integer status;
    
    
    @Schema(name = "关联竣工计划id")
    @TableField(value = "JGPLANID")
    private BigDecimal jgplanid;
    
    @Schema(name = "项目数量")
    @TableField(value = "ITEMCOUNT")
    private Integer itemCount;
    
    
    @Schema(name = "项目状态:1已做审计项目，其他为未做")
    @TableField(value = "XMSTATUS")
    private Integer xmstatus;
    
    /**
     *项目类别
     */
    @Schema(name = "项目类别")
    @TableField(value = "XMZTTYPE")
    private String xmzttype;
    

}