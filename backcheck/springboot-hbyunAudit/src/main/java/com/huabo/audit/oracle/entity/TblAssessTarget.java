package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_ASSESS_TARGET")
@Schema(name="TblAssessTarget对象")
public class TblAssessTarget implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "评价立项Id")
      @TableField("ASSID")
    private BigDecimal assid;

      @Schema(name = "被评价对象")
      @TableField("ORGID")
    private BigDecimal orgid;

      @Schema(name = "初步评分")
      @TableField("FINALSCORE")
    private Float finalscore;

      @Schema(name = "校正级别")
      @TableField("CHECKLEVEL")
    private String checklevel;

      @Schema(name = "校正原因")
      @TableField("CHECKREASON")
    private String checkreason;

      @Schema(name = "状态")
      @TableField("STATUS")
    private String status;

      @Schema(name = "评价结果Id")
      @TableField("ASSESSTARGETID")
    private BigDecimal assesstargetid;

      @Schema(name = "初步级别")
      @TableField("FINALLEVEL")
    private String finallevel;

//TblAssessStaff
    @Transient
    @TableField(exist = false)
    private BigDecimal assstaffid;
    @Transient
    @TableField(exist = false)
    private BigDecimal assweight;
    @Transient
    @TableField(exist = false)
    private String memo;
    @Transient
    @TableField(exist = false)
    private BigDecimal staffid;
    @Transient
    @TableField(exist = false)
    private BigDecimal score;
    @Transient
    @TableField(exist = false)
    private String reason;
    @Transient
    @TableField(exist = false)
    private LocalDateTime assdatetime;
    @Transient
    @TableField(exist = false)
    private BigDecimal assmarkid;
    @Transient
    @TableField(exist = false)
    private BigDecimal attid;
    @Transient
    @TableField(exist = false)
    private String examination;
    @Transient
    @TableField(exist = false)
    private BigDecimal sheetid;

    //TblAssessMark
    @Transient
    @TableField(exist = false)
    private String suitable;
    @Transient
    @TableField(exist = false)
    private BigDecimal asseleid;
    @Transient
    @TableField(exist = false)
    private String state;
    @Transient
    @TableField(exist = false)
    private BigDecimal assorgid;


    //TblAssess
    @Schema(name = "评价项目编号")
    @Transient
    @TableField(exist = false)
    private String assessid;
    @Schema(name = "开始时间")
    @Transient
    @TableField(exist = false)
    private LocalDateTime startdate;
    @Schema(name = "结束是时间")
    @Transient
    @TableField(exist = false)
    private LocalDateTime enddate;
    @Schema(name = "状态(1创建2启动)")
    @Transient
    @TableField(exist = false)
    private String assstatus;
    @Schema(name = "发起日期")
    @Transient
    @TableField(exist = false)
    private LocalDateTime assstartday;
    @Schema(name = "评价组织")
    @Transient
    @TableField(exist = false)
    private String assorgs;
    @Schema(name = "评价项目名称")
    @Transient
    @TableField(exist = false)
    private String assessname;
    @Schema(name = "评价对象")
    @Transient
    @TableField(exist = false)
    private String asssponsor;
    @Schema(name = "归档人")
    @Transient
    @TableField(exist = false)
    private String archiveperson;
    @Schema(name = "归档时间")
    @Transient
    @TableField(exist = false)
    private LocalDateTime archivetime;
    @Schema(name = "初步评价等级")
    @Transient
    @TableField(exist = false)
    private String preliminaryasslevel;
    @Schema(name = "初步评价评分")
    @Transient
    @TableField(exist = false)
    private BigDecimal preliminaryassscore;
    @Schema(name = "校正级别")
    @Transient
    @TableField(exist = false)
    private String adustlevel;
    @Schema(name = "校正原因")
    @Transient
    @TableField(exist = false)
    private String adjustreson;
    @Transient
    @TableField(exist = false)
    private String analysissummary;
    @Transient
    @TableField(exist = false)
    private LocalDateTime assessdate;
    @Schema(name = "评价负责人")
    @Transient
    @TableField(exist = false)
    private BigDecimal leaderid;
    @Transient
    @TableField(exist = false)
    private String tblcomany;
    @Schema(name = "海装新增-缺陷来源项目")
    @Transient
    @TableField(exist = false)
    private String defectsource;
    @Schema(name = "海装新增-缺陷来源文件编号")
    @Transient
    @TableField(exist = false)
    private String sourcefile;

    //TblOrganization
    @Transient
    @TableField(exist = false)
    @Schema(name = "公司名称")
    private String orgname;
    @Transient
    @TableField(exist = false)
    private BigDecimal fatherorgid;
    @Transient
    @TableField(exist = false)
    @Schema(name = "公司编号")
    private String orgnumber;
    @Transient
    @TableField(exist = false)
    @Schema(name = "公司简介")
    private String orgmeno;

    @Transient
    @TableField(exist = false)
    private String icode;///行业架构ID（在哪个行业下创建行业知识库/行业缺陷库/行业问题库/行业数据库/行业指标库/行业规则库/行业模型库，该字段为哪个行业ID）
    @Transient//是否是公司（普通部门为0，一级公司为1，二级公司为2，
    // 三级公司为3，行业架构为100，行业问题库为101，行业缺陷库为102，行业规则库为103，
    // 行业指标库为104，行业模型库为105，行业知识库为106，审计经验库为107，行业数据库为108）
    @TableField(exist = false)
    private Integer orgtype;
    @Transient//是否是主责部门/审计部（1是，0否）
    @TableField(exist = false)
    private Integer auditType;
    @Transient//是否开启望远镜
    @TableField(exist = false)
    private String iszy;
    @Transient//标识行业知识库的所属模块（风险管控为fxmanage，内部控制为nbkz，智能审计为znsj，智能监控为znjk）
    @TableField(exist = false)
    private String hyzsktype;
    @Transient//排序编号，用于在显示组织架构排序
    @TableField(exist = false)
    private Integer orderid;
    @TableField(exist = false)
    @Transient //标识企业来源 为null是本系统，1：蜂信，以后可能为2,3...来表示其它来源   3:华博云注册公司   4:首冠注册用户  5:中财协注册用户  6：大成方略注册用户 7.用友用户
    private Integer outsideid;
    @TableField(exist = false)
    @Transient//外部同步企业来源Id
    private String outsideopendid;
    @TableField(exist = false)
    @Schema(name = "是否使用自动编号 0 不使用；1 使用")
    @Transient
    private Integer isautonumber;
    @TableField(exist = false)
    @Transient
    private Date orgcreate;
    @TableField(exist = false)
    @Transient// 判断该组织有没有初始化 0初始化 ，
    private Integer isinitialization;
    @Schema(name = "职务")
    @Transient
    @TableField(exist = false)
    private String duties;
    @Schema(name = "行业编号")
    @Transient
    @TableField(exist = false)
    private Integer industryid;
    @Schema(name = "新增来源于微信 1为微信 0为pc")
    @Transient
    @TableField(exist = false)
    private String bywx;
    @Transient
    @TableField(exist = false)
    private String datasource;
    @Transient
    @TableField(exist = false)
    private String historycode;
    @Transient
    @TableField(exist = false)
    private String historydepartmentid;
}
