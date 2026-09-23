package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-06-27
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_NBSJ_REFOPM")
@Schema(name="TblNbsjRefopm对象")
public class TblNbsjRefopm implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Integer WFP=0;
	public static final Integer ZGZ=1;
	public static final Integer YWC=2;

    @TableId("REFORMID")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Schema(name = "整改任务id")
  	@Column(name = "reformid")
    private BigDecimal reformid;

    @TableField("REFORMMEASURE")
    @Schema(name = "整改措施")
    private String reformmeasure;

    @TableField("REFORMRESULT")
    @Schema(name = "整改结论 下拉选择：未整改、持续整改、已完成整改")
    private String reformresult;

    @TableField("REFORMCARRYOUT")
    @Schema(name = "整改落实情况")
    private String reformcarryout;

    @TableField("HANDLING")
    @Schema(name = "责任人处理情况")
    private String handling;

    @TableField("REFORMDEADLINE")
    @Schema(name = "整改截止日期")
    private Date reformdeadline;

    @TableField("PERONINCHARGE")
    @Schema(name="整改执行人ID",hidden=true)
    private BigDecimal peronincharge;

    @TableField("REFORMTIME")
    @Schema(name="整改信息填写时间",hidden=true)
    private Date reformtime;

    @TableField("QUESTIONID")
    @Schema(name="整改内容关联审计发现ID",hidden=true)
    private BigDecimal questionid;

    @TableField("STATUS")
    @Schema(name = "整改状态  1.开始 2.整改中 3.整改完成")
    private Integer status;

    @TableField("CREATEID")
    @Schema(name="创建任务人员ID",hidden=true)
    private BigDecimal createid;

    @TableField("CREATEDATE")
    @Schema(name="创建任务时间",hidden=true)
    private Date createdate;

    @TableField("LASTREFORMSTATUS")
    @Schema(name = "是否是最后一个整改人 1最新整改人  2 整改人已整改完成  0指派中间人")
    private Integer lastreformstatus;

    @TableField("SOLUTIONID")
    @Schema(name="关联整改方案ID",hidden=true)
    private BigDecimal solutionid;

    @TableField("BUGID")
    @Schema(name="整改内容关联缺陷ID",hidden=true)
    private BigDecimal bugid;

    @TableField("PROJECTID")
    @Schema(name="整改内容关联项目ID",hidden=true)
    private BigDecimal projectid;

    @TableField("NEXTMEASURES")
    @Schema(name = "下一步整改措施")
    private String nextmeasures;

    @TableField("NEXTMPLANCOMDATE")
    @Schema(name = "计划完成整改时间")
    private Date nextmplancomdate;

    @TableField("INSPECT")
    @Schema(name = "检查过程")
    private String inspect;

    @TableField("ZGJGRESULT")
    @Schema(name = "评价结果   已整改到位、未整改、已整改未到位、关闭")
    private String zgjgresult;

    @TableField("ZGDWDATE")
    @Schema(name = "已整改完成时间")
    private Date zgdwdate;

    @TableField("ZGSTATUS")
	@Schema(name = "整改评价状态 1评价中  2退回 3评价完成")
    private Integer zgstatus;

    @TableField("CLOSEYY")
    @Schema(name = "关闭原因")
    private String closeyy;

    @TableField("WZGREASON")
    @Schema(name = "未整改原因")
    private String wzgreason;

    @TableField("WXHSTATUS")
    @Schema(name = "未销号问题跟踪状态 1、已下发  0未下发")
    private Integer wxhstatus;

    @TableField("ZGWDREASON")
    @Schema(name = "持续整改原因")
    private String zgwdreason;

    @TableField("THREASON")
    @Schema(name = "整改评价退回意见")
    private String threason;
    
    
    
    @Schema(name = "问题id")
	private String problemid;

	@Schema(name = "问题编号")
	private String code;

	@Schema(name = "问题详情")
	private String details;
	
	@Schema(name = "问题来源")
    private String source;
	
	@Schema(name = "发现人")
    private String discoverer;
	
	@Schema(name = "被审计单位")
    private String company;

	
	@Schema(name = "整改执行人")
	private String zgzxxrname;//整改人
	
	@Schema(name = "所属项目名称")
    private String projectname;
	
	@Schema(name = "关联整改方案实体")
	private TblNbsjReformSolution tblNbsjReformSolution;
	
	
	
	
}
