package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.dto.TblYqnsEnginProjectAttDto;
import com.huabo.audit.oracle.entity.base.BaseProjectEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;

/**
 * TBL_YQNS_ENGIN_AUDIT_PROJECT
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_ENGIN_AUDIT_PROJECT")
@Schema(name="TblYqnsEnginAuditProjectEntity对象")
public class TblYqnsEnginAuditProjectEntity extends BaseProjectEntity implements Serializable {

    /**
     * 审计项目名称
     */
    @Schema(name = "审计项目名称")
    @TableField("NAME")
    private String name;


    /**
     * 审计组
     */
    @Schema(name = "小组")
    @TableField("AUDITGROUP")
    private String auditGroup;

    @Schema(name = "是否是同步工程数据 ，1-是 其余否")
    @TableField(value = "ISSYNCGC")
    private Integer isSyncgc;
    
    @Schema(name = "是否是同步竣工数据 ，1-是 其余否")
    @TableField(value = "ISSYNCJG")
    private Integer isSyncjg;
    
    
    
    /**
     * 被审计单位
     */
    @Schema(name = "被审计单位")
    @TableField("AUDITUNIT")
    private String auditUnit;

    /**
     * 被审计单位Id
     */
    @Schema(name = "被审计单位ID")
    @TableField("AUDITUNITID")
    private Long auditUnitId;

    /**
     * 项目数量
     */
    @Schema(name = "项目数量")
    @TableField("PROJECTNUM")
    private BigDecimal projectNum;

    /**
     * 金额,单位元
     */
    @Schema(name = "金额,单位元")
    @TableField("AMOUNT")
    private Double amount;

    @Schema(name = "计划关联id")
    @TableField("JHID")
    private BigDecimal jhid;
    
    @Schema(name = "关联计划项目id")
    @TableField("GLJHXMID")
    private BigDecimal gljhxmid;
    
    @Schema(name = "关联计划项目类型")
    @TableField("GLJHXMLX")
    private String gljhxmlx;
    
    
    @Schema(name = "关联计划id")
    @TableField("PLANID")
    private BigDecimal planid;
    

    @Schema(name = "关联计划名称")
    @TableField("PLANNAME")
    private String planname;

    
    @Schema(name = "现在开始时间")
    @TableField("XCSRARTTIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date xcsrarttime;

    
    @Schema(name = "现在结束时间")
    @TableField("XCENDTIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date xcendtime;
    
    
    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks;

    
    @Schema(name = "下发项目组人员ids")
    @TableField("XFSMZRYIDS")
    private String xfsmzryids;
    
    
    @Schema(name = "下发项目组人员名称")
    @TableField("XFXMZRYNAMES")
    private String xfxmzrynames;
    
    
    @Schema(name = "下发专业科室负责人员ids")
    @TableField("XFKSRYIDS")
    private String xfksryids;
    
    
    @Schema(name = "下发专业科室负责人员名称")
    @TableField("XFKSRYNAMES")
    private String xfksrynames;
    
    @TableField("FZZSTAFFID")
    @Schema(name = "副组长主键")
    private BigDecimal fzzStafffId;
    
    @TableField("FZZNAME")
    @Schema(name = "副组长名称")
    private String fzzName;
    
    
    @Schema(name = "分配专业科室督导人员ids")
    @TableField("FPKSRYIDS")
    private String fpksryids;
    
    
    @Schema(name = "分配专业科室督导人员名称")
    @TableField("FPKSRYNAMES")
    private String fpksrynames;
    
    @Schema(name = "分配审理科人员主键id")
    @TableField("FPSLKRYID")
    private String fpslkryid;
    
    @Schema(name = "分配审理科人员人员名称")
    @TableField("FPSLKRYNAME")
    private String fpslkryname;
    
    
    @Schema(name = "编号")
    @TableField(value = "CODE")
    private String code;

    @TableField(exist = false)
    @Schema(name = "关联附件")
    private List<TblYqnsEnginProjectAttDto> enginProjectAttDtoList;
    
    @Schema(name = "人员数量")
    @TableField("RSYQ")
    private Integer rsyq;
    
    @Schema(name = "项目数量")
    @TableField("XMSL")
    private Integer xmsl;
    
    @Schema(name = "下发状态（1已上报 0或null 未上报）")
    @TableField("XFSTATUS")
    private Integer xfstatus;
    
    
    
    private static final long serialVersionUID = 1L;
}