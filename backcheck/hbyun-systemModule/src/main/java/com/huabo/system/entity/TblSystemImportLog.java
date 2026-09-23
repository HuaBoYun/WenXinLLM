package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="合同类型流程设计关联表单", description="")
@TableName(value = "TBL_SYSTEM_IMPORTLOG")
public class TblSystemImportLog implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final Integer IMPORTORG = 1;//导入组织信息
	public static final Integer IMPORTSTAFF = 2; //导入用户信息
	
	@TableId(value="LOGID",type = IdType.INPUT)
	@Schema(name="主键ID")
	private String logId;
	
	@TableField("CREATESTAFF")
	@Schema(name="导入人主键 Tbl_Staff(staffId)")
	private BigDecimal createStaff;
	
	@TableField("CREATETIME")
	@Schema(name="创建时间")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createTime;
	
	@TableField("IMPORTTYPE")
	@Schema(name="导入类型")
	private Integer importType;
	
	@TableField("IMPORTCONTENT")
	@Schema(name="日志内容")
	private String importContent;
	
	@TableField("IMPORTCOUNT")
	@Schema(name="已导入数量")
	private Integer importCount;
	
	@TableField("TOTALCOUNT")
	@Schema(name="导入总数量")
	private Integer totalCount;
	
	@TableField(exist = false)
	@Schema(name="导入人姓名")
	private String staffName;
}
