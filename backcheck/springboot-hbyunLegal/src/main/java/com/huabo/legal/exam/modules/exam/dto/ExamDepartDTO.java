package com.huabo.legal.exam.modules.exam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* <p>
* 考试部门数据传输类
* </p>
*
* @author 聪明笨狗
* @since 2020-09-03 17:24
*/
@Data
@Schema(name="考试部门", description="考试部门")
public class ExamDepartDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @Schema(name = "ID", required = true)
    private String id;
    
    @Schema(name = "考试ID", required = true)
    private String examId;
    
    @Schema(name = "部门ID", required = true)
    private String departId;
    
}
