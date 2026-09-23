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

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2024-04-03
 */
@Getter
@Setter
@Schema(name="法律案例列表查询对象")
public class JudicialCaseListParam extends BasePageParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "标题")
    private String title;

    @Schema(name = "全文")
    private String content;

    @Schema(name = "案件字号")
    private String caseFlag;

    @Schema(name = "案由code")
    private String categoryCode;

    @Schema(name = "案件类型code")
    private String caseClassCode;

    @Schema(name = "合同类型code")
    private String contractTypeCode;

    @Schema(name = "审理法院code")
    private String lastInstanceCourtCode;

    @Schema(name = "法院等级code")
    private String courtGradeCode;

    @Schema(name = "审理程序code")
    private String trialStepCode;

    @Schema(name = "文书类型code")
    private String documentAttrCode;

    @Schema(name = "参照级别code")
    private String caseGradeCode;

    @Schema(name = "公开类型code")
    private String noPublicReasonCode;

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
    private Date lastInstanceDateStart;

    @Schema(name = "实施时间结束日期，传参拼接( 23:59:59)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date lastInstanceDateEnd;

}
