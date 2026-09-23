package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_ZGLS_PROBLEM")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjZglsProblem {
	
	@TableId(value = "PROBLEMID", type= IdType.INPUT)
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Schema
    private BigDecimal problemid;
	
	@TableField(value = "PROBLEMTYPE")
    @Schema(name = "审计发现问题类型：金额类：调整会计账目、收回资金、挽回损失、归还原资金渠道、补缴税额、其他；非金额类：新制定制度、修订完善制度、优化完善业务流程、其他")
    private String problemtype;
	
	@TableField(value = "ZGNUM")
    @Schema(name = "数量")
    private Integer zgnum;
	
	@TableField(value = "CREATETIEM")
    @Schema(name = "创建时间")
    private Date createtiem;
	
	@TableField(value = "ZGMONTY")
    @Schema(name = "金额")
	private BigDecimal zgmonty;//
	
	@TableField(value = "SYSTEMNAME")
    @Schema(name = "制度名称")
	private String systemname;
	
	@TableField(value = "CREATESTAFF")
    @Schema(name = "创建人")
    private BigDecimal createstaff;
	
	@TableField(value = "CREATEORG")
    @Schema(name = "创建公司")
    private BigDecimal createorg;
	
	@TableField(value = "REFORMID")
    @Schema(name = "整改信息id")
    private BigDecimal reformid;
	
	@TableField(value = "MANUSCRIPT")
    @Schema(name = "制度信息")
    private String manuscript;
	
	@TableField(value = "DATATYPE")
    @Schema(name = "数据类型，1-金额类；2-非金额类；")
    private String datatype;
	
	@TableField(value = "RECTIFICATION")
    @Schema(name = "关联底稿id")
	private BigDecimal rectification; //底稿信息
	
	
	
	
}
