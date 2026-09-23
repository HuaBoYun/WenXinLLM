package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_YMFLOWRECORD_ATT")
@Schema(name="流程节点审批上传附件", description="")
public class TblYmFlowRecordAtt implements Serializable {

	private static final long serialVersionUID = 1L;

    @Schema(name="主键Id 自增")
    @TableId(value="ATTID",type = IdType.INPUT) 
    private Long attid;

    @Schema(name="附件名称")
    @TableField("ATTNAME")
    private String attname;

    @Schema(name="附件路径")
    @TableField("ATTPATH")
    private String attpath;

    @Schema(name="附件大小")
    @TableField("ATTSIZE")
    private Double attsize;

    @Schema(name="上传时间")
    @TableField("UPLOADTIME")
    private Date uploadtime;

    @Schema(name="上传人")
    @TableField("UPLOADER")
    private BigDecimal uploader;

    @Schema(name="flowTaskInfo中的Id  可以区分附件属于哪个流程")
    @TableField("FLOWTASKID")
    private String flowtaskid;

    @Schema(name="flowTaskOperatorRecordList中的taskOperatorId 可以区分审批节点")
    @TableField("OPERATORID")
    private String operatorid;
    
    @Transient
    private String uploaderName;
    
    @TableField("ISENCRYPTED")
    @Schema(name="是否加密存储，1表示加密，0表示不加密，默认值为0")
    private Boolean isEncrypted = true;
    
    @Schema(name="加密地址，用于预览")
    @TableField("JMURL")
    private String jmurl;
    
    @Schema(name="附件密级")
    @TableField("ATTACHMENTLEVEL")
    private BigDecimal attachmentlevel;
}
