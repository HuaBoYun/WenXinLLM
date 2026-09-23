package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hbfk.entity.TblAttachment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 整改方案
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_REFORM_SOLUTION")
public class TblReformSolution implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private BigDecimal solutionid;//主键自增id
	private String solutioncode;//方案编号
	private String solutionname;//方案名称
	private String solutionstatus;//状态
	private TblStaff tblStaff;//创建人
	private Date createdate;//创建日期
	private String memo;//备注
	private TblOrganization organization;//被整改公司
	private Integer runstatus;//1、开始整改  2、整改中  3、关闭   4、整改完成
	private TblOrganization reformCompany;//整改公司
	private TblStaff reformUser;//整改负责人
	private String  reformtype;//类型，例如nk\fx\zn\
	private Date enddate;//方案终止时间
	private Set<TblReform> tblReforms=new HashSet<TblReform>();
	private Set<TblAttachment> tblAttachments = new HashSet<TblAttachment>();//附件
}