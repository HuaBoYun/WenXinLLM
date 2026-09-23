package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Transient;
import javax.persistence.Column;



/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-09-05
 */
@TableName("TBL_FLOW")
@Schema(name="TblFlow对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblFlow implements Serializable {

    private static final long serialVersionUID = 1L;
    public final static Integer YES_VSESION=1;
    public final static Integer NO_VSESION=0;
    public final static Integer STATE_SP = 1;//审批中
    public final static Integer STATE_TZ = 2;//调整
    public final static Integer STATE_TG = 3;//已通过
    public final static Integer STATE_ZZ = 4;//已终止
    public final static Integer STATE_GZ = 5;//已跟踪
    public final static Integer STATE_WC = 6;//已完成

    @Schema(name="流程ID")
    @TableId(type= IdType.INPUT)
    private BigDecimal flowid;

    @Schema(name="流程编号")
    private String flownumber;

    @Schema(name="流程名称")
    private String flowname;

    @Schema(name="公司ID")
    private String company;

    @Schema(name="部门ID")
    private String departincharge;

    @Schema(name="使用范围")
    private String flowrange;

    @Schema(name="流程状态")
    private String flowstatus;

    @Schema(name="备注")
    private String memo;

    //@Schema(name="版本")
    //private BigDecimal version;
    @TableField("VERSION")
	@Column(name = "VERSION")
	private Integer version;
    
    private String flowchart;

    @Schema(name="业务参与部门")
    private String departassist;

    @Schema(name="录入人")
    private String editor;

    @Schema(name="更新时间")
    private LocalDateTime updatetime;

    private String relatedrules;

    private String affectdegree;

    @TableField("interface")
    private String interface_;

    @Schema(name="父流程ID")
    private BigDecimal fatherflowid;

    @Schema(name="创建时间")
    private String createtime;

    @Schema(name="最后更改时间")
    private String lastmodifiedtime;

    //private Integer flowbysystem;
    @TableField("FLOWBYSYSTEM")
	@Column(name = "FLOWBYSYSTEM")
	private String  flowbysystem;//标记  ：  0 无效   1 内部控制 流程图   2风险流程图   区分内控还是风险

    //private BigDecimal inflowdb;
    
    @TableField("INFLOWDB")
	private Integer inflowdb;//0 流程  1行业

    private BigDecimal position;

    private BigDecimal versiontype;

    @Transient
    @TableField(exist=false)
	private String comName;
    //private BigDecimal status;
    
    @TableField("STATUS")
	 private Integer status;//审批状态

    private String settingid;

    private BigDecimal fromid;

    //private BigDecimal firingstatus;
    @TableField("FIRINGSTATUS")
	private Integer firingStatus;//业务流程启动状态 1 启动 2弃用 null 未启动
    
    private String editmodule;

    private String flowmappingurl;
    
    @Transient
    @TableField(exist=false)
	private String deparChargeName;
}
