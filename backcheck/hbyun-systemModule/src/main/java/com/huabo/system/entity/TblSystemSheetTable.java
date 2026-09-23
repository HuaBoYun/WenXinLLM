package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="系统表单信息表", description="")
@TableName(value = "TBL_SYSTEM_SHEETTABLE")
public class TblSystemSheetTable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name="主键")
    @TableId(value="TABLEID",type = IdType.INPUT)
    private BigDecimal tableId;

    @TableField("TABLETYPE")
    @Schema(name="所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx 业务管控财经实训等后续新增的一级权限 为该权限的Id")
    private String tableType;

    @TableField("RIGHTID")
    @Schema(name="所属权限")
    private String rightId;
    
    @TableField("FLOWID")
    @Schema(name="所属流程")
    private String flowId;
   
    @TableField("CLASSNAME")
    @Schema(name="类名")
    private String className;
    
    @TableField("TABLENAME")
    @Schema(name="表名")
    private String tableName;
    
    @TableField("STATUSPRO")
    @Schema(name="状态字段")
    private String statusPro;
    
    @TableField("PRIMARYCOLUMN")
    @Schema(name="主键字段")
    private String primaryColumn;
    
    @TableField(exist = false)
    @Schema(name="流程启用状态 1-启用")
    private String qyStats;
    
    
    @TableField("YMFORMCODE")
    @Schema(name="流程平台表单字段")
    @Column(name="YMFORMCODE")
    private String ymFormCode;
    
    @TableField("SUBTABLENAME")
    @Schema(name="字表名")
    @Column(name="SUBTABLENAME")
    private String subTableName;
    
    @TableField("SUBTABLERELA")
    @Schema(name="关联字段")
    @Column(name="SUBTABLERELA")
    private String subtableRela;
    
    
    @TableField("SUBTABLESTATUS")
    @Schema(name="修改状态")
    @Column(name="SUBTABLESTATUS")
    private String subtableStatus;
    
    
    @TableField("SUBTABLESCOL")
    @Schema(name="修改子表列")
    @Column(name="SUBTABLESCOL")
    private String subtablesCol;
    
    @TableField("REWIRTEFLOWINFO")
    @Schema(name="是否反写流程信息 1是  0否")
    @Column(name="REWIRTEFLOWINFO")
    private Integer rewirteFlowInfo;
    
    @TableField("ISAUTOCOPY")
    @Schema(name="是否自动抄送流程信息至各个审批节点 1是  0否")
    @Column(name="ISAUTOCOPY")
    private Integer isAutoCopy;
    
    @TableField("ISCOPYFORM")
    @Schema(name="是否自选表单审批节点字段抄送流程信息 1是  0否")
    @Column(name="ISCOPYFORM")
    private Integer isCopyForm;
    
    @TableField("DEPTIDCOLUMN")
    @Schema(name="表单所属的公司字段")
    @Column(name="DEPTIDCOLUMN")
    private String deptIdColumn;
    
    @TableField(exist = false)
    @Schema(name="版本")
    private String version;
    
    @TableField(exist = false)
    @Schema(name="工作流名称")
    private String ymWorkName;
    
    @TableField(exist = false)
    @Schema(name="工作流主键")
    private String ymWorkFrom;
    
    @TableField(exist = false)
    @Schema(name="工作流数量")
    private Integer workCount;
    
    @TableField(exist = false)
    @Schema(name="分页序号")
    private Integer rowNo;
    
    @TableField("MIDDLETABLE")
    @Schema(name="中间关系表")
    @Column(name="MIDDLETABLE")
    private String middleTable;
    
    @TableField("MIDDLECOLUMN")
    @Schema(name="中间关系变关联审批主体字段")
    @Column(name="MIDDLECOLUMN")
    private String middleColumn;
    
    @TableField("MIDDLERELA")
    @Schema(name="中间关系表关修改关联主体字段")
    @Column(name="MIDDLERELA")
    private String middleRela;
    
    @TableField("SECRECTCOLUMN")
    @Schema(name="密级主键字段")
    private String secrectColumn;
    
    @TableField("STAFFSCOPECOLUMN")
    @Schema(name="密级主键字段")
    private String staffScopeColumn;
    
    @Schema(name="业务单据主键")
    @TableField(exist = false)
    private String formId;
    
    @Schema(name="流程设计模板主键")
    @TableField(exist = false)
    private String flowtemplateId;
    
}
