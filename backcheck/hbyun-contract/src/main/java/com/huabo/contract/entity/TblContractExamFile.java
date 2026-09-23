package com.huabo.contract.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("TBL_CONTRACT_EXAMFILE")
@Schema(name="审核合同文本")
public class TblContractExamFile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "主键Id 自增")
	@TableId(value= "ID",type= IdType.INPUT)
	private BigDecimal id;

	@Schema(name = "附件名称")
	@TableField("FILENAME")
	private String fileName;

	@Schema(name = "附件路径")
	@TableField("FILEPATH")
	private String filePath;

	@Schema(name = "附件大小")
	@TableField("FILESIZE")
	private BigDecimal fileSize;

	@Schema(name = "附件类型")
	@TableField("FILETYPE")
	private Integer fileType;

	@Schema(name = "文件状态")
	@TableField("FILESTATUS")
	private Integer fileStatus;

	@Schema(name = "所属合同")
	@TableField("CONSTRACTID")
	private BigDecimal constractId;

	@Schema(name = "上传时间")
	@TableField("UPLOADTIME")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date uploadTime;

	@Schema(name = "上传人ID")
	@TableField("UPLOADER")
	private BigDecimal uploader;

	@TableField("UPLOADER")
	@Schema(name = "上传人姓名")
	private String uploaderName;

}
