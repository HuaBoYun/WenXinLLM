package com.huabo.contract.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2021-10-29
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_CONTRACT_APPENDIXSIGNING")
@Schema(name="TblContractAppendixsigning对象")
public class TblContractAppendixsigning implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键Id 自增")
        @TableId(value = "SINGINGID" , type = IdType.INPUT)
      private BigDecimal singingId;

      @Schema(name = "附件名称")
      @TableField("SINGINGNAME")
    private String singingName;

      @Schema(name = "附件路径")
      @TableField("SINGINGPATH")
    private String singingPath;

      @Schema(name = "附件大小")
      @TableField("SINGINGSIZE")
    private BigDecimal singingSize;

      @Schema(name = "附件类型")
      @TableField("SINGINGTYPE")
    private Integer singingType;

      @Schema(name = "文件状态")
      @TableField("SINGINGSTATUS")
    private Integer singingStatus;

      @Schema(name = "PDF文件地址")
      @TableField("PDFVIEWFILEPATH")
    private String pdfViewFilePath;

      @Schema(name = "所属合同")
      @TableField("CONSTRACTID")
    private BigDecimal constractId;

      @Schema(name = "上床时间")
      @TableField("UPLOADTIME")
      @JSONField(format = "yyyy-MM-dd")
    private Date uploadTime;

      @Schema(name = "上传人ID")
      @TableField("UPLOADER")
    private BigDecimal uploader;


    @Schema(name = "上传人姓名")
    private String uploaderName;

    private String realname;
    
    @Schema(name = "OA历史合同历史数据对应id")
    @TableField("OAATTID")
    private String oaattid;
}
