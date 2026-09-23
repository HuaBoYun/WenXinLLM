package com.huabo.audit.vo.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneratingTableListResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private String tableName;
}
