package com.huabo.audit.oracle.vo;


import com.alibaba.fastjson.annotation.JSONField;
import com.huabo.audit.util.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.vo
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/26
 * @Time:16:23
 */
@Data
public class TblWgzzVo extends BaseVo {
    @Schema(name = "涉及单位")
    private String clueUnit;

    @Schema(name = "涉及责任人")
    private String clueHandLing;

    @Schema(name = "创建人")
    private Integer creator;

    @Schema(name = "创建时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date impcreateuserName;

}
