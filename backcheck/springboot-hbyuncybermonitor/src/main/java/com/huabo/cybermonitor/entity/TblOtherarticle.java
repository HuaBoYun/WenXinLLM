package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    private Integer orgid;

    @TableField("ARTICLEBODY")
    @Schema(name = "文章内容")
    private String articlebody;

    @TableField("MODELTYPE")
    @Schema(name = "所属模块,不用传值")
    private String modeltype;

    private Set<Attachment> tblAttachments = new HashSet<Attachment>(0);

    public Set<Attachment> getTblAttachments() {
        return tblAttachments;
    }
    public void setTblAttachments(Set<Attachment> tblAttachments) {
        this.tblAttachments = tblAttachments;
    }

    // Manual setter methods for Lombok compatibility
    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }

    public Date getPublishtime() {
        return this.publishtime;
    }

    public void setArticletitle(String articletitle) {
        this.articletitle = articletitle;
    }

    public String getArticletitle() {
        return this.articletitle;
    }

    public void setArticlestatus(String articlestatus) {
        this.articlestatus = articlestatus;
    }

    public String getArticlestatus() {
        return this.articlestatus;
    }

    public void setAruticleauther(String aruticleauther) {
        this.aruticleauther = aruticleauther;
    }

    public String getAruticleauther() {
        return this.aruticleauther;
    }

    public void setArticlebody(String articlebody) {
        this.articlebody = articlebody;
    }

    public String getArticlebody() {
        return this.articlebody;
    }

    public void setModeltype(String modeltype) {
        this.modeltype = modeltype;
    }

    public String getModeltype() {
        return this.modeltype;
    }

}
