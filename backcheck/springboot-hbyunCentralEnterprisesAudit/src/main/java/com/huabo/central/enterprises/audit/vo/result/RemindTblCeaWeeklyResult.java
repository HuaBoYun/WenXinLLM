package com.huabo.central.enterprises.audit.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemindTblCeaWeeklyResult {

	@Schema(name="是否提醒：true-提醒 false-不提醒")
	private Boolean flagRemind;

	@Schema(name="提醒内容")
	private String remindMsg;

	public RemindTblCeaWeeklyResult(Boolean flagRemind) {
		this.flagRemind = flagRemind;
	}
}
