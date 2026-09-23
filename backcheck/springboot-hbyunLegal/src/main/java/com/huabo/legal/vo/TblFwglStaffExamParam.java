package com.huabo.legal.vo;


import java.io.Serializable;

import com.huabo.legal.util.PageableParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class TblFwglStaffExamParam extends PageableParam implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Schema(name = "用户id")
	private String staffId;
	@Schema(name = "考试id")
	private String examId;
	@Schema(name = "真实姓名")
	private String realName;
	@Schema(name = "用户名")
	private String userName;
	@Schema(name = "考试状态:0-进行中 1-已禁用 2-待开始 3-已结束")
	private Integer state = 0;


}
