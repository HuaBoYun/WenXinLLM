package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name="TBL_YQNS_ADVICEAPR")
@Schema(name="审计通知审批", description="审计通知审批")
public class TblYqnsAdviceAprEntity extends BaseReservedProperty {

    public final static Integer NO_DEL=0;//未删除 使用中
    public final static Integer YE_DEL=1;//已删除 已作废
    public final static Integer SPZ=2;//审批中
    public final static Integer XTZ=3;//需调整
    public final static Integer YTG=4;//已通过
    public final static Integer YZZ=5;//已终止

    private static final long serialVersionUID = 1L;

    @TableId(value = "adviceid", type= IdType.AUTO)
    @Schema(name = "主键ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    private BigDecimal adviceid;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    @Transient
    private TblStaff tblCreater;

    @TableField(value = "creatrtime")
    @Schema(name="创建时间",hidden=true)
    @IgnoreSwaggerParameter
    private Date creatrtime;

    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    @Transient
    private ImplementPlanEntity project;

//    @TableField(value = "advicecoed")
//    @Schema(name = "审计通知书编号")
//    private String advicecoed;

    @TableField(value = "advicename")
    @Schema(name = "审计通知书名称")
    private String advicename;

    @TableField(value = "PROGECTID")
    @Schema(name = "项目ID")
    private String progectid;

    @TableField(value = "status")
    @Schema(name = "状态")
    private Integer status;

//    @TableField(value = "content")
//    @Schema(name = "内容")
//    private String content;
//
//    @TableField(value = "des")
//    @Schema
//    private String des;

//    @Schema(hidden=true)
//    @IgnoreSwaggerParameter
//    @Transient
//    private List<TblAttachment> tblNoteAtts;

    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    @Transient
    private TblOrganization organization;

    @TableField(value = "CREATESTAFFID")
    @Schema(name="员工ID",hidden=true)
    private String createstaffid;

    @TableField(value = "SJSSTIME")
    @Schema(name = "审计实施时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sjsstime;

    @TableField(value = "TEAMLEADER")
    @Schema(name = "审计组长")
    private String teamleader;
    
    @Schema(name = "组长id")
    @TableField(value = "ZCSTAFFID")
    private BigDecimal zcstaffid;
    

    @TableField(value = "MAINREVIEWER")
    @Schema(name = "审计主审")
    private String mainreviewer;
    
    
    @Schema(name = "主审id")
    @TableField(value = "ZSSTAFFID")
    private BigDecimal zsstaffid;
    
    

    @TableField(value = "HELPREVIEWER")
    @Schema(name = "审计助审")
    private String helpreviewer;
    
    
    @TableField(value = "ASSISTAPPROVERID")
    @Schema(name = "助审Id")
    private String assistapproverid;
    

    @TableField(value = "OPERATOR")
    @Schema(name = "经办人")
    private String operator;

    @TableField(value = "DEPARTMENT")
    @Schema(name = "审计项目实施部门")
    private String department;

    @TableField(value = "ORGIDS")
    @Schema(name = "被审计单位全称")
    private BigDecimal orgids;


    @TableField(value = "FZSTAFFIDS")
    @Schema(name = "副组长ID")
    private BigDecimal fzstaffids;
    
    
    @TableField(value = "FZNAMES")
    @Schema(name = "副组长名称")
    private String fznames;
    
    
    @TableField(exist = false)
    @Schema(name = "项目名称")
    @Transient
    private String projectname;
    
    
    @TableField(exist = false)
    @Schema(name = "项目名称")
    @Transient
    private String orgName;
    
    @TableField(exist = false)
    @Schema(name = "查询筛选类型：sjtzsp-审计通知审批选择项目")
    private String xctype;
    
    
}
