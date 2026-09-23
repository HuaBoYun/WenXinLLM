package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="我的底稿列表查询入参")
public class TBlNbsjSheetVo extends BaseVo {
	@Schema(name = "底稿名称")
    private String sheetname;

    @Schema(name = "底稿编号")
    private String sheetcode;
    
    @Schema(name = "审核状态")
    private String status;
    
    @Schema(name = "拟稿人")
    private String staffid;
    
    @Schema(hidden=true)
    private Integer projectid;

}