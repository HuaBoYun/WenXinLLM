package com.huabo.monitor.mysql.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_BI_REPORT_MENU")
@Schema(name="TblBiReportMenu")
public class TblBiReportMenuMySql implements Serializable {
    @TableId("PAGEID")
    @Id
    private BigDecimal pageid;
    @TableField("PAGENAME")
    private String pagename;///
    @TableField("URL")
    private String url;///
    @TableField("FORBIDDEN")
    private BigDecimal forbidden;///
    @TableField("UNIT")
    private String unit; ///
    @TableField("PAGEUSER")
    private String pageuser;///
    @TableField("CREATER")
    private String creater;///
    @TableField("MEMO1")
    private String memo1;///
    @TableField("MEMO2")
    private String memo2;///
    @TableField("PAGECODE")
    private String pagecode;///
    @TableField("THEME")
    private String theme;///
    @TableField("TREEID")
    private BigDecimal treeid;///
    @TableField("CREATEDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdate;///
    @TableField("PAGEBODY")
    private BigDecimal pagebody;//一级id
    @TableField("PAGEBODY")
    private BigDecimal rightid;//
    @TableField("PAGEDES")
    private String pageDes;///
    @TableField("RQURL")
    private String rqurl;///
    @TableField("TYPE")
    private String type;
    @Transient
    private String pid;
    @Transient
    private String orgname;

    private Set tblAttachments = new HashSet(0);


}
