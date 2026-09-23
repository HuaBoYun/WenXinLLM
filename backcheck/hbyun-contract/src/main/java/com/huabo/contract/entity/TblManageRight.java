package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_MANAGE_RIGHT")
@Schema(name="权限对象")
@AllArgsConstructor
@NoArgsConstructor
public class TblManageRight implements Serializable {

    private static final long serialVersionUID = 2066646943386033777L;
    public static final int ISSHOW_INDICATORSTATUS = 1;
    
    @Schema(name="权限主键ID")
    private BigDecimal rightid;
    @Schema(name="权限名称")
    private String rightname;
    @Schema(name="权限路径")
    private String righturl;
    @Schema(name="权限备注")
    private String memo;
    @Schema(name="未知")
    private BigDecimal leaf;
    @Schema(name="父级权限")
    private BigDecimal fatherrightid;
    @Schema(name="权限编码")
    private String rightcode;
    
    @Schema(name="权限排序，从小到大")
    private Integer funcorder;
    @Schema(name="是否为报表")
    private Integer custompage;
    @Schema(name="启用状态  1-启用，0-弃用")
    private Integer indicatorstatus;
    @Schema(name="权限描述")
    private String rightdesc;
    @Schema(name="未知")
    private BigDecimal rightisbz;
    @Schema(name="权限html代码")
    private String rightcontent;
    @Schema(name="权限css样式")
    private String cssClass;
    
    @Schema(name="权限图片路径")
    private String rightImgUrl;
    
    @Schema(name="权限所属模块")
    private String rightModuleType;
    

    private String rightname1;
    @Schema(name="子集权限集合")
    private List<TblManageRight> rightList = new ArrayList(0);

    public void add(String listSql) {

    }
}
