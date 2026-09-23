package com.huabo.legal.exam.core.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 通用ID列表类操作，用于批量删除、修改状态等
 * @author bool 
 * @date 2019-08-01 19:07
 */
@Data
@Schema(name="删除参数", description="删除参数")
public class BaseIdsReqDTO extends BaseDTO {


    private static final long serialVersionUID = 1L;

	@JsonIgnore
    private String userId;

    @Schema(name = "要删除的ID列表", required = true)
    private List<String> ids;
}
