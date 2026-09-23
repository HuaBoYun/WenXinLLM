package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 密级信息配置表
 * </p>
 *
 * @author lhp
 * @since 2024-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SECRECT_LEVEL")
@Schema(name="TblSecrecyLevel对象", description="密级信息配置表")
public class TblSecrectLevel implements Serializable {

	private static final long serialVersionUID = 1L;

    @Schema(name="主键")
    @TableId("LEVELID")
    private BigDecimal levelId;

    @Schema(name="密级名称")
    @TableField("LEVELNAME")
    private String levelName;

    @Schema(name="密级类型，1-功能模块，2-业务单据，3-人员，4-附件")
    @TableField("LEVELTYPE")
    private Integer levelType;

    @Schema(name="密级功能范围限制")
    @TableField("SECRECYMENUSCOPE")
    private String secrectMenuScope;

    @Schema(name="密级人员范围限制")
    @TableField("SECRECYSTAFFSCOPE")
    private String secrectStaffScope;
    
    @Schema(name="创建人主键")
    @TableField("CREATESTAFFID")
    private BigDecimal createStaffId;
      
    @Schema(name="创建人姓名")
    @TableField("CREATESTAFFNAME")
    private String createStaffName;
      
    @Schema(name="创建时间")
    @TableField("CREATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
  	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date createTime;
      
    @Schema(name="修改人主键")
    @TableField("MODIFYSTAFFID")
    private BigDecimal modifyStaffId;
      
    @Schema(name="修改人姓名")
    @TableField("MODIFYSTAFFNAME")
    private String modifyStaffName;
      
    @Schema(name="修改时间")
    @TableField("MODIFYTIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
  	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date modifyTime;

}
