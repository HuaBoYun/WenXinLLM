package com.huabo.monitor.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "TBL_MANAGE_RIGHT")
@ApiModel(value="权限对象")
@AllArgsConstructor
@NoArgsConstructor
public class TblManageRight implements Serializable {

    private static final long serialVersionUID = 2066646943386033777L;
    public static final int ISSHOW_INDICATORSTATUS = 1;


    @TableId("FLOWID")
    @ApiModelProperty(value = "权限主键ID",dataType="BigDecimal")
    @Id
    @KeySql(sql = "select MANAGERIGHT_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
    private BigDecimal rightid;

    @Column(name="RIGHTNAME")
    @ApiModelProperty(value = "权限名称",dataType="String")
    private String rightname;
    @Column(name="RIGHTURL")
    @ApiModelProperty(value = "权限路径",dataType="String")
    private String righturl;
    @Column(name="MEMO")
    @ApiModelProperty(value = "权限备注",dataType="String")
    private String memo;
    @Column(name="LEAF")
    @ApiModelProperty(value = "未知",dataType="String")
    private BigDecimal leaf;
    @Column(name="FATHERRIGHTID")
    @ApiModelProperty(value = "父级权限",dataType="BigDecimal")
    private BigDecimal fatherrightid;
    @Column(name="RIGHTCODE")
    @ApiModelProperty(value = "权限编码",dataType="String")
    private String rightcode;

    @Column(name="FUNCORDER")
    @ApiModelProperty(value = "权限排序，从小到大",dataType="Integer")
    private BigDecimal funcorder;
    @Column(name="CUSTOMPAGE")
    @ApiModelProperty(value = "是否为报表",dataType="Integer")
    private Integer custompage;
    @Column(name="INDICATORSTATUS")
    @ApiModelProperty(value = "启用状态  1-启用，0-弃用",dataType="Integer")
    private String indicatorstatus;
    @Column(name="RIGHTDESC")
    @ApiModelProperty(value = "权限描述",dataType="String")
    private String rightdesc;
    @Column(name="RIGHTISBZ")
    @ApiModelProperty(value = "未知",dataType="String")
    private BigDecimal rightisbz;
    @Column(name="RIGHTCONTENT")
    @ApiModelProperty(value = "权限html代码",dataType="String")
    private String rightcontent;
    @Column(name="CSSCLASS")
    @ApiModelProperty(value = "权限css样式",dataType="String")
    private String cssClass;

    @Column(name="RIGHTIMGURL")
    @ApiModelProperty(value = "权限图片路径",dataType="String")
    private String rightImgUrl;

    @Column(name="RIGHTMODULETYPE")
    @ApiModelProperty(value = "权限所属模块",dataType="String")
    private String rightModuleType;


    @Transient
    private String rightname1;
    @Transient
    @ApiModelProperty(value = "子集权限集合",dataType="List")
    private List<TblManageRight> rightList = new ArrayList(0);
}
