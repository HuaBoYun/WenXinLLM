package com.huabo.file.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Schema(description="文件上传管理")
@TableName("TBL_FILE_UPLOAD")
@Data
public class FileUpload {

    @TableId("FILEID")
    @Schema(name = "文件ID，主键，需外部生成，非自增")
    private Long fileId;

    @TableField("FILENAME")
    @Schema(name = "文件原始名称（上传时的名称）")
    private String fileName;

    @TableField("FILEEXTENSION")
    @Schema(name = "文件后缀，例如 .jpg, .pdf, .png 等")
    private String fileExtension;

    @TableField("FILESIZE")
    @Schema(name = "文件大小，单位字节")
    private Long fileSize;

    @TableField("FILEPATH")
    @Schema(name = "文件存储路径（绝对路径或相对路径）")
    private String filePath;

    @TableField("SAVETYPE")
    @Schema(name = "文件保存类型，本地：LOCAL，NFS：NFS，云存储：OSS 等")
    private String saveType;

    @TableField("UPLOADTIME")
    @Schema(name = "文件上传时间，默认当前时间")
    private Date uploadTime;

    @TableField("FILEHASH")
    @Schema(name = "文件的唯一哈希值，用于校验文件内容或防止重复上传")
    private String fileHash;

    @TableField("ISENCRYPTED")
    @Schema(name = "是否加密存储，1表示加密，0表示不加密，默认值为1")
    private Boolean isEncrypted;

    @TableField("ISACTIVE")
    @Schema(name = "文件是否有效，1表示有效，0表示无效，默认值为1")
    private Boolean isActive;

    @TableField("DESCRIPTION")
    @Schema(name = "文件的附加描述信息")
    private String description;

    @TableField("UPLOADERNAME")
    @Schema(name = "上传人名称")
    private String uploaderName;

    @TableField("UPLOADERACCOUNT")
    @Schema(name = "上传账号")
    private String uploaderAccount;

    @TableField("USERID")
    @Schema(name = "用户id")
    private Long userId;

}
