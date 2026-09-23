package com.huabo.financialdata.entity.vo.export;

import java.math.BigDecimal;

import com.huabo.financialdata.entity.base.BaseDbSource;
import com.huabo.financialdata.entity.vo.diaryBook.DiaryBookResponseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日记账 - 分页列表 - 返回参数
 *
 * @author Mr.xiang
 * @since 2022-10-20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="导出文件Excel传入参数")
public class ExportRequestVo extends BaseDbSource  {

	@Schema(name = "通用参数 1-导出科目表,2-导出日记账,3-凭证库,4-明细分类账、5-科目余额表、6-总分类账、7-辅助信息表、8-辅助余额表、9-辅助总账")
	private Integer exprotType;
	
	@Schema(name = "操作日记账、明细账 当前查询的科目Id，accid")
	private String accid;
	
	@Schema(name = "操作科目表、余额表时传入 科目编码，多个用逗号分割")
	private String accidStrs;
	
	@Schema(name = "(key)操作 传入凭证号，多个用逗号分割  **,**")
	private String pzh;
	
	@Schema(name = "(value)操作日记账、凭证库、明细账传入分录序号，同一凭证下用,线分割 不同凭证用~分割，示例 2,3~3,5  ")
	private String entryId;
	
	@Schema(name = "(value)操作余额表、辅助总账传入月份，同一科目id下用,线分割 不同凭证用~分割，示例 2,3~3,5  ")
	private String amonths;
	
	private String sqlStr;
	
	@Schema(name = "辅助信息表，传入需要导出的ID，多个用,分割")
	private String assIds;
	
	@Schema(name = "辅助余额表，传入需要导出的aid，多个用,分割")
	private String aids;
	
	@Schema(name = "(key)操作日记账、明细账、凭证库 传入返回参数（glh），多个用逗号分割  **,**")
	private String glhStrs;
	
	@Schema(name = "辅助账总账，传入assid,acid拼接字符串，多个示例 assid,acid~assid,acid~,assid,acid")
	private String asacIdStrs;
	
	
	
	
}
