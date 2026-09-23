package com.huabo.legal.exam.modules.exam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.exam.modules.paper.enums.ExamState;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 考试数据传输类
* </p>
*
* @author 聪明笨狗
* @since 2020-07-25 16:18
*/
@Data
@Schema(name="考试", description="考试")
public class ExamDTO implements Serializable {


    private static final long serialVersionUID = 1L;


    @Schema(name = "ID", required = true)
    private String id;

    @Schema(name = "考试名称", required = true)
    private String title;

    @Schema(name = "考试描述", required = true)
    private String content;

    @Schema(name = "1公开2部门3定员", required = true)
    private Integer openType;

    @Schema(name = "组题方式1题库,2指定", required = true)
    private Integer joinType;

    @Schema(name = "难度:0不限,1普通,2较难", required = true)
    private Integer examLevel;


    @Schema(name = "考试状态", required = true)
    private Integer state;

    @Schema(name = "是否限时", required = true)
    private Boolean timeLimit;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(name = "开始时间", required = true)
    private Date startTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(name = "结束时间", required = true)
    private Date endTime;

    @Schema(name = "创建时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name = "更新时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Schema(name = "总分数", required = true)
    private Integer totalScore;

    @Schema(name = "总时长（分钟）", required = true)
    private Integer totalTime;

    @Schema(name = "及格分数", required = true)
    private Integer qualifyScore;




    /**
     * 是否结束
     * @return
     */
    public Integer getState(){

        if(this.timeLimit!=null && this.timeLimit){

            if(System.currentTimeMillis() < startTime.getTime() ){
                return ExamState.READY_START;
            }

            if(System.currentTimeMillis() > endTime.getTime()){
                return ExamState.OVERDUE;
            }

            if(System.currentTimeMillis() > startTime.getTime()
                    && System.currentTimeMillis() < endTime.getTime()
                    && !ExamState.DISABLED.equals(this.state)){
                return ExamState.ENABLE;
            }

        }

        return this.state;
    }
}
