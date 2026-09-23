package com.huabo.audit.oracle.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.entity
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/26
 * @Time:10:23
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name="TBL_WGZZ_WGZZ")
@Schema
public class TblWgzzEntity {
	
	
    @TableId(value = "CLUEID", type= IdType.INPUT)
    @Schema
    @Id
    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
    private BigDecimal clueid;

   @TableField("CLUENABER")
   @Schema(name = "线索编号")
    private String cluenaber;

    @TableField("CLUEUNIT")
    @Schema(name = "涉及单位")
    private BigDecimal clueunit;

    @TableField("CLUEUNITNAME")
    @Schema(name = "涉及单位名称")
    private String clueunitname;

	@TableField("CLUEUNITNEWID")
	@Schema(name = "涉及单位,多个逗号形式隔开")
	private String clueunitnewid;

	@TableField("CLUEUNITNENAME")
	@Schema(name = "涉及单位名称,多个逗号形式隔开")
	private String clueunitnename;

    @TableField("CLUEHANDLING")
    @Schema(name = "涉及责任人ID,多个逗号形式隔开")
    private String  cluehandling;

    @TableField("CLUEHANDLINGNAME")
    @Schema(name = "涉及责任人名称,多个逗号形式隔开")
    private String  cluehandlingname;

    @TableField("MESSAGETIME")
    @Schema(name = "报送时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date messagetime;

    @TableField("MESSAGEMANNER")
    @Schema(name = "报送方式")
    private String messagemanner;

    @TableField("CLUESOURCE")
    @Schema(name = "线索来源")
    private String cluesource;

    @TableField("OCCURRENCETIME")
    @Schema(name = "发生时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date occurrencetime;

    @TableField("MAINCLUE")
    @Schema(name = "主要问题线索")
    private String mainclue;

    @TableField("STATUS")
    @Schema(name = "状态")
    private Integer status;//0未結束，1結束

    @TableField("CLUETYPE")
    @Schema(name = "线索类别")
    private Integer cluetype;

   @TableField("CREATOR")
   @Schema(name = "创建人")
   private BigDecimal creator;

   @TableField("IMPCREATEUSERNAME")
   @Schema(name = "创建时间")
   @JSONField(format = "yyyy-MM-dd HH:mm:ss")
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
   @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
   private Date impcreateusername;

    @TableField("CLUEBGZT")
    @Schema(name = "报告主体")
    private String cluebgzt;

    @TableField("CLUEJC")
    @Schema(name = "级次")
    private String cluejc;

    @TableField("CONSEQUENCES")
    @Schema(name = "资产损失程度或不良后果")
    private String consequences;

    @TableField("CLUEGS")
    @Schema(name = "概述")
    private String cluegs;

    @TableField("CLUECLQK")
    @Schema(name = "处理情况")
    private String  clueclqk;


    @TableField("CLUESFWJ")
    @Schema(name = "是否完结")
    private Integer cluesfwj;

	@TableField("ISACCEPTED")
	@Schema(name = "是否受理 默认0 0-未受理 1-已受理")
	private Integer isaccepted;

	@Transient
	@Schema(name = "问题线索概述（来源与初步核实里面的核实内容）")
	private String verifycontentnew;
}
