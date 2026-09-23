package com.huabo.financialdata.entity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lee
 * @version 1.0.0
 **/
@Data
@TableName("TBL_MANAGE_USER_BOOK")
@Schema(name="用户账套管理")
public class ManageUserBook {

    // 员工ID，类型为BigDecimal
    @Schema(name = "员工ID，类型为BigDecimal")
    private BigDecimal staffid;

    // 账套ID，类型为String
    @Schema(name = "账套ID，类型为String")
    private String bookid;

    // 状态，类型为Integer
    @Schema(name = "状态")
    private Integer status;


}
