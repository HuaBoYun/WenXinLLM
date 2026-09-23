package com.huabo.legal.exam.ability.upload.dto;

import com.huabo.legal.exam.core.api.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传文件结果
 * @author bool
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="文件上传响应", description="文件上传响应")
public class UploadRespDTO extends BaseDTO {

    @Schema(name = "上传后的完整的URL地址", required = true)
    private String url;

}
