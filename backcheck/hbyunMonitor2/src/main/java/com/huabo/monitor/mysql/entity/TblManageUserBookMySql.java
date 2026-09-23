package com.huabo.monitor.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_MANAGE_USER_BOOK")
@Schema(name="TblManageUserBookMySql")
public class TblManageUserBookMySql {


    //    @TableId("STAFFID")
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @TableField("STAFFID")
    private BigDecimal staffid;
    @TableField("BOOKID")
    private String bookid;
    @TableField("STATUS")
    private BigDecimal status;

//    private TblManageUserBookId id;
//    private TblStaff tblStaff;
//    private TblAccBook tblAccBook;
}
