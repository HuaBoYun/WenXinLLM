package com.huabo.legal.vo.param;

import com.huabo.legal.oracle.entity.TblFwglAnnualExamineTopicExtOracle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TblFwglAnnualExamineTopicExtBatchAdd {

	@Schema(name="题目对象")
	private List<TblFwglAnnualExamineTopicExtOracle> list;

	@NotNull(message = "examineType 考核类型 不能为空")
	@Schema(name="考核类型 1-外部监管考核 2-外部监管考核")
	private Integer examineType;
}
