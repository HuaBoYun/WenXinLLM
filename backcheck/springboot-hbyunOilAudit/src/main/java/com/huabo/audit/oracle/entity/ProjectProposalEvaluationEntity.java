package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author CJ
 * @ClassName ProjectProposalEvaluationEntity
 * @Description
 * @DATE 2024/5/28
 */
@Data
@Table(name = "TBL_YQNS_PROJECT_EVALUATION")
@Schema(name="立项建议专业评估实体")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProjectProposalEvaluationEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final Integer YEWSHOW = 1;//显示在列表页上
    public static final Integer NOSHOW = 0;//不显示在列表页上


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Schema(name = "id")
    private BigDecimal id;

    //@Column(name="PROJECTNAME")
    @Column(name = "PROJECTNAME")
    @Schema(name="审计项目名称")
    private String projectName;

    @Column(name = "PROJECTPURPOSE")
    @Schema(name="立项理由及审计目的")
    private String projectPurpose;

    @Column(name = "PROJECTTYPE")
    @Schema(name="业务类型：工程  财务")
    private String projectType;

    @Column(name = "ITEMTYPE")
    @Schema(name="项目类型")
    private String itemType;

    @Column(name = "CONCERNSCONTENT")
    @Schema(name="重点关注内容")
    private String concernsContent;

    @Column(name = "UNITRANGE")
    @Schema(name="单位范围")
    private String unitRange;


    @Column(name = "SORTNUMBER")
    @Schema(name="排序")
    private Integer sortNumber;

    @Column(name = "REMARK")
    @Schema(name="备注")
    private String remark;

    @Column(name = "CREATEUSER")
    @Schema(name="创建人")
    private BigDecimal createUser;

    @Column(name = "CREATETIME")
    @Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    @Column(name = "DEPARTMENTID")
    @Schema(name="建议科室ID")
    private BigDecimal departmentId;

    @Column(name = "DEPARTMENTNAME")
    @Schema(name="建议科室NAME")
    private String departmentName;


    @Column(name = "AUDITSCOPE")
    @Schema(name="审计范围")
    private String auditScope;

    @TableField(exist = false)
    @Schema(name="关联用户信息")
    private TblStaff createStaff;

    @Schema(name="是否显示在列表 默认不显示  1-显示")
    @Column(name = "ISSHOWLIST")
    private Integer isShowList;


    @Column(name = "UNITRANGEID")
    @Schema(name="单位范围ID")
    private BigDecimal unitRangeId;

    @Column(name = "TIMERANGEL")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(name="时间范围左")
    private java.util.Date timeRangel;

    @Column(name = "TIMERANGER")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(name="时间范围右")
    private java.util.Date timeRangeR;

    @Schema(name="立项建议表主键")
    @Column(name = "SUGGESTIONID")
    @TableField(value = "SUGGESTIONID")
    private BigDecimal suggestionId;


    @Schema(name="创建年度")
    @Transient
    private java.util.Date startyear;

    @Schema(name="创建年度")
    @Transient
    private java.util.Date endyear;

    @Schema(name="组织id集合")
    @Transient
    private List<String> deptids;


    @Schema(name="关联填报id")
    @TableField(exist = false)
    @Transient
    private BigDecimal tbid;

    @Schema(name="填报表状态用来查询")
    @TableField(exist = false)
    @Transient
    private Integer status;

    @Schema(name="关联id")
    @TableField(exist = false)
    @Transient
    private BigDecimal relaid;

    @Schema(name="JHid")
    @TableField(exist = false)
    @Transient
    private BigDecimal jhid;

    @Schema(name="来源类型 1-草稿 ，2-初稿，3-终稿 默认1")
    @TableField(exist = false)
    @Transient
    private Integer sourceType;

    @Schema(name="ids")
    @Transient
    private List<String> ids;

}
