package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

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
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.entity
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:17:47
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_WGZZ_SHBG")
@Data
@Schema(name="违规报告实体")
@Accessors(chain = true)
public class TblWgzzShbg {

    @TableField(value = "ID")
    @Schema(name = "线索id")
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

    @TableField(value = "CLUEHSFW")
    @Schema(name = "核实范围")
    private String cluehsfw;

    @TableField(value = "FILUE")
    @Schema(name = "附件")
    private String filue;

    @TableField(value = "CLUEGZZZ")
    @Schema(name = "工作组织")
    private String cluegzzz;

    @TableField(value = "CLUEZRDX")
//    @Schema(name = "责任定性")
    @Schema(name = "否发现需要追究责任事项")
    private String cluezrdx;

    @TableField(value = "CLUESSRD")
//    @Schema(name = "资产损失认定")
    @Schema(name = "分类处置")
    private String cluessrd;

    @TableField(value = "CLUECLJY")
    @Schema(name = "责任追究处理建议")
    private String cluecljy;

    @TableField(value = "VERIFYCONKZGCQK")
    @Schema(name = "初步核实开展过程情况")
    private String verifyconkzgcqk;

    @TableField(value = "VERIFYCONHSJG")
    @Schema(name = "初步核实结果")
    private String verifyconhsjg;

    @TableField(value = "CLUEGZJY")
    @Schema(name = "工作建议")
    private String cluegzjy;

    @TableField("STATUS")
    @Schema(name = "状态")
    private Integer status;

    @TableField("CREATOR")
    @Schema(name = "创建人")
    private BigDecimal creator;

    @TableField("IMPCREATEUSERNAME")
    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date impcreateusername;

    @Transient
    private String attIds;

	@TableField("CONTENT")
	@Schema(name = "富文本")
	private String content;
}
