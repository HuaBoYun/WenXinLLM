package com.huabo.compliance.vo.result;

import com.huabo.compliance.oracle.entity.TblComplianceFileOracle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVo<T> {

	@Schema(name="详情返回对象")
	private T data;

	@Schema(name="文件集合")
	private List<TblComplianceFileOracle> file;
}
