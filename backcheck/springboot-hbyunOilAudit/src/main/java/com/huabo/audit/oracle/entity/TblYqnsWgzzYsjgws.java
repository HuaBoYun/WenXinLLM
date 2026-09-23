package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.huabo.audit.oracle.entity.base.ReservedEntity;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_WGZZ_YSJGWS")
@Data
@Schema(name="移送结果文书")
@Accessors(chain = true)
public class TblYqnsWgzzYsjgws  extends ReservedEntity {

    @TableField(value = "ID")
    @Schema(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    private BigDecimal id;

	@TableField("YJTIME")
    @Schema(name = "移交时间")
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date yjtime;

    @TableField(value = "WSNUMBER")
    @Schema(name = "编号")
    private String wsnumber;

    @TableField(value = "WSTITLE")
    @Schema(name = "标题")
    private String wstitle;

    @TableField("HANDSTAFFID")
    @Schema(name = "经办人")
    private BigDecimal handstaffid;

    //经办人姓名
    @Transient
    private String handstaffname;

    @TableField("STATUS")
    @Schema(name = "状态")
    private Integer status;

    @TableField("CREATOR")
    @Schema(name = "创建人")
    private BigDecimal creator;

    @Transient
    private String attIds;

    @TableField(value = "FILUE")
    @Schema(name = "附件")
    private String filue;

	@TableField("CONTENT")
	@Schema(name = "富文本")
	private String content;

	@TableField("PRORESULT")
	@Schema(name = "处理结果")
	private String proresult;

	@TableField(value = "EDITORGID")
	@Schema(name = "填报单位")
	private BigDecimal editorgid;

	//填报单位名称
	@Transient
    private String editorgname;

}
