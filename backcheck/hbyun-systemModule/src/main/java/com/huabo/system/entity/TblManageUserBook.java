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
@TableName("TBL_MANAGE_USER_BOOK")
@Schema(name="TblManageUserBook")
public class TblManageUserBook {


//    @TableId("STAFFID")
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @TableField("STAFFID")
    @Schema(name="用户主键")
    private BigDecimal staffid;
    @TableField("BOOKID")
    @Schema(name="张涛主键")
    private String bookid;
    @TableField("STATUS")
    @Schema(name="状态 是否启用 0 启用")
    private BigDecimal status;

//    private TblManageUserBookId id;
//    private TblStaff tblStaff;
//    private TblAccBook tblAccBook;
}
