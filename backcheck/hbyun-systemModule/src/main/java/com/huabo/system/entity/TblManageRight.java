package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "TBL_MANAGE_RIGHT")
@Schema(name="权限对象")
@AllArgsConstructor
@NoArgsConstructor
public class TblManageRight implements Serializable {

    private static final long serialVersionUID = 2066646943386033777L;
    public static final int ISSHOW_INDICATORSTATUS = 1;
    

    @TableId(value="FLOWID",type = IdType.INPUT)
    @Schema(name="权限主键ID",format="BigDecimal")
    private BigDecimal rightid;

    @Column(name="RIGHTNAME")
    @Schema(name="权限名称",format="String")
    private String rightname;
    @Column(name="RIGHTURL")
    @Schema(name="权限路径",format="String")
    private String righturl;
    @Column(name="MEMO")
    @Schema(name="权限备注",format="String")
    private String memo;
    @Column(name="LEAF")
    @Schema(name="未知",format="String")
    private BigDecimal leaf;
    @Column(name="FATHERRIGHTID")
    @Schema(name="父级权限",format="BigDecimal")
    private BigDecimal fatherrightid;
    @Column(name="RIGHTCODE")
    @Schema(name="权限编码",format="String")
    private String rightcode;
    
    @Column(name="FUNCORDER")
    @Schema(name="权限排序，从小到大",format="Integer")
    private BigDecimal funcorder;
    @Column(name="CUSTOMPAGE")
    @Schema(name="是否为报表",format="Integer")
    private Integer custompage;
    @Column(name="INDICATORSTATUS")
    @Schema(name="启用状态  1-启用，0-弃用",format="Integer")
    private String indicatorstatus;
    @Column(name="RIGHTDESC")
    @Schema(name="权限描述",format="String")
    private String rightdesc;
    @Column(name="RIGHTISBZ")
    @Schema(name="未知",format="String")
    private BigDecimal rightisbz;
    @Column(name="RIGHTCONTENT")
    @Schema(name="权限html代码",format="String")
    private String rightcontent;
    @Column(name="CSSCLASS")
    @Schema(name="权限css样式",format="String")
    private String cssClass;
    
    @Column(name="RIGHTIMGURL")
    @Schema(name="权限图片路径",format="String")
    private String rightImgUrl;
    
    @Column(name="RIGHTMODULETYPE")
    @Schema(name="权限所属模块",format="String")
    private String rightModuleType;
    

    @Transient
    private String rightname1;
    @Transient
    @Schema(name="子集权限集合",format="List")
    private List<TblManageRight> rightList = new ArrayList(0);

//    public void add(String listSql) {
//
//    }
}
