package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author LHP
 * @since 2023-11-24
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_RELATION_SHEET")
@Schema(name="TblRelationSheet对象")
@Table(name = "TBL_RELATION_SHEET")
public class TblRelationSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String STAFFTYPE = "STAFF"; //关联类型 用户
    
    public static final String ORGTYPE = "ORGANIZATION"; //关联类型 组织
    
      @Schema(name = "主键")
      @TableId("FID")
      @Id
      @Column(name = "FID")
      private BigDecimal fid;

      @Schema(name = "关联的业务表单主键")
      @TableField("FORMID")
      @Column(name = "FORMID")
    private String formid;

      @Schema(name = "关联的组织或用户主键")
      @TableField("OBJECTID")
      @Column(name = "OBJECTID")
    private BigDecimal objectid;

      @Schema(name = "关联的类型 组织 或用户")
      @TableField("OBJTYPE")
      @Column(name = "OBJTYPE")
    private String objtype;

      @Schema(name = "关联所属的业务表单表名")
      @TableField("FORMTYPE")
      @Column(name = "FORMTYPE")
    private String formtype;

      @Schema(name = "关联所属业务表单的所属列名")
      @TableField("FORMCOL")
      @Column(name = "FORMCOL")
    private String formcol;

      @Schema(name = "排序")
      @TableField("SORT")
      @Column(name = "v")
    private Integer sort;


}
