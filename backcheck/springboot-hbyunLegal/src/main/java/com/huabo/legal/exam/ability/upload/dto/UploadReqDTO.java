package com.huabo.legal.exam.ability.upload.dto;


import com.huabo.legal.exam.core.api.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传请求类
 * @author 
 * @date 2019-12-26 17:54
 */
@Data
@Schema(name="文件上传参数", description="文件上传参数")
public class UploadReqDTO extends BaseDTO {

    @Schema(name = "上传文件内容", required = true)
    private MultipartFile file;

}
