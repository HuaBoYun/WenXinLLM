package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

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
@Schema(name="系统表单信息表")
@Table(name = "TBL_SYSTEM_SHEETTABLE")
public class TblSystemSheetTableMySql implements Serializable {
	
	private static final long serialVersionUID = -8359754594588105141L;

	@Id
    @Schema(name = "主键")
    @TableId("TABLEID")
	@Column(name="TABLEID")
    private Integer tableId;

    @TableField("TABLETYPE")
    @Schema(name = "所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx 业务管控财经实训等后续新增的一级权限 为该权限的Id")
    @Column(name="TABLETYPE")
    private String tableType;

    @TableField("RIGHTID")
    @Schema(name = "所属权限")
    @Column(name="RIGHTID")
    private String rightId;
    
    @TableField("FLOWID")
    @Schema(name = "所属流程")
    @Column(name="FLOWID")
    private String flowId;
   
    @TableField("CLASSNAME")
    @Schema(name = "类名")
    @Column(name="CLASSNAME")
    private String className;
    
    @TableField("TABLENAME")
    @Schema(name = "表名")
    @Column(name="TABLENAME")
    private String tableName;
    
    @TableField("STATUSPRO")
    @Schema(name = "状态字段")
    @Column(name="STATUSPRO")
    private String statusPro;
    
    @TableField("PRIMARYCOLUMN")
    @Schema(name = "主键字段")
    @Column(name="PRIMARYCOLUMN")
    private String primaryColumn;
    
    @TableField("YMFORMCODE")
    @Schema(name = "流程平台表单字段")
    @Column(name="YMFORMCODE")
    private String ymFormCode;
    
    @Transient
    @Schema(name = "工作流名称")
    private String ymWorkName;
    
    @Transient
    @Schema(name = "工作流主键")
    private String ymWorkFrom;
    
    @Transient
    @Schema(name = "工作流数量")
    private Integer workCount;
    
}
