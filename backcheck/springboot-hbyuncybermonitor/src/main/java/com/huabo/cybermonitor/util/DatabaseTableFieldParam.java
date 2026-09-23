package com.huabo.cybermonitor.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseTableFieldParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "bookid 不能为空")
	@Schema(name = "bookid")
	private String bookid;

	@NotNull(message = "table 表名 不能为空")
	@Schema(name = "表名")
	private String table;

}

