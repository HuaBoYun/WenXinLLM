package com.huabo.know.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.know.page.BasePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Schema(name="查询法律法规对象")
public class LawRegulationParam extends BasePageParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "标题")
    private String title;

    @Schema(name = "发文字号")
    private String documentNo;

    @Schema(name = "全文")
    private String content;

    @Schema(name = "合同类型code")
    private String contractTypeCode;

    @Schema(name = "效力级别code")
    private String effectivenessDicCode;

    @Schema(name = "时效性code")
    private String timelinessDicCode;

    @Schema(name = "发布部门code（制定机关）")
    private String issueDepartmentCode;

    @Schema(name = "发布年份")
    private String issueYear;

    @Schema(name = "发布时间开始日期，传参拼接( 00:00:00)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date issueDateStart;

    @Schema(name = "发布时间结束日期，传参拼接( 23:59:59)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date issueDateEnd;

    @Schema(name = "实施时间开始日期，传参拼接( 00:00:00)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date implementDateStart;

    @Schema(name = "实施时间结束日期，传参拼接( 23:59:59)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date implementDateEnd;

}
