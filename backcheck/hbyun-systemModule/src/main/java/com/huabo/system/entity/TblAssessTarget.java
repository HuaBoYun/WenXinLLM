package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

      @Schema(name= "评价立项Id")
      @TableField("ASSID")
    private BigDecimal assid;

      @Schema(name= "被评价对象")
      @TableField("ORGID")
    private BigDecimal orgid;

      @Schema(name= "初步评分")
      @TableField("FINALSCORE")
    private Float finalscore;

      @Schema(name= "校正级别")
      @TableField("CHECKLEVEL")
    private String checklevel;

      @Schema(name= "校正原因")
      @TableField("CHECKREASON")
    private String checkreason;

      @Schema(name= "状态")
      @TableField("STATUS")
    private String status;

      @Schema(name= "评价结果Id")
      @TableField("ASSESSTARGETID")
    private BigDecimal assesstargetid;

      @Schema(name= "初步级别")
      @TableField("FINALLEVEL")
    private String finallevel;

//TblAssessStaff
    @Transient
    private BigDecimal assstaffid;
    @Transient
    private BigDecimal assweight;
    @Transient
    private String memo;
    @Transient
    private BigDecimal staffid;
    @Transient
    private BigDecimal score;
    @Transient
    private String reason;
    @Transient
    private LocalDateTime assdatetime;
    @Transient
    private BigDecimal assmarkid;
    @Transient
    private BigDecimal attid;
    @Transient
    private String examination;
    @Transient
    private BigDecimal sheetid;

    //TblAssessMark
    @Transient
    private String suitable;
    @Transient
    private BigDecimal asseleid;
    @Transient
    private String state;
    @Transient
    private BigDecimal assorgid;


    //TblAssess
    @Schema(name= "评价项目编号")
    @Transient
    private String assessid;
    @Schema(name= "开始时间")
    @Transient
    private LocalDateTime startdate;
    @Schema(name= "结束是时间")
    @Transient
    private LocalDateTime enddate;
    @Schema(name= "状态(1创建2启动)")
    @Transient
    private String assstatus;
    @Schema(name= "发起日期")
    @Transient
    private LocalDateTime assstartday;
    @Schema(name= "评价组织")
    @Transient
    private String assorgs;
    @Schema(name= "评价项目名称")
    @Transient
    private String assessname;
    @Schema(name= "评价对象")
    @Transient
    private String asssponsor;
    @Schema(name= "归档人")
    @Transient
    private String archiveperson;
    @Schema(name= "归档时间")
    @Transient
    private LocalDateTime archivetime;
    @Schema(name= "初步评价等级")
    @Transient
    private String preliminaryasslevel;
    @Schema(name= "初步评价评分")
    @Transient
    private BigDecimal preliminaryassscore;
    @Schema(name= "校正级别")
    @Transient
    private String adustlevel;
    @Schema(name= "校正原因")
    @Transient
    private String adjustreson;
    @Transient
    private String analysissummary;
    @Transient
    private LocalDateTime assessdate;
    @Schema(name= "评价负责人")
    @Transient
    private BigDecimal leaderid;
    @Transient
    private String tblcomany;
    @Schema(name= "海装新增-缺陷来源项目")
    @Transient
    private String defectsource;
    @Schema(name= "海装新增-缺陷来源文件编号")
    @Transient
    private String sourcefile;

    //TblOrganization
    @Transient
    @Schema(name= "公司名称")
    private String orgname;
    @Transient
    private BigDecimal fatherorgid;
    @Transient
    @Schema(name= "公司编号")
    private String orgnumber;
    @Transient
    @Schema(name= "公司简介")
    private String orgmeno;

    @Transient
    private String icode;///行业架构ID（在哪个行业下创建行业知识库/行业缺陷库/行业问题库/行业数据库/行业指标库/行业规则库/行业模型库，该字段为哪个行业ID）
    @Transient//是否是公司（普通部门为0，一级公司为1，二级公司为2，
    // 三级公司为3，行业架构为100，行业问题库为101，行业缺陷库为102，行业规则库为103，
    // 行业指标库为104，行业模型库为105，行业知识库为106，审计经验库为107，行业数据库为108）
    private Integer orgtype;
    @Transient//是否是主责部门/审计部（1是，0否）
    private Integer auditType;
    @Transient//是否开启望远镜
    private String iszy;
    @Transient//标识行业知识库的所属模块（风险管控为fxmanage，内部控制为nbkz，智能审计为znsj，智能监控为znjk）
    private String hyzsktype;
    @Transient//排序编号，用于在显示组织架构排序
    private Integer orderid;
    @Transient //标识企业来源 为null是本系统，1：蜂信，以后可能为2,3...来表示其它来源   3:华博云注册公司   4:首冠注册用户  5:中财协注册用户  6：大成方略注册用户 7.用友用户
    private Integer outsideid;
    @Transient//外部同步企业来源Id
    private String outsideopendid;
    @Schema(name= "是否使用自动编号 0 不使用；1 使用")
    @Transient
    private Integer isautonumber;
    @Transient
    private Date orgcreate;
    @Transient// 判断该组织有没有初始化 0初始化 ，
    private Integer isinitialization;
    @Schema(name= "职务")
    @Transient
    private String duties;
    @Schema(name= "行业编号")
    @Transient
    private Integer industryid;
    @Schema(name= "新增来源于微信 1为微信 0为pc")
    @Transient
    private String bywx;
    @Transient
    private String datasource;
    @Transient
    private String historycode;
    @Transient
    private String historydepartmentid;
}
