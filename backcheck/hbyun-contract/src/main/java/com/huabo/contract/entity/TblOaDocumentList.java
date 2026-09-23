package com.huabo.contract.entity;


import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 合同关联OA公文/协同列表
 */
@Data
@TableName("TBL_OA_DOCUMENTLIST")
@Schema(name="TblOaDocumentList对象")
public class TblOaDocumentList {
	
	@TableId(value = "DOCUMENTID",type=IdType.INPUT)
	@Schema(name = "主键")
	private BigDecimal documentId;

	@TableField("CONTRACTID")
	@Schema(name = "合同id")
	private String contractId;

	@TableField("ID")
	@Schema(name = "oa唯一值")
	private String id;
	
	@TableField("SUBJECT")
	@Schema(name = "标题")
	private String subject;

	@TableField("STATENAME")
	@Schema(name = "状态")
	private String stateName;
	
	@TableField("URL")
	@Schema(name = "pc地址")
	private String url;
	
	@TableField("H5URL")
	@Schema(name = "h5地址")
	private String h5url;
	
	@TableField("EDOCMARK")
	@Schema(name = "公文文号（协同忽略）")
	private String edocmark;
	
	@TableField("ISFOLDER")
	@Schema
	private String isFolder;
	
	@TableField("SENDNAME")
	@Schema(name = "当前人员姓名")
	private String sendName;
	
	@TableField("FRTYPENAME")
	@Schema
	private String frtypeName;
	
	@TableField("CATEGORYNAME")
	@Schema(name = "类别名称")
	private String cateGoryName;
	
}
