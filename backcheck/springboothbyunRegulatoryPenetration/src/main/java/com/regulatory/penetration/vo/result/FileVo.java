package com.regulatory.penetration.vo.result;

import com.regulatory.penetration.oracle.entity.TblAttachment;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVo<T> {

	@ApiModelProperty("详情返回对象")
	private T data;

	@ApiModelProperty("文件集合")
	private List<TblAttachment> file;
}
