package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
@Schema(name="TblSystemRightMySql对象")
public class TblSystemRightMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键Id 自增")
    @TableId("ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HBYUNRIGHT_SEQUENCE.nextval from dual")
    private BigDecimal id;

    @Schema(name = "组件地址")
    @TableField("COMPONENT")
    private String component;

    @Schema(name = "图标")
    @TableField("ICON")
    private String icon;

    @Schema(name = "是否外链0否1是")
    @TableField("ISLINK")
    private Integer islink;

    @Schema(name = "名称")
    @TableField("NAME")
    private String name;

    @Schema(name = "上级ID")
    @TableField("PARENT")
    private Integer parent;

    @Schema(name = "路由地址")
    @TableField("PATH")
    private String path;

    @Schema(name = "唯一标识")
    @TableField("PERMS")
    private String perms;

    @Schema(name = "排序越小越靠前")
    @TableField("SORT")
    private Integer sort;

    @Schema(name = "类型0目录1页面2操作")
    @TableField("TYPE")
    private Integer type;

    @Schema(name = "是否隐藏1否0是")
    @TableField("VISIBLE")
    private Integer visible;

    @TableField("MODULETYPE")
    @Schema(name = "所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx 业务管控财经实训等后续新增的一级权限 为该权限的Id")
    private String moduletype;


    @Transient
    @Schema(name = "子集集合")
    private List<TblSystemRightMySql> children;

    @Transient
    @Schema(name = "是否授权 true有   false没有")
    private boolean checked = true;
}
