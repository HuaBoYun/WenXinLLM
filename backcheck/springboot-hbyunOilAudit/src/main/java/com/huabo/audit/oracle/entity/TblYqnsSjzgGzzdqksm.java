package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 央企模块-审计整改-规章制度情况说明
 * @TableName TBL_YQNS_SJZG_GZZDQKSM
 */
@TableName(value ="TBL_YQNS_SJZG_GZZDQKSM")
@Data
public class TblYqnsSjzgGzzdqksm implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * 规章制度情况说明主键
     */
    @Schema(name = "规章制度情况说明主键")
    @TableId(value = "GZZDQKSMID",type = IdType.INPUT)
    private BigDecimal gzzdqksmid;

    
    @Schema(name = "问题整改主键")
    @TableField("WTZGID")
    private BigDecimal wtzgid;
    
    
    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField(value = "CJR")
    private String cjr;
    
    @Schema(name = "创建人主键")
    @TableField(value = "CJRID")
    private BigDecimal cjrId;
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField(value = "CJSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cjsj;
    

    /**
     * 规章制度情况说明
     */
    @Schema(name = "规章制度情况说明")
    @TableField(value = "GZZDQKSM")
    private String gzzdqksm;


    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    @IgnoreSwaggerParameter
    private List<TblAttachment> attachments;
}