package com.huabo.finance.vo;

import java.math.BigDecimal;


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
public class ExportRequestVo {
	
	@Schema(name = "财务组织")
	private String pkOrg;

	@Schema(name = "通用参数 1-导出科目表,2-导出日记账,3-凭证库,4-明细分类账、5-科目余额表、6-总分类账、7-辅助信息表、8-辅助余额表、9-辅助总账")
	private Integer exprotType;
	
	@Schema(name = "科目表操作 科目主键数组")
	private String[] pkAccounts;
	
	@Schema(name = "日记账、明细账操作 凭证明细主键数组")
	private String[] pkDetails;
	
	@Schema(name = "凭证库操作 凭证主键数组")
	private String[] pkVouchers;
	
	@Schema(name = "余额表、总分类账操作 余额主键数组")
	private String[] pkBalances;
	
	@Schema(name = "辅助信息表操作 辅助信息主键数组")
	private String[] pkAccasss;
	
	@Schema(name = "辅助余额表、辅助总账操作  辅助余额主键数组")
	private String[] pkAssbalances;
	
	@Schema(name = "是否导出功能 ，1-是开启导出下载功能，0或为空不开启导出下载功能；")
	private String isExport;
	
	
}
