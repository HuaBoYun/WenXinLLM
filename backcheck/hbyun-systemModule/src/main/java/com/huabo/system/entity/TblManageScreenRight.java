package com.huabo.system.entity;


import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_MANAGE_SCREEN_RIGHT")
@Schema(name="报表权限实体类")
public class TblManageScreenRight {

    private static final long serialVersionUID = 1535410253795834109L;
    @TableId(value="RIGTHID",type = IdType.INPUT)
    @Schema(name="主键ID")
    private BigDecimal rightId;
    @TableField("RIGHTNAME")
    @Schema(name="权限名称")
    private String rightName;
    @TableField("RIGHTCODE")
    @Schema(name="权限编码")
    private String rightCode;
    @TableField("IMGRUL")
    @Schema(name="权限图标路径")
    private String imgUrl;
    @TableField("RIGHTTYPE")
    @Schema(name="权限类型")
    private Integer rightType;
    @TableField("RIGHTORDER")
    @Schema(name="权限排序")
    private Integer rightOrder;
    @TableField("FATHERID")
    @Schema(name="父ID")
    private BigDecimal fatherId;
    @TableField("RITHTMEMO")
    @Schema(name="权限描述")
    private String rightMemo;
    @TableField("RIGHTSTATUS")
    @Schema(name="权限状态")
    private Integer rightStatus;

    @Transient
    private Integer staffid;
    //private List<TblManageScreenRight> rightList = new ArrayList(0);
    
    @Transient
    @Schema(name="是否有子集 true有   false没有")
    private boolean checked;
    
    @Schema(name="子集集合")
    private List<TblManageScreenRight> children;

}
