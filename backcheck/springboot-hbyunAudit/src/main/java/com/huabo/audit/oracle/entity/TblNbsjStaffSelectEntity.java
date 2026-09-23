package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 用户实施项目状态
 */
@TableName("TBL_NBSJ_STAFFSELECT")
@Data
@Schema(name="用户实施项目")
@Accessors(chain = true)
public class TblNbsjStaffSelectEntity {
    @TableId(value = "selectid", type = IdType.INPUT)
    @Schema(name = "已经选择ID")
    private BigDecimal selectId;
    @TableField(value = "projectid")
    @Schema(name = "项目id")
    private BigDecimal projectId;
    @TableField(value = "staffid")
    @Schema(name = "组员id")
    private BigDecimal staffId;
}
