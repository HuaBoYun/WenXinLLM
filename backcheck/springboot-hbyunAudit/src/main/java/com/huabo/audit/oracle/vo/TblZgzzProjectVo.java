package com.huabo.audit.oracle.vo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.util.BaseVo;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 整改清单表
 * </p>
 *
 * @author LHP
 * @since 2023-11-16
 */
@Data
public class TblZgzzProjectVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "方案主键")
    private BigDecimal planId;

      @Schema(name = "方案编号")
    private String planCode;

      
      @Schema(name = "方案名称")
    private String planName;
 
    
}
