package com.huabo.system.entity;


import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_MANAGE_USER_SCREEN")
@Schema(name="TblManageUserBook")
public class TblManageUserScreen {


	@TableField(value="RIGTHID")
    private BigDecimal Rigthid;

    @TableField("STAFFID")
    private BigDecimal Staffid;
}
