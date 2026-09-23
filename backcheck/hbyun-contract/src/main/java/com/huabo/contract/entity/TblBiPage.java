package com.huabo.contract.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-08
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_BI_PAGE")
@Schema(name="TblBiPage对象")
public class TblBiPage implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "PAGEID",type = IdType.INPUT)
      private BigDecimal pageid;

    @TableField("PAGENAME")
    private String pagename;

    @TableField("URL")
    private String url;

    @TableField("FORBIDDEN")
    private BigDecimal forbidden;

    @TableField("UNIT")
    private String unit;

    @TableField("PAGEUSER")
    private String pageuser;

    @TableField("CREATER")
    private String creater;

    @TableField("MEMO1")
    private String memo1;

    @TableField("MEMO2")
    private String memo2;

    @TableField("PAGECODE")
    private String pagecode;

    @TableField("THEME")
    private String theme;

    @TableField("CREATEDATE")
    private LocalDateTime createdate;

    @TableField("PAGEBODY")
    private BigDecimal pagebody;

    @TableField("RIGHTID")
    private BigDecimal rightid;

    @TableField("TREEID")
    private BigDecimal treeid;

    @TableField("PAGEDES")
    private String pagedes;

    @TableField("RQURL")
    private String rqurl;

    @TableField("TYPE")
    private String type;

    @TableField("SORT")
    private BigDecimal sort;


}
