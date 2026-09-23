package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hbfk.entity.TblAttachment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TblReform entity. @author MyEclipse Persistence Tools
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_REFORM")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblReform implements java.io.Serializable {

	// Fields

	/**
	 * 老项目实体
	 */
	private static final long serialVersionUID = 1L;
	public static final Integer WFP=0;
	public static final Integer ZGZ=1;
	public static final Integer YWC=2;

	@TableId(type= IdType.INPUT)
	private BigDecimal reformid;
	private TblProblem tblProblem;
	private TblBug tblBug;
	private String reformmeasure;//整改措施
	private String reformresult;//整改结论
	private String reformcarryout;//整改落实情况 
	private String handling;//责任人处理情况
	private Date reformdeadline;//整改截止日期
	private String memo;
	private String peronincharge;//整改人
	private Date reformtime;
	private TblStaff tblCeaters;//创建人
	private TblWorksheet tblWorksheet;
	private Integer reformstatus;//整改状态  1.开始 2.整改中 3.整改完成  0废弃
	private Date createDate;//创建时间
	private Integer reformuserid;//整改人id
	private Integer lastreformstatus;//是否是最后一个整改人 1最新整改人  2 整改人已整改完成  0指派中间人
	
    @TableField(exist = false)
	private TblReformSolution tblReformSolution;
	
	//整改落实附件
    @TableField(exist = false)
	private Set<TblAttachment> tblReformAtts = new HashSet<TblAttachment>();
	// Constructors
}