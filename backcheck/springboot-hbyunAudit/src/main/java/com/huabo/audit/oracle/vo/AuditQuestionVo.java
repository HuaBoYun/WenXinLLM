package com.huabo.audit.oracle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName ProjectStatusVo
 * @Description 审计项目查询入参
 * @Author ZiYao
 * @Date 2022/4/12 19:19
 * @Version 1.0
 */
@Data
@Schema(name="审计问题查询入参")
public class AuditQuestionVo extends ProjectAuditVo {
    @Schema(name = "问题标题")
    private String questionTitle;
    @Schema(name = "发现人")
    private Integer findPeopleId;
}
