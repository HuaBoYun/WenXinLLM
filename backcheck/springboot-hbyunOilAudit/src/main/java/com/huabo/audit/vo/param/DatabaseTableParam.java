package com.huabo.audit.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseTableParam implements Serializable {

	@NotNull(message = "bookid 不能为空")
	@Schema(name = "bookid")
	private BigDecimal bookid;

}

