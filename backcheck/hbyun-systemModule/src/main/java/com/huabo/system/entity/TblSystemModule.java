package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SYSTEM_MODULE")
@Schema(name="功能模块创建")
public class TblSystemModule implements Serializable{

	private static final long serialVersionUID = -1107456731364079572L;

	@TableId(value="MODELID",type = IdType.INPUT)
	@Schema(name="主键")
	private BigDecimal modelId;
	@TableField("MODELNO")
	@Schema(name="模块编号")
	private String modelNo;
	@TableField("MODELNAME")
	@Schema(name="模块名称")
	private String modelName;
	@TableField("MODELURL")
	@Schema(name="模块路径")
	private String modelUrl;
	@TableField("MODELSTATUS")
	@Schema(name="模块状态")
	private Integer modelStatus;//0未启动   1.已启动  2.已弃用
	@TableField("MODELTYPE")
	@Schema(name="模块所属类型  合同，风险，内控，审计等等")
	private String modelType;
	@TableField("CREATETIME")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Schema(name="创建时间")
	private Date createTime;
	@TableField("MODIFYTIME")
	@Schema(name="修改时间")	
	private Date modifyTime;
	@TableField("MODELORG")
	@Schema(name="所属公司")
	private BigDecimal modelOrg;
	@TableField("CREATEPERSON")
	@Schema(name="创建人")
	private BigDecimal createPerson;
	@TableField("MODIFYPERSON")
	@Schema(name="修改人")
	private BigDecimal modifyPerson;
	@TableField("MODELORDER")
	@Schema(name="排序")
	private Integer modelorder;

	@Transient
	private String createPersonName;
	@Transient
	private TblStaff staff;
	@Transient
	private TblOrganization organization;

	@Transient
	private List<TblSystemModelFlow> tblSystemModelFlowList = new ArrayList<TblSystemModelFlow>(0);
	@Transient
	private Set<TblOrganization> tblOrgSet = new HashSet<TblOrganization>(0);
	@Transient
	private List<TblFlow> flowList = new ArrayList<TblFlow>(0);

}
