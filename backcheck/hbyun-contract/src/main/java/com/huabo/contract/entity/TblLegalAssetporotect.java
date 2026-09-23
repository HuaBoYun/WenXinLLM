package com.huabo.contract.entity;

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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资产保全
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_LEGAL_ASSETPOROTECT")
@Schema(name="资产保全实体类")
public class TblLegalAssetporotect implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId(value = "ID",type=IdType.INPUT)
    private BigDecimal id;
	
	@Schema(name = "申请人")
	@TableField("APPER")
	private String apper;
	
	@Schema(name = "被申请人")
	@TableField("RESPONDER")
	private String responder;
	
	@Schema(name = "所属纠纷")
	@TableField("DISPUTEID")
	private BigDecimal disputeid;
	
	@Schema(name = "诉讼阶段")
	@TableField("ACTIONSTAGE")
	private String actionstage;
	
	@Schema(name = "是否申请保全")
	@TableField("ISAPPPRESERVA")
	private BigDecimal isapppreserva;
	
	@Schema(name = "保全资产数额（万元）")
	@TableField("PRESERVAAMOUNT")
	private BigDecimal preservaamount;
	
	@Schema(name = "保全资产性质")
	@TableField("PRESERVANATURE")
	private String preservanature;
	
	@Schema(name = "是否执行扣划")
	@TableField("ISEXECUTDEDUCTION")
	private BigDecimal isexecutdeduction;
	
	@Schema(name = "执行金额（万元）")
	@TableField("EXECUTAMOUNT")
	private BigDecimal executamount;
	
	@Schema(name = "是否解除保全")
	@TableField("ISRELEASEPRESERVA")
	private BigDecimal isreleasepreserva;
	
	@Schema(name = "诉讼过程")
	@TableField("LITIGATIONID")
	private BigDecimal litigationid;
	
	@Schema(name = "仲裁过程")
	@TableField("ARBITRAID")
	private BigDecimal arbitraid;
	
	@Schema(name = "创建时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("CREATETIME")
    private Date createtime;
	
	@Schema(name = "创建人")
	@TableField("CREATESTAFFID")
	private BigDecimal createstaffid;
	
	@Schema(name = "纠纷名称")
	private String disputeitem;
	
	
}
