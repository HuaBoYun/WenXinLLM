package com.huabo.financialdata.entity.vo.gbi;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@Schema(name="应用配置-更新入参", description="应用配置-更新入参")
public class GbiApplicationConfigSaveRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(name="主键id，不传则根据当前用户更新")
    private String id;

    @Schema(name="1-自动联系上下文回答，2仅基于当前提问回答")
    @TableField("CONTINUOUS_QUESTIONING")
    private String continuousQuestioning;
}
