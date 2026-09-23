package com.huabo.legal.startup.bbs.service.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 留言查询条件
 *
 * @author zhuhuix
 * @date 2022-06-09
 */
@Schema(name="留言查询条件")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BbsQueryDto {

    @Schema(name = "用户昵称")
    private String nickName;

    @Schema(name = "留言内容")
    private String content;

    @Schema(name = "是否回复")
    private Boolean replied;

	@TableField(value = "知识类型 1-劳动用工、2-知识产权、3-投融资、4-法律尽调、5-法律纠纷、6-其他")
	private Integer knowledgeType;

    @Schema(name = "留言起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Long createTimeStart;

    @Schema(name = "留言结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Long createTimeEnd;

    @Schema(name = "当前页数")
    private Integer currentPage;

    @Schema(name = "每页条数")
    private Integer pageSize;
}
