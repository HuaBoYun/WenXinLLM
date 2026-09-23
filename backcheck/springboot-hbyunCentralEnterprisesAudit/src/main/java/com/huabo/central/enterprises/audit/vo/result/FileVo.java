package com.huabo.central.enterprises.audit.vo.result;

import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
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
	private List<TblAttachment> file;
}
