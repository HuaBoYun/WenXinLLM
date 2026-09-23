package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CONTRACT_PLANNODE")
@Schema(name="TblContractPlannode对象")
public class TblContractPlannode implements Serializable {
//对应收款项
	// 付款计划
	private static final long serialVersionUID = 1L;

	@TableId(value = "NODEID" , type = IdType.INPUT)
	private BigDecimal nodeid;

	@TableField("NODECONTENT") // 对应收款项
	private String nodecontent;// 履行内容

	@TableField("PROJECTID")
	private BigDecimal projectid;

	@TableField("BLPROJECTID")
	private BigDecimal blprojectid;

	@TableField("PLANSTARTDATE")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date planstartdate;

	@TableField("PLANENDDATE")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date planenddate;// 预计结束时间

	@TableField("NODEPOST")
	private String nodepost;

	@TableField("NODEPLANPAYDATE")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date nodeplanpaydate;// 预计开始时间

	@TableField("DISPATCHSTAFF")
	private BigDecimal dispatchstaff;

	@TableField("DISPATCHDEPT") // 履行情况
	private BigDecimal dispatchdept;

	@TableField("PLANNODESTATUS")//落实状态 0未落实  ， 1-已落实未完成  ，2-已完成
	private BigDecimal plannodestatus;

	@TableField("CONTRACTID")
	private BigDecimal contractid;
	@Schema(name = "反馈意见")
	@TableField("FEEDBACK")
	private String feedback;
	@Schema(name = "金额")
	@TableField("NODEMONEY")
	private BigDecimal nodemoney;// 未付款金额

	@Schema(name = "履行类别（下拉选择：收付款、货物-2、工期、服务期、交付成果、其他")
	@TableField("PERFORMANCECATEGORY")
	private String performanceCategory;
	@Schema(name = "货物名称")
	@TableField("GOODSNAME")
	private String goodsName;
	@Schema(name = "货物数量")
	@TableField("GOODSCOUNT")
	private int goodsCount;

	@TableField("BUDGETIDS") // 相对方id(,分割)
	private String budgetIds;
	private String budgetName;// 相对方名称
	private String budgettype;// 相对方类型
	private BigDecimal yfMoney;// 金额
	private String realname;
	private String orgname;
	private BigDecimal jbstaffid;// 执行人
	private BigDecimal jbunitid;// 执行单位
	private BigDecimal tcpspnodeid;
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tcsstartdate;
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tcsenddate;
	private String tcsnodememo;
	private String tcsnodecontent;
	private String tcsnodepost;
	private String tcsnodemoney;
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tcsnodefinishdate;
	private String tcudctype;
	private String iswy;
	private List<TblCyhwProjectbudget> TblContractBudgetList;//相对方
	@TableField(exist = false)
	private String nodememo;
}
