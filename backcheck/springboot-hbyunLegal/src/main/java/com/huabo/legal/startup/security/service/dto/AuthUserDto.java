package com.huabo.legal.startup.security.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证用户
 *
 * @author zhuhuix
 * @date 2020-04-03
 */
@Schema(name="授权用户信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDto {


    @Schema(name = "用户名")
    private String userName;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "临时登录凭证")
    private String code;

    @Schema(name = "邮箱")
    private String email ;

}
