package com.huabo.system.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-05-22
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_SYSTEM_RIGHT")
@Schema(name="TblSystemRight对象", description="")
public class TblSystemRight implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final Integer MENUTYPE = 0;
    public static final Integer PAGETYPE = 1;
    public static final Integer BTNTYPE = 2;

    @Schema(name="主键Id 自增")
    @TableId(value="ID",type = IdType.INPUT)
    private BigDecimal id;

      @Schema(name="组件地址")
      @TableField("COMPONENT")
    private String component;

      @Schema(name="图标")
      @TableField("ICON")
    private String icon;

      @Schema(name="是否外链0否1是")
      @TableField("ISLINK")
    private Integer islink;

      @Schema(name="名称")
      @TableField("NAME")
    private String name;

      @Schema(name="上级ID")
      @TableField("PARENT")
    private BigDecimal parent;

      @Schema(name="路由地址")
      @TableField("PATH")
    private String path;

      @Schema(name="唯一标识")
      @TableField("PERMS")
    private String perms;

      @Schema(name="排序越小越靠前")
      @TableField("SORT")
    private Integer sort;

      @Schema(name="类型0目录1页面2操作")
      @TableField("TYPE")
    private Integer type;

      @Schema(name="是否隐藏1否0是")
      @TableField("VISIBLE")
    private Integer visible;

    @TableField("MODULETYPE")
    @Schema(name="所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx 业务管控财经实训等后续新增的一级权限 为该权限的Id")
    private String moduletype;
    
    @Schema(name="密级信息主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;

    @Transient
    @Schema(name = "密级名称")
    @TableField(exist = false)
    private String secrectLevelName;
    
    @Transient
    @Schema(name = "外部报表跳转方式判断，5-派可")
    @TableField(exist = false)
    private String reportType;

    
    @Transient
    @Schema(name = "外部报表跳转方式判断，5-派可")
    @TableField(exist = false)
    private String reportLinkUrl;

    @Transient
    @Schema(name="子集集合")
    @TableField(exist = false)
    private List<TblSystemRight> children;
    
    @Transient
    @Schema(name="是否授权 true有   false没有")
    @TableField(exist = false)
    private boolean checked = true;
    
    @Schema(name="是否授权 大于0有   0没有")
    @TableField(exist = false)
    private Integer isChecked;
    
    public TblSystemRight() {
    }
    
    public TblSystemRight(BigDecimal id,String name,BigDecimal parent
    		,String perms,Integer sort,
    		Integer visible,Integer type,String component,String icon
    		,Integer islink,String path,String moduletype) {
    	this.id = id;
    	this.name = name;
    	this.parent = parent;
    	this.perms = perms;
    	this.sort = sort;
    	this.visible = visible;
    	this.type = type;
    	this.component = component;
    	this.icon = icon;
    	this.islink = islink;
    	this.path = path;
    	this.moduletype = moduletype;
    }
    
}
