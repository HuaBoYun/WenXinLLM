package com.huabo.legal.exam.modules.sys.config.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* <p>
* 通用配置请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-17 09:12
*/
@Data
@Schema(name="通用配置", description="通用配置")
public class SysConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(name = "ID", required = true)
    private String id;

    @Schema(name = "系统名称")
    private String siteName;

    @Schema(name = "前端LOGO")
    private String frontLogo;

    @Schema(name = "后台LOGO")
    private String backLogo;

    @Schema(name = "版权信息")
    private String copyRight;
    
}
