package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工程项目造价中间表
 * @TableName TBL_YQNS_GCXMZJ_ZJB
 */
@Data
@TableName(value ="TBL_YQNS_GCXMZJ_ZJB")
public class TblYqnsGcxmzjZjb extends BaseReservedProperty implements Serializable {
	
    /**
     * 工程项目造价中间表主键
     */
    @Schema(name = "工程项目造价中间表主键")
    @TableId(value = "GCXMZJZJBID", type = IdType.INPUT)
    private BigDecimal gcxmzjzjbid;
    
    @Schema(name = "工程项目造价主键")
    @TableField(value = "GCXMZJJZBNO")
    private BigDecimal gcxmzjZjbNo;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;
    
    /**
     * 工程项目造价主键
     */
    @Schema(name = "工程项目造价主键")
    @TableField(value = "GCXMZJID")
    private BigDecimal gcxmzjid;

    /**
     * 额度金额
     */
    @Schema(name = "额度金额")
    @TableField(value = "EDJE")
    private String edje;

    /**
     * 内外部
     */
    @Schema(name = "内外部")
    @TableField(value = "NWB")
    private String nwb;

    /**
     * 组织
     */
    @Schema(name = "组织")
    @Column(name = "ORGID")
    private Long orgid;

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
     * 审批状态
     */
    @Schema(name = "审批状态")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(value = "SPZT")
    private Long spzt;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private TblYqnsGcxmzj tblYqnsGcxmzj;

    @Schema(name = "开始时间")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;

    @Schema(name = "结束时间")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;

    @TableField(value = "STATUS")
    @Schema(name="状态")
    private Integer status;

    @TableField(exist = false)
    private List<String> ids;
    
    @TableField(exist = false)
    @Schema(name="创建年度")
    private String createYear;
    
    
    /**
     * 二审审查金额
     */
    @Schema(name = "二审审查金额（万元）")
    @TableField(exist = false)
    private BigDecimal esscjewy;
    
    
    @Schema(name = "下发人员id")
    @TableField(value="RWIDS")
    private String rwids;
    
    @Schema(name = "下发人员名称")
    @TableField(value="RWNAMES")
    private String rwnames;
    
    @TableField(exist = false)
    private String deips;
    
    
    @Schema(name = "筛选条件 -建设单位")
    @TableField(exist = false)
    private String jsdw;
    
    @TableField(exist = false)
    @Schema(name="排序规则  1-倒序  0-升序  默认1")
    private Integer order;


    @Schema(name = "关联父级ID")
    @TableField(value = "PARENTID")
    private BigDecimal parentid;
    
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