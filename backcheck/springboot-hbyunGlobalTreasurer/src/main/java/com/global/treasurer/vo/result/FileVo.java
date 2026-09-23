package com.global.treasurer.vo.result;

import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;

import java.util.List;

// @Data // 已移除,使用手动编写的getter/setter
// @NoArgsConstructor // 已移除
// @AllArgsConstructor // 已移除
public class FileVo<T> {
	@ApiModelProperty("详情返回对象")
	private T data;

	@ApiModelProperty("文件集合")
	private List<Object> file;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public List<Object> getFile() { return file; }
    public void setFile(List<Object> file) { this.file = file; }

}
