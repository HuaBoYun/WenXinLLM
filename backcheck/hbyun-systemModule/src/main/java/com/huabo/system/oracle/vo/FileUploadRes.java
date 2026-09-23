package com.huabo.system.oracle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FileUploadRes {
//    @Schema(name="文件ID")
//    private String fileId;
//    @Schema(name="文件原始名称（上传时的名称）")
//    private String fileName;
//    @Schema(name="文件大小，单位字节")
//    private long fileSize;

    @Schema(name="文件ID")
    private String attid;
    @Schema(name="附件名称")
    private String attname;
    @Schema(name="附件路径")
    private String attpath;
    @Schema(name="附件大小")
    private double attsize;
    @Schema(name="上传人名称")
    private String uploader;
    @Schema(name="上传时间")
    private String uploadTime;
    @Schema(name="预览地址")
    private String previewUrl;
    @Schema(name="是否加密存储，1表示加密，0表示不加密，默认值为1")
    private String isEncrypted = "1";

}
