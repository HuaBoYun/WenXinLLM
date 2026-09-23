package com.huabo.system.vo.param;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.huabo.system.entity.TblSystemProjectAuthOracle;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblSystemProjectAuthParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "关联项目ID 不能为空")
	@Schema(name="关联项目ID")
	private BigDecimal projectId;

	@Schema(name="保存 授权数组")
	private List<TblSystemProjectAuthOracle> list;

	@Schema(name="所属集团ID", hidden = true)
	private BigDecimal belongGroup;
}
