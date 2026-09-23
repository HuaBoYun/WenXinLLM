package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 整改落实落实信息表
 * </p>
 *
 * @author LHP
 * @since 2023-11-25
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_ZGZZ_RECTIFICATIONIMPL")
@Table(name = "TBL_ZGZZ_RECTIFICATIONIMPL")
@Schema(name="TblZgzzRectificationimpl对象", description="整改落实落实信息表")
public class TblZgzzRectificationimpl implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
      @TableId("IMPLID")
      @Id
      @Column(name = "IMPLID")
      private BigDecimal implId;

      @Schema(name = "落实事项主键")
      @TableField("RELAID")
      @Column(name = "RELAID")
    private String relaid;

      @Schema(name = "所属部门主键")
      @TableField("LINKDEPT")
      @Column(name = "LINKDEPT")
    private BigDecimal linkDept;

      @Schema(name = "所属公司主键")
      @TableField("LINKORG")
      @Column(name = "LINKORG")
    private BigDecimal linkOrg;

      @Schema(name = "创建人主键")
      @TableField("CREATESTAFF")
      @Column(name = "CREATESTAFF")
    private BigDecimal createStaff;

      @Schema(name = "创建时间")
      @TableField("CREATETIME")
      @Column(name = "CREATETIME")
      @JSONField(format = "yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

      @Schema(name = "修改人主键")
      @TableField("UPDATESTAFF")
      @Column(name = "UPDATESTAFF")
    private BigDecimal updateStaff;

      @Schema(name = "修改时间")
      @TableField("UPDATETIME")
      @Column(name = "UPDATETIME")
      @JSONField(format = "yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

      @Schema(name = "细化的整改措施")
      @TableField("RECTIFICATIONMEASURES")
      @Column(name = "RECTIFICATIONMEASURES")
    private String rectificationMeasures;

      @Schema(name = "整改完成标准")
      @TableField("SITUATIONOVERVIEW")
      @Column(name = "SITUATIONOVERVIEW")
    private String situationoverView;

      @Schema(name = "已采取的整改措施")
      @TableField("ACHIVEMENT")
      @Column(name = "ACHIVEMENT")
    private String achivement;

      @Schema(name = "对应的完成时间")
      @TableField("DEADLINE")
      @Column(name = "DEADLINE")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date deadline;

      @Schema(name = "是否完成整改")
      @TableField("CONCLUSION")
      @Column(name = "CONCLUSION")
    private String conclusion;

      @Schema(name = "未整改到位问题原因及下一步计划")
      @TableField("NEXTMEASURES")
      @Column(name = "NEXTMEASURES")
    private String nextMeasures;

      @Schema(name = "计划完成时间")
      @TableField("FINISHTIME")
      @Column(name = "FINISHTIME")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date finishTime;
      
      @Schema(name = "审批状态 0-未落实 1-审批中 2-已退回 3-已撤销 6-已完成")
      @TableField("STATUS")
      @Column(name = "STATUS")
    private Integer status;
      
      //==
    @Schema(name = "一级单位id")
    @TableField("ONEORGID")
    @Column(name = "ONEORGID")
    private BigDecimal oneorgid;
      
    @Schema(name = "一级单位")
    @TableField("ONEORGNAME")
    @Column(name = "ONEORGNAME")
    private String oneorgname;
      
    @Schema(name = "问题来源")
    @TableField("PROBLEMSRC")
    @Column(name = "PROBLEMSRC")
    private String problemsrc;
    
    @Schema(name = "审计报告出具年份")
    @TableField("REPORTYEAR")
    @Column(name = "REPORTYEAR")
    private String reportyear;
    
    @Schema(name = "问题类别")
    @TableField("PROBLEMTYPE")
    @Column(name = "PROBLEMTYPE")
    private String problemtype;
    
    @Schema(name = "一级标题")
    @TableField("ONETITLE")
    @Column(name = "ONETITLE")
    private String onetitle;
    
    @Schema(name = "二级标题")
    @TableField("TWOTITLE")
    @Column(name = "TWOTITLE")
    private String twotitle;
    
    @Schema(name = "三级标题")
    @TableField("THREETITLE")
    @Column(name = "THREETITLE")
    private String threetitle;
    
    @Schema(name = "在审计报告中的表述")
    @TableField("REPORTEXPRESSION")
    @Column(name = "REPORTEXPRESSION")
    private String reportexpression;
    
    @Schema(name = "具体问题表述")
    @TableField("QUEEXPRESSION")
    @Column(name = "QUEEXPRESSION")
    private String queexpression;
    
    @Schema(name = "问题金额(万元)")
    @TableField("QUEMONEY")
    @Column(name = "QUEMONEY")
    private String quemoney;
    
    @Schema(name = "负有监督管理责任的主管部门(可以列出多个)")
    @TableField("SUPERVISION")
    @Column(name = "SUPERVISION")
    private String supervision;
    
    @Schema(name = "整改类型")
    @TableField("RECTTYPE")
    @Column(name = "RECTTYPE")
    private String recttype;
    
    @Schema(name = "法规政策依据")
    @TableField("LEGALBASIS")
    @Column(name = "LEGALBASIS")
    private String legalbasis;
    
    @Schema(name = "整改要求")
    @TableField("RECTDEMAND")
    @Column(name = "RECTDEMAND")
    private String rectdemand;
    
    @Schema(name = "整改时限")
    @TableField("RECTTIMELIMIT")
    @Column(name = "RECTTIMELIMIT")
    private String recttimelimit;
    
    @Schema(name = "整改第一责任人id")
    @TableField("FIRSTRESPONSTAFFID")
    @Column(name = "FIRSTRESPONSTAFFID")
    private BigDecimal firstresponstaffid;
    
    @Schema(name = "整改第一责任人")
    @TableField("FIRSTRESPONSTAFFNAME")
    @Column(name = "FIRSTRESPONSTAFFNAME")
    private String firstresponstaffname;
    
    @Schema(name = "协助整改工作的领导")
    @TableField("ASSISTLEADER")
    @Column(name = "ASSISTLEADER")
    private String assistleader;
    
    @Schema(name = "牵头整改部门责任人及联系电话")
    @TableField("MAINDEPTHEADTEL")
    @Column(name = "MAINDEPTHEADTEL")
    private String maindeptheadtel;
    
    @Schema(name = "配合整改部门责任人及联系电话")
    @TableField("ASSISTDEPTHEADTEL")
    @Column(name = "ASSISTDEPTHEADTEL")
    private String assistdeptheadtel;
    
    @Schema(name = "审计部门责任人及联系电话")
    @TableField("AUDITDEPTHEADTEL")
    @Column(name = "AUDITDEPTHEADTEL")
    private String auditdeptheadtel;
    
    @Schema(name = "项目数(个)")
    @TableField("PJCNT")
    @Column(name = "PJCNT")
    private String pjcnt;
    
    @Schema(name = "问题整改金额(万元)")
    @TableField("RECTMONEY")
    @Column(name = "RECTMONEY")
    private String rectmoney;
    
    @Schema(name = "追缴资金(万元)")
    @TableField("RECOVERYMONEY")
    @Column(name = "RECOVERYMONEY")
    private String recoverymoney;
    
    @Schema(name = "归还原渠道(万元)")
    @TableField("BACKMONEY")
    @Column(name = "BACKMONEY")
    private String backmoney;
    
    @Schema(name = "统筹盘活(万元)")
    @TableField("OVERALLMONEY")
    @Column(name = "OVERALLMONEY")
    private String overallmoney;
    
    @Schema(name = "加快拨付(万元)")
    @TableField("ACCELERATEMONEY")
    @Column(name = "ACCELERATEMONEY")
    private String acceleratemoney;
    
    @Schema(name = "退抵税费或补缴补发(万元)")
    @TableField("RETRIEVEMONEY")
    @Column(name = "RETRIEVEMONEY")
    private String retrievemoney;
    
    @Schema(name = "调整账表(万元)")
    @TableField("ADJUSTMONEY")
    @Column(name = "ADJUSTMONEY")
    private String adjustmoney;
    
    @Schema(name = "中止或调整金融业务服务(万元)")
    @TableField("STOPMONEY")
    @Column(name = "STOPMONEY")
    private String stopmoney;
    
    @Schema(name = "补办手续、重签协议、停止收费等加强管理(万元)")
    @TableField("REISSUEMONEY")
    @Column(name = "REISSUEMONEY")
    private String reissuemoney;
    
    @Schema(name = "其他方式")
    @TableField("OTHERWAY")
    @Column(name = "OTHERWAY")
    private String otherway;
    
    @Schema(name = "其他方式金额(万元)")
    @TableField("OTHERMONEY")
    @Column(name = "OTHERMONEY")
    private String othermoney;
    
    @Schema(name = "土地、森林等面积(公顷)")
    @TableField("LANDAREA")
    @Column(name = "LANDAREA")
    private String landarea;
    
    @Schema(name = "矿产资源、产能等(万吨)")
    @TableField("MINERALS")
    @Column(name = "MINERALS")
    private String minerals;
    
    @Schema(name = "单位(个)")
    @TableField("ORGCNT")
    @Column(name = "ORGCNT")
    private String orgcnt;
    
    @Schema(name = "家庭(户)")
    @TableField("FAMILYCNT")
    @Column(name = "FAMILYCNT")
    private String familycnt;
    
    @Schema(name = "人数(人)")
    @TableField("PERSONCNT")
    @Column(name = "PERSONCNT")
    private String personcnt;
    
    @Schema(name = "住房(套)")
    @TableField("HOUSECNT")
    @Column(name = "HOUSECNT")
    private String housecnt;
    
    @Schema(name = "追责问题情况-情形")
    @TableField("ACCOUNTABILITYINFO")
    @Column(name = "ACCOUNTABILITYINFO")
    private String accountabilityinfo;
    
    @Schema(name = "追责问题情况-人数")
    @TableField("ACCOUNTABILITYCNT")
    @Column(name = "ACCOUNTABILITYCNT")
    private String accountabilitycnt;
    
    @Schema(name = "完善制度情况-数量(个)")
    @TableField("INSTITUTIONCNT")
    @Column(name = "INSTITUTIONCNT")
    private String institutioncnt;
    
    @Schema(name = "完善制度情况-分修订、制定，文件名称")
    @TableField("INSTITUTIONINFO")
    @Column(name = "INSTITUTIONINFO")
    private String institutioninfo;
    
    
    @Schema(name = "是否销号")
    @TableField("ISXH")
    @Column(name = "ISXH")
    private String isxh;
    
    @Schema(name = "再次整改标记")
    @TableField("ZCSTATUS")
    @Column(name = "ZCSTATUS")
    private Integer zcstatus;

    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;

    @Schema(name = "知悉范围id")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;

    @Schema(name = "知悉范围名称")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;
}
