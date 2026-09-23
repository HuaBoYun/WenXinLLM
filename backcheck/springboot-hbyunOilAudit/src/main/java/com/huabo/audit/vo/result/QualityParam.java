package com.huabo.audit.vo.result;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QualityParam {

	@Schema(name = "批次")
	private String  pc;

	@Schema(name = "项目承担单位（实施单位）")
	private String ssdw;

	@Schema(name = "被审计单位")
	private String auditorgname;
	
	@Schema(name = "项目名称")
	private String projectname;
	
	
	@Schema(name = "经责科负责人")
	private String ksfzr;
	
	@Schema(name = "审计项目组人员")
	private String xmzcy;
	
	@Schema(name = "现场审计开始日期")
	private Date xcsrarttime;
	
	@Schema(name = "实际现场结束日期")
	private Date xcendtime;
	
	@Schema(name = "实际工作天数")
	private String sj;
	
	@Schema(name = "投入资源（人日）")
	private String trzy;
	
	@Schema(name = "工效比")
	private String gxb;
	
	@Schema(name = "复核底稿数量")
	private String fhdg;
	
	@Schema(name = "问题底稿数量")
	private String wtdg;
	
	
	@Schema(name = "被审计单位")
	private String bsjdw;
	
	
	@Schema(name = "总数")
	private String zs;
	
	
	@Schema(name = "完成项目数")
	private String wcs;
	
	
	@Schema(name = "未完成项目数")
	private String wwcs;
	
	@Schema(name = "月份")
	private String yf;
	
	@Schema(name = "项目数量")
	private String sl;
	
	@Schema(name = "项目状态")
	private String status;
}
