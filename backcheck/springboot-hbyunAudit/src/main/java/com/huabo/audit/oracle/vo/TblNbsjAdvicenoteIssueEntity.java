package com.huabo.audit.oracle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class TblNbsjAdvicenoteIssueEntity {
    @Schema(name = "创建人")
    private String createstaffName;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String createtime;

    @Schema(name = "接收人ID")
    private String reciver;

    @Schema(name = "接收人姓名")
    private String reciverName;

    @Schema(name = "接收时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String recivedate;

    @Schema(name = "接收状态")
    private String isread;

}
