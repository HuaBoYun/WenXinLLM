package com.huabo.legal.exam.modules.sys.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 管理用户请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
@Data
@Schema(name="管理用户", description="管理用户")
public class SysUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(name = "ID", required = true)
    private String id;

    @Schema(name = "用户名", required = true)
    private String userName;

    @Schema(name = "真实姓名", required = true)
    private String realName;

    @Schema(name = "密码", required = true)
    private String password;

    @Schema(name = "密码盐", required = true)
    private String salt;

    @Schema(name = "角色列表", required = true)
    private String roleIds;

    @Schema(name = "部门ID", required = true)
    private String departId;

    @Schema(name = "创建时间", required = true)
    private Date createTime;

    @Schema(name = "更新时间", required = true)
    private Date updateTime;

    @Schema(name = "状态", required = true)
    private Integer state;
    
}
