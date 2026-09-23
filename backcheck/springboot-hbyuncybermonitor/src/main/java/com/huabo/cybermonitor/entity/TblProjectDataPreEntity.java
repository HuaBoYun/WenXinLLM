package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Set;

@TableName("TBL_PROJECT_DATAPRE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblProjectDataPreEntity {
	
	@TableId(value = "id", type= IdType.AUTO)
    @Schema
    private Integer id;

    @TableField(value = "projectDataPreId")
    @Schema
    private String projectDataPreId;
    
    @TableField(value = "dataName")
    @Schema
    private String dataName;
    
    @TableField(value = "dataCapacity")
    @Schema
    private String dataCapacity;
    
    @TableField(value = "detaDate")
    @Schema
    private Date detaDate;
    
    @TableField(value = "orgid")
    @Schema
    private String orgid;
    
    @TableField(value = "projectName")
    @Schema
    private String projectName;
    
    @TableField(value = "userName")
    @Schema
    private String userName;
    
    @TableField(value = "projectId")
    @Schema
    private String projectId;
    
    @TableField(value = "tblProjectDataPreAtts")
    @Schema
    private Set<TblAttachment> tblProjectDataPreAtts;
    
}
