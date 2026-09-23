package com.huabo.system.entity;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统业务单据下发通知表
 * </p>
 *
 * @author LHP
 * @since 2023-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SYSTEM_URGENTEVENTS")
@Schema(name="TBL_SYSTEM_URGENTEVENTS", description="催办事件信息表")
public class TblSystemUrgentEvents implements Serializable {

	private static final long serialVersionUID = 2479382855038642580L;

	//1313
	
	@Schema(name="催办主键")
    @TableId("EVENTID")
	private String eventId;

    @TableField("CONFIGID")
    @Schema(name="系统编号表主键")
    private String configId;

    @Schema(name="关联表单主键")
    @TableField("REFORMID")
    private String reformId;

    @Schema(name="催办时间")
    @TableField("CREATETIME")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name="确认时间")
    @TableField("CONFIRMTIME")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date confirmTime;

    @Schema(name="催办人")
    @TableField("CREATESTAFF")
    private BigDecimal createStaff;

    @Schema(name="接收人")
    @TableField("RECIPIENT")
    private BigDecimal recipient;

    @Schema(name="催办内容")
    @TableField("EVENTMEMO")
    private String eventMemo;

    @Schema(name="是否确认  1 - 是，0-否")
    @TableField("ISCONFIRM")
    private Integer isConfirm;

    @Schema(name="接收人姓名")
    @TableField("RECIPIENTNAME")
    private String recipientName;

    @Schema(name="催办人姓名")
    @TableField("CREATESTAFFNAME")
    private String createStaffName;
    
    @Schema(name="模块名称")
    @TableField("MODULENAME")
    private String moduleName;
      
    @Schema(name="模块类型-筛选条件")
    @TableField("MODULETYPE")
    private String moduleType;
}
