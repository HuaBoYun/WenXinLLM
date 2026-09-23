package com.huabo.compliance.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Transient;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_MANAGE_SCREEN_RIGHT")
@Schema(name="TblManageScreenRightMySql")
public class TblManageScreenRightMySql {

    private static final long serialVersionUID = 1535410253795834109L;
    @TableId("RIGTHID")
    private Integer rightId;
    @TableField("RIGHTNAME")
    private String rightName;
    @TableField("RIGHTCODE")
    private String rightCode;
    @TableField("IMGRUL")
    private String imgUrl;
    @TableField("RIGHTTYPE")
    private Integer rightType;
    @TableField("RIGHTORDER")
    private Integer rightOrder;
    @TableField("FATHERID")
    private Integer fatherId;
    @TableField("RITHTMEMO")
    private String rightMemo;
    @TableField("RIGHTSTATUS")
    private Integer rightStatus;

    @Transient
    private Integer staffid;
    //private List<TblManageScreenRight> rightList = new ArrayList(0);

    @Transient
    @Schema(name = "是否有子集 true有   false没有")
    private boolean checked;

    private List<TblManageScreenRightMySql> children;

}
