package com.huabo.audit.oracle.entity;

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

import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.entity
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:17:58
 */
@TableName("TBL_WGZZ_WGHC_BG")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name="违规核查報告实体")
@Accessors(chain = true)
public class TblWgzzWghcBg {

   @TableField(value = "ID")
   @Id
   @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
   @Schema(name = "主键id")
   private BigDecimal id;

    @TableField("WGHCID")
    @Schema(name = "違規核查ID")
    private BigDecimal wghcid;

    @TableField("CLUENABER")
    @Schema(name = "线索编号")
    private Integer cluenaber;

    @TableField(value = "HSCONTENT")
    @Schema(name = "核实内容")
    private String hscontent;

    @TableField(value = "HSFW")
    @Schema(name = "核实范围")
    private String hsfw;

    @TableField(value = "ORGNAME")
    @Schema(name = "工作组织名称")
    private String orgname;

    @TableField("ORGID")
    @Schema(name = "工作组织ID")
    private BigDecimal orgid;

    @TableField(value = "ZRDX")
    @Schema(name = "责任定性")
    private String zrdx;

    @TableField(value = "ZCSSRD")
    @Schema(name = "资产损失认定")
    private String zcssrd;

    @TableField(value = "ZRZJCLJY")
    @Schema(name = "责任追究处理建议")
    private String zrzjcljy;

    @TableField(value = "JBRNAME")
    @Schema(name = "经办人名称")
    private String jbrname;

    @TableField(value = "JBRID")
    @Schema(name = "经办人ID")
    private BigDecimal jbrid;

    @TableField(value = "BGSZRNAME")
    @Schema(name = "违规经营投资责任追究工作领导小组办公室主任")
    private String bgszrname;

    @TableField(value = "LDXZZZNAME")
    @Schema(name = "违规经营投资责任追究工作领导小组组长")
    private String ldxzzzname;

    @TableField(value = "LDXZHYJYID")
    @Schema(name = "领导小组会议纪要文件ID")
    private BigDecimal ldxzhyjyid;

    @TableField(value = "DWHHYJYID")
    @Schema(name = "公司党委会纪要文件ID")
    private BigDecimal dwhhyjyid;

    @TableField(value = "HCBGFILEID")
    @Schema(name = "核查报告文件ID")
    private BigDecimal hcbgfileid;

	@TableField("CONTENT")
	@Schema(name = "富文本")
	private String content;
}
