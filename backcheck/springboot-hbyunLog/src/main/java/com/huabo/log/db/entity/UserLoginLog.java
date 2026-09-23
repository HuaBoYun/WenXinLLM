package com.huabo.log.db.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;


@Data
@TableName("TBL_USER_LOGIN_LOG")
public class UserLoginLog {

    @ExcelIgnore
    @Schema(name = "id")
    @TableField("ID")
    private Long id;

    @ColumnWidth(20)
    @ExcelProperty(value = "登陆时间",index = 2)
    @Schema(name = "登陆时间")
    @TableField("LOGIN_TIME")
    private Date loginTime;
    
    @ColumnWidth(20)
    @ExcelProperty(value = "登出时间",index = 2)
    @Schema(name = "登出时间")
    @TableField("LOGOUT_TIME")
    private Date logoutTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "用户id",index = 0)
    @Schema(name = "用户id")
    @TableField("USER_ID")
    private Long userId;

    @ColumnWidth(20)
    @ExcelProperty(value = "劳人号",index = 2)
    @Schema(name = "劳人号")
    @TableField("USER_NAME")
    private String username;

    @ColumnWidth(20)
    @ExcelProperty(value = "IP地址",index = 4)
    @Schema(name = "IP地址")
    @TableField("LOGIN_IP")
    private String ip;

    @ColumnWidth(20)
    @ExcelProperty(value = "登陆设备",index = 5)
    @Schema(name = "登陆设备")
    @TableField("LOGIN_DEVICE")
    private String loginDevice;

    @ColumnWidth(20)
    @ExcelProperty(value = "登陆信息",index = 6)
    @Schema(name = "登陆信息")
    @TableField("LOGIN_MSG")
    private String loginMsg;

    @ColumnWidth(20)
    @ExcelProperty(value = "登陆结果",index = 7)
    @Schema(name = "登陆结果（0-成功，1-失败）")
    @TableField("LOGIN_RES")
    private String loginRes;

    @ExcelIgnore
    @ExcelProperty("创建时间")
    @Schema(name = "创建时间")
    @TableField("CREATED_AT")
    private Date createdAt;
    
    @ColumnWidth(20)
    @ExcelProperty(value = "用户名称",index = 1)
    @Schema(name = "用户名称")
    @TableField("REAL_NAME")
    private String realname;

}
