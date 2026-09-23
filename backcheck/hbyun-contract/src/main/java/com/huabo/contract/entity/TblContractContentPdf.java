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
@TableName("TBL_CONTRACT_CONTENTPDF")
@Schema(name="合同正文PDF")
public class TblContractContentPdf implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键Id 自增")
      @TableId(value = "CONTENTPDFID" ,type=IdType.INPUT)
      private BigDecimal contentPdfId;

      @Schema(name = "附件名称")
      @TableField("CONTENTPDFNAME")
    private String contentPdfName;

      @Schema(name = "附件路径")
      @TableField("CONTENTPDFPATH")
    private String contentPdfPath;

      @Schema(name = "附件大小")
      @TableField("CONTENTPDFSIZE")
    private BigDecimal contentPdfSize;

      @Schema(name = "附件类型")
      @TableField("CONTENTPDFTYPE")
    private Integer contentPdfType;

      @Schema(name = "文件状态")
      @TableField("CONTENTPDFSTATUS")
    private Integer contentPdfStatus;

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
