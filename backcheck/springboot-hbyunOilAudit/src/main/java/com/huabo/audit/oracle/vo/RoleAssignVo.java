package com.huabo.audit.oracle.vo;

import com.huabo.audit.util.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName ProjectStatusVo
 * @Description 任务分配角色列表入参
 * @Author ZiYao
 * @Date 2022/4/12 19:19
 * @Version 1.0
 */
@Data
@Schema(name="任务分配角色列表入参")
public class RoleAssignVo extends BaseVo {
    @Schema(name = "项目id")
    private Integer projectId;

    @Schema(name = "模板id")
    private Integer tempId;

    @Schema(name = "目标id")
    private Integer targetId;
}
