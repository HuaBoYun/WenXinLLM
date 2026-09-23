package com.huabo.audit.oracle.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="知识库查询对象")

public class TblNbsjInnerRuleVo {
		
	@Schema(name = "制度编号")
    private String rulecode;

	@Schema(name = "制度名称")
    private String rulename;
	
	@Schema(name = "发文机构")
    private String publishorg;
	
	@Schema(name = "类别")
    private String innruletype;
	
	@Schema(name = "状态：草稿、发布待审核、已发布、发布审核拒绝、已修订、已废止、废止待审核、废纸审核拒绝")
    private String status;   
	
	@Schema(name="所属公司",hidden=true)
    private String companyid;
	
	@Schema(name = "开始日期 yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date starttime;
	
	@Schema(name = "结束日期 yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date endtime;
	
	@Schema(name = "內容")
	private String content;
	
	@Schema(name = "制度类型")
	private String zdtype;
}
