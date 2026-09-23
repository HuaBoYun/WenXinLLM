package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-06-30
 */
@TableName("TBL_OTHERARTICLE")
@Data
@Schema(name="行业知识")
@Accessors(chain = true)
public class TblOtherarticle implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId("OTHARTID")
	@Schema(name = "主键")
	private BigDecimal othartid;

    @TableField("ARTICLETITLE")
    @Schema(name = "文章标题")
    private String articletitle;

    @TableField("ARTICLESTATUS")
    @Schema(name = "状态：草稿、已发布、已废止")
    private String articlestatus;

    @TableField("ARUTICLEAUTHER")
    @Schema(name = "文章作者")
    private String aruticleauther;

    @TableField("PUBLISHTIME")
    @Schema(name = "发布时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date publishtime;

    @TableField("MEMO")
    @Schema(name = "备注")
    private String memo;

    @TableField("ORGID")
    @Schema(name = "所属行业id（外层行业树的orgid）")
    private BigDecimal orgid;

    @TableField("ARTICLEBODY")
    @Schema(name = "文章内容")
    private String articlebody;

    @TableField("MODELTYPE")
    @Schema(name = "所属模块,不用传值")
    private String modeltype;

}
