package com.huabo.audit.oracle.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.util.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName ProjectStatusVo
 * @Description 审计实施阶段入参
 * @Author ZiYao
 * @Date 2022/4/12 19:19
 * @Version 1.0
 */
@Data
@Schema(name="审计实施阶段入参")
public class ExecPhraseVo extends BaseVo {
    @Schema(name = "项目id")
    private String projectId;

    @Schema(name = "用户登录的token")
    private String token;

    @Schema(name = "搜索-开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @Schema(name = "搜索-结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    @Schema(name = "进场纪要编号")
    private String entercoed;

    @Schema(name = "进场纪要名称")
    private String entername;
}
