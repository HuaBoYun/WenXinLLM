package com.huabo.audit.oracle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-14 11:13
 **/
@Data
public class TblYqnsEnginProjectAttDto {

    /**
     * 主键ID
     */
    @Schema(name = "工程审计项目附件关系主键ID")
    private Long id;

    /**
     * 工程审计项目ID
     */
    @Schema(name = "工程审计项目ID")
    private Long enginAuditProjectId;

    /**
     * 附件ID
     */
    @Schema(name = "附件ID")
    private String attachmentId;

    /**
     * 附件名称
     */
    @Schema(name = "附件名称")
    private String attname;

    /**
     * 附件大小
     */
    @Schema(name = "附件大小")
    private double attsize;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    private String uploader;

    /**
     * 附件ID
     */
    @Schema(name = "附件ID")
    private BigDecimal attid;
}
