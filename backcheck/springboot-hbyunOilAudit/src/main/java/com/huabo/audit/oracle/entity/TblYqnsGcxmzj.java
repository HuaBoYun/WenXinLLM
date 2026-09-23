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
 * 工程项目造价表
 *
 * @TableName TBL_YQNS_GCXMZJ
 */
@Data
@TableName(value = "TBL_YQNS_GCXMZJ")
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsGcxmzj extends BaseReservedProperty implements Serializable {

    @Bean
    public OracleKeyGenerator genKey() {
        return new OracleKeyGenerator();
    }

    /**
     * 工程项目造价表主键
     */
    @Schema(name = "工程项目造价表主键")
    @TableId(value = "GCXMZJID", type = IdType.INPUT)
    private BigDecimal gcxmzjid;
    
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;
    
    
    @Schema(name = "序号")
    @TableField(value = "SERIALNUMBER")
    private Integer serialNumber;
    
    /**
     * 合同编号
     */
    @Schema(name = "合同编号")
    @TableField(value = "HTBH")
    private String htbh;



    /**
     * 工程id
     */
    @Schema(name = "工程id")
    @TableField(value = "GCID")
    private String gcid;

    /**
     * 工程名称
     */
    @Schema(name = "工程名称")
    @TableField(value = "GCMC")
    private String gcmc;


    /**
     * 建设单位
     */
    @Schema(name = "建设单位")
    @TableField(value = "JSDW")
    private String jsdw;

    /**
     * 施工单位
     */
    @Schema(name = "施工单位")
    @TableField(value = "SGDW")
    private String sgdw;

    /**
     * 联系人
     */
    @Schema(name = "联系人")
    @TableField(value = "LXR")
    private String lxr;

    /**
     * 联系电话
     */
    @Schema(name = "联系电话")
    @TableField(value = "LXDH")
    private String lxdh;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(value = "CJSJ")
    private Date cjsj;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(value = "GXSJ")
    private Date gxsj;

    /**
     * 二审审查金额
     */
    @Schema(name = "二审审查金额（元）")
    @TableField(value = "ESSCJE")
    private BigDecimal esscje;
    
    
    /**
     * 二审审查金额
     */
    @Schema(name = "二审审查金额（万元）")
    @TableField(value = "ESSCJEWY")
    private BigDecimal esscjewy;

    /**
     * 审批状态
     */
    @Schema(name = "审批状态")
    @TableField(value = "SPZT")
    private Long spzt;

    /**
     * 组织
     */
    @Schema(name = "组织")
    @TableField(value = "ORGID")
    private Long orgid;

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

    /**
     * 扩展字段
     */
    @Schema(name = "扩展字段")
    @TableField(value = "EXT1")
    private String ext1;

    /**
     * 扩展字段2
     */
    @Schema(name = "扩展字段2")
    @TableField(value = "EXT2")
    private String ext2;

    @TableField(value = "STATUS")
    @Schema(name="状态")
    private Integer status;

    /**
     * 扩展字段3
     */
    @Schema(name = "扩展字段3")
    @TableField(value = "EXT3")
    private String ext3;

    private static final long serialVersionUID = 1L;
    
    @TableField(exist = false)
    @Schema(name="创建年度")
    private String createYear;
    
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
    private BigDecimal parentid;
    
    @Schema(name = "任务项目状态：0或其他 任务项目、1主项目")
    @TableField(value = "FZSTATUS")
    private Integer fzstatus;

}