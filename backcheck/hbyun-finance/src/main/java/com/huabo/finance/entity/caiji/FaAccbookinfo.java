package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 账簿信息
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("FA_ACCBOOKINFO")
@Schema(name="FaAccbookinfo对象", description="账簿信息")
public class FaAccbookinfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("PK_ACCBOOKINFO")
      private String pkAccbookinfo;

      @Schema(name = "组织_集团 ")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "组织_业务单元_财务组织 ")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "账簿_账簿类型 ")
      @TableField("PK_SETOFBOOK")
    private String pkSetofbook;

      @Schema(name = "外币折算日期   	0=卡片建卡日期;1=业务发生日期;")
      @TableField("CONVERT_DATE")
    private Integer convertDate;

      @Schema(name = "本币原值来源")
      @TableField("LOCALORIGINVALUE")
    private String localoriginvalue;

      @Schema(name = "明细 ")
      @TableField("BODYVOS")
    private String bodyvos;
      
      @Schema(name = "账簿名称 ")
      @TableField("BOOKNAME")
    private String bookName;

      @Schema(name = "数据采集方案")
      @TableField("PK_FINANPLANID")
    private String pkFinanplanid;

      @Schema(name = "账簿类别编码 ")
      @TableField("ACCBOOKTYPECODE")
    private String accbooktypecode;

      @Schema(name = "账簿类别名称 ")
      @TableField("ACCBOOKTYPENAME")
    private String accbooktypename;

      @Schema(name = "创建人")
      @TableField("CREATOR")
    private String creator;

      @Schema(name = "创建时间 ")
      @TableField("CREATIONTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;

      @Schema(name = "最后修改人")
      @TableField("MODIFIER")
    private String modifier;

      @Schema(name = "最后修改时间")
      @TableField("MODIFIEDTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;


}
