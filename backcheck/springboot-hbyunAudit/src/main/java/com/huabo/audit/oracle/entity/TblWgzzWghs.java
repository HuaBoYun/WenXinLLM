package com.huabo.audit.oracle.entity;

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
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.entity
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:10:30
 */
@TableName("TBL_WGZZ_WGHS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name="违规核实实体类")
@Accessors(chain = true)
@Table(name = "TBL_WGZZ_WGHS")
public class TblWgzzWghs {

    public final static Integer STATUS_1 = 1; //未审批
    public final static Integer STATUS_2 = 2;//审批中
    public final static Integer STATUS_3 = 3;//需调整
    public final static Integer STATUS_4 = 4;//已终止
    public final static Integer STATUS_5 = 5;//已通过

    @TableField(value = "ID")
    @Schema
    @Id
    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
    private BigDecimal id;

	@TableField(value = "CLUEID")
	@Schema(name = "违规追责受理主键ID")
	private BigDecimal clueid;

    @TableField(value = "CLUENABER")
    @Schema(name = "线索编号")
    private String cluenaber;

    @TableField(value = "VERIFYCONTENT")
    @Schema(name = "核实内容")
    private String verifycontent;

    @TableField("STATUS")
    @Schema(name = "状态")
    private Integer status;

    @TableField("CREATOR")
    @Schema(name = "创建人")
    private Integer creator;

    @TableField("IMPCREATEUSERNAME")
    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date impcreateusername;

    @TableField(value = "REPDESC")
//    @Schema(name = "富文本框")
    @Schema(name = "责任追究处理情况")
    private String repdesc;

    @TableField(value = "CLUENAME")
//    @Schema(name = "线索名称")
    @Schema(name = "核查工作开展过程情况")
    private String cluename;

    @TableField(value = "CLUEHSFW")
    @Schema(name = "核实范围")
    private String cluehsfw;

    @TableField(value = "CLUEGZZZ")
    @Schema(name = "工作组织")
    private String cluegzzz;

    @TableField("APTIME")
    @Schema(name = "时间安排")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date aptime;

    @TableField(value = "CLUEBMRY")
    @Schema(name = "涉及部门及人员")
    private String cluebmry;

    @TableField(value = "CLUEWGQX")
//    @Schema(name = "违规情形初步核实情况")
    @Schema(name = "发现的主要问题")
    private String cluewgqx;

    @TableField(value = "CLUESSQK")
//    @Schema(name = "资产损失和损失风险初步核实情况")
    @Schema(name = "责任定性情况及依据")
    private String cluessqk;

    @TableField(value = "CLUEWJSM")
//    @Schema(name = "涉嫌违纪问题线索说明")
    @Schema(name = "资产损失认定情况")
    private String cluewjsm;

    @Transient
    private String attIds;

	@TableField("WGHSSCOPE")
//	@Schema(name = "核实范围")
	@Schema(name = "是否需要移送纪检部门")
	private String wghsscope;

	@TableField("WORKORGANIZATION")
	@Schema(name = "工作组织")
	private String workorganization;

	@TableField("TIMEARRANGE")
	@Schema(name = "时间安排")
	private String timearrange;

	@TableField("HANDLERID")
	@Schema(name = "经办人ID")
	private BigDecimal handlerid;

	@TableField("CONTENT")
	@Schema(name = "富文本")
	private String content;

	@Transient
	@Schema(name = "经办人名称")
	private String handlername;
}
