package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2022-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_MANAGE_USER_RIGHT")
@Schema(name="TblManageUserRightMySql对象")
public class TblManageUserRightMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("RIGHTID")
    private BigDecimal rightid;

    @TableField("STAFFID")
    private BigDecimal staffid;


}
