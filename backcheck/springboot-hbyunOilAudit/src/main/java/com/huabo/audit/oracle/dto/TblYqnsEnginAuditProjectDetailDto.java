package com.huabo.audit.oracle.dto;

import com.huabo.audit.oracle.entity.base.BaseProjectEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-14 11:12
 **/
@Data
public class TblYqnsEnginAuditProjectDetailDto extends BaseProjectEntity {

    /**
     * 审计项目名称
     */
    @Schema(name = "审计项目名称")
    private String name;

    /**
     * 审计组
     */
    @Schema(name = "小组")
    private String auditGroup;

    /**
     * 被审计单位
     */
    @Schema(name = "被审计单位")
    private String auditUnit;

    /**
     * 被审计单位Id
     */
    @Schema(name = "被审计单位ID")
    private Long auditUnitId;

    /**
     * 项目数量
     */
    @Schema(name = "项目数量")
    private BigDecimal projectNum;

    /**
     * 金额,单位元
     */
    @Schema(name = "金额,单位元")
    private Long amount;

    private List<TblYqnsEnginProjectAttDto> enginProjectAttDtoList;

}
