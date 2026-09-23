package com.huabo.legal.exam.modules.sys.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* <p>
* 管理员登录请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
@Data
@Schema(name="管理员保存请求类", description="管理员保存请求类")
public class SysUserSaveReqDTO implements Serializable {

    @Schema(name = "ID", required = true)
    private String id;

    @Schema(name = "用户名", required = true)
    private String userName;

    @Schema(name = "头像", required = true)
    private String avatar;

    @Schema(name = "真实姓名", required = true)
    private String realName;

    @Schema(name = "密码", required = true)
    private String password;

    @Schema(name = "部门", required = true)
    private String departId;

    @Schema(name = "角色列表", required = true)
    private List<String> roles;
    
}
