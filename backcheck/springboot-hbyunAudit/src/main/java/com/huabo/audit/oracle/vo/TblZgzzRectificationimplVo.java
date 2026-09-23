package com.huabo.audit.oracle.vo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumns;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
@Schema(name="TblZgzzRectificationimpl对象", description="整改落实落实信息表返回对象")
public class TblZgzzRectificationimplVo implements Serializable {

	private static final long serialVersionUID = 1L;
		
	@Schema(name = "主键")
	private String implId;
	
	  @Schema(name = "落实事项主键")
	private String relaid;
	
	  @Schema(name = "所属部门主键")
	private BigDecimal linkDept;
	
	  @Schema(name = "所属公司主键")
	private BigDecimal linkOrg;
	
	  @Schema(name = "创建人主键")
	private BigDecimal createStaff;
	  
	  @Schema(name = "创建人姓名")
	private String createStaffName;
	
	  @Schema(name = "创建时间")
	  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
	  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	  @Schema(name = "修改人主键")
	private BigDecimal updateStaff;
	
	  @Schema(name = "修改时间")
	  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
	  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	  @Schema(name = "细化的整改措施")
	private String rectificationMeasures;
	
	  @Schema(name = "整改完成标准")
	private String situationoverView;
	
	  @Schema(name = "已采取的整改措施")
	private String achivement;
	
	  @Schema(name = "整改截止日期")
	  @JSONField(format = "yyyy-MM-dd")
	  @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date deadline;
	
	  @Schema(name = "整改结论")
	private String conclusion;
	
	  @Schema(name = "下一步整改措施")
	private String nextMeasures;
	
	  @Schema(name = "计划完成时间")
	  @JSONField(format = "yyyy-MM-dd")
	  @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date finishTime;
	  
	  @Schema(name = "审批状态 0-未落实 1-审批中 2-已退回 3-已撤销 6-已完成")
	private Integer status;
	
	  @Schema(name = "附件列表")
	  @IgnoreSwaggerParameter
	private List<TblAttachment> attList = new ArrayList<TblAttachment>(0);
	  
	  
	  
	//==
	    @Schema(name = "一级单位id")
	    private BigDecimal oneorgid;
	      
	    @Schema(name = "一级单位")
	    private String oneorgname;
	      
	    @Schema(name = "问题来源")
	    private String problemsrc;
	    
	    @Schema(name = "审计报告出具年份")
	    private String reportyear;
	    
	    @Schema(name = "问题类别")
	    private String problemtype;
	    
	    @Schema(name = "一级标题")
	    private String onetitle;
	    
	    @Schema(name = "二级标题")
	    private String twotitle;
	    
	    @Schema(name = "三级标题")
	    private String threetitle;
	    
	    @Schema(name = "在审计报告中的表述")
	    private String reportexpression;
	    
	    @Schema(name = "具体问题表述")
	    private String queexpression;
	    
	    @Schema(name = "问题金额(万元)")
	    private String quemoney;
	    
	    @Schema(name = "负有监督管理责任的主管部门(可以列出多个)")
	    private String supervision;
	    
	    @Schema(name = "整改类型")
	    private String recttype;
	    
	    @Schema(name = "法规政策依据")
	    private String legalbasis;
	    
	    @Schema(name = "整改要求")
	    private String rectdemand;
	    
	    @Schema(name = "整改时限")
	    private String recttimelimit;
	    
	    @Schema(name = "整改第一责任人id")
	    private BigDecimal firstresponstaffid;
	    
	    @Schema(name = "整改第一责任人")
	    private String firstresponstaffname;
	    
	    @Schema(name = "协助整改工作的领导")
	    private String assistleader;
	    
	    @Schema(name = "牵头整改部门责任人及联系电话")
	    private String maindeptheadtel;
	    
	    @Schema(name = "配合整改部门责任人及联系电话")
	    private String assistdeptheadtel;
	    
	    @Schema(name = "审计部门责任人及联系电话")
	    private String auditdeptheadtel;
	    
	    @Schema(name = "项目数(个)")
	    private String pjcnt;
	    
	    @Schema(name = "问题整改金额(万元)")
	    private String rectmoney;
	    
	    @Schema(name = "追缴资金(万元)")
	    private String recoverymoney;
	    
	    @Schema(name = "归还原渠道(万元)")
	    private String backmoney;
	    
	    @Schema(name = "统筹盘活(万元)")
	    private String overallmoney;
	    
	    @Schema(name = "加快拨付(万元)")
	    private String acceleratemoney;
	    
	    @Schema(name = "退抵税费或补缴补发(万元)")
	    private String retrievemoney;
	    
	    @Schema(name = "调整账表(万元)")
	    private String adjustmoney;
	    
	    @Schema(name = "中止或调整金融业务服务(万元)")
	    private String stopmoney;
	    
	    @Schema(name = "补办手续、重签协议、停止收费等加强管理(万元)")
	    private String reissuemoney;
	    
	    @Schema(name = "其他方式")
	    private String otherway;
	    
	    @Schema(name = "其他方式金额(万元)")
	    private String othermoney;
	    
	    @Schema(name = "土地、森林等面积(公顷)")
	    private String landarea;
	    
	    @Schema(name = "矿产资源、产能等(万吨)")
	    private String minerals;
	    
	    @Schema(name = "单位(个)")
	    private String orgcnt;
	    
	    @Schema(name = "家庭(户)")
	    private String familycnt;
	    
	    @Schema(name = "人数(人)")
	    private String personcnt;
	    
	    @Schema(name = "住房(套)")
	    private String housecnt;
	    
	    @Schema(name = "追责问题情况-情形")
	    private String accountabilityinfo;
	    
	    @Schema(name = "追责问题情况-人数")
	    private String accountabilitycnt;
	    
	    @Schema(name = "完善制度情况-数量(个)")
	    private String institutioncnt;
	    
	    @Schema(name = "完善制度情况-分修订、制定，文件名称")
	    private String institutioninfo;
	    
	    
	    @Schema(name = "是否销号:1是  0 否")
	    private String isxh;
      
}
