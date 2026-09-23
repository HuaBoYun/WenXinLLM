package com.huabo.compliance.vo.result;

import com.huabo.compliance.oracle.entity.TblComplianceFileOracle;
import com.huabo.compliance.oracle.entity.TblComplianceRiskOracle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblComplianceRiskResult implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name="文件集合")
	private List<TblComplianceFileOracle> file;

	@Schema(name="对象")
	private TblComplianceRiskOracle data;
}
